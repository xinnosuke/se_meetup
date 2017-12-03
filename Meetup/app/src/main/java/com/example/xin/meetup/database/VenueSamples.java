package com.example.xin.meetup.database;

import java.util.ArrayList;
import java.util.List;

public class VenueSamples {

    public static List<Venue> getVenueSample() {
        final List<Venue> venueList = new ArrayList<>();

        final Venue venue1 = new Venue("Hudson Place", 0, "220 W 50th St, New York, NY 10036",
                100, "$$$", "11:00", "22:00",
                "hudsonplace@gmail.com", "917-256-8945", (float) 4.5,
                "Amazing view of Hudson River. Great for larger events. ");

        final Venue venue2 = new Venue("Greenpoint Cafe", 1, "920 Manhattan Ave, Brooklyn, NY 11222",
                20, "$$", "10:00", "22:00",
                "greencafe@gmail.com", "718-106-0245", (float) 5,
                "Cozy space and romantic back garden. Great wine selection.");

        venueList.add(venue1);
        venueList.add(venue2);

        return venueList;
    }
}
