package com.example.xin.meetup.map;

import android.Manifest;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.xin.meetup.database.Event;
import com.example.xin.meetup.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {
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

//    private GoogleMap mMap;

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

            googleMap.addMarker(mo);
        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(final GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
