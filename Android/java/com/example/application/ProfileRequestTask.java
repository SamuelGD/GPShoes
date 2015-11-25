package com.example.application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

class ProfileRequestTask extends AsyncTask<String, String, String> {
    private Activity activity;
    private int mode; // 0 pour recuperer le poids, 1 pour mettre a jour le poids, 2 sinon
    private TextView tv_messageMaj;

    public ProfileRequestTask(Activity activity, int mode) {
        this.activity = activity;
        this.mode = mode;

        if (mode == 0 || mode == 1) {
            tv_messageMaj = (TextView) activity.findViewById(R.id.tv_messageMaj);
        }
        if (mode == 1) {
            tv_messageMaj.setText(R.string.chargement);
            tv_messageMaj.setTextColor(0xFF00A000);
        }
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mode == 0) { // On recupere le poids
            EditText et_poids = (EditText) activity.findViewById(R.id.et_poids_options);

            et_poids.setText(result);
        } else if (mode == 1) { // On met a jour le poids
            if (result.contains("Oui")) {
                tv_messageMaj.setText(R.string.maj_succes);
                tv_messageMaj.setTextColor(0xFF00a000);

            } else {
                tv_messageMaj.setText(R.string.maj_echec);
                tv_messageMaj.setTextColor(0xFF000000);
            }
        }
    }
}