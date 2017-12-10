package com.example.xin.meetup.event.as_organizer;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.CustomItemClickListener;
import com.example.xin.meetup.main.EventPageFragment;
import com.example.xin.meetup.main.EventRecyclerAdapter;
import com.example.xin.meetup.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class YourEventListFragment extends Fragment {

    private int userId;
    private List<Event> listEvent;
    private DBHelper dbHelper;

    public static Fragment newInstance(final int userId) {
        final Fragment fragment = new YourEventListFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        userId = bundle.getInt(Constants.USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

        dbHelper = DBHelper.getInstance(getContext());
        listEvent = new ArrayList<>();

        getDataFromSQLite();

        final TextView noEventTextView = rootView.findViewById(R.id.empty_view);

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(final View view, final int position, final int eventId) {
                final FragmentManager fragmentManager = getFragmentManager();
                final Fragment eventPageFragment = EventPageFragment.newInstance(eventId, userId, "organizer");

                fragmentManager.beginTransaction()
                        .replace(R.id.event_list_fragment, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };

        final EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(listEvent, dbHelper, getFragmentManager(), listener);
        final RecyclerView recyclerViewEvent = rootView.findViewById(R.id.recycler_view_event);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvent.setLayoutManager(mLayoutManager);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvent.setHasFixedSize(true);
        recyclerViewEvent.setAdapter(eventRecyclerAdapter);

        if (listEvent.isEmpty()) {
            recyclerViewEvent.setVisibility(View.INVISIBLE);
            noEventTextView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewEvent.setVisibility(View.VISIBLE);
            noEventTextView.setVisibility(View.INVISIBLE);
        }

        final FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final FragmentManager fragmentManager = getFragmentManager();
                final Fragment fragment = CreateNewEventFragment.newInstance(userId);
                fragmentManager.beginTransaction()
                        .replace(R.id.event_list_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
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
