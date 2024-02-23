package com.dark.muslimspro;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import android.content.Context;
import androidx.annotation.RawRes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class JsonUtils {
    public static List<District> readDistrictsFromRawResource(Context context, @RawRes int resourceId) {
        List<District> districtList = new ArrayList<>();

        try {
            // Read the JSON file as a string
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Parse the JSON string
            JSONObject json = new JSONObject(stringBuilder.toString());
            JSONArray districtsArray = json.getJSONArray("districts");

            // Iterate through the array and create District objects
            for (int i = 0; i < districtsArray.length(); i++) {
                JSONObject districtJson = districtsArray.getJSONObject(i);

                String id = districtJson.getString("id");
                String name = districtJson.getString("name");
                String bnName = districtJson.getString("bn_name");
                String lat = districtJson.getString("lat");
                String lon = districtJson.getString("lon");

                District district = new District(id, name, bnName, lat, lon);
                districtList.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return districtList;
    }
}

