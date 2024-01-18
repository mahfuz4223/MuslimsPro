package com.dark.muslimspro.audioQuran;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.Uri;
import android.os.*;
import android.os.Environment;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dark.muslimspro.R;
import java.io.*;
import java.util.*;

public class audioQuran extends AppCompatActivity implements SurahAdapter.OnItemClickListener {

    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;
    private SurahAdapter adapter;
    private List<Surah> surahList;
    private Context context;
    private boolean isOnlineAudioPlaying;

    private static final String DOWNLOAD_MAP_PREF_KEY = "download_map";

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {
                Surah downloadedSurah = findSurahByDownloadId(downloadId);
                if (downloadedSurah != null) {
                    if (!isOnlineAudioPlaying) {
                        playLocalAudio(downloadedSurah);
                    }
                }
            }
        }
    };

    private void playLocalAudio(Surah surah) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getAudioFilePath(surah.getNumber()));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_quran);

        context = this;

        recyclerView = findViewById(R.id.recyclerView_quran);
        surahList = loadSurahs();
        adapter = new SurahAdapter(surahList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Surah> loadSurahs() {
        String[] surahNames = {
                "Al-Fatiha", "Al-Baqara", "Al-Imran",
                "Nisa", "Al-Ma'idah", "Al-Anam",
                "Al-Araf", "Al-Anfal", "At-Tawbah",
                "Yunus", "Hud", "Yusuf", "Ar-Rad",
                "Ibrahim", "Al-Hijr", "An-Nahl",
                "Bani Israel", "Al-Kahf", "Maryam",
                "twa ha", "Al-Ambiyyah", "Al-Hajj", "Al-Muminun",
                "An-Nur", "Al-Furqan", "Ash-Shura",
                "An-Namal", "Al-Qasas", "Al-Ankabut", "Al-Rum",
                "Luqman", "As-Sajdah", "Al-Ahzab", "As-Sheba",
                "Al-Fatir", "Ya Sin", "As-Saffat",
                "Sawad", "Az-Zumar", "Al-Mu'min", "Hamim Sajdah",
                "Ash-Shura", "Az-Zukhruf", "Ad-Dukhan", "Al-Jasiyyah",
                "Al-Ahqaf", "Muhammad", "Al-Fatah", "Al-Hujurat",
                "Qaf", "Az-Zariat", "At-Tur", "An-Nazm",
                "Al-Qamar", "Ar-Rahman", "Al-Waqiyah", "Al-Hadid",
                "Al-Mujadilah", "Al-Hashr", "Al-Mumtahana",
                "As-Saf", "Al-Jumu'ah", "Al-Munafiqun",
                "At-Tagabun", "at-talaq", "At-Tahreem", "Al-Mulk",
                "Al-Qalam", "Al-Haqqbah", "Al-Maarij",
                "Noah", "Al-Jinn", "Muzammil", "mudassir",
                "Al-Qiyamah", "Al-Insan", "Al-Mursalat",
                "An-Naba", "An-Naziyat", "Abasa", "At-Takbir",
                "Al-Infitar", "at-tatfiq", "Al-Inshiqaq",
                "Al-Buruj", "At-Tarik", "Al-Ala", "Al-Ghashiyyah",
                "Al-Fajr", "Al-Balad", "Ash-Shams", "Al-Layl",
                "Ad-Duha","Al-Inshirah", "At-teen", "al-alaq", "Al-Qadar",
                "Al-Bayyinah", "Al-Ziljal", "Al-Adiyat",
                "Al-Qariyyah", "At-Takasur", "Al-Asr",
                "Al-Humazah", "elephant", "Al-Quraysh", "Al-Ma'un",
                "Al-Kawsar", "Al-Kafirun", "An-Nasr",
                "Lahab", "Al-Ikhlas", "Al-Falaq", "An-Nas"
        };

        List<Surah> surahList = new ArrayList<>();

        for (int i = 0; i < surahNames.length; ++i) {
            String numberFormatted = String.format("%03d", i + 1); // Formats the number as three digits
            String url = "https://server6.mp3quran.net/download/thubti/" + numberFormatted + ".mp3";
            String name = surahNames[i];

            Surah surah = new Surah(i + 1, name, url);

            surahList.add(surah);
        }

        return surahList;
    }

    @Override
    public void onItemClick(Surah surah) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isOnlineAudioPlaying = false;
        }

        File audioFile = new File(getAudioFilePath(surah.getNumber()));
        if (audioFile.exists()) {
            playLocalAudio(surah);
        } else {
            streamAndPlayAudio(surah);
        }
    }

    private void streamAndPlayAudio(Surah surah) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(surah.getAudioUrl());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isOnlineAudioPlaying = true;
                downloadAudioFile(surah);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadAudioFile(Surah surah) {
        String audioUrl = surah.getAudioUrl();
        String fileName = String.format("%03d.mp3", surah.getNumber());

        DownloadManager.Request request = new DownloadManager.Request( Uri.parse(audioUrl));
        request.setTitle("Downloading " + surah.getName());
        request.setDescription("Downloading audio file");
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            long downloadId = downloadManager.enqueue(request);
            addDownloadMappingToSharedPreferences(downloadId, surah);
        }
    }

    private String getAudioFilePath(int surahNumber) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), String.format("%03d.mp3", surahNumber)).getAbsolutePath();
    }

    private void addDownloadMappingToSharedPreferences(long downloadId, Surah surah) {
        SharedPreferences sharedPreferences = getSharedPreferences(DOWNLOAD_MAP_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(String.valueOf(surah.getNumber()), downloadId);
        editor.apply();
    }

    private Surah findSurahByDownloadId(long downloadId) {
        SharedPreferences sharedPreferences = getSharedPreferences(DOWNLOAD_MAP_PREF_KEY, Context.MODE_PRIVATE);
        for (Surah surah : surahList) {
            long storedDownloadId = sharedPreferences.getLong(String.valueOf(surah.getNumber()), -1);
            if (storedDownloadId == downloadId) {
                return surah;
            }
        }
        return null;
    }

    @Override
    public void onPlayClick(Surah surah) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isOnlineAudioPlaying = false;
        }

        File audioFile = new File(getAudioFilePath(surah.getNumber()));
        if (audioFile.exists()) {
            playLocalAudio(surah);
        } else {
            streamAndPlayAudio(surah);
        }
    }

    @Override
    public void onPauseClick(Surah surah) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isOnlineAudioPlaying = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadCompleteReceiver);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
