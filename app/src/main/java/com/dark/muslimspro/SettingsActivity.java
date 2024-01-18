package com.dark.muslimspro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Spinner prayerMethodSpinner, locationSpinner;
    private RadioGroup madhabRadioGroup;
    private Button saveButton;

    private ArrayAdapter<CharSequence> adapter;
    Toolbar toolbar;  // Remove the redundant declaration here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prayerMethodSpinner = findViewById(R.id.prayerMethodSpinner);
        madhabRadioGroup = findViewById(R.id.madhab_radio_group);
        saveButton = findViewById(R.id.save_button);

        // Setup Spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.prayer_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prayerMethodSpinner.setAdapter(adapter);


        // Load saved settings
        loadSavedSettings();

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Read JSON data from raw resource
//        String jsonString = readJsonFromRawResource(this, R.raw.districts);
//
//        // Parse JSON data and populate spinner
//        if (jsonString != null) {
//            try {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                JSONArray districtsArray = jsonObject.getJSONArray("districts");
//
//                districtNames = new ArrayList<>();
//                for (int i = 0; i < districtsArray.length(); i++) {
//                    JSONObject districtObject = districtsArray.getJSONObject(i);
//                    String districtName = districtObject.getString("name");
//                    districtNames.add(districtName);
//                }
//
//                // Create an ArrayAdapter using the string array and a default spinner layout
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        this, android.R.layout.simple_spinner_item, districtNames);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                // Apply the adapter to the spinner
//                locationSpinner.setAdapter(adapter);
//
//                // Set item selected listener to save selected item to SharedPreferences
//                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                        String selectedDistrictName = districtNames.get(position);
//
//                        // Display Toast with "long", "lat", and "name" values
//                        displayToastForSelectedDistrict(districtsArray, selectedDistrictName);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parentView) {
//                        // Do nothing here
//                    }
//                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

        // Save Button Click Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }




    private String readJsonFromRawResource( Context context, int resourceId) {
        try {
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource (resourceId);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            return new String(b, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    private void loadSavedSettings( ) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String savedPrayerMethod = sharedPreferences.getString("PrayerMethod", "");
        String savedMadhab = sharedPreferences.getString("Madhab", "");

        if (!savedPrayerMethod.isEmpty()) {
            int spinnerPosition = adapter.getPosition(savedPrayerMethod);
            prayerMethodSpinner.setSelection(spinnerPosition);
        }

        if (savedMadhab.equals("Hanafi")) {
            madhabRadioGroup.check(R.id.radio_hanafi);
        } else if (savedMadhab.equals("Shafi")) {
            madhabRadioGroup.check(R.id.radio_shafi);
        }

    }

    private String saveSettings() {
        // Get selected prayer method
        String selectedPrayerMethod = prayerMethodSpinner.getSelectedItem().toString();


        // Get selected Madhab
        int selectedMadhabId = madhabRadioGroup.getCheckedRadioButtonId();
        String selectedMadhab = "";
        if (selectedMadhabId == R.id.radio_hanafi) {
            selectedMadhab = "HANAFI";
        } else if (selectedMadhabId == R.id.radio_shafi) {
            selectedMadhab = "SHAFI";
        }

        // Save data (using SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PrayerMethod", selectedPrayerMethod);
        editor.putString("Madhab", selectedMadhab);
        editor.apply();

        // Read JSON data from raw resource
        String jsonString = readJsonFromRawResource(this, R.raw.districts);



        return selectedPrayerMethod; // Return the selected prayer method if there is an issue with JSON parsing
    }




}
