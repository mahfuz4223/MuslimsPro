package com.dark.muslimspro.calander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dark.muslimspro.R;

import java.util.List;

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
        holder.tvFajr.setText("Fajr: " + prayerTime.getFajr());
        holder.tvDhuhr.setText("Dhuhr: " + prayerTime.getDhuhr());
        holder.tvAsr.setText("Asr: " + prayerTime.getAsr());
        holder.tvMaghrib.setText("Maghrib: " + prayerTime.getMaghrib());
        holder.tvIsha.setText("Isha: " + prayerTime.getIsha());
    }

    @Override
    public int getItemCount() {
        return prayerTimes.size();
    }

    class PrayerTimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvFajr, tvDhuhr, tvAsr, tvMaghrib, tvIsha;

        PrayerTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvFajr = itemView.findViewById(R.id.tvFajr);
            tvDhuhr = itemView.findViewById(R.id.tvDhuhr);
            tvAsr = itemView.findViewById(R.id.tvAsr);
            tvMaghrib = itemView.findViewById(R.id.tvMaghrib);
            tvIsha = itemView.findViewById(R.id.tvIsha);
        }
    }
}
