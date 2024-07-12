package com.scopesoftware.quickresq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class Home1 extends AppCompatActivity  implements OnMapReadyCallback {

    private RelativeLayout emergencyId, hospitalId, ambulanceId, bloodId, policeStationId, fireServiceId, needHelpId, logoutId;

    private static final String PREFS_NAME = "MyAppPreferences";
    private static final String KEY_INPUT1 = "input1";
    private static final String KEY_INPUT2 = "input2";
    private static final String KEY_DIALOG_SHOWN = "dialogShown";

    public static boolean myselectFragment=true;

    //-----------------for location--------------------------------------------------------
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText;
    private Button shareButton;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String currentLocationString;
    private TextView upazillaText;
    private TextView locationText2;
    private String input1;
    private String input2;
//----------------------------------------------------
    ImageView banner21,shareId;
    //--------------------------------------------------------
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home1);

        emergencyId = findViewById(R.id.EmergencyId);
        hospitalId = findViewById(R.id.HospitalId);
        ambulanceId = findViewById(R.id.AmbulanceId);
        bloodId = findViewById(R.id.BloodId);
        policeStationId = findViewById(R.id.PoliceStationId);
        fireServiceId = findViewById(R.id.FireServiceId);
        needHelpId = findViewById(R.id.NeedHelpId);
        logoutId = findViewById(R.id.LogoutId);
        banner21=findViewById(R.id.banner21);
        shareId=findViewById(R.id.shareId);


        locationText = findViewById(R.id.location_text);
        shareButton = findViewById(R.id.share_button);
        upazillaText = findViewById(R.id.upazilla_text);
        locationText2 = findViewById(R.id.location_text2);

       //------------
        // Check if the dialog has been shown before
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean dialogShown = preferences.getBoolean(KEY_DIALOG_SHOWN, false);

        if (!dialogShown) {
            // Show the dialog to get inputs if it has not been shown before
            showInputDialog();
        } else {
            // Retrieve saved inputs if dialog was shown before
            input1 = preferences.getString(KEY_INPUT1, "");
            input2 = preferences.getString(KEY_INPUT2, "");

            // Use input1 and input2 wherever needed in YourActivity
            // Example:
            // updateUIWithInputs();
        }
        //-------------------

        banner21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home1.this, "Working soon for website go", Toast.LENGTH_SHORT).show();
            }
        });

        shareId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Home1.this, "Working when playstore uploaded", Toast.LENGTH_SHORT).show();



            }
        });
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
        //-----------------------------

        AllBtnClik();

    }  //----------onCreate end---------

    private void AllBtnClik () {

        // Add your onClickListeners or any other logic here
        emergencyId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Emergency2.class));
        });

        hospitalId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Hospital.class));
        });

        ambulanceId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Ambulance.class));
        });

        bloodId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Blood.class));
        });

        policeStationId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Police.class));
        });

        fireServiceId.setOnClickListener(v -> {
            startActivity(new Intent(Home1.this,Fire.class));
        });

        needHelpId.setOnClickListener(v -> {
           // showUpdateDialog();

            startActivity(new Intent(Home1.this,SubmitReport.class));
        });

        logoutId.setOnClickListener(v -> {

            showInputDialog();

           // startActivity(new Intent(Home1.this,MainActivity.class));
        });


    }

    //--------------------------------------------
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home1.this);
        builder.setTitle("Any Inquiry contact our mail");
        builder.setMessage("QuickResq247@gmail.com");

        builder.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "QuickResq247@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "write your oponion");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //---------------------------------------

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
                        Toast.makeText(Home1.this, "Failed to get location. Please try again later.", Toast.LENGTH_SHORT).show();
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
            Ambulance.current_location1 = String.valueOf(district);
            Fire.current_location2 = String.valueOf(district);
            Police.current_location3 = String.valueOf(district);
            Hospital.current_location4 = String.valueOf(district);
            Nearestblood.current_location5 = String.valueOf(district);
          //  Ambulance.current_location5 = String.valueOf(district);
         //   Ambulance.current_location6 = String.valueOf(district);

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
                Toast.makeText(Home1.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_inputs, null);
        builder.setView(dialogView);

        final EditText input1EditText = dialogView.findViewById(R.id.input1);
        final EditText input2EditText = dialogView.findViewById(R.id.input2);
      //  Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // Load saved inputs if they exist
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedInput1 = preferences.getString(KEY_INPUT1, "");
        String savedInput2 = preferences.getString(KEY_INPUT2, "");
        input1EditText.setText(savedInput1);
        input2EditText.setText(savedInput2);

        AlertDialog alertDialog = builder.create();



        btnConfirm.setOnClickListener(v -> {
            String input1 = input1EditText.getText().toString();
            String input2 = input2EditText.getText().toString();

            // Save inputs and flag to SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_INPUT1, input1);
            editor.putString(KEY_INPUT2, input2);
            editor.putBoolean(KEY_DIALOG_SHOWN, true);
            editor.apply();

            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    //--------------------------------------------------
    ///====================================================
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (myselectFragment) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Assuming you want to finish the activity

            } else {
                Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

        } else {
            // If not in myselectFragment mode
            super.onBackPressed(); // Call the default behavior for onBackPressed()
            // Perform additional actions if needed, like resetting views
            // bottomNavigationView.setBackground(null);
            //  apptitle.setText("Home Service Hub");

        }
    }
    //---------------------------------------
}