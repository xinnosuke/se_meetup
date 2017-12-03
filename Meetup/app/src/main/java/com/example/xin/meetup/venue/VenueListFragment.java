package com.example.xin.meetup.venue;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.xin.meetup.database.Venue;
import com.example.xin.meetup.util.CustomItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class VenueListFragment extends Fragment {

    private List<Venue> venueList;
    private DBHelper dbHelper;

    public static Fragment newInstance() {
        return new VenueListFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_venue_list, container, false);

        dbHelper = DBHelper.getInstance(getContext());
        venueList = new ArrayList<>();

        getDataFromSQLite();

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(final View view, final int position, final int venueId) {
                final FragmentManager fragmentManager = getFragmentManager();
                final Fragment venuePageFragment = VenuePageFragment.newInstance(venueId);

                fragmentManager.beginTransaction()
                        .replace(R.id.venue_list_fragment, venuePageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };

        final TextView noEventTextView = rootView.findViewById(R.id.empty_view_venue);
        VenueRecyclerViewAdapter venueRecyclerViewAdapter = new VenueRecyclerViewAdapter(venueList, dbHelper, getFragmentManager(), listener);
        final RecyclerView recyclerViewVenue = rootView.findViewById(R.id.recycler_view_venue);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewVenue.setLayoutManager(mLayoutManager);
        recyclerViewVenue.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVenue.setHasFixedSize(true);
        recyclerViewVenue.setAdapter(venueRecyclerViewAdapter);

        if (venueList.isEmpty()) {
            recyclerViewVenue.setVisibility(View.INVISIBLE);
            noEventTextView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewVenue.setVisibility(View.VISIBLE);
            noEventTextView.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void getDataFromSQLite() {
        venueList.clear();
        venueList.addAll(dbHelper.venueTable.getAllVenue());
    }
}
