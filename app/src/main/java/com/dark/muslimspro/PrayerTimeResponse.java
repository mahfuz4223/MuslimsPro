package com.dark.muslimspro;

import com.google.gson.annotations.SerializedName;

public class PrayerTimeResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private Data data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("timings")
        private Timings timings;

        @SerializedName("date")
        private Date date;

        public Timings getTimings() {
            return timings;
        }

        public Date getDate() {
            return date;
        }
    }

    public class Timings {
        @SerializedName("Fajr")
        private String fajr;

        @SerializedName("Sunrise")
        private String sunrise;

        @SerializedName("Dhuhr")
        private String dhuhr;

        @SerializedName("Asr")
        private String asr;

        @SerializedName("Sunset")
        private String sunset;

        @SerializedName("Maghrib")
        private String maghrib;

        @SerializedName("Isha")
        private String isha;

        @SerializedName("Imsak")
        private String imsak;

        @SerializedName("Midnight")
        private String midnight;

        @SerializedName("Firstthird")
        private String firstthird;

        @SerializedName("Lastthird")
        private String lastthird;



        public String getFajr() {
            return fajr;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getDhuhr() {
            return dhuhr;
        }

        public String getAsr() {
            return asr;
        }

        public String getSunset() {
            return sunset;
        }

        public String getMaghrib() {
            return maghrib;
        }

        public String getIsha() {
            return isha;
        }

        public String getImsak() {
            return imsak;
        }

        public String getMidnight() {
            return midnight;
        }

        public String getFirstthird() {
            return firstthird;
        }

        public String getLastthird() {
            return lastthird;
        }
    }

    public class Date {
        @SerializedName("hijri")
        private Hijri hijri;

        public Hijri getHijri() {
            return hijri;
        }
    }

    public class Hijri {
        @SerializedName("day")
        private String day;

        @SerializedName("month")
        private Month month;

        @SerializedName("year")
        private String year;

        @SerializedName("holidays")
        private String[] holidays;

        public String getDay() {
            return day;
        }

        public Month getMonth() {
            return month;
        }

        public String getYear() {
            return year;
        }

        public String[] getHolidays() {
            return holidays;
        }
    }

    public class Month {
        @SerializedName("en")
        private String en;

        public String getEn() {
            return en;
        }
    }
}
