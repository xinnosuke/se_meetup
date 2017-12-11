package com.example.xin.meetup.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

public class EventPageActivity extends SingleFragmentActivity {
    private int eventId;
    private int userId;
    private String userType;

    public static Intent createIntent(
            final Context context, final int eventId, final int userId, final String userType)
    {
        final Intent intent = new Intent(context, EventPageActivity.class);
        intent.putExtra(Constants.EVENT_ID_ARG, eventId);
        intent.putExtra(Constants.USER_ID_ARG, userId);
        intent.putExtra(Constants.USER_TYPE_ARG, userType);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        eventId = intentArgs.getInt(Constants.EVENT_ID_ARG);
        userId = intentArgs.getInt(Constants.USER_ID_ARG);
        userType = intentArgs.getString(Constants.USER_TYPE_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return EventPageFragment.newInstance(eventId, userId, userType);
    }
}
