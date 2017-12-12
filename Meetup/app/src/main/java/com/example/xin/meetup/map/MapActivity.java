package com.example.xin.meetup.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.SingleFragmentActivity;

import java.util.ArrayList;

public class MapActivity extends SingleFragmentActivity {
    private ArrayList<MapLocation> locations;

    public static Intent createIntent(final Context context, final ArrayList<MapLocation> locations) {
        final Intent intent = new Intent(context, MapActivity.class);
        intent.putParcelableArrayListExtra(Constants.LOCATIONS_ARG, locations);
        return intent;
    }

    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
        locations = intentArgs.getParcelableArrayList(Constants.LOCATIONS_ARG);
    }

    @Override
    protected Fragment createFragment() {
        return MapFragment.newInstance(locations);
    }
}
