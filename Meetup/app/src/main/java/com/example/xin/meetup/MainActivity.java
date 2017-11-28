package com.example.xin.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.event.CustomItemClickListener;
import com.example.xin.meetup.event.EventPageFragment;
import com.example.xin.meetup.event.EventPageFragmentRegister;
import com.example.xin.meetup.event.EventRecyclerAdapter;
import com.example.xin.meetup.event.YourEventListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int userId;
    private List<Event> listEvent;
    private DBHelper dbHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt("UserId", userId);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("UserId", userId);

                    startActivity(intent);
                    return true;
                case R.id.navigation_your_events:
                    Intent intent2 = new Intent(MainActivity.this, YourEventListActivity.class);
                    intent2.putExtra("UserId", userId);
                    Toast.makeText(getApplicationContext(), " Your Event List", Toast.LENGTH_SHORT).show();

                    startActivity(intent2);
                    return true;
//                case R.id.navigation_search_for_events:
//                    Fragment eventFragment3 = new ();
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("UserId");

        setContentView(R.layout.activity_main);

        final BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dbHelper = new DBHelper(this);
        listEvent = new ArrayList<>();

        getDataFromSQLite();

        final TextView noEventTextView = findViewById(R.id.empty_view_registered);

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(View view, int position, int eventId, int userId) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("EventId", eventId);
                bundle1.putInt("UserId", userId);

                Toast.makeText(getApplication(), "EventId " + eventId, Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment eventPageFragment = new EventPageFragment();
                eventPageFragment.setArguments(bundle1);

                fragmentManager.beginTransaction()
                        .replace(R.id.frame_fragment, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };

        final EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(listEvent, dbHelper, getSupportFragmentManager(), listener);
        final RecyclerView recyclerViewEvent = findViewById(R.id.recycler_view_registered_event);
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
    }

    private void getDataFromSQLite() {
        if (!dbHelper.guestTable.tableEmpty()) {
            List<Integer> listEventId = new ArrayList<>();
            listEventId.addAll(dbHelper.guestTable.getMyEvents(userId));
            listEvent.clear();

            if (!listEventId.isEmpty()) {
                for (Integer eventId : listEventId) {
                    listEvent.add(dbHelper.eventTable.getEventById(eventId));
                }
            }
        }
    }
}
