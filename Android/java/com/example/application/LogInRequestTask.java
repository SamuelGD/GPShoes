package com.example.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class LogInRequestTask extends AsyncTask<String, String, String> {
    private int role;
    private Activity activity;
    private TextView messageRetour;
    private String email;
    private String password;

    public LogInRequestTask(int role, Activity activity, String email, String password) {
        this.role = role;
        this.activity = activity;
        this.messageRetour = (TextView) this.activity.findViewById(R.id.messageRetour);
        messageRetour.setText(R.string.chargement);
        messageRetour.setTextColor(0xFF00a000);

        this.email = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... uri) {
        StringBuffer sb = null;
        try {
            URL url = new URL(uri[0]);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri[0]));

            HttpResponse response = null;
            response = client.execute(request);
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

        switch (this.role) {
            case 1: // Creation de compte
                if (result.contains("Oui")) {
                    messageRetour.setText("");

                    // On sauvegarde les identifiants
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("EMAIL", email);
                    editor.putString("PASSWORD", password);
                    editor.commit();


                    // On change d'activite
                    Intent activiteAccueil = new Intent(activity, MainMenuActivity.class);
                    activity.startActivity(activiteAccueil);
                } else {
                    messageRetour.setTextColor(0xFFFF0000);
                    messageRetour.setText(R.string.erreurCreation);
                }
                break;
            case 2: // Connexion a un compte
                if (result.contains("Oui")) {
                    messageRetour.setText("");

                    // On sauvegarde les identifiants
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("EMAIL", email);
                    editor.putString("PASSWORD", password);
                    editor.commit();

                    // Changer d'activite
                    Intent activiteAccueil = new Intent(activity, MainMenuActivity.class);
                    activity.startActivity(activiteAccueil);
                } else {
                    messageRetour.setTextColor(0xFFFF0000);
                    messageRetour.setText(R.string.erreurConnexion);
                }
                break;
        }
    }
}