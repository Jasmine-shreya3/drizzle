package com.example.drizzle;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RouteActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    EditText destinationInput;
    Button findRouteButton;
    TextView routeInfo;

    GoogleMap googleMap;

    double currentLat;
    double currentLng;

    double destinationLat;
    double destinationLng;

    String destinationName = "";

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        destinationInput = findViewById(R.id.destinationInput);
        findRouteButton = findViewById(R.id.findRouteButton);
        routeInfo = findViewById(R.id.routeInfo);

        currentLat = getIntent().getDoubleExtra("latitude", 0);
        currentLng = getIntent().getDoubleExtra("longitude", 0);

        firestore = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findRouteButton.setOnClickListener(v -> findDestination());
    }

    private void findDestination() {

        String destination = destinationInput.getText()
                .toString()
                .trim();

        if (destination.isEmpty()) {
            Toast.makeText(
                    this,
                    "Enter destination",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        new Thread(() -> {

            try {

                Geocoder geocoder =
                        new Geocoder(
                                RouteActivity.this,
                                Locale.getDefault()
                        );

                List<Address> addresses =
                        geocoder.getFromLocationName(
                                destination,
                                1
                        );

                if (addresses == null || addresses.isEmpty()) {

                    runOnUiThread(() ->
                            Toast.makeText(
                                    this,
                                    "Destination not found",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );

                    return;
                }

                Address address = addresses.get(0);

                destinationLat = address.getLatitude();
                destinationLng = address.getLongitude();

                String detectedName =
                        address.getAddressLine(0);

                if (detectedName == null ||
                        detectedName.trim().isEmpty()) {

                    detectedName = destination;
                }

                destinationName = detectedName;

                String finalDestinationName = destinationName;

                runOnUiThread(() ->
                        showRoute(
                                finalDestinationName,
                                destinationLat,
                                destinationLng
                        )
                );

            } catch (IOException e) {

                runOnUiThread(() ->
                        Toast.makeText(
                                this,
                                "Could not find destination",
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }

        }).start();
    }

    private void showRoute(
            String destination,
            double destinationLat,
            double destinationLng) {

        if (googleMap == null) {
            return;
        }

        LatLng start =
                new LatLng(currentLat, currentLng);

        LatLng end =
                new LatLng(
                        destinationLat,
                        destinationLng
                );

        googleMap.clear();

        googleMap.addMarker(
                new MarkerOptions()
                        .position(start)
                        .title("Your Location")
        );

        googleMap.addMarker(
                new MarkerOptions()
                        .position(end)
                        .title("Destination")
        );

        googleMap.addPolyline(
                new PolylineOptions()
                        .add(start)
                        .add(end)
                        .width(10)
        );

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        end,
                        10
                )
        );

        float[] distance = new float[1];

        android.location.Location.distanceBetween(
                currentLat,
                currentLng,
                destinationLat,
                destinationLng,
                distance
        );

        double km = distance[0] / 1000;

        routeInfo.setText(
                "📍 Destination: " + destination +
                        "\n\n📏 Approx. Distance: " +
                        String.format(
                                Locale.getDefault(),
                                "%.2f km",
                                km
                        ) +
                        "\n\n🌦️ Weather available for destination"
        );

        saveRoute(
                destination,
                destinationLat,
                destinationLng,
                km
        );

        // Open destination weather
        openDestinationWeather(
                destination,
                destinationLat,
                destinationLng
        );
    }

    private void openDestinationWeather(
            String destination,
            double lat,
            double lng) {

        Intent intent =
                new Intent(
                        RouteActivity.this,
                        WeatherActivity.class
                );

        // Current location
        intent.putExtra(
                "current_lat",
                currentLat
        );

        intent.putExtra(
                "current_lng",
                currentLng
        );

        // Destination location
        intent.putExtra(
                "destination_lat",
                lat
        );

        intent.putExtra(
                "destination_lng",
                lng
        );

        intent.putExtra(
                "destination_name",
                destination
        );

        startActivity(intent);
    }

    private void saveRoute(
            String destination,
            double destinationLat,
            double destinationLng,
            double distance) {

        if (FirebaseAuth.getInstance().getCurrentUser()
                == null) {
            return;
        }

        String uid =
                FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid();

        java.util.Map<String, Object> route =
                new java.util.HashMap<>();

        route.put(
                "destination",
                destination
        );

        route.put(
                "startLatitude",
                currentLat
        );

        route.put(
                "startLongitude",
                currentLng
        );

        route.put(
                "destinationLatitude",
                destinationLat
        );

        route.put(
                "destinationLongitude",
                destinationLng
        );

        route.put(
                "distanceKm",
                distance
        );

        route.put(
                "timestamp",
                System.currentTimeMillis()
        );

        firestore.collection("users")
                .document(uid)
                .collection("routes")
                .add(route);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        LatLng current =
                new LatLng(
                        currentLat,
                        currentLng
                );

        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        current,
                        7
                )
        );

        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}