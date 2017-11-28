package com.example.xin.meetup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class GuestTable {
    private static final String TABLE_GUEST = "guest_list";

    // User Table Columns names
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_GUEST_ID = "guest_id";

    // create table sql query
    private String CREATE_GUEST_LIST_TABLE = "CREATE TABLE " + TABLE_GUEST + "("
            + COLUMN_EVENT_ID + " INTEGER, "
            + COLUMN_GUEST_ID + " INTEGER" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_GUEST;

    private final SQLiteOpenHelper helper;

    public GuestTable(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GUEST_LIST_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addGuest(int eventId, int guestId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_ID, String.valueOf(eventId));
        values.put(COLUMN_GUEST_ID, String.valueOf(guestId));

        // Inserting Row
        db.insert(TABLE_GUEST, null, values);
        db.close();
    }

    public List<Integer> getGuestIdByEvent(int eventId) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_GUEST_ID,
        };

        List<Integer> guestList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = COLUMN_EVENT_ID + " = ?";
        String[] selectArgs = {String.valueOf(eventId)};

        Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,          //columns to return
                selection,        //columns for the WHERE clause
                selectArgs,       //The values for the WHERE clause
                null,     //group the rows
                null,      //filter by row groups
                null);    //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                guestList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_GUEST_ID))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return guestList;
    }

    public List<Integer> getMyEvents(int guestId) {
        String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_GUEST_ID,
        };

        List<Integer> eventList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = COLUMN_GUEST_ID + " = ?";
        String[] selectArgs = {String.valueOf(guestId)};

        Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,          //columns to return
                selection,        //columns for the WHERE clause
                selectArgs,       //The values for the WHERE clause
                null,     //group the rows
                null,      //filter by row groups
                null);    //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                eventList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_GUEST_ID))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return eventList;
    }

    public void deleteGuest(int guestId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_GUEST, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(guestId)});
        db.close();
    }

    public boolean checkGuest(int guestId) {
        // array of columns to fetch
        String[] columns = {COLUMN_EVENT_ID};
        SQLiteDatabase db = helper.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_GUEST_ID + " = ?";
        // selection argument
        String[] selectionArgs = {String.valueOf(guestId)};

        Cursor cursor = db.query(TABLE_GUEST, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean tableEmpty() {
        final String count = "SELECT count(*) FROM" + TABLE_GUEST;
        final SQLiteDatabase db = helper.getReadableDatabase();

        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();

        final int icount = mcursor.getInt(0);

        return  icount == 0;
    }
}
