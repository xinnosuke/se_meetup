package com.example.xin.meetup.event.as_organizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

public class CreateNewEventActivity extends SingleFragmentActivity {
    private int userId;

    public static Intent createIntent(final Context context, final int userId) {
        final Intent intent = new Intent(context, CreateNewEventActivity.class);
        intent.putExtra(Constants.USER_ID_ARG, userId);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        userId = intentArgs.getInt(Constants.USER_ID_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return CreateNewEventFragment.newInstance(userId);
    }
}
