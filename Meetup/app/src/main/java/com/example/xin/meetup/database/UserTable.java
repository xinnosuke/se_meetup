package com.example.xin.meetup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UserTable {

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private final SQLiteOpenHelper helper;

    public UserTable(final SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void addUser(final User user) {
        final SQLiteDatabase db = helper.getWritableDatabase();

        final ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public User getUser(final String email) {
        final String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        final String selection = COLUMN_USER_EMAIL + " = ?";
        final String[] selectionArgs = {email};
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        cursor.moveToFirst();
        final User user = userFromCursor(cursor);

        cursor.close();
        db.close();

        return user;
    }

    public User getUser(final int userId) {
        final String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        final String selection = COLUMN_USER_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                           //columns to return
                selection,                         //columns for the WHERE clause
                selectionArgs,                     //The values for the WHERE clause
                null,                     //group the rows
                null,                      //filter by row groups
                null);                    //The sort order

        cursor.moveToFirst();
        final User user = userFromCursor(cursor);

        cursor.close();
        db.close();

        return user;
    }

    public String getUserName(final int userId) {
        final String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        final String selection = COLUMN_USER_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));

        cursor.close();
        db.close();

        return userName;
    }

    public List<User> getAllUser() {
        final String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };

        final String sortOrder = COLUMN_USER_NAME + " ASC";
        final List<User> userList = new ArrayList<>();
        final SQLiteDatabase db = helper.getReadableDatabase();

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        if (cursor.moveToFirst()) {
            do {
                final User user = userFromCursor(cursor);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return userList;
    }

    public boolean checkUser(final String email) {
        final String[] columns = {COLUMN_USER_ID};
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_USER_EMAIL + " = ?";
        final String[] selectionArgs = {email};

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,              //group the rows
                null,               //filter by row groups
                null);              //The sort order

        final int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    public boolean checkUser(final String email, final String password) {
        final String[] columns = {COLUMN_USER_ID};
        final SQLiteDatabase db = helper.getReadableDatabase();
        final String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        final String[] selectionArgs = { email, password };

        final Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                null);              //The sort order

        final int cursorCount = cursor.getCount();

        cursor.close();
        db.close();

        return cursorCount > 0;
    }

    private static User userFromCursor(final Cursor cursor) {
        final User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        return user;
    }
}
