package com.trainsleeperbeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.NamedLocation;
import model.NamedLocationProvider;
import model.NsApiParser;

/**
 * @author Hendrik Werner
 * @author Jasper Haasdijk
 * @author Janne van Den Hout
 * @author Arne Diehl
 */
public class StationChoiceActivity extends AppCompatActivity {

    private List<NamedLocation> listNamedLocation;
    private AutoCompleteTextView stationChoice;
    private List<String> listStationNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_choice);
        stationChoice = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        listNamedLocation = (List<NamedLocation>) new NsApiParser().getLocations();
        listStationNames = new ArrayList<>();
        getStationNames();
        setupStationChoice();
    }

    protected void handleChooseButton(View v) {
        String enteredName = stationChoice.getText().toString();
        NamedLocation nl = findLocation(enteredName);
        if (nl == null) {
            Toast toast = Toast.makeText(this, String.format("%s is not a station.", enteredName), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(StationChoiceActivity.this, DistanceActivity.class);
            intent.putExtra("model.NamedLocation", nl);
            startActivity(intent);
        }
    }

    private void getStationNames() {
        for (NamedLocation location : listNamedLocation) {
            listStationNames.add(location.name);
        }
    }

    private NamedLocation findLocation(String stationName) {
        for (NamedLocation location : listNamedLocation) {
            if (location.name.equals(stationName)) {
                return location;
            }
        }
        return null;
    }

    private void setupStationChoice() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listStationNames);
        stationChoice.setAdapter(adapter);
        stationChoice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        handleChooseButton(v);
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
