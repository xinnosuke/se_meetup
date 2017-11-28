package com.example.xin.meetup.event;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.login.InputValidation;
import com.example.xin.meetup.R;
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
    private AppCompatTextView appCompatTextViewCancel;
    private InputValidation inputValidation;
    private DBHelper databaseHelper;
    private Event event;
    private String category = "";
    private int userId;
    Calendar myCalendar = Calendar.getInstance();
    Calendar calendarTime = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_activity, container, false);
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
        appCompatTextViewCancel = rootView.findViewById(R.id.appCompatTextViewCancel);

        Bundle bundle = getActivity().getIntent().getExtras();
        userId = bundle.getInt("UserId");
        // Spinner element
        Spinner spinner = rootView.findViewById(R.id.spinner_event_category);
        // Spinner Drop down elements
        final List<String> categories = new ArrayList<>();
        categories.add("Select");
        categories.add(Event.Category.Art.toString());
        categories.add(Event.Category.Outdoor.toString());
        categories.add(Event.Category.Food.toString());
        categories.add(Event.Category.Movie.toString());
        categories.add(Event.Category.Travel.toString());
        categories.add(Event.Category.Book.toString());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // On selecting a spinner item
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

        appCompatTextViewCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                goBackToEventList();
            }
        });
    }

    private void initObjects() {
        inputValidation = new InputValidation(getActivity());
        databaseHelper = new DBHelper(getActivity());
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

        String eventName = textInputEditTextEventName.getText().toString().trim();
        String location = textInputEditTextLocation.getText().toString().trim();
        int capacity = Integer.parseInt(textInputEditTextCapacity.getText().toString().trim());
        String date = buttonTextDate;
        String time = buttonTextTime;
        String description = textInputEditTextDescription.getText().toString().trim();

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

//    public void goBackToMainPage() {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.create_event_fragment, new RegisteredEventListFragment())
//                .addToBackStack(null)
//                .commit();
//    }

    public void goBackToEventList() {
        Intent intent = new Intent(getContext(), YourEventListFragment.class);
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
        if (!inputValidation.isInputEditTextFilled(buttonTextDate, "missing")) {
            valid = false;
        }
        return valid;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        buttonTextDate = sdf.format(myCalendar.getTime());
        buttonDate.setText(buttonTextDate);

    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarTime.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }
    };

    private void updateTimeLabel() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
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