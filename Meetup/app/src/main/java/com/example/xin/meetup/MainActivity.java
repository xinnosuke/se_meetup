package com.example.xin.meetup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.xin.meetup.event.as_user.RegisteredEventFragment;
import com.example.xin.meetup.event.as_user.SearchForEventsFragment;
import com.example.xin.meetup.event.as_organizer.YourEventListFragment;

public class MainActivity extends AppCompatActivity {

    private int userId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            final Bundle bundle = new Bundle();
            bundle.putInt("UserId", userId);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    final Fragment fragment = new RegisteredEventFragment();
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_fragment, fragment)
                            .commit();
                    return true;
                case R.id.navigation_your_events:
                    final Fragment fragment2 = new YourEventListFragment();
                    fragment2.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_fragment, fragment2)
                            .commit();
                    return true;
                case R.id.navigation_search_for_events:
                    Fragment eventFragment3 = new SearchForEventsFragment();
                    eventFragment3.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_fragment, eventFragment3)
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("UserId");
        setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Bundle bundle2 = new Bundle();
        bundle.putInt("UserId", userId);

        final Fragment fragment = new RegisteredEventFragment();
        fragment.setArguments(bundle2);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, fragment)
                .commit();
    }
}
