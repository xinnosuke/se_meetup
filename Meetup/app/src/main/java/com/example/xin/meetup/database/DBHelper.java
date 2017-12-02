package com.example.xin.meetup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static DBHelper dbhelper;
    private static final String DATABASE_NAME = "DBHelper.db";
    public final UserTable userTable;
    public final EventTable eventTable;
    public final GuestTable guestTable;

    public static synchronized DBHelper getInstance(final Context context) {
        if (dbhelper == null) {
            dbhelper = new DBHelper(context.getApplicationContext());
        }
        return dbhelper;
    }

    public DBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        userTable = new UserTable(this);
        eventTable = new EventTable(this);
        guestTable = new GuestTable(this);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        userTable.onCreate(db);
        eventTable.onCreate(db);
        guestTable.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        userTable.onUpgrade(db, oldVersion, newVersion);
        eventTable.onUpgrade(db, oldVersion, newVersion);
        guestTable.onUpgrade(db, oldVersion, newVersion);
    }
}
