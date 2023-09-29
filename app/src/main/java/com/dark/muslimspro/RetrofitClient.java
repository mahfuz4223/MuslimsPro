package com.dark.muslimspro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    // Define the base URL of the API
    private static final String BASE_URL = "http://api.aladhan.com/v1/timings/";

    // Create a Retrofit instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
                    .build();
        }
        return retrofit;
    }
}

