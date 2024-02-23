package com.dark.muslimspro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.dark.muslimspro.AllahNames.AllahAr99NamAndFojilotMainActivity;
import com.dark.muslimspro.Hadith.Hadith;
import com.dark.muslimspro.Hadith.HadithManager;
import com.dark.muslimspro.Hadith.JsonFileLoader;
import com.dark.muslimspro.audioQuran.audioQuran;
import com.dark.muslimspro.tools.BanglaDateConverter;
import com.dark.muslimspro.tools.CircularProgressBar;
import com.dark.muslimspro.tools.NetworkChangeReceiver;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_REQUEST_CODE = 1;

    private CalculationMethod selectedPrayerMethod;
    private Madhab selectedMadhab;

    // UI elements
    private TextView locationText, sunriseText, sunsetText, nextPrayerTimeToGo, upcoming_prayer_name, current_prayer_time_name_Text, hidate_text, fajrTime, dhuhrTime, asrTime, maghribTime, ishaTime;
    private TextView fajr_ends, dhuhr_ends, asr_ends, maghrib_ends, isha_ends;

    // Other variables
    private HadithManager hadithManager;
    private TextView hadithNameTextView, hadithDescriptionTextView, hadithReferencesTextView;
    private SharedPreferences sharedPreferences;
    private NetworkChangeReceiver networkChangeReceiver;

    private PopupWindow locationPopup;

    private DistrictAdapter adapter;

    private List< String > locations;


    private MaterialCardView QuranTabelCard;
    private MaterialCardView calanderView;
    private MaterialCardView compassCardView;
    private MaterialCardView namesbtn;
    private MaterialCardView kalimabtn;
    private MaterialCardView settingsButton;
    private CircularProgressBar nextTimeToGoProgress;
    private String latitude, longitude;

    private final Runnable updateTimerRunnable = new Runnable ( ) {
        @Override
        public void run( ) {
            // Implement the code to update the timer here
            handler.postDelayed ( this , 1000 ); // Update every 1 second (1000 milliseconds)
        }
    };


    // Define your SharedPreferences keys
    private static final String PREFS_NAME = "PrayerSettings";
    private static final String PREF_PRAYER_METHOD = "PrayerMethod";
    private static final String PREF_MADHAB = "Madhab";


    private final Handler handler = new Handler ( );


    private static final String HIJRI_JSON = "[\n" +
            "  {\n" +
            "    \"name\": \"মহররম\",\n" +
            "    \"start\": \"2023-07-20\",\n" +
            "    \"end\": \"2023-08-17\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"সফর\",\n" +
            "    \"start\": \"2023-08-18\",\n" +
            "    \"end\": \"2023-09-16\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"রবিউল আউয়াল\",\n" +
            "    \"start\": \"2023-09-17\",\n" +
            "    \"end\": \"2023-10-16\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"রবিউস সানি\",\n" +
            "    \"start\": \"2023-10-17\",\n" +
            "    \"end\": \"2023-11-15\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"জমাদিউল আউয়াল\",\n" +
            "    \"start\": \"2023-11-16\",\n" +
            "    \"end\": \"2023-12-14\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"জমাদিউস সানি\",\n" +
            "    \"start\": \"2023-12-15\",\n" +
            "    \"end\": \"2024-01-13\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"রজব\",\n" +
            "    \"start\": \"2024-01-14\",\n" +
            "    \"end\": \"2024-02-11\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"শাবান\",\n" +
            "    \"start\": \"2024-02-12\",\n" +
            "    \"end\": \"2024-03-11\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"রমজান\",\n" +
            "    \"start\": \"2024-03-12\",\n" +
            "    \"end\": \"2024-04-10\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"শাওয়াল\",\n" +
            "    \"start\": \"2024-04-11\",\n" +
            "    \"end\": \"2024-05-09\",\n" +
            "    \"hijri_year\": \"1445\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"জিলক্বদ\",\n" +
            "    \"start\": \"2023-05-22\",\n" +
            "    \"end\": \"2023-06-19\",\n" +
            "    \"hijri_year\": \"1444\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"জিলহজ্জ\",\n" +
            "    \"start\": \"2023-06-20\",\n" +
            "    \"end\": \"2023-07-19\",\n" +
            "    \"hijri_year\": \"1444\"\n" +
            "  }\n" +
            "]";


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        // Initialize TextView elements
        locationText = findViewById ( R.id.location_text );
        sunriseText = findViewById ( R.id.sunrise );
        sunsetText = findViewById ( R.id.sunset );
        upcoming_prayer_name = findViewById ( R.id.upcoming_prayer_name );

        nextPrayerTimeToGo = findViewById ( R.id.next_prayer_time_to_go );
        hidate_text = findViewById ( R.id.hijri_date );
        current_prayer_time_name_Text = findViewById ( R.id.current_prayer_time_name );
        // Initialize UI elements
        nextTimeToGoProgress = findViewById ( R.id.next_time_to_go_progress );
        fajrTime = findViewById ( R.id.fajr );
        dhuhrTime = findViewById ( R.id.dhuhr );
        asrTime = findViewById ( R.id.asr );
        maghribTime = findViewById ( R.id.maghrib );
        ishaTime = findViewById ( R.id.isha );

        fajr_ends = findViewById ( R.id.fajr_end );
        dhuhr_ends = findViewById ( R.id.dhuhr_end );
        asr_ends = findViewById ( R.id.asr_end );
        maghrib_ends = findViewById ( R.id.maghrib_end );
        isha_ends = findViewById ( R.id.isha_end );


        // Initialize your TextViews
        hadithNameTextView = findViewById ( R.id.hadithNameTextView );
        hadithDescriptionTextView = findViewById ( R.id.hadithDescriptionTextView );
        hadithReferencesTextView = findViewById ( R.id.hadithReferencesTextView );
        // hadithGradeTextView = findViewById(R.id.hadithGradeTextView);


        // Find the MaterialCardView with ID "tasbih"
        MaterialCardView tasbihCardView = findViewById ( R.id.tasbih );
        QuranTabelCard = findViewById ( R.id.QuranTabelCard );
        calanderView = findViewById ( R.id.calanderCard );
        compassCardView = findViewById ( R.id.compassCard );
        settingsButton = findViewById ( R.id.settingsac );
        kalimabtn = findViewById ( R.id.kalima );

        namesbtn = findViewById ( R.id.namesbtn );

        // Find the linner_location LinearLayout
        LinearLayout linnerLocation = findViewById ( R.id.linner_location );


        // Set click listener for linner_location
        linnerLocation.setOnClickListener ( v -> {
            // Show the popup
            showLocationPopup ( );
        } );


        try {
            hidate_text.setText ( getHijriDate ( ) );
        } catch ( JSONException | ParseException e ) {
            hidate_text.setText ( "Error in parsing date" );
            e.printStackTrace ( );
        }


        // Initialize the NetworkChangeReceiver
        networkChangeReceiver = new NetworkChangeReceiver ( this );


        // Initialize HadithManager
        hadithManager = new HadithManager ( this );

        // Load and display the random hadith
        loadAndDisplayRandomHadith ( );


        NetworkConnected ( );



        checkAndShowLocationPopup();


        //bangla added

        String banglaDate = BanglaDateConverter.pickBanglaDate ( );

        // Find the TextView for the Bangla date
        TextView banglaDateTextView = findViewById ( R.id.bangla_days ); // Replace with your TextView's ID

        // Update the TextView with the calculated Bangla date
        banglaDateTextView.setText ( banglaDate );

        // Start the timer to update the circular progress bar every second
        handler.post ( updateTimerRunnable );


        // Get the prayer method and madhab from the shared preferences
        SharedPreferences prefs = getSharedPreferences ( PREFS_NAME , MODE_PRIVATE );
        selectedPrayerMethod = CalculationMethod.valueOf ( prefs.getString ( PREF_PRAYER_METHOD , CalculationMethod.KARACHI.name ( ) ) );
        selectedMadhab = Madhab.valueOf ( prefs.getString ( PREF_MADHAB , Madhab.HANAFI.name ( ) ) );

        // Get the prayer method and madhab from the intent
        if ( getIntent ( ) != null ) {
            Bundle bundle = getIntent ( ).getExtras ( );
            if ( bundle != null ) {
                selectedPrayerMethod = CalculationMethod.valueOf ( bundle.getString ( "PrayerMethod" , CalculationMethod.KARACHI.name ( ) ) );
                selectedMadhab = Madhab.valueOf ( bundle.getString ( "Madhab" , Madhab.HANAFI.name ( ) ) );

                // Save the selected values to SharedPreferences
                SharedPreferences.Editor editor = prefs.edit ( );
                editor.putString ( PREF_PRAYER_METHOD , selectedPrayerMethod.name ( ) );
                editor.putString ( PREF_MADHAB , selectedMadhab.name ( ) );

                editor.apply ( );
            }
        }


        // Display the current prayer method and madhab


        showToast (  "Prayer Method: " + selectedPrayerMethod + ", Madhab: " + selectedMadhab );


        // Set an OnClickListener on the tasbihCardView
        tasbihCardView.setOnClickListener ( v -> {
            // Create an Intent to navigate to the desired activity
            Intent intent = new Intent ( MainActivity.this , TasbihActivity.class );

            // Start the new activity
            startActivity ( intent );
        } );

        QuranTabelCard.setOnClickListener ( v -> {
            // Create an Intent to navigate to the desired activity
            Intent intent = new Intent ( MainActivity.this , audioQuran.class );

            // Start the new activity
            startActivity ( intent );
        } );


        compassCardView.setOnClickListener ( v -> {
            // Create an Intent to navigate to the CalendarActivity
            Intent intent = new Intent ( MainActivity.this , QiblaActivity.class );

            // Start the new activity
            startActivity ( intent );
        } );

        namesbtn.setOnClickListener ( v -> {
            // Create an Intent to navigate to the CalendarActivity
            Intent intent = new Intent ( MainActivity.this , AllahAr99NamAndFojilotMainActivity.class );

            // Start the new activity
            startActivity ( intent );
        } );

        // Set an OnClickListener on the settingsButton to open the SettingActivity
        settingsButton.setOnClickListener ( v -> {

            Intent intent = new Intent ( MainActivity.this , SettingsActivity.class );

            startActivityForResult ( intent , SETTINGS_REQUEST_CODE );

        } );

        kalimabtn.setOnClickListener ( v -> {
            // Create an Intent to navigate to the CalendarActivity
            Intent intent = new Intent ( MainActivity.this , KalimaActivity.class );

            // Start the new activity
            startActivity ( intent );
        } );

        calanderView.setOnClickListener ( v -> {
            // Create an Intent to navigate to the CalendarActivity
            Intent intent = new Intent ( MainActivity.this , ZakatActivity.class );

            // Start the new activity
            startActivity ( intent );
        } );
    }


    private void showLocationPopup() {
        // Read data from districts.json and populate the RecyclerView
        List<District> districtList = JsonUtils.readDistrictsFromRawResource(this, R.raw.districts);

        // Create a Dialog with no title
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_layout);

        // Set dimensions of the dialog
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Find views in the popup layout
        SearchView customSearchView = dialog.findViewById(R.id.customSearchView);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        // Initialize the adapter for RecyclerView
        adapter = new DistrictAdapter();
        adapter.setDistricts(districtList);
        recyclerView.setAdapter(adapter);

        // Set layout manager for RecyclerView (e.g., LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set item click listener for the RecyclerView
        adapter.setOnItemClickListener(district -> {
            // Update the selected location TextView
            locationText.setText(district.getBnName());

            // Save the selected location and its latitude and longitude to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("LocationData", MODE_PRIVATE).edit();
            editor.putString("SelectedLocation", district.getBnName());
            editor.putString("Latitude", String.valueOf(district.getLat()));
            editor.putString("Longitude", String.valueOf(district.getLon()));
            editor.apply();

//             Calculate and display prayer times
            calculateAndDisplayPrayerTimes(
                    String.valueOf(district.getLat()),
                    String.valueOf(district.getLon())
            );

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Add a TextWatcher to the customSearchView
        customSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the RecyclerView based on the user's input
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Show the dialog
        dialog.show();
    }

    public void checkAndShowLocationPopup() {
        SharedPreferences preferences = getSharedPreferences("LocationData", MODE_PRIVATE);
        String selectedLocation = preferences.getString("SelectedLocation", "");
        String latitude = preferences.getString("Latitude", "");
        String longitude = preferences.getString("Longitude", "");

        if (selectedLocation == null || latitude == null || longitude == null) {
            showLocationPopup(); // Show popup if no location data is saved
        } else {
            // Use the saved location data
            locationText.setText(selectedLocation);

            Log.d("LocationData", "Latitude: " + latitude + ", Longitude: " + longitude);

            calculateAndDisplayPrayerTimes(latitude, longitude);
        }
    }

    public void calculateAndDisplayPrayerTimes(String latitude, String longitude) {
        if (selectedPrayerMethod == null) {
            // Initialize selectedPrayerMethod here

        }

        if (selectedPrayerMethod != null) {
            Log.d("PrayerTimes", "Calculating prayer times for Latitude: " + latitude + ", Longitude: " + longitude);

            // Get prayer times and end times
            PrayerTimes prayerTimes = calculatePrayerTimes(latitude, longitude, selectedPrayerMethod, selectedMadhab);
            if (prayerTimes != null) {
                displayPrayerTimes(prayerTimes);
                displayEndTimes(prayerTimes);
                determineUpcomingPrayer(prayerTimes);
                updateCircularProgressBar(prayerTimes);
            } else {
                // Handle error or inform the user
                Toast.makeText(getApplicationContext(), "Failed to calculate prayer times. Please check your settings", Toast.LENGTH_SHORT).show();
                Log.e("PrayerTimes", "Failed to calculate prayer times. Latitude: " + latitude + ", Longitude: " + longitude);
            }
        } else {
            // Handle case where selectedPrayerMethod is still null
            Toast.makeText(getApplicationContext(), "Prayer method is not selected. Please select a method.", Toast.LENGTH_SHORT).show();
            Log.e("PrayerTimes", "Prayer method is not selected. Latitude: " + latitude + ", Longitude: " + longitude);
        }
    }



    private PrayerTimes calculatePrayerTimes(String latitude, String longitude, CalculationMethod method, Madhab madhab) {
        // Convert latitude and longitude strings to doubles
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        Coordinates coordinates = new Coordinates(lat, lon);
        DateComponents dateComponents = DateComponents.from(new Date());

        CalculationParameters params = method.getParameters();
        params.madhab = madhab;

        try {
            return new PrayerTimes(coordinates, dateComponents, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void displayPrayerTimes( PrayerTimes prayerTimes ) {
        // Display the five daily prayers
        fajrTime.setText ( formatTime ( prayerTimes.fajr ) );
        dhuhrTime.setText ( formatTime ( prayerTimes.dhuhr ) );
        asrTime.setText ( formatTime ( prayerTimes.asr ) );
        maghribTime.setText ( formatTime ( prayerTimes.maghrib ) );
        ishaTime.setText ( formatTime ( prayerTimes.isha ) );
        sunriseText.setText ( formatTime ( prayerTimes.sunrise ) );
        sunsetText.setText ( formatTime ( prayerTimes.maghrib ) );
    }

    private void displayEndTimes( PrayerTimes prayerTimes ) {
        // Display the end times for Dhuhr, Asr, Maghrib, and Isha
        fajr_ends.setText ( formatTime ( prayerTimes.sunrise ) );
        dhuhr_ends.setText ( formatTime ( prayerTimes.asr ) ); // Dhuhr ends when Asr starts
        asr_ends.setText ( formatTime ( prayerTimes.maghrib ) ); // Asr ends when Maghrib starts
        maghrib_ends.setText ( formatTime ( prayerTimes.isha ) ); // Maghrib ends when Isha starts

        // Calculate and display Isha end time (midnight)
        long maghribMillis = prayerTimes.maghrib.getTime ( );
        long fajrMillis = prayerTimes.fajr.getTime ( );
        long midnightMillis = maghribMillis + ( fajrMillis - maghribMillis ) / 2;
        isha_ends.setText ( formatTime ( new Date ( midnightMillis ) ) );
    }


    private void determineUpcomingPrayer( PrayerTimes prayerTimes ) {
        Date now = new Date ( );

        // Check if it's Friday (Jumu'ah)
        Calendar calendar = Calendar.getInstance ( );
        calendar.setTime ( now );
        int dayOfWeek = calendar.get ( Calendar.DAY_OF_WEEK );

        // Check if it's time for Tahajjud (between Isha and Fajr)
        Date ishaTime = prayerTimes.isha;
        Date fajrTime = prayerTimes.fajr;

        if ( now.after ( ishaTime ) && now.before ( fajrTime ) ) {
            // It's time for Tahajjud
            upcoming_prayer_name.setText ( "Tahajjud" );
            return;
        }

        // Determine the next regular prayer
        if ( now.before ( prayerTimes.fajr ) ) {
            setCurrentAndUpcomingPrayer ( "Tahajjud" , "Fajr" );

        } else if ( now.before ( prayerTimes.dhuhr ) ) {


            if ( dayOfWeek == Calendar.FRIDAY ) {
                // It's Friday, so Jumu'ah prayer is upcoming
                upcoming_prayer_name.setText ( "Jumu'ah" );
            } else setCurrentAndUpcomingPrayer ( "Fajr" , "Dhuhr" );

        } else if ( now.before ( prayerTimes.asr ) ) {


            if ( dayOfWeek == Calendar.FRIDAY ) {
                // It's Friday, so Jumu'ah prayer is upcoming
                current_prayer_time_name_Text.setText ( "Jumu'ah" );
            } else setCurrentAndUpcomingPrayer ( "Dhuhr" , "Asr" );

        } else if ( now.before ( prayerTimes.maghrib ) ) {
            setCurrentAndUpcomingPrayer ( "Asr" , "Maghrib" );

        } else if ( now.before ( prayerTimes.isha ) ) {
            setCurrentAndUpcomingPrayer ( "Maghrib" , "Isha" );

        } else {
            setCurrentAndUpcomingPrayer ( "Isha" , "Tahajjud" );
        }
    }

    private void setCurrentAndUpcomingPrayer( String currentPrayer , String upcomingPrayer ) {
        current_prayer_time_name_Text.setText ( currentPrayer );
        upcoming_prayer_name.setText ( upcomingPrayer );
    }


    private void updateCircularProgressBar( PrayerTimes prayerTimes ) {
        Date now = new Date ( );

        // Calculate time differences for the current and next prayers in seconds
        long currentTimeMillis = now.getTime ( );
        long currentPrayerMillis = 0;
        long nextPrayerMillis = 0;

        // Determine the current and next prayer times
        if ( now.before ( prayerTimes.fajr ) ) {
            // Before Fajr
            nextPrayerMillis = ( prayerTimes.fajr.getTime ( ) - currentTimeMillis ) / 1000; // Convert to seconds
        } else if ( now.before ( prayerTimes.dhuhr ) ) {
            currentPrayerMillis = ( currentTimeMillis - prayerTimes.fajr.getTime ( ) ) / 1000; // Convert to seconds
            nextPrayerMillis = ( prayerTimes.dhuhr.getTime ( ) - currentTimeMillis ) / 1000; // Convert to seconds
        } else if ( now.before ( prayerTimes.asr ) ) {
            currentPrayerMillis = ( currentTimeMillis - prayerTimes.dhuhr.getTime ( ) ) / 1000; // Convert to seconds
            nextPrayerMillis = ( prayerTimes.asr.getTime ( ) - currentTimeMillis ) / 1000; // Convert to seconds
        } else if ( now.before ( prayerTimes.isha ) ) {
            currentPrayerMillis = ( currentTimeMillis - prayerTimes.maghrib.getTime ( ) ) / 1000; // Convert to seconds
            nextPrayerMillis = ( prayerTimes.isha.getTime ( ) - currentTimeMillis ) / 1000; // Convert to seconds
        } else {
            // Handle the case where it's after Isha (possibly for the next day)
            // Calculate the time difference until the next day's Fajr
            currentPrayerMillis = ( currentTimeMillis - prayerTimes.isha.getTime ( ) ) / 1000; // Convert to seconds
            nextPrayerMillis = ( prayerTimes.fajr.getTime ( ) + ( 24 * 60 * 60 * 1000 ) - currentTimeMillis ) / 1000; // Convert to seconds
        }

        // Calculate progress percentages
        int currentProgress = ( int ) ( ( currentPrayerMillis * 100 ) / ( nextPrayerMillis + currentPrayerMillis ) );

        // Update the CircularProgressBar
        nextTimeToGoProgress.setProgress ( currentProgress );

        // Start the countdown timer
        startCountdownTimer ( nextPrayerMillis );
    }

    private void startCountdownTimer( long countdownMillis ) {
        new CountDownTimer ( countdownMillis * 1000 , 1000 ) {
            public void onTick( long millisUntilFinished ) {
                long hours = millisUntilFinished / 3600000;
                long minutes = ( millisUntilFinished % 3600000 ) / 60000;
                long seconds = ( millisUntilFinished % 60000 ) / 1000;

                // Update the TextView to display the time information
                nextPrayerTimeToGo.setText ( String.format ( Locale.getDefault ( ) , "%02d:%02d:%02d" , hours , minutes , seconds ) );
            }

            public void onFinish( ) {
                // Handle any actions when the countdown timer finishes
            }
        }.start ( );
    }


    private String getHijriDate( ) throws JSONException, ParseException {
        JSONArray hijriMonths = new JSONArray ( HIJRI_JSON );
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd" );
        Date today = new Date ( );

        for ( int i = 0; i < hijriMonths.length ( ); i++ ) {
            JSONObject month = hijriMonths.getJSONObject ( i );
            Date startDate = sdf.parse ( month.getString ( "start" ) );
            Date endDate = sdf.parse ( month.getString ( "end" ) );

            if ( !today.before ( startDate ) && !today.after ( endDate ) ) {
                // Calculate Hijri date
                long difference = today.getTime ( ) - startDate.getTime ( );
                int days = ( int ) ( difference / ( 24 * 60 * 60 * 1000 ) ) + 1; // +1 for inclusive count

                return days + "  " + month.getString ( "name" ) + "  " + month.getString ( "hijri_year" );
            }
        }

        return "Hijri date not found";
    }


    @Override
    protected void onDestroy( ) {
        super.onDestroy ( );

        // Stop the timer when the activity is destroyed to prevent memory leaks
        handler.removeCallbacks ( updateTimerRunnable );
    }

    private String formatTime( Date date ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat ( "hh:mm" , Locale.getDefault ( ) );
        return dateFormat.format ( date );
    }


    private void NetworkConnected( ) {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) getSystemService ( Context.CONNECTIVITY_SERVICE );
        if ( connectivityManager != null ) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo ( );
            if ( activeNetworkInfo != null ) {
                activeNetworkInfo.isConnected ( );
            }
        }
    }


    private void loadAndDisplayRandomHadith( ) {
        // Load JSON data from your db.json file
        String jsonString = JsonFileLoader.loadJSONFromAsset ( this , "db.json" );

        try {
            if ( jsonString != null && !jsonString.isEmpty ( ) ) {
                JSONObject jsonObject = new JSONObject ( jsonString );
                JSONArray hadithsArray = jsonObject.getJSONArray ( "hadiths" );

                // Deserialize JSON data into a list of Hadith objects using Gson
                Type listType = new TypeToken< ArrayList< Hadith > > ( ) {
                }.getType ( );
                ArrayList< Hadith > hadithList = new Gson ( ).fromJson ( hadithsArray.toString ( ) , listType );

                if ( !hadithList.isEmpty ( ) ) {
                    // Get a random hadith using HadithManager
                    Hadith randomHadith = hadithManager.getRandomHadith ( hadithList );

                    // Display the random hadith in the TextViews
                    if ( randomHadith != null ) {
                        // Update the TextViews with the hadith information
                        hadithNameTextView.setText ( randomHadith.getName ( ) );
                        hadithDescriptionTextView.setText ( randomHadith.getDescription ( ) );
                        hadithReferencesTextView.setText ( randomHadith.getReferences ( ) );
                        // hadithGradeTextView.setText("Grade: " + randomHadith.getGrade());
                    } else {
                        // Handle the case where randomHadith is null
                        Log.e ( "RandomHadithError" , "Random Hadith is null" );
                        showToast ( "Failed to load a random Hadith. Please try again later." );
                    }
                } else {
                    // Handle the case where hadithList is empty
                    Log.e ( "HadithListError" , "List of Hadiths is empty" );
                    showToast ( "No Hadiths available at the moment. Please check your data source." );
                }
            } else {
                // Handle the case where jsonString is null or empty
                Log.e ( "JsonLoadError" , "JSON data is null or empty" );
                showToast ( "Failed to load JSON data. Please check your network connection." );
            }
        } catch ( JSONException e ) {
            e.printStackTrace ( );
            // Handle the JSON parsing exception
            Log.e ( "JsonParsingError" , "Error parsing JSON data: " + e.getMessage ( ) );
            showToast ( "An error occurred while parsing JSON data. Please try again later." );
        }
    }

    private void showToast( String message ) {
        Toast.makeText ( this , message , Toast.LENGTH_SHORT ).show ( );
    }
}