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
import com.dark.muslimspro.AllahNames.AllahAr99NamAndFojilotMainActivity;
import com.dark.muslimspro.calander.CalendarActivity;
import com.dark.muslimspro.prayertime.PrayerTimeAdapter;
import com.dark.muslimspro.prayertime.PrayerTimeApiService;
import com.dark.muslimspro.prayertime.PrayerTimeModel;
import com.dark.muslimspro.prayertime.PrayerTimeResponse;
import com.dark.muslimspro.prayertime.RetrofitClient;
import com.dark.muslimspro.tools.CircularProgressBar;
import com.dark.muslimspro.tools.NetworkChangeReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final long MAX_COUNTDOWN_TIME = 360000;

    private TextView locationText, sunriseText, sunsetText, hijri_holidays, nextPrayerTimeToGo, next_prayer_time, upcoming_prayer_name, current_prayer_time_name_Text, hidate_text, hijrimonth_text, hijriyear_text,fajrTime, dhuhrTime, asrTime, maghribTime, ishaTime;;
    private TextView fajr_ends,dhuhr_ends,asr_ends,maghrib_ends,isha_ends;
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

    private boolean isAnimationRunning = false;
    private LinearLayoutManager layoutManager;
    private   MaterialCardView tasbihCardView, calanderView, compassCardView,namesbtn;

    private CircularProgressBar nextTimeToGoProgress;
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
        // Initialize UI elements
        nextTimeToGoProgress = findViewById(R.id.next_time_to_go_progress);
        fajrTime = findViewById(R.id.fajr);
        dhuhrTime = findViewById(R.id.dhuhr);
        asrTime = findViewById(R.id.asr);
        maghribTime = findViewById(R.id.maghrib);
        ishaTime = findViewById(R.id.isha);

        fajr_ends = findViewById(R.id.fajr_end);
        dhuhr_ends = findViewById(R.id.dhuhr_end);
        asr_ends = findViewById(R.id.asr_end);
        maghrib_ends = findViewById(R.id.maghrib_end);
        isha_ends = findViewById(R.id.isha_end);



        // Find the MaterialCardView with ID "tasbih"
        tasbihCardView = findViewById(R.id.tasbih);
        calanderView =  findViewById(R.id.calanderCard);
        compassCardView =  findViewById(R.id.compassCard);

        namesbtn =  findViewById(R.id.namesbtn);


        requestQueue = Volley.newRequestQueue(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("prayer_times", Context.MODE_PRIVATE);


        // Initialize the NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver(this);





        //bangla added
        // Inside your onCreate method or where you want to update the Bangla date
        String banglaDate = pickBanglaDate();

        // Find the TextView for the Bangla date
        TextView banglaDateTextView = findViewById(R.id.bangla_days); // Replace with your TextView's ID

        // Update the TextView with the calculated Bangla date
        banglaDateTextView.setText(banglaDate);





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

        namesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the CalendarActivity
                Intent intent = new Intent(MainActivity.this, AllahAr99NamAndFojilotMainActivity.class);

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

//                            String fajr = prayerTimeResponse.getData().getTimings().getFajr();
//                            String dhuhr = prayerTimeResponse.getData().getTimings().getDhuhr();
//                            String asr = prayerTimeResponse.getData().getTimings().getAsr();
//                            String magrib = prayerTimeResponse.getData().getTimings().getMaghrib();
//                            String isha = prayerTimeResponse.getData().getTimings().getIsha();

                            String[] hijri_holiday = prayerTimeResponse.getData().getDate().getHijri().getHolidays();

                            if (hijri_holiday != null && hijri_holiday.length > 0) {
                                hijri_holidays.setText(hijri_holiday[0]);
                                hijri_holidays.setVisibility(View.VISIBLE);
                            } else {
                                hijri_holidays.setVisibility(View.GONE);
                            }

                            // Convert hijri_date to an integer and subtract 1
                            try {
                                int hijriDay = Integer.parseInt(hijri_date);
                                hijriDay--; // Subtract 1 from the Hijri day
                                hijri_date = String.valueOf(hijriDay); // Convert it back to a string
                            } catch (NumberFormatException e) {
                                // Handle the case where hijri_date is not a valid integer
                                e.printStackTrace();
                            }

                            hidate_text.setText(hijri_date);

                            // Adjust prayer times
                            adjustPrayerTimes(prayerTimeResponse);

                            // Calculate and display current and upcoming prayer times
                            calculateAndDisplayPrayerTimes(prayerTimeResponse);


                            calculateEndtime(prayerTimeResponse);




                            // Save the data to SharedPreferences
                            saveDataToSharedPreferences(sunriseTime, sunsetTime, hijri_date, hijri_month, hijri_year, hijri_holiday);
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

    private void calculateEndtime(PrayerTimeResponse prayerTimeResponse) {

        String fajr_end = prayerTimeResponse.getData().getTimings().getFajr();
        String dhuhr_end = prayerTimeResponse.getData().getTimings().getDhuhr();
        String asr_end = prayerTimeResponse.getData().getTimings().getAsr();
        String maghrib_end = prayerTimeResponse.getData().getTimings().getMaghrib();
        String isha_end = prayerTimeResponse.getData().getTimings().getIsha();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Adjust the prayer times
        fajr_end = adjustPrayerTime(fajr_end, 61);     // Add 47 minutes to Fajr
        dhuhr_end = adjustPrayerTime(dhuhr_end, 254);   // Add 249 minutes to Dhuhr
        asr_end = adjustPrayerTime(asr_end, 149);       // Add 98 minutes to Asr
       maghrib_end = adjustPrayerTime(maghrib_end, 74);  // Add 71 minutes to Maghrib
        isha_end = adjustPrayerTime(isha_end, 588);     // Add 580 minutes to Isha

        // Update the TextViews with the adjusted prayer times
        fajr_ends.setText(convertTo12HourFormat(fajr_end));
        dhuhr_ends.setText(convertTo12HourFormat(dhuhr_end));
        asr_ends.setText(convertTo12HourFormat(asr_end));
        maghrib_ends.setText(convertTo12HourFormat(maghrib_end));
        isha_ends.setText(convertTo12HourFormat(isha_end));


        // Calculate time remaining until the end of the current prayer and update the progress bar
        String currentPrayerEndTime = getCurrentPrayerEndTime();
        long timeRemaining = getTimeDifference(getCurrentTime(), currentPrayerEndTime);
        updateNextTimeToGoProgress(timeRemaining);

    }

    private void adjustPrayerTimes(PrayerTimeResponse prayerTimeResponse) {

        // Get the original prayer times
        String fajr = prayerTimeResponse.getData().getTimings().getFajr();
        String dhuhr = prayerTimeResponse.getData().getTimings().getDhuhr();
        String asr = prayerTimeResponse.getData().getTimings().getAsr();
        String magrib = prayerTimeResponse.getData().getTimings().getMaghrib();
        String isha = prayerTimeResponse.getData().getTimings().getIsha();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Adjust the prayer times
        fajr = adjustPrayerTime(fajr, -12); // Subtract 12 minutes from Fajr
        dhuhr = adjustPrayerTime(dhuhr, 4);  // Add 4 minutes to Dhuhr
        asr = adjustPrayerTime(asr, 51);     // Add 50 minutes to Asr
        magrib = adjustPrayerTime(magrib, 3); // Add 2 minutes to Maghrib
        isha = adjustPrayerTime(isha, 12);   // Add 12 minutes to Isha

        // Update the TextViews with the adjusted prayer times
        fajrTime.setText(convertTo12HourFormat(fajr));
        dhuhrTime.setText(convertTo12HourFormat(dhuhr));
        asrTime.setText(convertTo12HourFormat(asr));
        maghribTime.setText(convertTo12HourFormat(magrib));
        ishaTime.setText(convertTo12HourFormat(isha));
    }


    private String adjustPrayerTime(String prayerTime, int minutesToAddOrSubtract) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date prayerTimeDate = sdf.parse(prayerTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(prayerTimeDate);

            // Add or subtract minutes from the prayer time
            calendar.add(Calendar.MINUTE, minutesToAddOrSubtract);

            // Format the updated time back to a string in HH:mm format
            String adjustedPrayerTime = sdf.format(calendar.getTime());

            return adjustedPrayerTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return prayerTime; // Return the original time in case of an error
        }
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
                // Calculate progress based on the time remaining
                int maxProgress = (int) timeRemaining; // Maximum progress value
                int progress = (int) ((timeRemaining / (float) MAX_COUNTDOWN_TIME) * maxProgress);

                // Set the progress for nextTimeToGoProgress
                nextTimeToGoProgress.setProgress(progress);

                // Start the countdown timer
                startCountdownTimer(timeRemaining);
            } else {
                // Set the progress to 100% if the prayer time has passed
                nextTimeToGoProgress.setProgress(100);
                nextPrayerTimeToGo.setText("Prayer time has passed");
            }

            current_prayer_time_name_Text.setText(currentPrayer.getPrayerName());
            upcoming_prayer_name.setText(upcomingPrayer.getPrayerName());
            next_prayer_time.setText(convertTo12HourFormat(upcomingPrayerTime));
        } else {
            current_prayer_time_name_Text.setText("No prayer");
            upcoming_prayer_name.setText("No prayer");
            next_prayer_time.setText("");

            // Set the progress to 0 if there is no upcoming prayer
            nextTimeToGoProgress.setProgress(0);
        }
    }









    private String getCurrentPrayerEndTime() {
        // Implement logic to determine the current prayer and its end time
        // ...

        return ""; // Return the end time of the current prayer
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateNextTimeToGoProgress(long milliseconds) {
        // Calculate progress based on the time remaining
        int maxProgress = (int) milliseconds;
        int progress = (int) ((milliseconds / (float) MAX_COUNTDOWN_TIME) * maxProgress);

        // Set the progress for nextTimeToGoProgress
        nextTimeToGoProgress.setProgress(progress);

        // Start the countdown timer
        startCountdownTimer(milliseconds);
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
    private void startCountdownTimer(long milliseconds) {
        CountDownTimer timer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;

                long hours = secondsRemaining / 3600;
                long minutes = (secondsRemaining % 3600) / 60;
                long seconds = secondsRemaining % 60;

                String timeRemaining = String.format(Locale.getDefault(), "%02d : %02d : %02d ", hours, minutes, seconds);

                // Update both the TextView and CircularProgressBar
                nextPrayerTimeToGo.setText(timeRemaining);


                int progress = (int) ((millisUntilFinished / 1000) / 60); // Convert to minutes
                nextTimeToGoProgress.setProgress(progress);

            }

            @Override
            public void onFinish() {
                nextPrayerTimeToGo.setText("Prayer time has arrived");
            }
        };
        timer.start();
    }






    // bangla calander

    private String convertToBanglaNumber(String input) {
        String[] numbers = {"০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯"};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch >= '0' && ch <= '9') {
                result.append(numbers[ch - '0']);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String pickBanglaDate() {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
        String strMonth = formatter.format(date);

        String Month = "", banglaNumber = "";
        int banglaDay = 1, i, dayNumber = 1, banglaYear;
        banglaYear = year - 593;

        if (strMonth.equals("April") && day <= 13) {
            Month = getString(R.string.april); //চৈত্র
            banglaYear = banglaYear - 1;
            dayNumber = 1;
            banglaDay = 14;
            for (i = dayNumber; i > day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("April") && day > 13) {
            Month = getString(R.string.boishakh); //বৈশাখ
            dayNumber = 14;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("May") && day <= 14) {
            Month = getString(R.string.boishakh2); //বৈশাখ
            dayNumber = 1;
            banglaDay = 15;
            for (i = dayNumber; i > day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("May") && day > 14) {
            Month = getString(R.string.jaistho); //জৈষ্ঠ্য
            dayNumber = 15;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("June") && day <= 14) {
            Month = getString(R.string.jaistho2); //জৈষ্ঠ্য
            dayNumber = 1;
            banglaDay = 15;
            for (i = dayNumber; i > day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("June") && day > 14) {
            Month = getString(R.string.ashar); //আষাঢ়
            dayNumber = 15;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("July") && day <= 15) {

            Month = getString(R.string.ashar2); //আষাঢ়
            dayNumber = 1;
            banglaDay = 16;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + day;
            }

        } else if (strMonth.equals("July") && day > 15) {
            Month = getString(R.string.srabon); //শ্রাবণ
            dayNumber = 16;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("August") && day <= 15) {
            dayNumber = 1;
            banglaDay = 16;
            Month = getString(R.string.srabon2); //শ্রাবণ

            for (i = dayNumber; i <= day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("August") && day > 15) {
            dayNumber = 16;
            banglaDay = 1;
            Month = getString(R.string.vadro); //ভাদ্র

            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("September") && day <= 15) {
            Month = getString(R.string.vadro2); //ভাদ্র
            dayNumber = 1;
            banglaDay = 16;

            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("September") && day > 15) {
            Month = getString(R.string.ashwin); //আশ্বিন
            dayNumber = 16;
            banglaDay = 1;

            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("October") && day <= 16) {
            Month = getString(R.string.ashwin2); //আশ্বিন

            dayNumber = 1;
            banglaDay = 17;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("October") && day > 16) {
            Month = getString(R.string.kartik); //কার্ত্তিক
            dayNumber = 17;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("November") && day <= 15) {
            Month = getString(R.string.kartik2); //কার্ত্তিক
            dayNumber = 1;
            banglaDay = 16;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }

        } else if (strMonth.equals("November") && day > 15) {
            Month = getString(R.string.agun); //অগ্রহায়ণ
            dayNumber = 16;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("December") && day <= 15) {

            Month = getString(R.string.agun2); //অগ্রহায়ণ
            dayNumber = 1;
            banglaDay = 16;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("December") && day > 15) {
            Month = getString(R.string.poush); //পৌষ
            dayNumber = 16;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("January") && day <= 14) {
            Month = getString(R.string.poush2); //পৌষ
            banglaYear = banglaYear - 1;
            dayNumber = 1;
            banglaDay = 17; //for 15
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("January") && day > 14) {
            Month = getString(R.string.magh); //মাঘ
            banglaYear = banglaYear - 1;
            dayNumber = 15;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("February") && day <= 13) {
            Month = getString(R.string.magh2); //মাঘ
            banglaYear = banglaYear - 1;
            dayNumber = 1;
            banglaDay = 14;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;

            }
        } else if (strMonth.equals("February") && day > 13) {
            Month = getString(R.string.falgun); //ফাল্গুন
            banglaYear = banglaYear - 1;
            dayNumber = 14;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        } else if (strMonth.equals("March") && day <= 14) {
            Month = getString(R.string.falgun2); //ফাল্গুন
            banglaYear = banglaYear - 1;
            dayNumber = 1;
            banglaDay = 15;

            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
                if (((year % 400 == 0) || (year % 100 != 0) && (year % 4 == 00))) {
                    if (banglaDay > 30) {
                        banglaDay = 1;
                        break;
                    }
                } else {
                    if (banglaDay > 29) {
                        banglaDay = 1;
                        break;
                    }
                }

            }

        } else if (strMonth.equals("March") && day > 14) {
            Month = getString(R.string.choitro); //চৈত্র
            banglaYear = banglaYear - 1;
            dayNumber = 15;
            banglaDay = 1;
            for (i = dayNumber; i < day; i++) {
                banglaDay = banglaDay + 1;
            }
        }

        return ((convertToBanglaNumber(String.valueOf(banglaDay - 1))) + " " + Month + " " + (convertToBanglaNumber(String.valueOf(banglaYear))));
    }


}