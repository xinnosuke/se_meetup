package com.example.xin.meetup.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.event.EventPageActivity;
import com.example.xin.meetup.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final int REQUEST_ERROR = 0;

    private int userId;
    private String userType;
    private ArrayList<Event> events;
    private Address[] addresses;
    private GoogleApiClient gapiClient;

    public static MapFragment newInstance(
            final int userId,
            final String userType,
            final ArrayList<Event> events)
    {
        final MapFragment fragment = new MapFragment();
        final Bundle args = new Bundle();
        args.putInt(Constants.USER_ID_ARG, userId);
        args.putString(Constants.USER_TYPE_ARG, userType);
        args.putParcelableArrayList(Constants.EVENTS_ARG, events);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        final GoogleApiAvailability gapiAvailability = GoogleApiAvailability.getInstance();
        final int errorCode = gapiAvailability.isGooglePlayServicesAvailable(getContext());

        if (errorCode != ConnectionResult.SUCCESS) {
            final Dialog errorDialog = gapiAvailability.getErrorDialog(getActivity(), errorCode, REQUEST_ERROR);
            errorDialog.show();
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        final Bundle args = getArguments();
        userId = args.getInt(Constants.USER_ID_ARG);
        userType = args.getString(Constants.USER_TYPE_ARG);
        events = args.getParcelableArrayList(Constants.EVENTS_ARG);

//        gapiClient = new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .build();

        final Geocoder geocoder = new Geocoder(getContext());

        addresses = new Address[events.size()];
        for (int i = 0; i < events.size(); ++i) {
            final Event event = events.get(i);
            final List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(event.location, 1);
            } catch (final IOException e) {
                e.printStackTrace();
                continue;
            }
            if (!addressList.isEmpty()) {
                addresses[i] = addressList.get(0);
            }
        }

        getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();

//        gapiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

//        gapiClient.disconnect();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);

        final LatLng nyit = new LatLng(40.769879, -73.982533);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nyit, 16));

        for (int i = 0; i < addresses.length; ++i) {
            final Address address = addresses[i];
            if (address == null) {
                continue;
            }

            final Event event = events.get(i);
            final LatLng ll = new LatLng(address.getLatitude(), address.getLongitude());
            final MarkerOptions mo = new MarkerOptions()
                    .position(ll)
                    .title(event.name);

            final Marker marker = googleMap.addMarker(mo);
            marker.setTag(event);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Object tag = marker.getTag();
        if (tag == null || !(tag instanceof Event)) {
            return false;
        }

        final Event event = (Event)tag;

        final Intent eventPageIntent = EventPageActivity.createIntent(
                getContext(), event.id, userId, userType);
        startActivity(eventPageIntent);

        return true;
    }
}
