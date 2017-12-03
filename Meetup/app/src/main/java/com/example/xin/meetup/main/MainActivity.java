package com.example.xin.meetup.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.xin.meetup.R;
import com.example.xin.meetup.event.as_organizer.YourEventListFragment;
import com.example.xin.meetup.event.as_user.RegisteredEventFragment;
import com.example.xin.meetup.event.as_user.SearchForEventsFragment;
import com.example.xin.meetup.util.Constants;

public class MainActivity extends AppCompatActivity {

    private int userId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = RegisteredEventFragment.newInstance(userId);
                    break;
                case R.id.navigation_your_events:
                    fragment = YourEventListFragment.newInstance(userId);
                    break;
                case R.id.navigation_search_for_events:
                    fragment = SearchForEventsFragment.newInstance(userId);
                    break;
                default:
                    return false;
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_fragment, fragment)
                    .commit();
            return true;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle intentArgs = getIntent().getExtras();
        userId = intentArgs.getInt(Constants.USER_ID);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Fragment fragment = RegisteredEventFragment.newInstance(userId);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, fragment)
                .commit();
    }
}
