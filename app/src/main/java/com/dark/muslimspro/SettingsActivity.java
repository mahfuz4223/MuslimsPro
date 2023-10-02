package com.dark.muslimspro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private Spinner hijriAdjustmentSpinner;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        hijriAdjustmentSpinner = findViewById(R.id.hijriAdjustmentSpinner);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.hijri_adjustment_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        hijriAdjustmentSpinner.setAdapter(adapter);

        // Set a listener to save the selected item to SharedPreferences when an item is selected
        hijriAdjustmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Save the selected item to SharedPreferences
               // saveHijriAdjustment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set up the bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item selection based on the item ID
                switch (item.getItemId()) {
                    case R.id.menu_qibla:
                        // Start QiblaActivity
                        startActivity(QiblaActivity.class);
                        break;
                    case R.id.menu_home:
                        startActivity(MainActivity.class);
                        break;
                    case R.id.menu_settings:
                        // Do nothing as we are already in the settings activity
                        break;
                    // Add more cases for additional menu items as needed
                }
                return true; // Return true to indicate that the item has been selected
            }
        });

        // Set the "Settings" menu item as active
        bottomNavigationView.setSelectedItemId(R.id.menu_settings);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected adjustment from hijriAdjustmentSpinner
                String selectedAdjustment = hijriAdjustmentSpinner.getSelectedItem().toString();

                // Save the selected adjustment to SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("prayer_settings", Context.MODE_PRIVATE).edit();
                editor.putString("hijriAdjustment", selectedAdjustment);
                editor.apply();

                // Return to MainActivity
                startActivity(MainActivity.class);
            }
        });
    }


    // Helper method to start activities
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

}
