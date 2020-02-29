package reidnagurka.cs420.cs.wm.edu.grabbincoffee;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //get user location
        //move camera to user location
        //add markers on coffee shops

//         Turn on the My Location layer and the related control on the map.
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
        // Get the current location of the device and set the position of the map.
        try {
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//            Task locationResult = mFusedLocationProviderClient.getLastLocation();
//            System.out.println(locationResult);

            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            Log.d("Location: ", location.toString());
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });



//            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if (task.isSuccessful()) {
//                        // Set the map's camera position to the current location of the device.
//                        mLastKnownLocation = task.getResult();
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                new LatLng(mLastKnownLocation.getLatitude(),
//                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                    } else {
//                        Log.d(TAG, "Current location is null. Using defaults.");
//                        Log.e(TAG, "Exception: %s", task.getException());
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                    }
//                }
//            });


        }

        catch (SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }




    }
 }


