package com.example.xin.meetup.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

import java.util.ArrayList;

public class MapActivity extends SingleFragmentActivity {
    private int userId;
    private String userType;
    private ArrayList<Event> events;

    public static Intent createIntent(
            final Context context,
            final int userId,
            final String userType,
            final ArrayList<Event> events)
    {
        final Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(Constants.USER_ID_ARG, userId);
        intent.putExtra(Constants.USER_TYPE_ARG, userType);
        intent.putParcelableArrayListExtra(Constants.EVENTS_ARG, events);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        userId = intentArgs.getInt(Constants.USER_ID_ARG);
        userType = intentArgs.getString(Constants.USER_TYPE_ARG);
        events = intentArgs.getParcelableArrayList(Constants.EVENTS_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return MapFragment.newInstance(userId, userType, events);
    }
}
