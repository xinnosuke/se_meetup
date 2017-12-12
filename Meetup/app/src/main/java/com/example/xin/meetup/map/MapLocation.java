package com.example.xin.meetup.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MapLocation implements Parcelable {
    public final LatLng position;
    public final String title;

    public MapLocation(final LatLng position, final String title) {
        this.position = position;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(position, flags);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<MapLocation> CREATOR = new Parcelable.Creator<MapLocation>() {
        @Override
        public MapLocation createFromParcel(final Parcel source) {
            return new MapLocation(source);
        }

        @Override
        public MapLocation[] newArray(final int size) {
            return new MapLocation[size];
        }
    };

    private MapLocation(final Parcel in) {
        position = in.readParcelable(getClass().getClassLoader());
        title = in.readString();
    }
}
