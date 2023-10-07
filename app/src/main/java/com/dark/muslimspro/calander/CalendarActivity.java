package com.dark.muslimspro.calander;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dark.muslimspro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrayerTimeAdapter adapter;
    private List<PrayerTime> prayerTimesList = new ArrayList<>();

    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private TextView locationText;

    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrayerTimeAdapter(prayerTimesList);
        recyclerView.setAdapter(adapter);

        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        locationText = findViewById(R.id.location_text); // Location TextView

        double latitude = getIntent().getDoubleExtra("latitude", 0.0); // Provide a default value
        double longitude = getIntent().getDoubleExtra("longitude", 0.0); // Provide a default value

        fetchPrayerTimes(latitude, longitude);

        setupSpinners();

        // Fetch location and set it to locationText
        fetchLocation(latitude, longitude);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        List<String> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear - 10; i--) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Setting the current month and year based on the device system settings
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        monthSpinner.setSelection(currentMonth);
        yearSpinner.setSelection(0); // 0 corresponds to the current year

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMonth = position + 1;
                double latitude = getIntent().getDoubleExtra("latitude", 0.0); // Provide a default value
                double longitude = getIntent().getDoubleExtra("longitude", 0.0); // Provide a default value

                fetchPrayerTimes(latitude, longitude);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedYear = thisYear - position;
                double latitude = getIntent().getDoubleExtra("latitude", 0.0); // Provide a default value
                double longitude = getIntent().getDoubleExtra("longitude", 0.0); // Provide a default value

                fetchPrayerTimes(latitude, longitude);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    public void fetchPrayerTimes(double latitude, double longitude) {
        String url = "https://api.aladhan.com/v1/calendar/" + selectedYear + "?latitude=" + latitude + "&longitude=" + longitude + "&method=2";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            prayerTimesList.clear();
                            JSONObject dataObject = response.getJSONObject("data");
                            JSONArray monthArray = dataObject.getJSONArray(String.valueOf(selectedMonth));
                            for (int i = 0; i < monthArray.length(); i++) {
                                JSONObject dayObject = monthArray.getJSONObject(i);
                                JSONObject dateObject = dayObject.getJSONObject("date");
                                String date = dateObject.getString("readable");
                                JSONObject timingsObject = dayObject.getJSONObject("timings");
                                String fajr = timingsObject.getString("Fajr");
                                String dhuhr = timingsObject.getString("Dhuhr");
                                String asr = timingsObject.getString("Asr");
                                String maghrib = timingsObject.getString("Maghrib");
                                String isha = timingsObject.getString("Isha");

                                PrayerTime prayerTime = new PrayerTime(date, fajr, dhuhr, asr, maghrib, isha);
                                prayerTimesList.add(prayerTime);
                            }

                            adapter.notifyDataSetChanged();

                            // Scroll to the item with the current system date
                            scrollToCurrentDate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", error.toString());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void scrollToCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < prayerTimesList.size(); i++) {
            if (prayerTimesList.get(i).getDate().contains(String.valueOf(currentDay))) {
                final int finalPosition = i;
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(finalPosition);
                    }
                }, 100); // Delayed scrolling for a smoother effect
                break;
            }
        }
    }

    private void fetchLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                String locationName = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                locationText.setText(locationName);
            } else {
                locationText.setText("Location not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("Location not found");
        }
    }
}
