package com.dark.muslimspro.calander;

public class PrayerTime {

    private String date;
    private String fajr;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;
//    private String hijriDay;
//    private String hijriMonthEn;
//    private String hijriYear;

//    public PrayerTime(String date, String fajr, String dhuhr, String asr, String maghrib, String isha,
//                      String hijriDay, String hijriMonthEn, String hijriYear) {
//        this.date = date;
//        this.fajr = fajr;
//        this.dhuhr = dhuhr;
//        this.asr = asr;
//        this.maghrib = maghrib;
//        this.isha = isha;
//        this.hijriDay = hijriDay;
//        this.hijriMonthEn = hijriMonthEn;
//        this.hijriYear = hijriYear;
//    }

    public PrayerTime(String date, String fajr, String dhuhr, String asr, String maghrib, String isha) {
        this.date = date;
        this.fajr = fajr;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

    public String getDate() { return date; }
    public String getFajr() { return fajr; }
    public String getDhuhr() { return dhuhr; }
    public String getAsr() { return asr; }
    public String getMaghrib() { return maghrib; }
    public String getIsha() { return isha; }
//    public String getHijriDay() { return hijriDay; }
//    public String getHijriMonthEn() { return hijriMonthEn; }
//    public String getHijriYear() { return hijriYear; }
}
