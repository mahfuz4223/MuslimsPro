package com.dark.muslimspro.audioQuran;

public class Surah {
    private int number;
    private String name;
    private String audioUrl;
    private boolean playing;

    public Surah(int number, String name, String audioUrl) {
        this.number = number;
        this.name = name;
        this.audioUrl = audioUrl;
        this.playing = false; // Initialize as paused
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
