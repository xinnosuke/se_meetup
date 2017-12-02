package com.example.xin.meetup.event;

import android.view.View;

public interface CustomItemClickListener {
    void onItemClick(View view, int position, int eventId, int organizerId);
}
