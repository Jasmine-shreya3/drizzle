package com.example.drizzle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    TextView locationText;
    Button destinationButton;
    Button weatherButton;
    Button profileButton;

    FusedLocationProviderClient locationClient;

    double latitude = 0;
    double longitude = 0;

    ActivityResultLauncher<String[]> locationPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> {

                        Boolean fine =
                                result.get(Manifest.permission.ACCESS_FINE_LOCATION);

                        Boolean coarse =
                                result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                        if (Boolean.TRUE.equals(fine) ||
                                Boolean.TRUE.equals(coarse)) {

                            getLocation();

                        } else {

                            Toast.makeText(this,
                                    "Location permission required",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.locationText);
        destinationButton = findViewById(R.id.destinationButton);
        weatherButton = findViewById(R.id.weatherButton);
        profileButton = findViewById(R.id.profileButton);

        locationClient =
                LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();

        destinationButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            RouteActivity.class);

            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);

            startActivity(intent);
        });

        weatherButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            WeatherActivity.class);

            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);

            startActivity(intent);
        });

        profileButton.setOnClickListener(v ->
                startActivity(new Intent(
                        MainActivity.this,

                        ProfileActivity.class
                ))
        );
    }

    private void checkLocationPermission() {

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            locationPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    });

        } else {

            getLocation();
        }
    }

    private void getLocation() {

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        locationText.setText(
                                "📍 Current Location\n\n" +
                                        "Latitude: " + latitude +
                                        "\nLongitude: " + longitude
                        );

                    } else {

                        locationText.setText(
                                "📍 Location unavailable\n" +
                                        "Turn on GPS and try again."
                        );
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationClient != null) {
            checkLocationPermission();
        }
    }
}