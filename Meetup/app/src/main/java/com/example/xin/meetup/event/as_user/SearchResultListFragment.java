package com.example.xin.meetup.event.as_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.xin.meetup.event.CustomItemClickListener;
import com.example.xin.meetup.event.EventPageFragment;
import com.example.xin.meetup.event.EventRecyclerAdapter;
import com.example.xin.meetup.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultListFragment extends Fragment {

    private List<Event> listEvent;
    private DBHelper dbHelper;
    private String category;
    private int range;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("Category");
            range = bundle.getInt("Range");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

        dbHelper = DBHelper.getInstance(getContext());
        listEvent = new ArrayList<>();

        getDataFromSQLite();

        final TextView noEventTextView = rootView.findViewById(R.id.empty_view);

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(final View view, final int position, final int eventId, final int userId) {
                final Bundle bundle1 = new Bundle();
                bundle1.putInt("EventId", eventId);
                bundle1.putInt("OrganizerId", userId);
                bundle1.putString("UserType", "user");

                final FragmentManager fragmentManager = getFragmentManager();
                final Fragment eventPageFragment = new EventPageFragment();
                eventPageFragment.setArguments(bundle1);

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
        fab.setVisibility(View.GONE);

        return rootView;
    }

    private void getDataFromSQLite() {
        if (!dbHelper.eventTable.tableEmpty()) {
            listEvent.clear();
            listEvent.addAll(dbHelper.eventTable.getEventByCategoryAndDate(Event.Category.valueOf(category), range));
            for (Event event : listEvent) {
                if (dbHelper.guestTable.hasRegistered(event.getId(), LoginActivity.getUserId())) {
                    listEvent.remove(event);
                }
            }
        }
    }
}