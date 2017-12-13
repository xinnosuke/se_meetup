package com.example.xin.meetup.database;

public class Event {
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
}
