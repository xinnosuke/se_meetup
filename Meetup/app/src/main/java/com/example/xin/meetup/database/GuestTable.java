package com.example.xin.meetup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GuestTable {
    private static final String TABLE_GUEST = "guest_list";
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_GUEST_ID = "guest_id";

    private static final String CREATE_GUEST_LIST_TABLE = "CREATE TABLE " + TABLE_GUEST + "("
            + COLUMN_EVENT_ID + " INTEGER, "
            + COLUMN_GUEST_ID + " INTEGER" + ")";

    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_GUEST;

    private final SQLiteOpenHelper helper;

    public GuestTable(final SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_GUEST_LIST_TABLE);
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void addGuest(final int eventId, final int guestId) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_ID, String.valueOf(eventId));
        values.put(COLUMN_GUEST_ID, String.valueOf(guestId));

        db.insert(TABLE_GUEST, null, values);
        db.close();
    }

    public List<Integer> getGuestIdByEvent(final int eventId) {
       final String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_GUEST_ID,
        };

        final List<Integer> guestList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_EVENT_ID + " = ?";
        final String[] selectArgs = {String.valueOf(eventId)};

        final Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,          //columns to return
                selection,        //columns for the WHERE clause
                selectArgs,       //The values for the WHERE clause
                null,     //group the rows
                null,      //filter by row groups
                null);    //The sort order

        if (cursor.moveToFirst()) {
            do {
                guestList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_GUEST_ID))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return guestList;
    }

    public List<Integer> getMyEvents(final int guestId) {
        final String[] columns = {
                COLUMN_EVENT_ID,
        };

        final List<Integer> eventList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_GUEST_ID + " = ?";
        final String[] selectArgs = {String.valueOf(guestId)};

        final Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,          //columns to return
                selection,        //columns for the WHERE clause
                selectArgs,       //The values for the WHERE clause
                null,     //group the rows
                null,      //filter by row groups
                null);    //The sort order

        if (cursor.moveToFirst()) {
            do {
                eventList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    public void deleteGuest(final int guestId) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_GUEST, COLUMN_GUEST_ID + " = ?", new String[]{String.valueOf(guestId)});
        db.close();
    }

    public void deleteGuestFromEvent(final int guestId, final int eventId) {
        final String selection = COLUMN_EVENT_ID + " = ? AND " + COLUMN_GUEST_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(eventId), String.valueOf(guestId)};

        final SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_GUEST, selection, selectionArgs);
        db.close();
    }

    public void deleteEvent(final int eventID) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_GUEST, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(eventID)});
        db.close();
    }

    public boolean hasRegistered(final int guestId, final int eventId) {
        final String[] columns = {COLUMN_EVENT_ID, COLUMN_GUEST_ID};
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_EVENT_ID + " = ? AND " + COLUMN_GUEST_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(eventId), String.valueOf(guestId)};

        final Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,                            //columns to return
                selection,                          //columns for the WHERE clause
                selectionArgs,                      //The values for the WHERE clause
                null,                       //group the rows
                null,                        //filter by row groups
                null);                      //The sort order

        final int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public boolean tableEmpty() {
        final String count = "SELECT count(*) FROM" + TABLE_GUEST;
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();

        final int icount = mcursor.getInt(0);

        return icount == 0;
    }
}
