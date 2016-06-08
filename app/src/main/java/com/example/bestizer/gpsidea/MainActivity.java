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

    public static final int PERMISSIONS_REQUEST_LOCATION_ID_INITIAL = 97;
    public static final int PERMISSIONS_REQUEST_LOCATION_ID_TRAIN = 96;
    public static final int PERMISSIONS_REQUEST_LOCATION_ID_MAP = 95;

    private Toast noInternetToast;
    private Toast noLocationPermissionToast;

    public void handleMapButton(View v) {
        if (hasFineLocationPermission()) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        } else {
            noLocationPermissionToast.show();
            requestFineLocation(PERMISSIONS_REQUEST_LOCATION_ID_MAP);
        }
    }

    public void handleTrainButton(View v) {
        if (!tryMapsActivity()) {
            if(!hasFineLocationPermission()) {
                noLocationPermissionToast.show();
                requestFineLocation();
            } else if (!networkAvailable()) {
                noInternetToast.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION_ID_TRAIN) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(MainActivity.this, StationChoiceActivity.class));
            } else {
                noLocationPermissionToast.show();
            }
        } else if (requestCode == PERMISSIONS_REQUEST_LOCATION_ID_MAP) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            } else {
                noLocationPermissionToast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternetToast = Toast.makeText(this, "No Internet Connection available.", Toast.LENGTH_SHORT);
        noLocationPermissionToast = Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT);
        requestFineLocation(PERMISSIONS_REQUEST_LOCATION_ID_INITIAL);
}

    private boolean hasFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestFineLocation(int id) {
        if (!hasFineLocationPermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    id
            );
        }
    }

    private boolean networkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean tryMapsActivity() {
        if (networkAvailable() && hasFineLocationPermission()) {
            startActivity(new Intent(MainActivity.this, StationChoiceActivity.class));
            return true;
        } else {
            return false;
        }
    }

}
