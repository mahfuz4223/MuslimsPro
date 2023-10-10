package com.dark.muslimspro.Hadith;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HadithManager {
    private static final String SHARED_PREF_KEY = "last_selected_hadith";
    private static final String SHARED_PREF_TIME_KEY = "last_selection_time";

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public HadithManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("HadithPrefs", Context.MODE_PRIVATE);
    }

    public Hadith getRandomHadith(List<Hadith> hadithList) {
        if (hadithList.isEmpty()) {
            return null;
        }

        // Get the current date and time
        Calendar currentTime = Calendar.getInstance();

        // Set the time to 6 AM
        currentTime.set(Calendar.HOUR_OF_DAY, 6);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);

        // Get the current date and time
        Date now = currentTime.getTime();

        // Check if a new hadith is required
        boolean isNewHadithRequired = checkIfNewHadithIsRequired(now);

        if (isNewHadithRequired) {
            // Randomly select a new hadith
            Random random = new Random();
            int randomIndex = random.nextInt(hadithList.size());
            Hadith randomHadith = hadithList.get(randomIndex);

            // Save the selected hadith and the selection time
            saveSelectedHadith(randomHadith, now);

            return randomHadith;
        } else {
            // Load the previously selected hadith
            return loadLastSelectedHadith();
        }
    }

    private boolean checkIfNewHadithIsRequired(Date now) {
        // Get the last selection time from SharedPreferences
        long lastSelectionTime = sharedPreferences.getLong(SHARED_PREF_TIME_KEY, 0);

        // Calculate the time difference
        long timeDifferenceMillis = now.getTime() - lastSelectionTime;

        // Check if 24 hours have passed since the last selection
        return timeDifferenceMillis >= 24 * 60 * 60 * 1000;
    }

    private void saveSelectedHadith(Hadith hadith, Date selectionTime) {
        Gson gson = new Gson();
        String hadithJson = gson.toJson(hadith);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_KEY, hadithJson);
        editor.putLong(SHARED_PREF_TIME_KEY, selectionTime.getTime());
        editor.apply();
    }

    private Hadith loadLastSelectedHadith() {
        String hadithJson = sharedPreferences.getString(SHARED_PREF_KEY, "");

        if (!TextUtils.isEmpty(hadithJson)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Hadith>() {}.getType();
            return gson.fromJson(hadithJson, type);
        }

        return null;
    }
}

