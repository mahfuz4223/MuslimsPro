package com.dark.muslimspro;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrayerTimeApiService {
    // Define API endpoint and HTTP method
    @GET("timings")
    Call<PrayerTimeResponse> getPrayerTimes(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") String method
    );
}
