package com.dark.muslimspro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dark.muslimspro.calander.CalendarActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Spinner spinner = findViewById(R.id.prayerMethodSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prayer_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the selected prayer method from SharedPreferences and set it in the spinner
        SharedPreferences sharedPref = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);
        String selectedMethod = sharedPref.getString("selectedMethod", "1");
        int position = adapter.getPosition(selectedMethod);
        spinner.setSelection(position);

        // Handle spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMethod = parentView.getItemAtPosition(position).toString();

                // Save selected prayer method in SharedPreferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("selectedMethod", selectedMethod);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Get the "Save" button and add an OnClickListener to save data
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save selected prayer method when the button is clicked
                String selectedMethod = spinner.getSelectedItem().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("selectedMethod", selectedMethod);
                editor.apply();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // Get the selected prayer method from SharedPreferences
//        SharedPreferences sharedPref = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);
//        String selectedMethod = sharedPref.getString("selectedMethod","");
//
//        // Send the selected prayer method back to MainActivity
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("selectedMethod", selectedMethod);
//        startActivity(intent); // Start the activity
//        finish(); // Finish the current activity
//    }


    @Override
    public void onBackPressed() {
        // Get the selected prayer method from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);
        String selectedMethod = sharedPref.getString("selectedMethod","");

        // Send the selected prayer method back to MainActivity
        Intent intent = new Intent();
        intent.putExtra("selectedMethod", selectedMethod);
        setResult(RESULT_OK, intent); // Set the result as OK
        finish(); // Finish the current activity
    }

}
