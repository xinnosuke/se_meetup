package com.example.xin.meetup.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    public enum Category {Outdoor, Art, Food, Travel, Book, Movie, Sport}

    public final int id;
    public final String name;
    public final String location;
    public final String date;
    public final String time;
    public final int capacity;
    public final Category category;
    public final int organizerId;
    public final String description;
    public final int status;

    public Event(
            final int id,
            final String name,
            final String location,
            final String date,
            final String time,
            final int capacity,
            final Category category,
            final int organizerId,
            final String description,
            final int status)
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.category = category;
        this.organizerId = organizerId;
        this.description = description;
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(capacity);
        dest.writeString(category.toString());
        dest.writeInt(organizerId);
        dest.writeString(description);
        dest.writeInt(status);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(final Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(final int size) {
            return new Event[size];
        }
    };

    private Event(final Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        date = in.readString();
        time = in.readString();
        capacity = in.readInt();
        category = Event.Category.valueOf(in.readString());
        organizerId = in.readInt();
        description = in.readString();
        status = in.readInt();
    }
}
