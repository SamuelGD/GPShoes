package com.example.application;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Indication {


    private LatLng latlng;

    private String instruction;

    public Indication() {

        latlng = new LatLng(0, 0);
        instruction = "";
    }

    public Indication(LatLng latlng, String instruction) {

        this.latlng = latlng;
        this.instruction = instruction;
    }


    public double getLat() {
        return latlng.latitude;
    }

    public double getLng() {
        return latlng.longitude;
    }

    public LatLng getLatLng() {
        return latlng;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }


}