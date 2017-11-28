package com.example.xin.meetup.event;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.*;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder> implements View.OnClickListener {

    private List<Event> listEvent;
    private int eventId;
    private int organizerId;
    private DBHelper dbHelper;
    FragmentManager fragmentManager;
    CustomItemClickListener listener;


    public EventRecyclerAdapter(List<Event> listEvent, DBHelper databaseHelper, FragmentManager fragmentManager, CustomItemClickListener listener) {
        this.listEvent = listEvent;
        dbHelper = databaseHelper;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public Event event;
        public TextView textViewEventName;
        public TextView textViewDate;
        public TextView textViewLocation;
        public ImageView thumbnail;


        public EventViewHolder(View view, final CustomItemClickListener listener) {
            super(view);

            textViewEventName = view.findViewById(R.id.event_name);
            textViewDate = view.findViewById(R.id.event_date);
            textViewLocation = view.findViewById(R.id.event_location);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, getAdapterPosition(), eventId, organizerId);
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventRecyclerAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new EventViewHolder(itemView, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventRecyclerAdapter.EventViewHolder holder, int position) {
        eventId = listEvent.get(position).getId();
        organizerId = listEvent.get(position).getOrganizerId();
        holder.event = listEvent.get(position);
        holder.textViewEventName.setText(listEvent.get(position).getName());
        holder.textViewDate.setText(listEvent.get(position).getDate());
        holder.textViewLocation.setText(listEvent.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    @Override
    public void onClick(View view) {
    }
}

