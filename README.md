# 🌧️ Drizzle – Intelligent Rain & Travel Prediction App

**Know the rain. Plan the journey.**

Drizzle is an intelligent Android mobile application designed to help users plan their journeys based on weather conditions and rainfall predictions. By combining live location, destination details, route information, and weather forecasts, the application aims to identify potential rainfall along a journey and recommend suitable departure times.

## 📌 Project Overview

Unexpected rainfall can disrupt daily commutes, college travel, and long-distance journeys. Checking the weather only at the current location may not provide enough information about conditions at the destination or along the route.

Drizzle addresses this problem by providing location-aware weather information and rain-risk analysis to support smarter travel planning.

## 🎯 Objectives

- Retrieve the user's current location using GPS.
- Allow users to specify their travel destination.
- Display current weather and rainfall information.
- Analyze expected rainfall conditions along a travel route.
- Calculate a rain-risk level for the planned journey.
- Recommend a suitable departure time based on available forecasts.
- Provide rain alerts and save frequently used routes.

## ✨ Key Features

### 1. User Authentication
- User registration and login.
- Secure authentication using Firebase Authentication.
- Logout functionality.

### 2. Live Location Detection
- Obtain the user's current location with permission.
- Display location-based weather information.
- Support destination selection.

### 3. Weather Information
- Current temperature and weather conditions.
- Rain probability and precipitation information.
- Humidity and other available weather parameters.
- Hourly weather forecasts.

### 4. Route-Based Rain Analysis
- Evaluate weather conditions at multiple points along a journey.
- Identify locations where rainfall is forecast.
- Display an overall rain-risk level.

### 5. Smart Travel-Time Recommendation
- Compare forecast conditions across possible departure times.
- Recommend a potentially more suitable travel window.
- Display the reason behind the recommendation.

### 6. Rain Alerts
- Notify users of forecast rainfall that may affect their journey.
- Provide relevant travel-weather warnings.

### 7. Saved Routes and Travel History
- Save frequently used routes.
- Store user preferences and travel history in Firebase Firestore.
- Retrieve saved information when required.

## 🛠️ Technology Stack

| Component | Technology |
|---|---|
| Development Environment | Android Studio |
| Programming Language | Java |
| User Interface | XML |
| Authentication | Firebase Authentication |
| Cloud Database | Firebase Firestore |
| Location Services | Android Location Services / GPS |
| Maps and Routing | Google Maps Platform |
| Weather Data | Weather API |
| Rain-Risk Analysis | Java-based calculation logic |
| Notifications | Firebase Cloud Messaging (optional) |

## 🔄 Application Workflow

1. The user registers or logs in.
2. The application requests location permission.
3. The user's current location is detected.
4. The user enters a destination.
5. The application retrieves route information.
6. Weather forecasts are obtained for relevant route locations and travel times.
7. The application calculates the journey's rain-risk level.
8. Potentially better departure times are evaluated.
9. The results are displayed with weather information and travel recommendations.
10. The user can save routes and receive relevant alerts.

## 🌧️ Rain-Risk Analysis

Drizzle can classify a journey into three levels:

- 🟢 **Low Risk:** Little or no significant rainfall is forecast.
- 🟡 **Moderate Risk:** Rain is possible at one or more points along the route.
- 🔴 **High Risk:** Significant rainfall is forecast during the journey.

The risk score can consider precipitation probability, forecast rainfall intensity, expected travel duration, and the proportion of the route affected.

The displayed risk is an estimate based on available weather data, not a guarantee of actual conditions.

## 📱 Proposed Application Structure

```text
Drizzle/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/drizzle/
│   │       │   ├── LoginActivity.java
│   │       │   ├── RegisterActivity.java
│   │       │   ├── MainActivity.java
│   │       │   ├── RouteActivity.java
│   │       │   ├── WeatherActivity.java
│   │       │   └── ProfileActivity.java
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   ├── drawable/
│   │       │   └── values/
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── README.md
└── build.gradle
```

*The structure may change as development progresses.*

## ⚙️ Setup and Installation

### Prerequisites

- Android Studio installed.
- JDK compatible with the selected Android Gradle Plugin.
- Android emulator or Android device.
- Google account for Firebase setup.
- API keys for the selected weather and mapping services.

### Installation Steps

1. Clone or download the project repository.
2. Open the project in Android Studio.
3. Allow Gradle synchronization to complete.
4. Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
5. Register the Android application using its exact package name.
6. Add the downloaded `google-services.json` file to the app module.
7. Enable Firebase Authentication with the Email/Password provider.
8. Set up Firebase Firestore and configure appropriate security rules.
9. Configure the required weather API and Google Maps credentials.
10. Add the required Android location permissions and runtime permission handling.
11. Build and run the application on an emulator or physical device.

**Important:** Never commit API secrets, private credentials, or unrestricted production keys to a public repository.

## 🔐 Permissions

Depending on the implemented features, Drizzle may require:

- `INTERNET` — access weather services and Firebase.
- `ACCESS_FINE_LOCATION` — precise location when permitted by the user.
- `ACCESS_COARSE_LOCATION` — approximate location.
- Notification permission on Android versions that require it.

Location access should be requested only when needed, and the app should provide useful functionality when permission is denied.

## 🚀 Future Enhancements

- Machine-learning-based rainfall prediction.
- Improved route-level precipitation analysis.
- Integration of live traffic conditions.
- Support for multiple travel modes.
- Customizable weather-alert thresholds.
- Rain-aware alternative-route suggestions.
- Multilingual support.
- Weather-map visualization.
- More detailed forecast confidence information.

## 🎓 Project Type

**Mobile Application Development (MAD)**

Drizzle demonstrates Android application development, cloud integration, location services, weather-data processing, and decision-support functionality.

## ⚠️ Disclaimer

Drizzle provides travel recommendations based on available forecasts and calculated risk estimates. Weather predictions can change, and the application cannot guarantee rain-free travel or road safety. Users should follow official weather warnings and local safety guidance.



