package com.example.xin.meetup.event.as_organizer;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xin.meetup.R;
import com.example.xin.meetup.database.DBHelper;
import com.example.xin.meetup.database.User;

import java.util.List;

public class GuestRecyclerAdapter extends RecyclerView.Adapter<GuestRecyclerAdapter.GuestViewHolder> {

    private final List<User> listGuest;
    private final DBHelper dbHelper;
    private final FragmentManager fragmentManager;

    public GuestRecyclerAdapter(final List<User> listGuest, final DBHelper databaseHelper, final FragmentManager fragmentManager) {
        this.listGuest = listGuest;
        dbHelper = databaseHelper;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public GuestRecyclerAdapter.GuestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_list_item, parent, false);

        return new GuestRecyclerAdapter.GuestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GuestRecyclerAdapter.GuestViewHolder holder, final int position) {
        holder.guest = listGuest.get(position);
        holder.textViewGuestName.setText(listGuest.get(position).getName());
        holder.textViewEmail.setText(listGuest.get(position).getEmail());
    }

    public class GuestViewHolder extends RecyclerView.ViewHolder {
        public User guest;
        private final AppCompatTextView textViewGuestName;
        private final AppCompatTextView textViewEmail;
        private final AppCompatImageView thumbnail;

        public GuestViewHolder(final View view) {
            super(view);

            textViewGuestName = view.findViewById(R.id.guest_name);
            textViewEmail = view.findViewById(R.id.guest_email);
            thumbnail = view.findViewById(R.id.thumbnail_guest);
        }
    }

    @Override
    public int getItemCount() {
        return listGuest.size();
    }
}