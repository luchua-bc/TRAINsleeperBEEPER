package com.example.bestizer.gpsidea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import model.AlarmLocation;
import model.NamedLocation;

public class DistanceActivity extends AppCompatActivity {

    private static final int MAX_DISTANCE = 100;

    private NumberPicker distancePicker;
    private NamedLocation namedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        distancePicker = (NumberPicker) findViewById(R.id.distancePicker);
        distancePicker.setMaxValue(MAX_DISTANCE);
        namedLocation = getIntent().getParcelableExtra("model.NamedLocation");
    }

    public void handleChooseButton(View v) {
        Intent intent = new Intent(DistanceActivity.this, AlarmActivity.class);
        intent.putExtra("model.AlarmLocation", new AlarmLocation(namedLocation, distancePicker.getValue() * 1000));
        startActivity(intent);
    }

}
