package com.example.xin.meetup.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.xin.meetup.event.as_organizer.GuestListActivity;
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
        final int organizerId = event.organizerId;
        final String organizerName = dbHelper.userTable.getUserName(organizerId);
        final String dateTime = event.date + " " + event.time;
        final String eventName = event.name;

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
        locationTextView.setText(event.location);
        dataTimeTextView.setText(dateTime);
        categoryTextView.setText(event.category.toString());
        capacityTextView.setText(String.valueOf(event.capacity));
        organizerTextView.setText(organizerName);
        descriptionTextView.setText(event.description);

        switch (event.category) {
            case Art:
                imageView.setImageResource(R.drawable.art);
                break;
            case Book:
                imageView.setImageResource(R.drawable.books);
                break;
            case Food:
                imageView.setImageResource(R.drawable.food_avocado);
                break;
            case Movie:
                imageView.setImageResource(R.drawable.popcorn);
                break;
            case Sport:
                imageView.setImageResource(R.drawable.yoga);
                break;
            case Travel:
                imageView.setImageResource(R.drawable.travel);
                break;
            case Outdoor:
                imageView.setImageResource(R.drawable.outdoor);
                break;
            default:
                imageView.setImageResource(R.drawable.popcorn);
                break;
        }

        final Button deleteButton = rootView.findViewById(R.id.deleteEventButton);
        final TextView viewGuestListTextView = rootView.findViewById(R.id.guestListTextView);
        final Button rsvpButton = rootView.findViewById(R.id.rsvpButton);

        if (userType != null && userType.equals(Constants.USER_TYPE_ORGANIZER)) {
            deleteButton.setOnClickListener(view -> {
                dbHelper.deleteEvent(eventId);

                final Activity activity = getActivity();
                activity.setResult(RESULT_OK);
                activity.finish();
            });

            viewGuestListTextView.setOnClickListener(view -> {
                final Intent viewGuestListIntent = GuestListActivity.createIntent(getContext(), eventId);
                startActivity(viewGuestListIntent);
            });

            rsvpButton.setVisibility(View.INVISIBLE);
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
            viewGuestListTextView.setVisibility(View.INVISIBLE);

            if (dbHelper.guestTable.hasRegistered(userId, eventId)) {
                rsvpButton.setText("Withdraw");
            } else {
                rsvpButton.setText("Join");
            }

            rsvpButton.setVisibility(View.VISIBLE);
            rsvpButton.bringToFront();
            rsvpButton.setOnClickListener(view -> {
                if (dbHelper.guestTable.hasRegistered(userId, eventId)) {
                    dbHelper.guestTable.deleteGuestFromEvent(userId, eventId);
                    rsvpButton.setText("Join");
                    Toast.makeText(getContext(), "Withdrawn from the event.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.guestTable.addGuest(eventId, userId);
                    rsvpButton.setText("Withdraw");
                    Toast.makeText(getContext(), "Woohoo!", Toast.LENGTH_SHORT).show();
                }
                getActivity().setResult(RESULT_OK);
            });
        }

        return rootView;
    }
}
