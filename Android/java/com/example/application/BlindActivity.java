package com.example.application;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BlindActivity extends ActionBarActivity {

    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
    public final static String keyGuide = "key_to_Guide";
    private LatLng latlng = null;
    private LatLng Paris = new LatLng(48.8259167, 2.3463);
    private ArrayList<LatLng> tableau = new ArrayList<LatLng>();
    private boolean empty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);

        setActionBar("Aller");


        final EditText text = (EditText) findViewById(R.id.editText);


        final View buttonText2 = findViewById(R.id.button8);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(
                BlindActivity.this, 6);
        if (typeFaceButton2 != null)
            ((TextView) buttonText2).setTypeface(typeFaceButton2);

        final View editText = findViewById(R.id.editText);
        Typeface typeFaceEditText = CustomFontsLoader.getTypeface(
                BlindActivity.this, 6);
        if (typeFaceEditText != null)
            ((TextView) editText).setTypeface(typeFaceEditText);

        final Button button = (Button) findViewById(R.id.button8);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (text.getText() == null) {

                } else if ((button.getText()).toString().compareTo("Calculer") == 0) {

                    tableau.add(Paris);
                    String adresse = text.getText().toString();
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(makeUrl(adresse));
                    button.setText("GO!");

                } else if ((button.getText()).toString().compareTo("GO!") == 0) {
                    if (!empty) {

                        Intent intent = new Intent(BlindActivity.this, Guide.class);

                        intent.putParcelableArrayListExtra(keyGuide, tableau);

                        startActivity(intent);

                        finish();
                    } else {
                        Toast toast = Toast.makeText(BlindActivity.this, "L'adresse entrée n'est pas valide/Lieu trop loin",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        text.setText("");
                        button.setText("Calculer!");
                        empty = false;


                    }
                }
            }


        });

    }

    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        android.support.v7.app.ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
        actionBar.setTitle(heading);
        actionBar.show();

    }

    public String makeUrl(String text) {

        String regexp = "[\\s,;\\n\\t]+"; // these are my delimiters
        String[] tokens = null;
        tokens = text.split(regexp);
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i]);
        }
        String bis = null;
        for (int i = 0; i < tokens.length; i++) {
            if (i == 0) {
                bis = tokens[0] + "+";
            } else if (i != (tokens.length - 1)) {
                bis = bis + tokens[i] + "+";
                System.out.println(bis);
            } else {
                bis = bis + tokens[i];
            }
        }

        return ("http://maps.googleapis.com/maps/api/geocode/json?address=$" + bis + "&sensor=false");

    }

    private class ReadTask extends AsyncTask<String, Integer, String> {

		/*
         * @Override protected void onPreExecute() { super.onPreExecute();
		 * Toast.makeText(getApplicationContext(),
		 * "D�but du traitement asynchrone", Toast.LENGTH_LONG).show(); }
		 *
		 * @Override protected void onProgressUpdate(Integer... values){
		 * super.onProgressUpdate(values); bar.setVisibility(View.VISIBLE); //
		 * Mise � jour de la ProgressBar bar.setProgress(values[0]); }
		 */

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            try {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ;


                JSONArray results = jObject.getJSONArray("results");
                if (!(results.length() == 0)) {
                    JSONObject resultsBis = results.getJSONObject(0);
                    JSONObject geometry = resultsBis.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    latlng = new LatLng(latitude, longitude);
                    tableau.add(latlng);
                    if (SphericalUtil.computeDistanceBetween(latlng, Paris) > 50000) {
                        empty = true;
                    }
                } else {
                    empty = true;
                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }


}

