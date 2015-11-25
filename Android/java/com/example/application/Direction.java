package com.example.application;

import com.google.android.gms.maps.model.LatLng;

public class Direction {

    private LatLng latlng;
    private Double angle;
    private String info;


    public Direction(LatLng latlng, Double angle, String info) {
        this.latlng = latlng;
        this.angle = angle;
        this.info = info;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public Double getAngle() {
        return angle;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}

