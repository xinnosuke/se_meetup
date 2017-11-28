package com.example.xin.meetup.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xin.meetup.*;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;

public class EventPageFragment extends Fragment {
    private static final String EVENT_ID = "EventId";
    private int eventId;

    public EventPageFragment() {
    }

    public static EventPageFragment newInstance(int eventId) {
        EventPageFragment fragment = new EventPageFragment();
        Bundle args = new Bundle();
        args.putInt(EVENT_ID, eventId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getArguments().getInt(EVENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final DBHelper dbHelper = new DBHelper(getContext());
        final Event event = dbHelper.eventTable.getEventById(eventId);
        final int organizerId = event.getOrganizerId();
        String organizerName = dbHelper.userTable.getUserName(organizerId);
        String dateTime = event.getDate() + " " + event.getTime();
        String eventName = event.getName();

        View rootView = inflater.inflate(R.layout.fragment_event_page, container, false);
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

        return rootView;
    }

//     TODO: Rename method, update argument and hook method into UI event
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
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
