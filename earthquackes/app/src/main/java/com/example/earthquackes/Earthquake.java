package com.example.earthquackes;

import java.util.Date;

public class Earthquake {
    private double magnitude;
    private String place;
    private long date;
    private String url;

    public Earthquake(double magnitude, String place, long date, String url){
        this.magnitude=magnitude;
        this.place=place;
        this.date=date;
        this.url=url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace(){
        return place;
    }

    public long getDate() {
        return date;
    }

    public String getUrl(){
        return url;
    }
}
