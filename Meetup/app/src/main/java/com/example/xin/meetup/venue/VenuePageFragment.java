package com.example.xin.meetup.venue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Venue;
import com.example.xin.meetup.util.Constants;

public class VenuePageFragment extends Fragment {
    private int venueId;

    public static VenuePageFragment newInstance(final int venueId) {
        final VenuePageFragment fragment = new VenuePageFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.VENUE_ID_ARG, venueId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venueId = getArguments().getInt(Constants.VENUE_ID_ARG);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final DBHelper dbHelper = DBHelper.getInstance(getContext());
        final Venue venue = dbHelper.venueTable.getVenueById(venueId);

        final View rootView = inflater.inflate(R.layout.fragment_venue_page, container, false);
        final ImageView imageView = rootView.findViewById(R.id.venue_image);
        final TextView venueNameTextView = rootView.findViewById(R.id.venue_title);
        final TextView locationTextView = rootView.findViewById(R.id.venue_location_textView);
        final TextView costTextView = rootView.findViewById(R.id.venue_cost);
        final TextView phoneTextView = rootView.findViewById(R.id.venue_phone_textView);
        final TextView emailTextView = rootView.findViewById(R.id.venue_email_textView);
        final TextView hoursTextView = rootView.findViewById(R.id.venue_hours_extView);
        final TextView capacityTextView = rootView.findViewById(R.id.venue_capacity_textView);
        final TextView descriptionTextView = rootView.findViewById(R.id.venue_description_textView);

        final String hours = venue.getHoursOpen() + " ~ " + venue.getHoursClose();
        venueNameTextView.setText(venue.getVenueName());
        locationTextView.setText(venue.getLocation());
        costTextView.setText(venue.getCost());
        phoneTextView.setText(venue.getPhone());
        emailTextView.setText(venue.getEmail());
        hoursTextView.setText(hours);
        capacityTextView.setText(String.valueOf(venue.getCapacity()));
        descriptionTextView.setText(venue.getDescription());

        switch (venue.getVenueId()) {
            case 1:
                imageView.setImageResource(R.drawable.hudson_rooftop);
                break;
            case 2:
                imageView.setImageResource(R.drawable.greenpoint_cafe);
                break;
            default:
                imageView.setImageResource(R.drawable.greenpoint_cafe);
                break;
        }

        return rootView;
    }
}
