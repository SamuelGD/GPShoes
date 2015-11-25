package com.example.application;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditItineraryActivity extends FragmentActivity implements
        android.location.LocationListener {

    private static final double EARTH_RADIUS = 6371000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not
    // available.
    private Marker marker;
    private LatLng Paris = new LatLng(48.8534100, 2.3488000);
    ArrayList<LatLng> monTableauEdit = new ArrayList<LatLng>();
    ArrayList<Marker> monTableauMarkerEdit = new ArrayList<Marker>();
    private double randomLong;
    private Polyline polyline;
    private int i = 0;
    private Marker mMarker;
    public final static String key = "key_to_SavePath";
    public final static String keyGuide = "key_to_Guide";
    private double length;
    private boolean isConnected = true;
    private Marker myPositionMarker;
    private LatLng myPosition;
    private boolean isItDone = false;

    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setUpMapIfNeeded();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 13));



        final View buttonText = findViewById(R.id.button1);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(
                EditItineraryActivity.this, 4);
        if (typeFaceButton != null)
            ((TextView) buttonText).setTypeface(typeFaceButton);

        final View buttonText1 = findViewById(R.id.button2);
        Typeface typeFaceButton1 = CustomFontsLoader.getTypeface(
                EditItineraryActivity.this, 4);
        if (typeFaceButton1 != null)
            ((TextView) buttonText1).setTypeface(typeFaceButton1);

        final View buttonText2 = findViewById(R.id.buttonSave);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(
                EditItineraryActivity.this, 4);
        if (typeFaceButton2 != null)
            ((TextView) buttonText2).setTypeface(typeFaceButton2);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Lorsqu'on clique sur la map
            @Override
            public void onMapClick(LatLng latlng) {

                if (i == 0) {

                    if (monTableauMarkerEdit.size() < 8) {

                        int p = monTableauMarkerEdit.size();

                        if (monTableauMarkerEdit.size() == 0) {

                            monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title("Départ")
                                    .snippet("La souffrance commence ici!")
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
                            monTableauEdit.add(latlng);

                        } else if (monTableauMarkerEdit.size() == 7) {
                            monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title("Arrivée")
                                    .snippet("La souffrance termine ici!")
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
                            monTableauEdit.add(latlng);
                        } else {

                            monTableauMarkerEdit.add(mMap.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title("point de passage" + " "
                                            + Integer.toString(p))
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
                            monTableauEdit.add(latlng);
                        }

                        monTableauMarkerEdit.get(p).showInfoWindow();

                    } else {
                        Toast.makeText(EditItineraryActivity.this, R.string.marker,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditItineraryActivity.this, R.string.preparation,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button button = (Button) findViewById(R.id.button2);
        final Button button1 = (Button) findViewById(R.id.button1);
        final Button button2 = (Button) findViewById(R.id.buttonSave);

        // lorsqu'on clique sur le bouton d'id 2, bouton remove
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (i == 0) {
                    if (!(monTableauMarkerEdit.isEmpty())) {
                        monTableauMarkerEdit.get(
                                monTableauMarkerEdit.size() - 1).remove();
                        monTableauMarkerEdit.remove(monTableauMarkerEdit.size() - 1);
                        monTableauEdit.remove(monTableauEdit.size() - 1);
                    } else {
                        Toast.makeText(EditItineraryActivity.this,
                                R.string.deletemarker, Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    if (!(monTableauMarkerEdit.isEmpty())) {

                        for (int j = 0; j < monTableauMarkerEdit.size(); j++) {
                            monTableauMarkerEdit.get(j).remove();
                        }
                        monTableauMarkerEdit.clear();
                        monTableauEdit.clear();
                        polyline.remove();
                        i = 0;
                        button1.setText("EDIT");

                    } else {
                        Toast.makeText(EditItineraryActivity.this,
                                R.string.deletemarker, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (monTableauEdit.size() > 2) {

                    if( isConnected ) {

                        Intent intent = new Intent(EditItineraryActivity.this, SaveItineraryActivity.class);

                        intent.putParcelableArrayListExtra(keyGuide, monTableauEdit);

                        intent.putExtra("longueur", length);

                        startActivity(intent);

                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditItineraryActivity.this, "Vous êtes déconnecté",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {


                    Toast.makeText(EditItineraryActivity.this, "Veuillez selectionner votre parcours",
                            Toast.LENGTH_SHORT).show();

                }

            }

        });


        // lorsqu'on clique sur le bouton d'id 1, bouton edit
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if ((i == 0) && (monTableauMarkerEdit.size() > 1)) {

                    String url = ConnectionPath
                            .getURLConnection(monTableauEdit);
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url);
                    i++;
                    button1.setText("CHOISIR CE PARCOURS");

                } else if ((i != 0) && ((monTableauMarkerEdit.size() > 1))) {

                    if( isConnected) {

                        Intent intent = new Intent(EditItineraryActivity.this, Guide.class);


                        intent.putParcelableArrayListExtra(keyGuide, monTableauEdit);

                        startActivity(intent);

                        finish();
                    }
                    else
                    {
                        Toast.makeText(EditItineraryActivity.this, "Vous êtes déconnecté",
                                Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(EditItineraryActivity.this, R.string.addmarker,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

        }
    }

    private void setUpMap() {

        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0))
                .title("Marker").snippet("Snippet"));


    }

    @Override
    public void onLocationChanged(Location location) {

        myPosition = new LatLng( location.getLatitude() , location.getLongitude());

        if( isItDone )
        {

        }
        else
        {
            mMap.addMarker(new MarkerOptions().position(myPosition).visible(true).draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition , 14));
            isItDone = true;
        }



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {

        isConnected = true;
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

       isConnected = false;
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }



    private class ReadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // bar.setVisibility(View.INVISIBLE);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask
            extends
            AsyncTask<String, Void, ArrayList<ArrayList<HashMap<String, String>>>> {

        @Override
        protected ArrayList<ArrayList<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            ArrayList<ArrayList<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);

                data3 = parser.getInfo2(jObject);
                // System.out.println("parseur");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(
                ArrayList<ArrayList<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                ArrayList<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    // System.out.println("derniere boucle for");
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(10);
                polyLineOptions.color(Color.BLUE);
            }
            Log.i("ParserTask", "Poly");

            polyline = mMap.addPolyline(polyLineOptions);

            System.out.println("votre parcours fait " + " " + getLength(data3));


            Toast toast = Toast.makeText(EditItineraryActivity.this, "votre parcours fait" + " " + getLength(data3) + "km environ",
                    Toast.LENGTH_SHORT);
            toast.show();


        }

        public double getLength(ArrayList<LatLng> retour) {
            for (int n = 0; n < data3.size(); n++) {
                if (n < (data3.size() - 1))
                    length = length + SphericalUtil.computeDistanceBetween(data3.get(n), data3.get(n + 1));
            }

            length = length * (0.01);

            length = Math.round(length);

            length = length * (0.1);

            return length;
        }


    }


}
