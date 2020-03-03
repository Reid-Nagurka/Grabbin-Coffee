package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity {

//    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launchIntent();

//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);



    }


    private void launchIntent() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            System.out.println(locationResult);
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            Log.d("Location: ", location.toString());
                            if (location != null) {
                                String latitude = Double.toString(location.getLatitude());
                                String longititude = Double.toString(location.getLongitude());

                                String uri_parse_string = "geo:" + latitude + "," + longititude + "?q=coffee";
                                Log.d("uri", uri_parse_string);
                                Uri gmmIntentUri = Uri.parse(uri_parse_string);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }

                            }
                        }
                    });
    }
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        //get user location
//        //move camera to user location
//        //add markers on coffee shops
//
//        //used later to generate url search
//
////         Turn on the My Location layer and the related control on the map.
//        try {
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        }
//        catch (SecurityException e){
//            Log.e("Exception: %s", e.getMessage());
//        }
//        // Get the current location of the device and set the position of the map.
//        try {
//            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////            Task locationResult = mFusedLocationProviderClient.getLastLocation();
////            System.out.println(locationResult);
//
//            mFusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            Log.d("Location: ", location.toString());
//                            if (location != null) {
//                                // Logic to handle location object
//                                //        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                                LatLng curLoc = new LatLng(location.getLatitude(), location.getLongitude());
//                                mMap.addMarker(new MarkerOptions().position(curLoc).title("Current Location"));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(curLoc));
//
//
//                                // Instantiate the RequestQueue.
//                                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
//                                String a = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=coffee&inputtype=textquery&locationbias=circle:2000@";
//                                String b = Double.toString(curLoc.latitude) + Double.toString(curLoc.longitude);
//                                String c = "&key=AIzaSyA1zzMuOqUskY8IERIh87ylqh-Lolr1Phg";
//                                String url = a + b + c;
//                                //String url = https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=mongolian%20grill&inputtype=textquery&locationbias=circle:2000@47.6918452,-122.2226413&key=YOUR_API_KEY;
//
//// Request a string response from the provided URL.
//                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                                        new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//                                                // Display the first 500 characters of the response string.
//                                                Log.d("Response", response);
//                                            }
//                                        }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Log.d("Error Response", error.toString());
//                                    }
//                                });
//
//// Add the request to the RequestQueue.
//                                queue.add(stringRequest);
//
//
//
//                            }
//                        }
//                    });
//
//            // mark coffee locations near current location
//// ...
//
//
//
//
//
//
//
////            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
////                @Override
////                public void onComplete(@NonNull Task task) {
////                    if (task.isSuccessful()) {
////                        // Set the map's camera position to the current location of the device.
////                        mLastKnownLocation = task.getResult();
////                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
////                                new LatLng(mLastKnownLocation.getLatitude(),
////                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
////                    } else {
////                        Log.d(TAG, "Current location is null. Using defaults.");
////                        Log.e(TAG, "Exception: %s", task.getException());
////                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
////                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
////                    }
////                }
////            });
//
//
//        }
//
//        catch (SecurityException e){
//            Log.e("Exception: %s", e.getMessage());
//        }




    }




