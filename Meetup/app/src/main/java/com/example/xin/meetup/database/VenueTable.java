package com.example.xin.meetup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class VenueTable {

    private static final String TABLE_NAME = "venue";
    private static final String COLUMN_VENUE_ID = "venue_id";
    private static final String COLUMN_VENUE_NAME = "venue_name";
    private static final String COLUMN_OWNER_ID = "venue_owner_id";
    private static final String COLUMN_LOCATION = "venue_location";
    private static final String COLUMN_CAPACITY = "venue_capacity";
    private static final String COLUMN_COST = "venue_cost";
    private static final String COLUMN_HOURS_OPEN = "venue_hours_open";
    private static final String COLUMN_HOURS_CLOSE = "venue_hours_close";
    private static final String COLUMN_EMAIL = "venue_email";
    private static final String COLUMN_PHONE = "venue_phone";
    private static final String COLUMN_RATING = "venue_rating";
    private static final String COLUMN_DESCRIPTION = "venue_description";


    private final String CREATE_VENUE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_VENUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_VENUE_NAME + " TEXT, "
            + COLUMN_OWNER_ID + " INTEGER, "
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_CAPACITY + " INTEGER, "
            + COLUMN_COST + " TEXT, "
            + COLUMN_HOURS_OPEN + " TEXT, "
            + COLUMN_HOURS_CLOSE + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PHONE + " TEXT, "
            + COLUMN_RATING + " REAL, "
            + COLUMN_DESCRIPTION + " TEXT" + ")";

    private final String DROP_VENUE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private final SQLiteOpenHelper helper;

    public VenueTable(final SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(DROP_VENUE_TABLE);
        db.execSQL(CREATE_VENUE_TABLE);
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(DROP_VENUE_TABLE);
        onCreate(db);
    }

    public boolean organizerHasEvent(final int organizerId) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String count = "SELECT count(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_OWNER_ID + " = " + organizerId;

        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();

        final int icount = cursor.getCount();
        cursor.close();
        db.close();

        return icount > 0;
    }

    public void addVenue(final Venue venue) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_VENUE_NAME, venue.getVenueName());
        values.put(COLUMN_OWNER_ID, venue.getOwnerId());
        values.put(COLUMN_LOCATION, venue.getLocation());
        values.put(COLUMN_CAPACITY, venue.getCapacity());
        values.put(COLUMN_COST, venue.getCost());
        values.put(COLUMN_HOURS_OPEN, venue.getHoursOpen());
        values.put(COLUMN_HOURS_CLOSE, venue.getHoursClose());
        values.put(COLUMN_EMAIL, venue.getEmail());
        values.put(COLUMN_PHONE, venue.getPhone());
        values.put(COLUMN_RATING, venue.getRating());
        values.put(COLUMN_DESCRIPTION, venue.getDescription());

        db.insert(TABLE_NAME, null, values);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<Venue> getAllVenue() {
        final String[] columns = {
                COLUMN_VENUE_ID,
                COLUMN_VENUE_NAME,
                COLUMN_OWNER_ID,
                COLUMN_LOCATION,
                COLUMN_CAPACITY,
                COLUMN_COST,
                COLUMN_HOURS_OPEN,
                COLUMN_HOURS_CLOSE,
                COLUMN_EMAIL,
                COLUMN_PHONE,
                COLUMN_RATING,
                COLUMN_DESCRIPTION,

        };

        final String sortOrder = COLUMN_VENUE_NAME + " ASC";
        final List<Venue> venueList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                null,               //columns for the WHERE clause
                null,           //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                sortOrder);                 //The sort order

        if (cursor.moveToFirst()) {
            do {
                Venue venue = new Venue();
                venue.setVenueId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VENUE_ID))));
                venue.setVenueName(cursor.getString(cursor.getColumnIndex(COLUMN_VENUE_NAME)));
                venue.setOwnerId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_OWNER_ID))));
                venue.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                venue.setCapacity(cursor.getInt(cursor.getColumnIndex(COLUMN_CAPACITY)));
                venue.setCost(cursor.getString(cursor.getColumnIndex(COLUMN_COST)));
                venue.setHoursOpen(cursor.getString(cursor.getColumnIndex(COLUMN_HOURS_OPEN)));
                venue.setHoursClose(cursor.getString(cursor.getColumnIndex(COLUMN_HOURS_CLOSE)));
                venue.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                venue.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                venue.setRating(cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)));
                venue.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));

                venueList.add(venue);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return venueList;
    }


    public void updateVenue(final Venue venue) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VENUE_NAME, venue.getVenueName());
        values.put(COLUMN_OWNER_ID, venue.getOwnerId());
        values.put(COLUMN_LOCATION, venue.getLocation());
        values.put(COLUMN_CAPACITY, venue.getCapacity());
        values.put(COLUMN_COST, venue.getCost());
        values.put(COLUMN_HOURS_OPEN, venue.getHoursOpen());
        values.put(COLUMN_HOURS_CLOSE, venue.getHoursClose());
        values.put(COLUMN_EMAIL, venue.getEmail());
        values.put(COLUMN_PHONE, venue.getPhone());
        values.put(COLUMN_RATING, venue.getRating());
        values.put(COLUMN_DESCRIPTION, venue.getCapacity());

        db.update(TABLE_NAME, values, COLUMN_VENUE_ID + " = ?", new String[] {String.valueOf(venue.getVenueId())});

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void deleteVenue(final Venue venue) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_VENUE_ID + " = ?", new String[]{String.valueOf(venue.getVenueId())});
        db.close();
    }

    public void deleteAll() {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public boolean tableEmpty() {
        final String count = "SELECT count(*) FROM" + TABLE_NAME;
        final SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();

        final int icount = cursor.getInt(0);
        cursor.close();
        db.close();

        return  icount <= 0;
    }

    public Venue getVenueById(final int venueId) {
        final String[] columns = {
                COLUMN_VENUE_ID,
                COLUMN_VENUE_NAME,
                COLUMN_OWNER_ID,
                COLUMN_LOCATION,
                COLUMN_CAPACITY,
                COLUMN_COST,
                COLUMN_HOURS_OPEN,
                COLUMN_HOURS_CLOSE,
                COLUMN_EMAIL,
                COLUMN_PHONE,
                COLUMN_RATING,
                COLUMN_DESCRIPTION,
        };

        final String selection = COLUMN_VENUE_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(venueId)};
        final SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                selection,                   //columns for the WHERE clause
                selectionArgs,               //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                null);               //The sort order

        cursor.moveToFirst();
        Venue venue = new Venue();
        venue.setVenueId(cursor.getInt(cursor.getColumnIndex(COLUMN_VENUE_ID)));
        venue.setVenueName(cursor.getString(cursor.getColumnIndex(COLUMN_VENUE_NAME)));
        venue.setOwnerId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_OWNER_ID))));
        venue.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
        venue.setCapacity(cursor.getInt(cursor.getColumnIndex(COLUMN_CAPACITY)));
        venue.setCost(cursor.getString(cursor.getColumnIndex(COLUMN_COST)));
        venue.setHoursOpen(cursor.getString(cursor.getColumnIndex(COLUMN_HOURS_OPEN)));
        venue.setHoursClose(cursor.getString(cursor.getColumnIndex(COLUMN_HOURS_CLOSE)));
        venue.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
        venue.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
        venue.setRating(cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)));
        venue.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));

        cursor.close();
        db.close();

        return venue;
    }
}
