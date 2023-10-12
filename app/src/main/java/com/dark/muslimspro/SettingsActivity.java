package com.dark.muslimspro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity"; // Define a tag for your logs

    private Spinner spinner;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = findViewById(R.id.prayerMethodSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Create an ArrayAdapter and set it to the spinner
        SharedPreferences sharedPref = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);
        String selectedMethod = sharedPref.getString("selectedMethod", "0");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.prayer_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // Set a listener for item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Log the selected position when an item is selected


                // Save selected prayer method in SharedPreferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("selectedMethod", selectedMethod);
                editor.apply();
                Log.d(TAG, "Selected position: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case where nothing is selected
                Log.d(TAG, "No item selected");
            }
        });
    }



    @Override
    public void onBackPressed() {
        int selectedPosition = spinner.getSelectedItemPosition();
        Log.d(TAG, "Back button pressed. Selected position: " + selectedPosition);

        // Create an intent to return the selected position to MainActivity
        Intent intent = new Intent();
        intent.putExtra("selectedMethodIndex", selectedPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

}
