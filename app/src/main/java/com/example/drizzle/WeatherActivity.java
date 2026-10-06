package com.example.drizzle;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    TextView cityText;
    TextView weatherText;
    TextView rainProbabilityText;
    TextView riskText;
    TextView bestTimeText;
    TextView forecastText;

    double currentLat;
    double currentLng;

    double destinationLat;
    double destinationLng;

    String destinationName;

    private static final String API_KEY = "4e4b671406c7d70834af3edbcb64f80e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityText = findViewById(R.id.cityText);
        weatherText = findViewById(R.id.weatherText);
        rainProbabilityText = findViewById(R.id.rainProbabilityText);
        riskText = findViewById(R.id.riskText);
        bestTimeText = findViewById(R.id.bestTimeText);
        forecastText = findViewById(R.id.forecastText);

        // Current location
        currentLat = getIntent().getDoubleExtra(
                "current_lat",
                getIntent().getDoubleExtra("latitude", 0)
        );

        currentLng = getIntent().getDoubleExtra(
                "current_lng",
                getIntent().getDoubleExtra("longitude", 0)
        );

        // Destination
        destinationLat = getIntent().getDoubleExtra(
                "destination_lat",
                0
        );

        destinationLng = getIntent().getDoubleExtra(
                "destination_lng",
                0
        );

        destinationName = getIntent().getStringExtra(
                "destination_name"
        );

        if (destinationName == null ||
                destinationName.trim().isEmpty()) {

            destinationName = "Destination";
        }

        // Validate current location
        if (currentLat == 0 && currentLng == 0) {

            Toast.makeText(
                    this,
                    "Current location not available",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // If destination was not supplied,
        // show current location weather only.
        if (destinationLat == 0 &&
                destinationLng == 0) {

            getWeather(
                    currentLat,
                    currentLng,
                    "CURRENT LOCATION"
            );

            getForecast(
                    currentLat,
                    currentLng,
                    "CURRENT LOCATION"
            );

        } else {

            // Current location
            getWeather(
                    currentLat,
                    currentLng,
                    "CURRENT LOCATION"
            );

            // Destination
            getWeather(
                    destinationLat,
                    destinationLng,
                    "DESTINATION: " + destinationName
            );

            // Destination forecast
            getForecast(
                    destinationLat,
                    destinationLng,
                    "DESTINATION: " + destinationName
            );
        }
    }

    private void getWeather(
            double lat,
            double lon,
            String locationTitle) {

        String url =
                "https://api.openweathermap.org/data/2.5/weather"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&appid=" + "4e4b671406c7d70834af3edbcb64f80e"

                        + "&units=metric";

        RequestQueue queue =
                Volley.newRequestQueue(this);

        StringRequest request =
                new StringRequest(
                        Request.Method.GET,
                        url,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                String city =
                                        json.optString(
                                                "name",
                                                locationTitle
                                        );

                                JSONObject main =
                                        json.getJSONObject("main");

                                double temp =
                                        main.getDouble("temp");

                                int humidity =
                                        main.getInt("humidity");

                                JSONObject weather =
                                        json.getJSONArray("weather")
                                                .getJSONObject(0);

                                String condition =
                                        weather.getString(
                                                "description"
                                        );

                                String weatherInfo =
                                        locationTitle
                                                + "\n📍 "
                                                + city
                                                + "\n\n"
                                                + "🌡️ Temperature: "
                                                + String.format(
                                                Locale.getDefault(),
                                                "%.1f",
                                                temp
                                        )
                                                + " °C\n\n"
                                                + "☁️ Condition: "
                                                + condition
                                                + "\n\n"
                                                + "💧 Humidity: "
                                                + humidity
                                                + "%";

                                /*
                                 * If destination weather arrives,
                                 * show it separately.
                                 */
                                if (locationTitle.startsWith(
                                        "DESTINATION")) {

                                    weatherText.append(
                                            "\n\n--------------------\n\n"
                                                    + weatherInfo
                                    );

                                } else {

                                    weatherText.setText(
                                            weatherInfo
                                    );
                                }

                                /*
                                 * Update city title
                                 */
                                if (locationTitle.startsWith(
                                        "DESTINATION")) {

                                    cityText.setText(
                                            "🌦️ DRIZZLE WEATHER\n\n"
                                                    + "📍 From: Current Location"
                                                    + "\n🎯 To: "
                                                    + destinationName
                                    );
                                } else {

                                    cityText.setText(
                                            "🌦️ DRIZZLE WEATHER"
                                    );
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Weather data error: "
                                                + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        },

                        error -> {

                            String message =
                                    "Unable to get weather";

                            if (error.networkResponse != null) {

                                int status =
                                        error.networkResponse.statusCode;

                                if (status == 401) {
                                    message =
                                            "OpenWeather API key is invalid";
                                } else if (status == 404) {
                                    message =
                                            "Weather location not found";
                                } else if (status >= 500) {
                                    message =
                                            "OpenWeather server error";
                                }
                            }

                            Toast.makeText(
                                    this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );

        queue.add(request);
    }

    private void getForecast(
            double lat,
            double lon,
            String locationTitle) {

        String url =
                "https://api.openweathermap.org/data/2.5/forecast"
                        + "?lat=" + lat
                        + "&lon=" + lon
                        + "&appid=" + "4e4b671406c7d70834af3edbcb64f80e"
                        + "&units=metric";

        RequestQueue queue =
                Volley.newRequestQueue(this);

        StringRequest request =
                new StringRequest(
                        Request.Method.GET,
                        url,

                        response -> {

                            try {

                                JSONObject json =
                                        new JSONObject(response);

                                JSONArray list =
                                        json.getJSONArray("list");

                                StringBuilder forecast =
                                        new StringBuilder();

                                double highestRain = 0;

                                double lowestRain = 101;

                                String bestTime = "";

                                /*
                                 * Show first 8 forecast periods
                                 * (approximately next 24 hours).
                                 */
                                for (
                                        int i = 0;
                                        i < Math.min(
                                                8,
                                                list.length()
                                        );
                                        i++
                                ) {

                                    JSONObject item =
                                            list.getJSONObject(i);

                                    String date =
                                            item.getString(
                                                    "dt_txt"
                                            );

                                    double rain =
                                            item.optDouble(
                                                    "pop",
                                                    0
                                            ) * 100;

                                    double temp =
                                            item.getJSONObject(
                                                            "main"
                                                    )
                                                    .getDouble(
                                                            "temp"
                                                    );

                                    String condition =
                                            item.getJSONArray(
                                                            "weather"
                                                    )
                                                    .getJSONObject(0)
                                                    .getString(
                                                            "description"
                                                    );

                                    if (rain > highestRain) {
                                        highestRain = rain;
                                    }

                                    if (rain < lowestRain) {
                                        lowestRain = rain;
                                        bestTime = date;
                                    }

                                    forecast.append("\n")
                                            .append(date)
                                            .append("\n🌡️ ")
                                            .append(
                                                    String.format(
                                                            Locale.getDefault(),
                                                            "%.1f",
                                                            temp
                                                    )
                                            )
                                            .append(" °C")
                                            .append("\n🌧️ Rain: ")
                                            .append(
                                                    Math.round(rain)
                                            )
                                            .append("%")
                                            .append("\n☁️ ")
                                            .append(condition)
                                            .append("\n");
                                }

                                String forecastTitle =
                                        "\n\n📅 "
                                                + locationTitle
                                                + " FORECAST\n";

                                /*
                                 * Destination forecast
                                 */
                                if (locationTitle.startsWith(
                                        "DESTINATION")) {

                                    forecastText.append(
                                            forecastTitle
                                                    + forecast
                                    );

                                    rainProbabilityText.setText(
                                            "🌧️ Destination Highest Rain: "
                                                    + Math.round(
                                                    highestRain
                                            )
                                                    + "%"
                                    );

                                    setRisk(
                                            highestRain
                                    );

                                    bestTimeText.setText(
                                            "🚗 BEST TRAVEL TIME\n\n"
                                                    + formatDate(
                                                    bestTime
                                            )
                                                    + "\n\n"
                                                    + "Destination predicted rain: "
                                                    + Math.round(
                                                    lowestRain
                                            )
                                                    + "%"
                                    );

                                } else {

                                    /*
                                     * Current location forecast
                                     */
                                    forecastText.setText(
                                            "📅 CURRENT LOCATION FORECAST\n"
                                                    + forecast
                                    );
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Forecast error: "
                                                + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        },

                        error -> {

                            String message =
                                    "Unable to get forecast";

                            if (error.networkResponse != null) {

                                int status =
                                        error.networkResponse.statusCode;

                                if (status == 401) {
                                    message =
                                            "OpenWeather API key is invalid";
                                } else if (status == 404) {
                                    message =
                                            "Forecast location not found";
                                }
                            }

                            Toast.makeText(
                                    this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );

        queue.add(request);
    }

    private void setRisk(double rainProbability) {

        if (rainProbability >= 70) {

            riskText.setText(
                    "🔴 HIGH RAIN RISK"
            );

        } else if (rainProbability >= 40) {

            riskText.setText(
                    "🟡 MODERATE RAIN RISK"
            );

        } else {

            riskText.setText(
                    "🟢 LOW RAIN RISK"
            );
        }
    }

    private String formatDate(String value) {

        if (value == null ||
                value.isEmpty()) {

            return "No recommendation";
        }

        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                    );

            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            Locale.getDefault()
                    );

            Date date = input.parse(value);

            if (date != null) {
                return output.format(date);
            }

            return value;

        } catch (Exception e) {

            return value;
        }
    }
}