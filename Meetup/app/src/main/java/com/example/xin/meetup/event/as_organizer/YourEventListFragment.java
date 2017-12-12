package com.example.xin.meetup.event.as_organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.xin.meetup.event.EventPageActivity;
import com.example.xin.meetup.event.EventRecyclerAdapter;
import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class YourEventListFragment extends Fragment {

    private final static int REQUEST_CREATE_NEW_EVENT = 0;
    private final static int REQUEST_EDIT_EVENT = 1;

    private int userId;
    private final List<Event> listEvent = new ArrayList<>();
    private DBHelper dbHelper;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private RecyclerView recyclerViewEvent;
    private TextView noEventTextView;

    public static Fragment newInstance(final int userId) {
        final Fragment fragment = new YourEventListFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.USER_ID_ARG, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        userId = bundle.getInt(Constants.USER_ID_ARG);

        dbHelper = DBHelper.getInstance(getContext());
        getDataFromDB();
    }

    @Override @Nullable
    public View onCreateView(
            final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

        noEventTextView = rootView.findViewById(R.id.empty_view);

        final CustomItemClickListener listener = (view, position, eventId) -> {
            final Intent eventPageIntent = EventPageActivity.createIntent(getContext(), eventId, userId, Constants.USER_TYPE_ORGANIZER);
            startActivityForResult(eventPageIntent, REQUEST_EDIT_EVENT);
        };

        eventRecyclerAdapter = new EventRecyclerAdapter(listEvent, dbHelper, getFragmentManager(), listener);
        recyclerViewEvent = rootView.findViewById(R.id.recycler_view_event);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvent.setLayoutManager(mLayoutManager);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvent.setHasFixedSize(true);
        recyclerViewEvent.setAdapter(eventRecyclerAdapter);

        updateVisibility();

        final FloatingActionButton fab = rootView.findViewById(R.id.createEventButton);
        fab.bringToFront();
        fab.setOnClickListener(view -> {
            final Intent createNewEventIntent = CreateNewEventActivity.createIntent(getContext(), userId);
            startActivityForResult(createNewEventIntent, REQUEST_CREATE_NEW_EVENT);
        });

        return rootView;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE_NEW_EVENT:
            case REQUEST_EDIT_EVENT:
                if (resultCode == RESULT_OK) {
                    getDataFromDB();
                    updateVisibility();
                    eventRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void updateVisibility() {
        if (listEvent.isEmpty()) {
            recyclerViewEvent.setVisibility(View.INVISIBLE);
            noEventTextView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewEvent.setVisibility(View.VISIBLE);
            noEventTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void getDataFromDB() {
        if (!dbHelper.eventTable.tableEmpty()) {
            if (dbHelper.eventTable.organizerHasEvent(userId)) {
                listEvent.clear();
                listEvent.addAll(dbHelper.eventTable.getEventByOrganizer(userId));
            }
        }
    }
}
