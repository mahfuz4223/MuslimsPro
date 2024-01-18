package com.dark.muslimspro.audioQuran;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.muslimspro.R;
import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private final List<Surah> surahList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Surah surah);
        void onPlayClick(Surah surah);
        void onPauseClick(Surah surah);
    }

    public SurahAdapter(List<Surah> surahList, OnItemClickListener listener) {
        this.surahList = surahList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sura_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Surah surah = surahList.get(position);
        holder.bind(surah, listener);
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView surahName;
        TextView surahNumber;
        ToggleButton playPauseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            surahName = itemView.findViewById(R.id.suraname);
            surahNumber = itemView.findViewById(R.id.surah_number);
            playPauseButton = itemView.findViewById(R.id.paly_sura);
        }

        public void bind(final Surah surah, final OnItemClickListener listener) {
            surahName.setText(surah.getName());
            surahNumber.setText(String.valueOf(surah.getNumber()));

            // Set the initial state of the ToggleButton based on the surah state (playing or paused)
            playPauseButton.setChecked(surah.isPlaying());

            playPauseButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Handle the ToggleButton state change here
                if (isChecked) {
                    // ToggleButton is checked (playing), perform play action
                    listener.onPlayClick(surah);
                } else {
                    // ToggleButton is unchecked (paused), perform pause action
                    listener.onPauseClick(surah);
                }
            });

            itemView.setOnClickListener(v -> listener.onItemClick(surah));
        }
    }
}
