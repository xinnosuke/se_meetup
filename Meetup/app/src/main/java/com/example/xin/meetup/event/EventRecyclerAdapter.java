package com.example.xin.meetup.event;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.CustomItemClickListener;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder> {

    private final List<Event> listEvent;
    private final DBHelper dbHelper;
    private final FragmentManager fragmentManager;
    private final CustomItemClickListener listener;

    public EventRecyclerAdapter(final List<Event> listEvent, final DBHelper databaseHelper,
                                final FragmentManager fragmentManager, final CustomItemClickListener listener) {
        this.listEvent = listEvent;
        dbHelper = databaseHelper;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    @Override
    public EventRecyclerAdapter.EventViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_item, parent, false);

        return new EventViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(final EventRecyclerAdapter.EventViewHolder holder, final int position) {
        holder.event = listEvent.get(position);
        holder.textViewEventName.setText(listEvent.get(position).name);
        holder.textViewDate.setText(listEvent.get(position).date);
        holder.textViewLocation.setText(listEvent.get(position).location);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public Event event;
        private final AppCompatTextView textViewEventName;
        private final AppCompatTextView textViewDate;
        private final AppCompatTextView textViewLocation;
        private final AppCompatImageView thumbnail;

        public EventViewHolder(final View view, final CustomItemClickListener listener) {
            super(view);

            textViewEventName = view.findViewById(R.id.event_name);
            textViewDate = view.findViewById(R.id.event_date);
            textViewLocation = view.findViewById(R.id.event_location);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    listener.onItemClick(view, getAdapterPosition(), event.id);
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        EventRecyclerAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }
}

