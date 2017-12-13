package com.example.xin.meetup.event.as_organizer;

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
import com.example.xin.meetup.database.User;
import com.example.xin.meetup.util.Constants;

import java.util.ArrayList;

public class GuestListFragment extends Fragment {

    private int eventId;
    private final ArrayList<User> guestList = new ArrayList<>();
    private final ArrayList<Integer> guestIdList = new ArrayList<>();
    private DBHelper dbHelper;
    private RecyclerView recyclerViewEvent;
    private TextView noGuestTextView;

    public static Fragment newInstance(final int eventId) {
        final Fragment fragment = new GuestListFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID_ARG, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        eventId = bundle.getInt(Constants.EVENT_ID_ARG);
        dbHelper = DBHelper.getInstance(getContext());

        getDataFromDB();
    }

    @Override @Nullable
    public View onCreateView(
            final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_guest_list, container, false);

        noGuestTextView = rootView.findViewById(R.id.empty_view_guest);

        final GuestRecyclerAdapter guestRecyclerAdapter;
        guestRecyclerAdapter = new GuestRecyclerAdapter(guestList, dbHelper, getFragmentManager());

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvent = rootView.findViewById(R.id.recycler_view_guest);
        recyclerViewEvent.setLayoutManager(mLayoutManager);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvent.setHasFixedSize(true);
        recyclerViewEvent.setAdapter(guestRecyclerAdapter);

        updateVisibility();

        return rootView;
    }

    private void updateVisibility() {
        if (guestList.isEmpty()) {
            recyclerViewEvent.setVisibility(View.INVISIBLE);
            noGuestTextView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewEvent.setVisibility(View.VISIBLE);
            noGuestTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void getDataFromDB() {
        if (!dbHelper.guestTable.tableEmpty()) {
            guestIdList.clear();
            guestList.clear();
            guestIdList.addAll(dbHelper.guestTable.getGuestIdByEvent(eventId));
            for (int guestId : guestIdList) {
                guestList.add(dbHelper.userTable.getUser(guestId));
            }
        }
    }
}

