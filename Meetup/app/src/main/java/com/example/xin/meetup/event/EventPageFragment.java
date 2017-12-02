package com.example.xin.meetup.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.meetup.*;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.login.LoginActivity;

public class EventPageFragment extends Fragment {
    private static final String EVENT_ID = "EventId";
    private static final String USER_TYPE = "UserType";
    private int eventId;
    private String userType;

    public EventPageFragment() {
    }

    public static EventPageFragment newInstance(final int eventId) {
        final EventPageFragment fragment = new EventPageFragment();
        final Bundle args = new Bundle();
        args.putInt(EVENT_ID, eventId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getArguments().getInt(EVENT_ID);
        userType = getArguments().getString(USER_TYPE);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final DBHelper dbHelper = new DBHelper(getContext());
        final Event event = dbHelper.eventTable.getEventById(eventId);
        final int organizerId = event.getOrganizerId();
        final String organizerName = dbHelper.userTable.getUserName(organizerId);
        final String dateTime = event.getDate() + " " + event.getTime();
        final String eventName = event.getName();

        final View rootView = inflater.inflate(R.layout.fragment_event_page, container, false);
        final ImageView imageView = rootView.findViewById(R.id.event_image);
        final TextView eventNameTextView = rootView.findViewById(R.id.event_title);
        final TextView locationTextView = rootView.findViewById(R.id.event_location_textView);
        final TextView dataTimeTextView = rootView.findViewById(R.id.event_dateTime_textView);
        final TextView categoryTextView = rootView.findViewById(R.id.event_category_textView);
        final TextView capacityTextView = rootView.findViewById(R.id.event_capacity_textView);
        final TextView organizerTextView = rootView.findViewById(R.id.event_organizer_textView);
        final TextView descriptionTextView = rootView.findViewById(R.id.event_description_textView);

        eventNameTextView.setText(eventName);
        locationTextView.setText(event.getLocation());
        dataTimeTextView.setText(dateTime);
        categoryTextView.setText(event.getCategory());
        capacityTextView.setText(String.valueOf(event.getCapacity()));
        organizerTextView.setText(organizerName);
        descriptionTextView.setText(event.getDescription());

        if (userType.equals("user")) {
            final FloatingActionButton fb = rootView.findViewById(R.id.fab_rsvp);
            fb.setVisibility(View.VISIBLE);
            fb.bringToFront();
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBHelper dbHelper = DBHelper.getInstance(getContext());
                    dbHelper.guestTable.addGuest(eventId, LoginActivity.getUserId());
                    Toast.makeText(getContext(), "Woohoo!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        return rootView;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
