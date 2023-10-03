package com.dark.muslimspro.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dark.muslimspro.MainActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    public NetworkChangeReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (mainActivity != null) {
            if (isNetworkAvailable(context)) {

            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
