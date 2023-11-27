package com.dark.muslimspro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Spinner prayerMethodSpinner;
    private RadioGroup madhabRadioGroup;
    private Button saveButton;
    private ArrayAdapter<CharSequence> adapter; // Declare the adapter as a field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize components
        prayerMethodSpinner = findViewById(R.id.prayerMethodSpinner);
        madhabRadioGroup = findViewById(R.id.madhab_radio_group);
        saveButton = findViewById(R.id.save_button);

        // Setup Spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.prayer_methods, android.R.layout.simple_spinner_item); // Initialize the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prayerMethodSpinner.setAdapter(adapter);

        // Load saved settings
        loadSavedSettings();

        // Save Button Click Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void loadSavedSettings() {
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

    private void saveSettings() {
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

        // Send data to MainActivity
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("PrayerMethod", selectedPrayerMethod);
        intent.putExtra("Madhab", selectedMadhab);
        startActivity(intent);
    }
}
