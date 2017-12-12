package com.example.xin.meetup.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.xin.meetup.util.SingleFragmentActivity;

public class MapActivity extends SingleFragmentActivity {
    @Override
    protected void processIntentArgs(final Bundle intentArgs) {
    }

    @Override
    protected Fragment createFragment() {
        return MapFragment.newInstance();
    }
}
