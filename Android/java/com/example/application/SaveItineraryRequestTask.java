package com.example.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class SaveItineraryRequestTask extends AsyncTask<String, String, String> {
    private String email;
    private String nomParcours;
    private ArrayList<LatLng> parcours;
    private String note;
    private boolean favori;
    private double vitesse;
    private String date;
    private double longueur;
    private String temps;

    public SaveItineraryRequestTask(String email, String nomParcours, ArrayList<LatLng> parcours, String note, boolean favori, double vitesse, String date,
                                    double longueur, String temps) {
        this.email = email;
        this.nomParcours = nomParcours;
        this.parcours = parcours;
        this.note = note;
        this.favori = favori;
        this.vitesse = vitesse;
        this.date = date;
        this.longueur = longueur;
        this.temps = temps;
    }

    @Override
    protected String doInBackground(String... uri) {
        StringBuffer sb = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(uri[0]));

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("nomParcours", nomParcours));
            pairs.add(new BasicNameValuePair("note", note));
            pairs.add(new BasicNameValuePair("email", email));
            pairs.add(new BasicNameValuePair("date", date));
            pairs.add(new BasicNameValuePair("longueur", Double.toString(longueur)));
            if (favori)
                pairs.add(new BasicNameValuePair("favori", "true"));
            pairs.add(new BasicNameValuePair("vitesse", Double.toString(vitesse)));
            pairs.add(new BasicNameValuePair("temps", temps));

            String parcoursChaine = "";
            for (LatLng point : parcours) {
                parcoursChaine += point.latitude + ":" + point.longitude + ";";
            }

            // on supprime le dernier point virgule
            parcoursChaine = parcoursChaine.substring(0, parcoursChaine.length() - 1);
            pairs.add(new BasicNameValuePair("parcours", parcoursChaine));

            request.setEntity(new UrlEncodedFormEntity(pairs));

            HttpResponse response = client.execute(request);

            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}