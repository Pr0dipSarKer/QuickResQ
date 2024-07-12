package com.scopesoftware.quickresq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Emergency2 extends AppCompatActivity  implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final String PREFS_NAME = "MyAppPreferences";
    private static final String KEY_INPUT1 = "input1";
    private static final String KEY_INPUT2 = "input2";

    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText;
    private Button shareButton;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String currentLocationString;
    private TextView upazillaText;
    private TextView locationText2;
     String input1;
     String input2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_emergency2);
        locationText = findViewById(R.id.location_text);
        shareButton = findViewById(R.id.share_button);
        upazillaText = findViewById(R.id.upazilla_text);
        locationText2 = findViewById(R.id.location_text2);


        // Get the inputs from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
         input1 = preferences.getString(KEY_INPUT1, "");
         input2 = preferences.getString(KEY_INPUT2, "");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, get last location
            getLastLocation();
        }

        // Setup share button click listener
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLocation();
            }
        });
    }  //--onCraete end----

    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        updateMapLocation();
                        try {
                            reverseGeocodeLocation(location);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        updateLocationText();
                    } else {
                        Log.d("Emergency", "Location is null");
                        Toast.makeText(Emergency2.this, "Failed to get location. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void reverseGeocodeLocation(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            String currentLocation = address.getAddressLine(0); // Full address
            String placeName = address.getFeatureName(); // Place name (could be building, road, etc.)
            String upazilla = address.getSubLocality(); // Upazilla (sub-district)
            String district = address.getSubAdminArea(); // District
            String postalCode = address.getPostalCode(); // Postal code

            locationText.setText("Current Location: " + currentLocation);
            upazillaText.setText("District: " + district + " Bangladesh");

            // PoliceFragment.current_location = String.valueOf(district);

            if (placeName != null && !placeName.isEmpty()) {
                locationText2.setText(placeName);
            } else if (upazilla != null && !upazilla.isEmpty()) {
                upazillaText.setText(district + " Bangladesh");
            } else {
                upazillaText.setText("District: " + district + " Bangladesh");
            }

            Log.d("Emergency", "Current Location: " + currentLocation + ", Upazilla: " + upazilla + ", Postal Code: " + postalCode);
        } else {
            currentLocationString = "Lat: " + location.getLatitude() + " Lon: " + location.getLongitude();
            locationText.setText(currentLocationString);
            upazillaText.setText("District: " + currentLocationString + " Bangladesh");
            Log.d("Emergency", currentLocationString);
        }
    }

    private void shareLocation() {
        if (latitude != 0 && longitude != 0) {
            String locationUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            String emailBody = "Dear, now I am in danger. Please track my location and save me.\n\nLocation: " + locationUrl;
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{input1, input2});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Emergency: Please Help");
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(Emergency2.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLocationText() {
        // Optionally update location text if needed
    }

    private void updateMapLocation() {
        if (mMap != null) {
            LatLng currentLatLng = new LatLng(latitude, longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                // Handle permission denied
                Log.d("Emergency", "Location permission denied.");
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Enable user location tracking if permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Permission not granted, request it again
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }




}
