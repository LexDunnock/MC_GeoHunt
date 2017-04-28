package com.example.alexander.geocachehunt;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    FloatingActionButton fabOpenCamera;
    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    float lat;
    float lng;
    float treasureLat = 50.990038f;
    float treasureLng = 11.021039f;
    // Locations
    Location playerLocation = new Location("");
    Location treasureLocation = new Location("");

    LatLng player = new LatLng(0, 0);

    LatLng flur = new LatLng(50.990038, 11.021039);
    LatLng wohnzimmer = new LatLng(50.989977, 11.021039);
    LatLng fh = new LatLng(50.98521, 11.043764);
    LatLng vorfh = new LatLng(50.985113, 11.04378);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
           onLocationChanged(location);
        }else{
            Toast.makeText(getApplicationContext(), "Position not found", Toast.LENGTH_SHORT).show();
        }

        fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
        fabOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(android.os.Build.VERSION.SDK_INT >= 21) {
                    Toast.makeText(getApplicationContext(), "Camera.2 Package still not supported", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MapsActivity.this, CameraActivity.class));
                }
            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        treasureLat = (float)marker.getPosition().latitude;
        treasureLng = (float)marker.getPosition().longitude;
        PlayerClass.setCurrentMarker(getApplicationContext(), marker.getTitle());
        return false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        // get player location
        lat = (float)(location.getLatitude());
        lng = (float)(location.getLongitude());

        // saving position for autozoom to player location
        LatLng position = new LatLng(lat, lng);
        player = position;

        // setting up variables for distance calculation
        playerLocation.setLatitude(lat);
        playerLocation.setLongitude(lng);
        treasureLocation.setLatitude(treasureLat);
        treasureLocation.setLongitude(treasureLng);

        // calculate distance in Meters from player location to selected marker
        float distanceInMeters = playerLocation.distanceTo(treasureLocation);
        if(distanceInMeters > 25){
            //Toast.makeText(getApplicationContext(), "Not in range!", Toast.LENGTH_SHORT).show();
            fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
            fabOpenCamera.hide();
        } else {
            //Toast.makeText(getApplicationContext(), "Collect the treasure!", Toast.LENGTH_SHORT).show();
            // Device vibrates if marker/treasure is reachable and shows the camerabutton to open CameraActivity
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
            fabOpenCamera.show();
        }
        //Toast.makeText(getApplicationContext(), "Distance " + (int)distanceInMeters + " m", Toast.LENGTH_SHORT).show();
        }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(player, 16.0f));
        mMap.addMarker(new MarkerOptions().position(flur).title("Flur"));
        mMap.addMarker(new MarkerOptions().position(wohnzimmer).title("Wohnzimmer"));
        mMap.addMarker(new MarkerOptions().position(fh).title("FH"));
        mMap.addMarker(new MarkerOptions().position(vorfh).title("vor der FH"));
    }
}
