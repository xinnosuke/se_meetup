package com.example.xin.meetup.venue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.SingleFragmentActivity;

public class VenueListActivity extends SingleFragmentActivity {

    public static Intent createIntent(final Context context) {
        return new Intent(context, VenueListActivity.class);
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
    }

    @Override
    protected Fragment createFragment() {
        return VenueListFragment.newInstance();
    }
}
