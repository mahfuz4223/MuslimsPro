package com.dark.muslimspro.prayertime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dark.muslimspro.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrayerTimeAdapter extends RecyclerView.Adapter<PrayerTimeAdapter.ViewHolder> {
    private List<PrayerTimeModel> prayerTimes;

    public PrayerTimeAdapter(List<PrayerTimeModel> prayerTimes) {
        this.prayerTimes = prayerTimes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allpayertime, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrayerTimeModel prayerTime = prayerTimes.get(position);

        // Bind prayer time data to ViewHolder views
        holder.prayerNameTextView.setText(prayerTime.getPrayerName()); // Use getPrayerName() here

        // Convert the time to 12-hour format and set it
        String startTime12Hour = convertTo12HourFormat(prayerTime.getStartTime());
        holder.prayerTimeTextView.setText(startTime12Hour);
    }

    // Helper method to convert time to 12-hour format
    private String convertTo12HourFormat(String time) {
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
    public int getItemCount() {
        return prayerTimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prayerNameTextView;
        TextView prayerTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prayerNameTextView = itemView.findViewById(R.id.prayername);
            prayerTimeTextView = itemView.findViewById(R.id.prayertime_starting);
        }
    }
}


