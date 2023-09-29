package com.dark.muslimspro;

public class PrayerTimeModel {
    private final String prayerName;
    private final String startTime;
    private final String endTime;
    private String name;
    private String time;

    public PrayerTimeModel(String prayerName, String startTime, String endTime, String name, String time) {
        this.prayerName = prayerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.time = time;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
