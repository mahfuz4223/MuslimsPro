package com.dark.muslimspro.tools;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationPermissionHelper {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final Activity activity;

    public LocationPermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public boolean checkLocationPermission() {
        // Check if the location permission has been granted
        return ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        // Request the location permission from the user
        ActivityCompat.requestPermissions(
                activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    public boolean handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if the permission request was for location and if it was granted
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                return true;
            }
        }
        return false;
    }
}
