package com.dark.muslimspro.calander;

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

public class PrayerTimeAdapter extends RecyclerView.Adapter<PrayerTimeAdapter.PrayerTimeViewHolder> {
    private List<PrayerTime> prayerTimes;

    public PrayerTimeAdapter(List<PrayerTime> prayerTimes) {
        this.prayerTimes = prayerTimes;


    }

    @NonNull
    @Override
    public PrayerTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new PrayerTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrayerTimeViewHolder holder, int position) {
        PrayerTime prayerTime = prayerTimes.get(position);
        holder.tvDate.setText(prayerTime.getDate());
        holder.tvFajr.setText(prayerTime.getFajr());
        holder.tvDhuhr.setText(prayerTime.getDhuhr());
        holder.tvAsr.setText(prayerTime.getAsr());
        holder.tvMaghrib.setText(prayerTime.getMaghrib());
        holder.tvIsha.setText(prayerTime.getIsha());
//        holder.tvHijriDay.setText(prayerTime.getHijriDay());
//        holder.tvHijriMonthEn.setText(prayerTime.getHijriMonthEn());
//        holder.tvHijriYear.setText(prayerTime.getHijriYear());
    }


    private String convertTo12HourFormat(String time) {
        try {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf24.parse(time);
            return sdf12.format(date);  // Converts "06:23" to "06:23 AM"
        } catch (Exception e) {
            e.printStackTrace();
            return time;  // Return the original time if there's any error
        }
    }


    @Override
    public int getItemCount() {
        return prayerTimes.size();
    }

    class PrayerTimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha;

//        class PrayerTimeViewHolder extends RecyclerView.ViewHolder {
//        TextView tvDate, tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha, tvHijriDay, tvHijriMonthEn, tvHijriYear;

        PrayerTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFajr = itemView.findViewById(R.id.tvFajr);
            tvDhuhr = itemView.findViewById(R.id.tvDhuhr);
            tvAsr = itemView.findViewById(R.id.tvAsr);
            tvMaghrib = itemView.findViewById(R.id.tvMaghrib);
            tvIsha = itemView.findViewById(R.id.tvIsha);
//            tvHijriDay = itemView.findViewById(R.id.tvHijriDay);
//            tvHijriMonthEn = itemView.findViewById(R.id.tvHijriMonth);
//            tvHijriYear = itemView.findViewById(R.id.tvHijriYear);
        }
    }
}
