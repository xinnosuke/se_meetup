package com.example.xin.meetup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventTable {

    private static final String TABLE_NAME = "event";
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_NAME = "event_name";
    private static final String COLUMN_EVENT_DATE = "event_date";
    private static final String COLUMN_EVENT_TIME = "event_time";
    private static final String COLUMN_EVENT_CAPACITY = "event_capacity";
    private static final String COLUMN_EVENT_CATEGORY = "event_category";
    private static final String COLUMN_EVENT_LOCATION = "event_location";
    private static final String EVENT_ORGANIZER_ID = "event_organizer_id";
    private static final String COLUMN_EVENT_DESCRIPTION = "event_description";
    private static final String COLUMN_EVENT_STATUS = "event_status";

    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EVENT_NAME + " TEXT, "
            + COLUMN_EVENT_DATE + " TEXT, "
            + COLUMN_EVENT_TIME + " TEXT, "
            + COLUMN_EVENT_CAPACITY + " INTEGER, "
            + COLUMN_EVENT_CATEGORY + " TEXT, "
            + COLUMN_EVENT_LOCATION + " TEXT, "
            + EVENT_ORGANIZER_ID + " INTEGER, "
            + COLUMN_EVENT_DESCRIPTION + " TEXT, "
            + COLUMN_EVENT_STATUS + " INTEGER" + ")";

    private static final String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private final SQLiteOpenHelper helper;

    public EventTable(final SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(DROP_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(DROP_EVENT_TABLE);
        onCreate(db);
    }

    public boolean organizerHasEvent(final int organizerId) {
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String count = "SELECT count(*) FROM " + TABLE_NAME + " WHERE " + EVENT_ORGANIZER_ID + " = " + organizerId;

        final Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();

        final int icount = cursor.getCount();
        db.close();

        return icount > 0;
    }

    public void addEvent(final Event event) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_TIME, event.getTime());
        values.put(COLUMN_EVENT_CAPACITY, event.getCapacity());
        values.put(COLUMN_EVENT_CATEGORY, event.getCategory());
        values.put(COLUMN_EVENT_LOCATION, event.getLocation());
        values.put(EVENT_ORGANIZER_ID, event.getOrganizerId());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getDescription());
        values.put(COLUMN_EVENT_STATUS, event.getStatus());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Event> getEventByCategory(final Event.Category category) {
        final String[] columns = {"*"};

        final List<Event> eventList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_EVENT_CATEGORY + " = ?";
        final String[] selectArgs = {category.toString()};
        final String order = COLUMN_EVENT_DATE;

        final Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                order); //The sort order

        if (cursor.moveToFirst()) {
            do {
                final Event event = eventFromCursor(cursor);
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    public List<Event> getEventByCategoryAndDate(final Event.Category category, int range) {
        final String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_CAPACITY,
                COLUMN_EVENT_CATEGORY,
                COLUMN_EVENT_LOCATION,
                EVENT_ORGANIZER_ID,
                COLUMN_EVENT_DESCRIPTION,
                COLUMN_EVENT_STATUS
        };

        final String myFormat = "yyyy-MM-dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        final Date now = new Date();
        final String today = sdf.format(now);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_YEAR, range);
        final Date date = calendar.getTime();
        final String futureDate = sdf.format(date);

        final List<Event> eventList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_EVENT_CATEGORY + " = ? AND " + COLUMN_EVENT_DATE + " > ? AND " + COLUMN_EVENT_DATE + " < ?";
        final String[] selectArgs = {category.toString(), today, futureDate};
        final String order = COLUMN_EVENT_DATE;

        final Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                selection,                   //columns for the WHERE clause
                selectArgs,                  //The values for the WHERE clause
                null,                //group the rows
                null,                 //filter by row groups
                order); //The sort order

        if (cursor.moveToFirst()) {
            do {
                final Event event = eventFromCursor(cursor);
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    public List<Event> getAllEvent() {
        final String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_CAPACITY,
                COLUMN_EVENT_CATEGORY,
                COLUMN_EVENT_LOCATION,
                EVENT_ORGANIZER_ID,
                COLUMN_EVENT_DESCRIPTION,
                COLUMN_EVENT_STATUS
        };

        final String sortOrder = COLUMN_EVENT_NAME + " ASC";
        final List<Event> eventList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                null,               //columns for the WHERE clause
                null,           //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                sortOrder);                 //The sort order

        if (cursor.moveToFirst()) {
            do {
                final Event event = eventFromCursor(cursor);
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return eventList;
    }

    public List<Event> getEventByOrganizer(final int organizerId) {
        final String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_CAPACITY,
                COLUMN_EVENT_CATEGORY,
                COLUMN_EVENT_LOCATION,
                EVENT_ORGANIZER_ID,
                COLUMN_EVENT_DESCRIPTION,
                COLUMN_EVENT_STATUS
        };

        final String selection = EVENT_ORGANIZER_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(organizerId)};
        final String sortOrder = COLUMN_EVENT_DATE + " ASC";
        final List<Event> eventList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                selection,                   //columns for the WHERE clause
                selectionArgs,               //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                sortOrder);                  //The sort order

        if (cursor.moveToFirst()) {
            do {
                final Event event = eventFromCursor(cursor);
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventList;
    }

    public void updateEvent(final Event event) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getName());
        values.put(COLUMN_EVENT_DATE, event.getDate());
        values.put(COLUMN_EVENT_TIME, event.getTime());
        values.put(COLUMN_EVENT_CAPACITY, event.getCapacity());
        values.put(COLUMN_EVENT_CATEGORY, event.getCategory());
        values.put(COLUMN_EVENT_LOCATION, event.getLocation());
        values.put(EVENT_ORGANIZER_ID, event.getOrganizerId());
        values.put(COLUMN_EVENT_DESCRIPTION, event.getCapacity());
        values.put(COLUMN_EVENT_STATUS, event.getStatus());

        db.update(TABLE_NAME, values, COLUMN_EVENT_ID + " = ?", new String[] { String.valueOf(event.getId()) });
        db.close();
    }

    public void deleteEvent(final Event event) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_EVENT_ID + " = ?", new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public boolean tableEmpty() {
        final String count = "SELECT count(*) FROM" + TABLE_NAME;
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        final int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();

        return icount <= 0;
    }

    public Event getEventById(final int eventId) {
        // array of columns to fetch
        final String[] columns = {
                COLUMN_EVENT_ID,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_DATE,
                COLUMN_EVENT_TIME,
                COLUMN_EVENT_CAPACITY,
                COLUMN_EVENT_CATEGORY,
                COLUMN_EVENT_LOCATION,
                EVENT_ORGANIZER_ID,
                COLUMN_EVENT_DESCRIPTION,
                COLUMN_EVENT_STATUS
        };

        final String selection = COLUMN_EVENT_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(eventId)};
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                     //columns to return
                selection,                   //columns for the WHERE clause
                selectionArgs,               //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                null);               //The sort order

        cursor.moveToFirst();
        final Event event = eventFromCursor(cursor);

        cursor.close();
        db.close();

        return event;
    }

    private static Event eventFromCursor(final Cursor cursor) {
        final Event event = new Event();
        event.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_EVENT_ID)));
        event.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_NAME)));
        event.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DATE)));
        event.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_TIME)));
        event.setCapacity(cursor.getInt(cursor.getColumnIndex(COLUMN_EVENT_CAPACITY)));
        event.setCategory(Event.Category.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_CATEGORY))));
        event.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_LOCATION)));
        event.setOrganizerId(cursor.getInt(cursor.getColumnIndex(EVENT_ORGANIZER_ID)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DESCRIPTION)));
        event.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_EVENT_STATUS)));
        return event;
    }
}
