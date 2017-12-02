package com.example.xin.meetup.database;

public class Venue {
    private int venueId;
    private String venueName;
    private int ownerId;
    private String location;
    private int capacity;
    private String cost;
    private String hoursOpen;
    private String hoursClose;
    private String email;
    private String phone;
    private float rating;
    private String description;

    public Venue(String venueName, int ownerId, String location, int capacity, String cost,
                 String hoursOpen, String hoursClose, String email, String phone, float rating, String description) {
        this.venueName = venueName;
        this.ownerId = ownerId;
        this.location = location;
        this.capacity = capacity;
        this.cost = cost;
        this.hoursOpen = hoursOpen;
        this.hoursClose = hoursClose;
        this.email = email;
        this.phone = phone;
        this.rating = rating;
        this.description = description;
    }

    public Venue() {
        venueName = null;
        ownerId = -1;
        location = null;
        capacity = -1;
        cost = null;
        hoursOpen = null;
        hoursClose = null;
        email = null;
        phone = null;
        rating = -1;
        description = null;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(String hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    public String getHoursClose() {
        return hoursClose;
    }

    public void setHoursClose(String hoursClose) {
        this.hoursClose = hoursClose;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
