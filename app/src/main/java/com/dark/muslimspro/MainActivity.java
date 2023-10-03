package com.dark.muslimspro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dark.muslimspro.calander.CalendarActivity;
import com.dark.muslimspro.prayertime.PrayerTimeAdapter;
import com.dark.muslimspro.prayertime.PrayerTimeApiService;
import com.dark.muslimspro.prayertime.PrayerTimeModel;
import com.dark.muslimspro.prayertime.PrayerTimeResponse;
import com.dark.muslimspro.prayertime.RetrofitClient;
import com.dark.muslimspro.tools.NetworkChangeReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private TextView locationText, sunriseText, sunsetText, hijri_holidays, nextPrayerTimeToGo, next_prayer_time, upcoming_prayer_name, current_prayer_time_name_Text, hidate_text, hijrimonth_text, hijriyear_text;
    private FusedLocationProviderClient fusedLocationClient;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private NetworkChangeReceiver networkChangeReceiver;
    private List<PrayerTimeModel> prayerTimes = new ArrayList<>();

    // RecyclerView related variables
    private RecyclerView recyclerView;
    private PrayerTimeAdapter adapter;
    private List<PrayerTimeModel> allPrayerTimes = new ArrayList<>();
    private int currentPosition = 0;
    private Handler handler = new Handler();

    private Timer scrollTimer;
    private boolean isAnimationRunning = false;
    private LinearLayoutManager layoutManager;
    private int scrollPosition = 0;
    private   MaterialCardView tasbihCardView, calanderView, compassCardView;

    private double latitude,longitude;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currentPosition < allPrayerTimes.size() - 1) {
                currentPosition++;
            } else {
                currentPosition = 0;
            }
            recyclerView.smoothScrollToPosition(currentPosition);
            handler.postDelayed(this, 3000); // Delay in milliseconds (3 seconds)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextView elements
        locationText = findViewById(R.id.location_text);
        sunriseText = findViewById(R.id.sunrise);
        sunsetText = findViewById(R.id.sunset);
        upcoming_prayer_name = findViewById(R.id.upcoming_prayer_name);
        hijri_holidays = findViewById(R.id.hijri_holidays);
        next_prayer_time = findViewById(R.id.upcoming_prayer_time);
        nextPrayerTimeToGo = findViewById(R.id.next_prayer_time_to_go);
        hidate_text = findViewById(R.id.hijri_date);
        hijrimonth_text = findViewById(R.id.hijri_month);
        hijriyear_text = findViewById(R.id.hijri_year);
        current_prayer_time_name_Text = findViewById(R.id.current_prayer_time_name);


        // Find the MaterialCardView with ID "tasbih"
        tasbihCardView = findViewById(R.id.tasbih);
        calanderView =  findViewById(R.id.calanderCard);
        compassCardView =  findViewById(R.id.compassCard);

        requestQueue = Volley.newRequestQueue(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        prayerTimes = new ArrayList<>();
        adapter = new PrayerTimeAdapter(prayerTimes);

        // Set the RecyclerView's layout manager and adapter
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Initialize the NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver(this);





        // Set an OnClickListener on the tasbihCardView
        tasbihCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the desired activity
                Intent intent = new Intent(MainActivity.this, TasbihActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });

        calanderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the CalendarActivity
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);

                // Pass the latitude and longitude as extras
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                // Start the new activity
                startActivity(intent);
            }
        });

        compassCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the CalendarActivity
                Intent intent = new Intent(MainActivity.this, QiblaActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Request location updates
            getLocation();
        }





        // If there is no network connection, retrieve data from SharedPreferences
        retrieveDataFromSharedPreferences();

        // Start the continuous scrolling animation
        startContinuousScrolling();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // User has finished scrolling, restart the animation
                    if (!isAnimationRunning) {
                        startContinuousScrolling();
                        isAnimationRunning = true;
                    }
                }
            }
        });
    }

    // Override the onPause method to stop the animation when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        stopContinuousScrolling();
    }

    // Add this method to start continuous scrolling
    private void startContinuousScrolling() {
        if (isAnimationRunning) {
            scrollPosition = 0; // Start from the first item (item0)
            scrollTimer = new Timer();
            scrollTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        scrollPosition = (scrollPosition + 1) % layoutManager.getItemCount();
                        recyclerView.smoothScrollToPosition(scrollPosition);
                    });
                }
            }, 0, 4000); // Scroll every 4 seconds
        }
    }

    // Add this method to stop the continuous scrolling
    private void stopContinuousScrolling() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer.purge();
        }
        isAnimationRunning = false;
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        // Fetch and update location text
                        String address = getAddressFromCoordinates(this, latitude, longitude);
                        locationText.setText(address);

                        // Fetch prayer time data
                        fetchLocationAndPrayerTimeData(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchLocationAndPrayerTimeData(double latitude, double longitude) {
        // Fetch prayer time data using RetrofitClient and update UI
        RetrofitClient.getClient().create(PrayerTimeApiService.class)
                .getPrayerTimes(latitude, longitude, "2")
                .enqueue(new Callback<PrayerTimeResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PrayerTimeResponse> call, @NonNull Response<PrayerTimeResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PrayerTimeResponse prayerTimeResponse = response.body();
                            String sunriseTime = prayerTimeResponse.getData().getTimings().getSunrise();
                            String sunsetTime = prayerTimeResponse.getData().getTimings().getSunset();
                            String hijri_date = prayerTimeResponse.getData().getDate().getHijri().getDay();
                            String hijri_month = prayerTimeResponse.getData().getDate().getHijri().getMonth().getEn();
                            String hijri_year = prayerTimeResponse.getData().getDate().getHijri().getYear();

                            String[] hijri_holiday = prayerTimeResponse.getData().getDate().getHijri().getHolidays();

                            if (hijri_holiday != null && hijri_holiday.length > 0) {
                                hijri_holidays.setText(hijri_holiday[0]);
                                hijri_holidays.setVisibility(View.VISIBLE);
                            } else {
                                hijri_holidays.setVisibility(View.GONE);
                            }

                            // Update the sunrise and sunset TextViews
                            sunriseText.setText(convertTo12HourFormat(sunriseTime));
                            sunsetText.setText(convertTo12HourFormat(sunsetTime));
                            hidate_text.setText(hijri_date  + " - " + 1);
                            hijrimonth_text.setText(" " + hijri_month);
                            hijriyear_text.setText(" " + hijri_year);

                            // Calculate and display current and upcoming prayer times
                            calculateAndDisplayPrayerTimes(prayerTimeResponse);

                            // Save the data to SharedPreferences
                            saveDataToSharedPreferences(sunriseTime, sunsetTime, hijri_date, hijri_month, hijri_year, hijri_holiday);

                            // Calculate and add prayer times to the list
                            List<PrayerTimeModel> prayerTimeList = calculatePrayerTimes(prayerTimeResponse);

                            // Clear the existing list and add the new prayer times
                            prayerTimes.clear();
                            prayerTimes.addAll(prayerTimeList);

                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to fetch prayer times", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PrayerTimeResponse> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void calculateAndDisplayPrayerTimes(PrayerTimeResponse prayerTimeResponse) {
        List<PrayerTimeModel> prayerTimes = calculatePrayerTimes(prayerTimeResponse);

        // Get the current time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        // Find the current and upcoming prayer times
        PrayerTimeModel currentPrayer = null;
        PrayerTimeModel upcomingPrayer = null;

        for (PrayerTimeModel prayer : prayerTimes) {
            String prayerTime = prayer.getStartTime();

            // Compare the current time with each prayer time
            try {
                Date current = sdf.parse(currentTime);
                Date prayerTimeDate = sdf.parse(prayerTime);

                if (current.before(prayerTimeDate)) {
                    if (prayer.getPrayerName().equals("Lastthird")) {
                        upcomingPrayer = new PrayerTimeModel("Tahajjud", prayerTime, "", "Name", prayerTime);
                    } else {
                        upcomingPrayer = prayer;
                    }
                    break;
                } else {
                    currentPrayer = prayer;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Display the current and upcoming prayer times
        if (currentPrayer != null && upcomingPrayer != null) {
            String currentTimes = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()); // Get the current time

            String upcomingPrayerTime = upcomingPrayer.getStartTime();

            long timeRemaining = getTimeDifference(currentTimes, upcomingPrayerTime);

            if (timeRemaining > 0) {
                // Start the countdown timer
                startCountdownTimer(timeRemaining);
            } else {
                nextPrayerTimeToGo.setText("Prayer time has passed");
            }

            current_prayer_time_name_Text.setText(currentPrayer.getPrayerName());
            upcoming_prayer_name.setText(upcomingPrayer.getPrayerName());
            next_prayer_time.setText(convertTo12HourFormat(upcomingPrayerTime));
        } else {
            current_prayer_time_name_Text.setText("No prayer");
            upcoming_prayer_name.setText("No prayer");
            next_prayer_time.setText("");
        }
    }




    private List<PrayerTimeModel> calculatePrayerTimes(PrayerTimeResponse prayerTimeResponse) {
        List<PrayerTimeModel> prayerTimes = new ArrayList<>();

        PrayerTimeResponse.Timings timings = prayerTimeResponse.getData().getTimings();

        // Get prayer times from the response
        String fajrTime = timings.getFajr();
        String dhuhrTime = timings.getDhuhr();
        String asrTime = timings.getAsr();
        String maghribTime = timings.getMaghrib();
        String ishaTime = timings.getIsha();
        String midnightTime = timings.getMidnight();
        String lastthirdTime = timings.getLastthird();

        // Create PrayerTimeModel objects and add them to the list
        prayerTimes.add(new PrayerTimeModel("Fajr", fajrTime, "", "Name", fajrTime));
        prayerTimes.add(new PrayerTimeModel("Dhuhr", dhuhrTime, "", "Name", dhuhrTime));
        prayerTimes.add(new PrayerTimeModel("Asr", asrTime, "", "Name", asrTime));
        prayerTimes.add(new PrayerTimeModel("Maghrib", maghribTime, "", "Name", maghribTime));
        prayerTimes.add(new PrayerTimeModel("Isha", ishaTime, "", "Name", ishaTime));
        prayerTimes.add(new PrayerTimeModel("Midnight", midnightTime, "", "Name", midnightTime));
        prayerTimes.add(new PrayerTimeModel("Lastthird", lastthirdTime, "", "Name", lastthirdTime));

        return prayerTimes;
    }

    public static String getAddressFromCoordinates(MainActivity context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressText = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                String upazila = address.getSubLocality(); // Upazila name
                String district = address.getLocality(); // District name
                String country = address.getCountryName(); // Country name

                if (upazila != null && !upazila.isEmpty()) {
                    addressText += upazila + ", ";
                }

                if (district != null && !district.isEmpty()) {
                    addressText += district;
                }

                if (!addressText.isEmpty()) {
                    addressText += ", ";
                }

                if (country != null && !country.isEmpty()) {
                    addressText += country;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressText;
    }

    public String convertTo12HourFormat(String time) {
        try {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf24.parse(time);
            return sdf12.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return time;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDataToSharedPreferences(String sunriseTime, String sunsetTime, String hijri_date, String hijri_month, String hijri_year, String[] hijri_holiday) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sunriseTime", sunriseTime);
        editor.putString("sunsetTime", sunsetTime);
        editor.putString("hijriDate", hijri_date);
        editor.putString("hijriMonth", hijri_month);
        editor.putString("hijriYear", hijri_year);

        if (hijri_holiday != null && hijri_holiday.length > 0) {
            editor.putString("hijriHoliday", hijri_holiday[0]);
        } else {
            editor.remove("hijriHoliday");
        }

        editor.apply();
    }

    private void retrieveDataFromSharedPreferences() {
        String sunriseTime = sharedPreferences.getString("sunriseTime", "");
        String sunsetTime = sharedPreferences.getString("sunsetTime", "");
        String hijri_date = sharedPreferences.getString("hijriDate", "" );
        String hijri_month = sharedPreferences.getString("hijriMonth", "");
        String hijri_year = sharedPreferences.getString("hijriYear", "");
        String hijri_holiday = sharedPreferences.getString("hijriHoliday", "");
        String savedLocationText = sharedPreferences.getString("locationText", "");

        // Update the TextViews or UI elements with the retrieved data
        sunriseText.setText(convertTo12HourFormat(sunriseTime));
        sunsetText.setText(convertTo12HourFormat(sunsetTime));
        hidate_text.setText(hijri_date);
        hijrimonth_text.setText(" " + hijri_month);
        hijriyear_text.setText(" " + hijri_year);
        hijri_holidays.setText(hijri_holiday);
        locationText.setText(savedLocationText);

        // You can use this retrieved data in your UI as needed
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Add this method to calculate the time difference in milliseconds
    private long getTimeDifference(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            long diff = endDate.getTime() - startDate.getTime();
            if (diff < 0) {
                // Add 24 hours if the end time is before the start time (crosses midnight)
                diff += 24 * 60 * 60 * 1000;
            }
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Add this method to start the countdown timer
    // Add this method to start the countdown timer
    private void startCountdownTimer(long milliseconds) {
        CountDownTimer timer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;

                long hours = secondsRemaining / 3600;
                long minutes = (secondsRemaining % 3600) / 60;
                long seconds = secondsRemaining % 60;

                String timeRemaining = String.format(Locale.getDefault(), "%02d Hours %02d Minutes %02d Seconds", hours, minutes, seconds);
                nextPrayerTimeToGo.setText(timeRemaining);
            }

            @Override
            public void onFinish() {
                nextPrayerTimeToGo.setText("Prayer time has arrived");
            }
        };
        timer.start();
    }

}