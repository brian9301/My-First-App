package com.keane9301.myapp001.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.databinding.ActivityMapsBinding;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String USER_LAT = "userLat";
    public static String USER_LNG = "userLng";

    public double currentLat;
    public double currentLong;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;
    private double USER_LOCATION_LATITUDE;
    private double USER_LOCATION_LONGITUDE;
    private LatLng userLocation;
    private LatLng userCurrentLocation;
    private Marker userMarker;
    private MarkerOptions userLocationOption;
    private MarkerOptions userCurrentLocationOption;
    Location mLastLocation;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        getLastLocation();




        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }




    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // Check if permission are given
        if(checkPermissions()) {
            // Check if location is enabled
            if(isLocationEnabled()) {
                // Getting last location from FusedLocationClient object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location == null) {
                            RequestNewLocationData();
                        } else {
                            // Set latitude and longitude text to textview here
                            USER_LOCATION_LATITUDE = location.getLatitude();
                            USER_LOCATION_LONGITUDE = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(MapsActivity.this, "Please turn on your location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            // If permission is not available, request for permission.
            requestPermissions();
        }
    }




    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    // Check if user device has location enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }





    @SuppressLint("MissingPermission")
    private void RequestNewLocationData() {
        // Initializing request with appropriate object method
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.getFastestInterval();
        mLocationRequest.setNumUpdates(1);

        // Setting location request on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }






    // Request for permission to access location from user
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }




    private LocationCallback mLocationCallback = new LocationCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLastLocation = locationResult.getLastLocation();
            Log.d(AddEditCustomer.TAG, "onLocationResult: locationCallBack");
            // Set
            onLocationChanged(locationResult.getLastLocation());
        }
    };




    // If everything is fine then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_ID) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermissions()) {
            getLastLocation();
        }
    }
    // End for getting user location



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            currentLat = (location.getLatitude());
                            currentLong = (location.getLongitude());

                            userCurrentLocation = new LatLng(currentLat, currentLong);


                            userCurrentLocationOption = new MarkerOptions().position(userCurrentLocation).title("You're here. Latitude: "
                                    + currentLat + "; Longitude: " + currentLong)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addMarker(userCurrentLocationOption);
                            LatLng latLng = new LatLng(userCurrentLocationOption.getPosition().latitude, userCurrentLocationOption.getPosition().longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            Toast.makeText(MapsActivity.this, "lat " + location.getLatitude() + "\nlong " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }




    // Passing back latitude and longitude to previous page
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        String latitude = String.valueOf(currentLat);
        String longitude = String.valueOf(currentLong);
        intent.putExtra(USER_LAT, latitude);
        intent.putExtra(USER_LNG, longitude);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        Log.d(AddEditCustomer.TAG, "onComplete: " + USER_LOCATION_LATITUDE + ' ' + USER_LOCATION_LONGITUDE);
    }



    public void onLocationChanged(Location location) {
        mLastLocation = location;
        // use latitude and longitude given by
        // location.getLatitude(), location.getLongitude()
        // for updated location marker
        Log.d("DEBUG_TAG aaaaaaaa===>", "" + location.getLatitude() + "\n" + location.getLongitude());
        // displayLocation();

        // to remove old markers
        mMap.clear();
        final LatLng loc = new LatLng(location.getLongitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(loc).title("This is Me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
    }




}