package com.example.bestizer.gpsidea;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_LOCATION_ID = 97;

    private Toast noInternetToast;
    private Toast noLocationPermissionToast;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void handleMapButton(View v) {
        if (hasFineLocationPermission()) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        } else {
            noLocationPermissionToast.show();
            requestFineLocation();
        }
    }

    public void handleTrainButton(View v) {
        if (networkAvailable() && hasFineLocationPermission()) {
            startActivity(new Intent(MainActivity.this, StationChoiceActivity.class));
        } else {
            if(!hasFineLocationPermission()) {
                noInternetToast.show();
                requestFineLocation();
            } else if (!networkAvailable()) {
                noInternetToast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternetToast = Toast.makeText(this, "No Internet Connection available.", Toast.LENGTH_SHORT);
        noLocationPermissionToast = Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT);
        requestFineLocation();
}

    private boolean hasFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestFineLocation() {
        if (!hasFineLocationPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION_ID
            );
        }
    }

    private boolean networkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
