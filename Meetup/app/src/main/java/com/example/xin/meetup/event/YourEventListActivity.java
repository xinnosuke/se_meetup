package com.example.xin.meetup.event;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.xin.meetup.MainActivity;
import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;

import java.util.ArrayList;
import java.util.List;

public class YourEventListActivity extends AppCompatActivity {

    private int userId;
    private List<Event> listEvent;
    private DBHelper dbHelper;
    private FloatingActionButton fab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt("UserId", userId);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(YourEventListActivity.this, MainActivity.class);
                    intent.putExtra("UserId", userId);
                    startActivity(intent);
                    return true;
                case R.id.navigation_your_events:
                    Intent intent2 = new Intent(YourEventListActivity.this, YourEventListActivity.class);
                    intent2.putExtra("UserId", userId);
                    startActivity(intent2);
                    return true;
//                case R.id.navigation_search_for_events:
//                    Fragment eventFragment3 = new RegisteredEventListFragment();
//                    eventFragment3.setArguments(bundle);
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.frame_fragment, eventFragment3)
//                            .commit();
//                    return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getInt("UserId");
        }

        setContentView(R.layout.activity_event_list);

        dbHelper = new DBHelper(this);
        listEvent = new ArrayList<>();

        getDataFromSQLite();

        final TextView noEventTextView = findViewById(R.id.empty_view);

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(View view, int position, int eventId, int userId) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("EventId", eventId);
                bundle1.putInt("OrganizerId", userId);

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment eventPageFragment = new EventPageFragment();
                eventPageFragment.setArguments(bundle1);

                fragmentManager.beginTransaction()
                        .replace(R.id.event_list_fragment, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };

        final EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(listEvent, dbHelper, getSupportFragmentManager(), listener);
        final RecyclerView recyclerViewEvent = findViewById(R.id.recycler_view_event);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewEvent.setLayoutManager(mLayoutManager);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvent.setHasFixedSize(true);
        recyclerViewEvent.setAdapter(eventRecyclerAdapter);

        if (listEvent.isEmpty()) {
            recyclerViewEvent.setVisibility(View.GONE);
            noEventTextView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewEvent.setVisibility(View.VISIBLE);
            noEventTextView.setVisibility(View.GONE);
        }

        fab = findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                final Fragment fragment = new CreateNewEventFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.event_list_fragment, fragment)
                        .commit();
            }
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void getDataFromSQLite() {
        if (!dbHelper.eventTable.tableEmpty()) {
            if (dbHelper.eventTable.organizerHasEvent(userId)) {
                listEvent.clear();
                listEvent.addAll(dbHelper.eventTable.getEventByOrganizer(userId));
            }
        }
    }
}
