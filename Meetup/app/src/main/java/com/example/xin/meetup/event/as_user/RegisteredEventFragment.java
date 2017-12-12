package com.example.xin.meetup.event.as_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class RegisteredEventFragment extends Fragment {

    private int userId;
    private List<Event> listEvent;
    private DBHelper dbHelper;

    public static Fragment newInstance(final int userId) {
        final Fragment fragment = new RegisteredEventFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_registered_event, container, false);

        dbHelper = DBHelper.getInstance(getContext());
        listEvent = new ArrayList<>();

        getDataFromSQLite();

        final TextView noEventTextView = rootView.findViewById(R.id.empty_view_registered);

        final CustomItemClickListener listener = (view, position, eventId) -> {
            final Intent eventPageIntent = EventPageActivity.createIntent(getContext(), eventId, userId, null);
            startActivity(eventPageIntent);
        };

        final EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(listEvent, dbHelper, getFragmentManager(), listener);
        final RecyclerView recyclerViewEvent = rootView.findViewById(R.id.recycler_view_registered_event);
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

        return rootView;
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
