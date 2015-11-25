package com.example.application;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;


public class Itinerary {
    private String nom;
    private int note;
    private ArrayList<LatLng> points;
    private int id;
    private boolean favori;
    private String date;

    public Itinerary(int id, String nom, int note, String chaine, boolean favori, String date) {
        this.nom = nom;
        this.note = note;
        this.id = id;
        this.favori = favori;
        this.date = date;

        // chaine designe les points sous forme lat1:lng1;lat2:lng2;...;latn:lngn
        // on convertit donc en un ArrayList<LatLng>
        this.points = new ArrayList<LatLng>();
        String[] result = chaine.split(";");

        for (int i = 0; i < result.length; i++) {
            String[] coordonnees = result[i].split(":");
            if (!coordonnees[0].isEmpty() && !coordonnees[1].isEmpty()) {
                LatLng point = new LatLng(Double.parseDouble(coordonnees[0]), Double.parseDouble(coordonnees[1]));
                this.points.add(point);
            }
        }
    }

    public String getDate() {
        return date;
    }

    public boolean getFavori() {
        return favori;
    }

    public String getNom() {
        return nom;
    }

    public int getNote() {
        return note;
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }
}

