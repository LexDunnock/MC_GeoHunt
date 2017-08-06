package com.app.felix.geocachehuntv5;

//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////                      MIT GPS TRACKER

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import android.support.v4.content.ContextCompat;
import android.content.Context;
import com.google.android.gms.maps.*;

/**
 * Maps Activity: this activity shows the map. The users can see where his current location is and
 * where the markers are. He can set markers and collect markers.
 */
public class MapsActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    FloatingActionButton fabOpenCamera;
    LocationManager locationManager;
    private LocationListener mlocationListener;
    String provider;
    float lat;
    float lng;
    float treasureLat = 50.990038f;
    float treasureLng = 11.021039f;
    // Locations
    Location playerLocation = new Location("");
    Location treasureLocation = new Location("");

    LatLng player = new LatLng(0, 0);

    LatLng nordpark = new LatLng(50.993397, 11.018263);
    LatLng nordbad = new LatLng(50.993363, 11.019276);
    LatLng fh = new LatLng(50.98521, 11.043764);
    LatLng vorfh = new LatLng(50.985113, 11.04378);

    GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;

    private EditText inputMarkerName;
    TextView markerName;

    private Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /**
     * onCreate function sets the map with current position
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        // instance for gpstracker
        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        //mLocation = getLocation();

        // current Latitude und Longitude
        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        // SupportMapFragment can be used when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);


        // Permissions should set ...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            //}else{
            //    finish();
            }
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.

        }

        // Asking for last known location
        // if it is null, then OnLocationChanged starts
        // otherwise there comes a toast
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);//mlocationListener.onLocationChanged(location);//gpsTracker.onLocationChanged(location);
        } else {
            Toast.makeText(getApplicationContext(), "Position not found", Toast.LENGTH_SHORT).show();
        }

        fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
        fabOpenCamera.setOnClickListener(new View.OnClickListener() {
            /**
             * change the activity with clicking on the button
             * @param view that is what the user can see
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, CameraActivity.class));
            }
        });
    }

    @Override
    public void onStatusChanged (String provider, int status, Bundle extras){
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled (String provider){
        Toast.makeText(getApplicationContext(), "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled (String provider){
        Toast.makeText(getApplicationContext(), "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    /**
     * That method checks if the permissions are set.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * getLocation checks which provider is available
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
                // if location not found from gps, it is found from network
                if (location == null) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }

                }
            }
        }catch(Exception ex){

        }
        return location;
    }

    /**
     * information of marker
     *
     * @param marker: clicking on a marker
     * @return boolean
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        treasureLat = (float)marker.getPosition().latitude;
        treasureLng = (float)marker.getPosition().longitude;
        PlayerClass.setCurrentMarker(getApplicationContext(), marker.getTitle());
        return false;
    }

    /**
     * This method should set a new marker if the user make a long click on the map where the marker
     * should be.
     *
     * @param latlng: position x and y
     */
    @Override
    public void onMapLongClick(final LatLng latlng) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Please enter a markername");
        inputMarkerName = new EditText(MapsActivity.this);
        builder.setView(inputMarkerName);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            /**
             * a marker should be added for the player
             */
            @Override
            public void onClick(DialogInterface Dialog, int which) {
                mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .title(markerName.getText().toString()));
                PlayerClass.addMarker(getApplicationContext(), inputMarkerName.getText().toString());
                markerName.setText(PlayerClass.getMarker1(getApplicationContext()));
                //playerMarkerArray
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    /**
     * Die Funktion sollte die Position und Entfernung zum Marker berechnen. Sowie
     * sagt
     *
     * @param location actual position
     */
    @Override
    public void onLocationChanged(Location location) {
        // get player location
        lat = (float) (location.getLatitude());
        lng = (float) (location.getLongitude());

        // saving position for autozoom to player location
        LatLng position = new LatLng(lat, lng);
        player = position;

        // setting up variables for distance calculation
        playerLocation.setLatitude(lat);
        playerLocation.setLongitude(lng);
        treasureLocation.setLatitude(treasureLat);      // should be markerposition!
        treasureLocation.setLongitude(treasureLng);

        player = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());


        // calculate distance in meters from player location to selected marker
        float distanceInMeters = playerLocation.distanceTo(treasureLocation);
        if (distanceInMeters > 25) {
            //Toast.makeText(getApplicationContext(), "Not in range!", Toast.LENGTH_SHORT).show();
            fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
            fabOpenCamera.hide();
        } else {
            //Toast.makeText(getApplicationContext(), "Collect the treasure!", Toast.LENGTH_SHORT).show();
            // Device vibrates if marker/treasure is reachable and shows the camerabutton to open CameraActivity
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
            fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
            fabOpenCamera.show();
        }
        //Toast.makeText(getApplicationContext(), "Distance " + (int)distanceInMeters + " m", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method allows to continue with the map.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                //Request location updates:
                locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            // public void onRequestPermissionsResult ( int requestCode, String[] permissions,
//            // int[] grantResults){
//            //     if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            //         if (permissions.length == 1 &&
//            //                 permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//            //                 grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            //             mMap.setMyLocationEnabled(true);
//            //
//
//            //             // to handle the case where the user grants the permission. See the documentation
//            //             // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.removeUpdates(MapsActivity.this);
//    }

    /**
     * The function shows the map. There are some markers and a function to set a marker.
     * @param googleMap GoogleMaps is included
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setMyLocationEnabled(true);
        // neu 01.08.2017
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
                mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void onMapLongClick(final LatLng latlng) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Please enter a markername");
                inputMarkerName = new EditText(MapsActivity.this);
                builder.setView(inputMarkerName);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    /**
                     * add a new marker
                     */
                    @Override
                    public void onClick(DialogInterface Dialog, int which) {
                        PlayerClass.addMarker(getApplicationContext(), inputMarkerName.getText().toString());
                        markerName.setText(PlayerClass.getPlayerName(getApplicationContext()));
                        mMap.addMarker(new MarkerOptions()
                                .position(latlng)
                                .title(markerName.getText().toString()));
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                //Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
            }
        });

        fabOpenCamera = (FloatingActionButton) findViewById(R.id.CameraFAB);
        fabOpenCamera.setOnClickListener(new View.OnClickListener() {
            /**
             * change the activity with clicking on the button
             * @param view that is what the user can see
             */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, CameraActivity.class));
            }
        });

//        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick (Marker marker){
//                //Toast.makeText(MapsActivity.this, "You clicked a marker", Toast.LENGTH_SHORT).show();
//                treasureLat = (float) marker.getPosition().latitude;
//                treasureLng = (float) marker.getPosition().longitude;
//                PlayerClass.setCurrentMarker(getApplicationContext(), marker.getTitle());
//                return false;
//
//                //marker.remove();
//                //return true;
//            }
//
//        });

       //add marker and move the camera
        LatLng here = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(player, 10.0f));
        //mMap.addMarker(new MarkerOptions().position(here).title("Here I am"));
        mMap.addMarker(new MarkerOptions().position(nordpark).title("Nordpark"));
        mMap.addMarker(new MarkerOptions().position(nordbad).title("Nordbad"));
        mMap.addMarker(new MarkerOptions().position(fh).title("FH"));
        //mMap.addMarker(new MarkerOptions().position(vorfh).title("vor der FH"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 18.0f));
    }
}