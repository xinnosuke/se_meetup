package com.example.xin.meetup.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.Constants;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EventPageFragment extends Fragment {
    private int eventId;
    private int userId;
    private String userType;

    public static EventPageFragment newInstance(
            final int eventId,
            final int userId,
            final String userType)
    {
        final EventPageFragment fragment = new EventPageFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID_ARG, eventId);
        args.putInt(Constants.USER_ID_ARG, userId);
        args.putString(Constants.USER_TYPE_ARG, userType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        eventId = args.getInt(Constants.EVENT_ID_ARG);
        userId = args.getInt(Constants.USER_ID_ARG);
        userType = args.getString(Constants.USER_TYPE_ARG);

        getActivity().setResult(RESULT_CANCELED);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final DBHelper dbHelper = DBHelper.getInstance(getContext());
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

//        if (userType != null && userType.equals(Constants.USER_TYPE_GUEST)) {
//            final FloatingActionButton fb = rootView.findViewById(R.id.fab_rsvp);
//            fb.setVisibility(View.VISIBLE);
//            fb.bringToFront();
//            fb.setOnClickListener(view -> {
//                dbHelper.guestTable.addGuest(eventId, userId);
//                Toast.makeText(getContext(), "Woohoo!", Toast.LENGTH_SHORT).show();
//            });
//        }

        final Button deleteButton = rootView.findViewById(R.id.deleteEventButton);
        if (userType != null && userType.equals(Constants.USER_TYPE_ORGANIZER)) {
            deleteButton.setOnClickListener(view -> {
                dbHelper.eventTable.deleteEvent(eventId);

                final Activity activity = getActivity();
                activity.setResult(RESULT_OK);
                activity.finish();
            });
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
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
