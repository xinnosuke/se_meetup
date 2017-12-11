package com.example.xin.meetup.venue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

public class VenuePageActivity extends SingleFragmentActivity {
    private int venueId;

    public static Intent createIntent(final Context context, final int venueId) {
        final Intent intent = new Intent(context, VenuePageActivity.class);
        intent.putExtra(Constants.VENUE_ID_ARG, venueId);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        venueId = intentArgs.getInt(Constants.VENUE_ID_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return VenuePageFragment.newInstance(venueId);
    }
}
