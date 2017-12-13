package com.example.xin.meetup.event.as_organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

public class GuestListActivity extends SingleFragmentActivity {

    private int eventId;

    public static Intent createIntent(final Context context, final int eventId) {
        final Intent intent = new Intent(context, GuestListActivity.class);
        intent.putExtra(Constants.EVENT_ID_ARG, eventId);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        eventId = intentArgs.getInt(Constants.EVENT_ID_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return GuestListFragment.newInstance(eventId);
    }
}