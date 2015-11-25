package com.example.application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

class StatisticsRequestTask extends AsyncTask<String, String, String> {
    private Activity activity;

    public StatisticsRequestTask(Activity activity) {
        this.activity = activity;
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

        if (!jsonResult.equals("Non")) {

            try {
                JSONObject statistiques = new JSONObject(jsonResult);

                double vitesse = statistiques.getDouble("vitesse");
                String temps = statistiques.getString("temps");
                double calories = statistiques.getDouble("calories");
                int longueur = statistiques.getInt("longueur");

                TextView statsVitesse = (TextView) activity.findViewById(R.id.vitesse_stats);
                TextView statsCalories = (TextView) activity.findViewById(R.id.calories_stats);
                TextView statsTemps = (TextView) activity.findViewById(R.id.temps_stats);
                TextView statsLongueur = (TextView) activity.findViewById(R.id.longueur_stats);

                statsVitesse.setText(Double.toString(vitesse));
                statsCalories.setText(Double.toString(calories));
                statsTemps.setText(temps);
                statsLongueur.setText(Integer.toString(longueur));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}