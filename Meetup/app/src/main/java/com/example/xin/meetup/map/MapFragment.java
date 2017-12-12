package com.example.xin.meetup.map;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;

import com.example.xin.meetup.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MapFragment extends SupportMapFragment {
    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final int REQUEST_ERROR = 0;

    private ArrayList<MapLocation> locations;
    private GoogleApiClient gapiClient;

//    private GoogleMap mMap;

    public static MapFragment newInstance(final ArrayList<MapLocation> locations) {
        final MapFragment fragment = new MapFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.LOCATIONS_ARG, locations);
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
        locations = args.getParcelableArrayList(Constants.LOCATIONS_ARG);

        gapiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        gapiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        gapiClient.disconnect();
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
