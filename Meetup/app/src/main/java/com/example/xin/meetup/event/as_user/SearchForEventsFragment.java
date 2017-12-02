package com.example.xin.meetup.event.as_user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.main.CustomItemClickListener;
import com.example.xin.meetup.main.EventPageFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchForEventsFragment extends Fragment {

    private String category = "";
    private int range;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search_events, container, false);

        final TextView noEventTextView = rootView.findViewById(R.id.empty_view);

        final CustomItemClickListener listener = new CustomItemClickListener() {
            public void onItemClick(final View view, final int position, final int eventId, final int userId) {
                final Bundle bundle1 = new Bundle();
                bundle1.putInt("EventId", eventId);
                bundle1.putInt("OrganizerId", userId);

                final FragmentManager fragmentManager = getFragmentManager();
                final Fragment eventPageFragment = new EventPageFragment();
                eventPageFragment.setArguments(bundle1);

                fragmentManager.beginTransaction()
                        .replace(R.id.search_event_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };

        final Spinner spinnerCategory = rootView.findViewById(R.id.spinner_category);
        final Spinner spinnerWhen = rootView.findViewById(R.id.spinner_when);

        final List<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add(Event.Category.Art.toString());
        categories.add(Event.Category.Outdoor.toString());
        categories.add(Event.Category.Food.toString());
        categories.add(Event.Category.Movie.toString());
        categories.add(Event.Category.Travel.toString());
        categories.add(Event.Category.Book.toString());

        final List<String> dateRange = new ArrayList<>();
        dateRange.add("When");
        dateRange.add("Today");
        dateRange.add("In a week");
        dateRange.add("In a month");

        final ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapterCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                category = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(), "Select", Toast.LENGTH_SHORT).show();
            }
        });

        final ArrayAdapter<String> dataAdapterWhen = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dateRange);
        dataAdapterWhen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerWhen.setAdapter(dataAdapterWhen);
        spinnerWhen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                switch (adapterView.getItemAtPosition(position).toString()) {
                    case "Today":
                        range = 0;
                        break;
                    case "In a week":
                        range = 7;
                        break;
                    case "In a month":
                        range = 30;
                        break;
                    default:
                        range = 0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(), "Select", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView searchButton = rootView.findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Bundle bundle = new Bundle();
                bundle.putString("Category", category);
                bundle.putInt("Range", range);

                final Fragment fragment = new SearchResultListFragment();
                fragment.setArguments(bundle);

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.search_result_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
