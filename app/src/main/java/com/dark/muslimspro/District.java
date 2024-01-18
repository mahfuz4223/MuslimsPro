package com.dark.muslimspro;


public class District {
    private String id;
    private String name;
    private String bnName;
    private String lat;
    private String lon;

    public District(String id, String name, String bnName, String lat, String lon) {
        this.id = id;
        this.name = name;
        this.bnName = bnName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBnName() {
        return bnName;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
