package com.example.xin.meetup.event.as_organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.Constants;
import com.example.xin.meetup.util.InputValidation;
import com.example.xin.meetup.venue.VenueListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateNewEventFragment extends Fragment {

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
    private static String buttonTextDate = "";
    private static String buttonTextTime = "";
    private AppCompatButton appCompatButtonCreateEvent;
    private AppCompatTextView appCompatTextViewViewVenue;
    private AppCompatTextView appCompatTextViewCancel;
    private InputValidation inputValidation;
    private DBHelper databaseHelper;
    private Event event;
    private String category = "";
    private int userId;
    private final Calendar myCalendar = Calendar.getInstance();
    private final Calendar calendarTime = Calendar.getInstance();

    public static Fragment newInstance(final int userId) {
        final Fragment fragment = new CreateNewEventFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        userId = args.getInt(Constants.USER_ID);
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
        buttonDate.setText(buttonTextTime);

        appCompatButtonCreateEvent = rootView.findViewById(R.id.appCompatButtonCreateEvent);
        appCompatTextViewViewVenue  =rootView.findViewById(R.id.appCompatTextViewVenue);
        appCompatTextViewCancel = rootView.findViewById(R.id.appCompatTextViewCancel);

        final Spinner spinner = rootView.findViewById(R.id.spinner_event_category);

        final List<String> categories = new ArrayList<>();
        categories.add("Select");
        categories.add(Event.Category.Art.toString());
        categories.add(Event.Category.Outdoor.toString());
        categories.add(Event.Category.Food.toString());
        categories.add(Event.Category.Movie.toString());
        categories.add(Event.Category.Travel.toString());
        categories.add(Event.Category.Book.toString());

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        buttonDate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonTime.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(getContext(), time,
                        calendarTime.get(Calendar.HOUR_OF_DAY),
                        calendarTime.get(Calendar.MINUTE), true).show();
            }
        });

        appCompatButtonCreateEvent.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (createNewEvent()) {
                    goBackToEventList();
                }
            }
        });

        appCompatTextViewViewVenue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = VenueListFragment.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.create_event_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        appCompatTextViewCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                goBackToEventList();
            }
        });
    }

    private void initObjects() {
        inputValidation = new InputValidation(getActivity());
        databaseHelper = DBHelper.getInstance(getActivity());
        event = new Event();
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

        event.setName(eventName);
        event.setLocation(location);
        event.setOrganizerId(userId);
        event.setCategory(Event.Category.valueOf(category));
        event.setCapacity(capacity);
        event.setDate(date.trim());
        event.setTime(time.trim());
        event.setDescription(description);

        databaseHelper.eventTable.addEvent(event);

        return true;
    }

    public void goBackToEventList() {
        final Intent intent = new Intent(getContext(), YourEventListFragment.class);
        startActivity(intent);
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

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void updateDateLabel() {
        final String myFormat = "yyyy-MM-dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonTextDate = sdf.format(myCalendar.getTime());
        buttonDate.setText(buttonTextDate);

    }

    final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarTime.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };

    private void updateTimeLabel() {
        final String myFormat = "hh:mm a";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonTextTime = sdf.format(calendarTime.getTime());
        buttonTime.setText(buttonTextTime);
    }

    private boolean didSelectCategory() {
        if (category.equals("Select")) {
            return false;
        }
        return true;
    }
}