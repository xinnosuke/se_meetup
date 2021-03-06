package com.example.xin.meetup.event.as_organizer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.InputValidation;
import com.example.xin.meetup.venue.VenueListActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CreateNewEventFragment extends Fragment {

    public final static String CATEGORY_SELECT_STR = "Select category";

    private TextInputLayout textInputLayoutEventName;
    private TextInputLayout textInputLayoutLocation;
    private TextInputLayout textInputLayoutOrganizer;
    private TextInputLayout textInputLayoutDescription;
    private TextInputEditText textInputEditTextEventName;
    private TextInputEditText textInputEditTextLocation;
    private TextInputEditText textInputEditTextCapacity;
    private TextInputEditText textInputEditTextDescription;
    private Button buttonDate;
    private Button buttonTime;
    private String buttonTextDate = "";
    private String buttonTextTime = "";
    private AppCompatButton appCompatButtonCreateEvent;
    private AppCompatTextView appCompatTextViewViewVenue;
    private AppCompatTextView appCompatTextViewCancel;
    private InputValidation inputValidation;
    private DBHelper databaseHelper;
    private String category = "";
    private int userId;
    private final Calendar myCalendar = Calendar.getInstance();
    private final Calendar calendarTime = Calendar.getInstance();

    public static Fragment newInstance(final int userId) {
        final Fragment fragment = new CreateNewEventFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.USER_ID_ARG, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        userId = args.getInt(Constants.USER_ID_ARG);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
        textInputLayoutEventName = rootView.findViewById(R.id.textInputLayoutEventName);
        textInputLayoutLocation = rootView.findViewById(R.id.textInputLayoutLocation);
        textInputLayoutOrganizer = rootView.findViewById(R.id.textInputLayoutOrganizer);
        textInputLayoutDescription = rootView.findViewById(R.id.textInputLayoutDescription);

        textInputEditTextEventName = rootView.findViewById(R.id.textInputEditTextEventName);
        textInputEditTextLocation = rootView.findViewById(R.id.textInputEditTextLocation);
        textInputEditTextCapacity = rootView.findViewById(R.id.textInputEditTextCapacity);
        textInputEditTextDescription = rootView.findViewById(R.id.textInputEditTextDescription);

        buttonDate = rootView.findViewById(R.id.buttonDate);
        buttonTime = rootView.findViewById(R.id.buttonTime);
        buttonDate.setText(buttonTextDate);
        buttonTime.setText(buttonTextTime);

        appCompatButtonCreateEvent = rootView.findViewById(R.id.appCompatButtonCreateEvent);
        appCompatTextViewViewVenue  =rootView.findViewById(R.id.appCompatTextViewVenue);
        appCompatTextViewCancel = rootView.findViewById(R.id.appCompatTextViewCancel);

        final Spinner spinner = rootView.findViewById(R.id.spinner_event_category);

        final List<String> categories = new ArrayList<>();
        categories.add(CATEGORY_SELECT_STR);
        for (final Event.Category category : Event.Category.values()) {
            categories.add(category.toString());
        }

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdpwn);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                category = adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(), "Select", Toast.LENGTH_SHORT).show();
            }
        });

        initListeners();
        initObjects();

        return rootView;
    }

    private void initListeners() {
        buttonDate.setOnClickListener(view -> new DatePickerDialog(
                getContext(),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        buttonTime.setOnClickListener(view -> new TimePickerDialog(
                getContext(),
                time,
                calendarTime.get(Calendar.HOUR_OF_DAY),
                calendarTime.get(Calendar.MINUTE),
                true).show());

        appCompatButtonCreateEvent.setOnClickListener(view -> {
            if (createNewEvent()) {
                goBackToEventList(RESULT_OK);
            }
        });

        appCompatTextViewViewVenue.setOnClickListener(view -> {
            final Intent venueListIntent = VenueListActivity.createIntent(getContext());
            startActivity(venueListIntent);
        });

        appCompatTextViewCancel.setOnClickListener(view -> goBackToEventList(RESULT_CANCELED));
    }

    private void initObjects() {
        inputValidation = new InputValidation(getActivity());
        databaseHelper = DBHelper.getInstance(getActivity());
    }

    public boolean createNewEvent() {
        if (!validInput()) {
            appCompatButtonCreateEvent.setEnabled(true);
            return false;
        }
        else if (!didSelectCategory()) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_LONG).show();
            return false;
        }

        final String eventName = textInputEditTextEventName.getText().toString().trim();
        final String location = textInputEditTextLocation.getText().toString().trim();
        final int capacity = Integer.parseInt(textInputEditTextCapacity.getText().toString().trim());
        final String date = buttonTextDate;
        final String time = buttonTextTime;
        final String description = textInputEditTextDescription.getText().toString().trim();

        final Event event = new Event(
                -1,
                eventName,
                location,
                date.trim(),
                time.trim(),
                capacity,
                Event.Category.valueOf(category),
                userId,
                description,
                0);

        databaseHelper.eventTable.addEvent(event);

        return true;
    }

    private void goBackToEventList(final int resultCode) {
        final Activity activity = getActivity();
        activity.setResult(resultCode);
        activity.finish();
    }

    private boolean validInput() {
        boolean valid = true;

        if (!inputValidation.isInputEditTextFilled(textInputEditTextEventName, textInputLayoutEventName, "Enter Valid Name")) {
            valid = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextLocation, textInputLayoutLocation, "Enter Valid Location")) {
            valid = false;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextCapacity, textInputLayoutOrganizer, "Enter Valid Organizer")) {
            valid = false;
        }
        // TODO: buttonDate and buttonTime validation
//        if (!inputValidation.isInputEditTextFilled(buttonDate, "missing")) {
//            valid = false;
//        }
        return valid;
    }

    final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateLabel();
    };

    private void updateDateLabel() {
        final String myFormat = "yyyy-MM-dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonTextDate = sdf.format(myCalendar.getTime());
        buttonDate.setText(buttonTextDate);
    }

    final TimePickerDialog.OnTimeSetListener time = (view, hourOfDay, minute) -> {
        calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarTime.set(Calendar.MINUTE, minute);
        updateTimeLabel();
    };

    private void updateTimeLabel() {
        final String myFormat = "hh:mm a";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonTextTime = sdf.format(calendarTime.getTime());
        buttonTime.setText(buttonTextTime);
    }

    private boolean didSelectCategory() {
        return !category.equals(CATEGORY_SELECT_STR);
    }
}
