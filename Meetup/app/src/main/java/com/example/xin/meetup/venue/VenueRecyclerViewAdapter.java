package com.example.xin.meetup.venue;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.Venue;
import com.example.xin.meetup.main.CustomItemClickListener;

import java.util.List;

public class VenueRecyclerViewAdapter extends RecyclerView.Adapter<VenueRecyclerViewAdapter.VenueViewHolder> {

    private List<Venue> venueList;
    private DBHelper dbHelper;
    FragmentManager fragmentManager;
    CustomItemClickListener listener;


    public VenueRecyclerViewAdapter(final List<Venue> venueList, final DBHelper databaseHelper,
                                    final FragmentManager fragmentManager, final CustomItemClickListener listener) {
        this.venueList = venueList;
        dbHelper = databaseHelper;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    @Override
    public VenueViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.venue_list_item, parent, false);

        return new VenueViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(final VenueViewHolder holder, final int position) {
        holder.venueId = venueList.get(position).getVenueId();
        holder.venue = venueList.get(position);
        holder.textViewVenueName.setText(venueList.get(position).getVenueName());
        holder.textViewLocation.setText(venueList.get(position).getLocation());
        holder.textViewCost.setText(venueList.get(position).getCost());
    }

    public class VenueViewHolder extends RecyclerView.ViewHolder {
        public Venue venue;
        public int venueId;
        private AppCompatTextView textViewVenueName;
        private AppCompatTextView textViewCost;
        private AppCompatTextView textViewLocation;
        private AppCompatImageView imageView;


        public VenueViewHolder(final View view, final CustomItemClickListener listener) {
            super(view);
            textViewVenueName = view.findViewById(R.id.venue_list_name);
            textViewLocation = view.findViewById(R.id.venue_list_location);
            textViewCost = view.findViewById(R.id.venue_list_cost);
            imageView = view.findViewById(R.id.venue_list_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    listener.onItemClick(view, getAdapterPosition(), venueId, -1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }
}

