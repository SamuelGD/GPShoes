package com.example.application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

class FavoriteRequestTask extends AsyncTask<String, String, String> {
    private Activity activity;
    private List<Itinerary> liste;
    private int numeroActivite; // 0 Favoris 1 Alentours 2 Historique
    private TextView messageRetour = null;

    public FavoriteRequestTask(Activity activity, int numeroActivite) {
        this.activity = activity;
        this.numeroActivite = numeroActivite;

        switch (numeroActivite) {
            case 0: // Activite favori
                this.messageRetour = (TextView) this.activity.findViewById(R.id.messageRetourF);
                this.messageRetour.setVisibility(View.VISIBLE); // on remet "chargement en cours"
                break;
            case 1: // Activite alentours
                this.messageRetour = (TextView) this.activity.findViewById(R.id.messageRetourA);
                break;
            case 2: // Activite historique
                this.messageRetour = (TextView) this.activity.findViewById(R.id.messageRetourH);
                this.messageRetour.setVisibility(View.VISIBLE); // on remet "chargement en cours"
                break;
        }

    }

    public List<Itinerary> getListeParcours() {
        return liste;
    }

    @Override
    protected String doInBackground(String... uri) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri[0]));

            HttpResponse response = client.execute(request);

            HttpEntity messageEntity = response.getEntity();
            InputStream is = messageEntity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);

        System.out.println("DBG ICI");

        messageRetour.setVisibility(View.GONE);

        try {
            JSONArray parcours = new JSONArray(jsonResult);

            liste = new ArrayList<Itinerary>();

            for (int i = 0; i < parcours.length(); i++) {
                // Pour chaque parcours favoris
                String nom = parcours.getJSONObject(i).getString("nom");
                int id = parcours.getJSONObject(i).getInt("id");
                int note = parcours.getJSONObject(i).getInt("note");
                String parcoursChaine = parcours.getJSONObject(i).getString("parcours");
                boolean favori = parcours.getJSONObject(i).getBoolean("favori");
                String date = parcours.getJSONObject(i).getString("date");


                Itinerary item = new Itinerary(id, nom, note, parcoursChaine, favori, date);
                liste.add(item);
            }

            ListView vue = (ListView) activity.findViewById(R.id.listeFavoris);

            ItineraryAdapter customAdapter = new ItineraryAdapter(activity, liste, numeroActivite);
            vue.setAdapter(customAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}