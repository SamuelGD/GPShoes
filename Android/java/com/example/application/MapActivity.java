package com.example.application;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends FragmentActivity {

    private static final double EARTH_RADIUS = 6371000;
    private GoogleMap mMap; // Might be null if Google Play services APK is not
    // available.
    private Marker marker;
    private LatLng Paris = new LatLng(48.8534100, 2.3488000);
    private ArrayList<LatLng> tableauIndic = new ArrayList<LatLng>();
    private double randomLong;
    private Polyline polyline;
    private int i = 0;
    private Marker mMarker;
    public final static String key = "key_to_SavePath";
    public final static String keyGuide = "key_to_Guide";
    private double length;
    private boolean done = true;
    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
    ArrayList<Direction> direction = new ArrayList<Direction>();
    private Handler mHandler;
    private ArrayList<Indication> retour = new ArrayList<Indication>();
    private ArrayList<String> data2 = new ArrayList<String>();
    private ArrayList<Direction> directionFinal = new ArrayList<Direction>();
    private double testAngle = 0;
    private int m = 1;
    private boolean zoom = false;
    private int idParcours = 0;
    private double vitesseVisee = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        setUpMapIfNeeded();

        final View button1 = findViewById(R.id.button1);
        Typeface typeFaceButton1 = CustomFontsLoader.getTypeface(
                MapActivity.this, 4);
        if (typeFaceButton1 != null)
            ((TextView) button1).setTypeface(typeFaceButton1);

        final View button2 = findViewById(R.id.button2);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(
                MapActivity.this, 4);
        if (typeFaceButton2 != null)
            ((TextView) button2).setTypeface(typeFaceButton2);

        Intent intent = getIntent();

        tableauIndic = intent.getParcelableArrayListExtra(keyGuide);
        idParcours = intent.getIntExtra("idParcours", 0);
        vitesseVisee = intent.getDoubleExtra("vitesse", 0);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tableauIndic.get(0), 14));

        final Button button = (Button) findViewById(R.id.button1);
        final Button button3 = (Button) findViewById(R.id.button2);

        // lorsqu'on clique sur le bouton d'id 2, bouton remove
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentGuide = new Intent(MapActivity.this, Guide.class);
                intentGuide.putParcelableArrayListExtra(keyGuide, tableauIndic);
                intentGuide.putExtra("idParcours", idParcours);
                intentGuide.putExtra("vitesse", vitesseVisee);

                startActivity(intentGuide);
                finish();

            }

        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentParcours = new Intent(MapActivity.this, FavoriteItinerariesActivity.class);
                startActivity(intentParcours);
                finish();

            }

        });


        String url = ConnectionPath.getURLConnection(tableauIndic);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);


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
                data2 = parser.getInfo(jObject);
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
                polyLineOptions.color(Color.BLACK);


            }
            Log.i("ParserTask", "Poly");

            // Initialiser les objet pour le retour
            data2 = RemoveInformation.getinfo(data2);


            retour.clear();
            directionFinal.clear();
            i++;
            System.out.println(i);
            System.out.println("car c'est la iEME" + " " + " requete");


            if (data2.size() == data3.size()) {

                System.out.println("ici on ajoute le début pour la première requete");
                retour.add(new Indication(tableauIndic.get(0), "début "));


                for (int i = 0; i < data2.size() - 1; i++) {

                    if (SphericalUtil.computeDistanceBetween(retour.get(retour.size() - 1).getLatLng(), data3.get(i)) < 20) {

                    } else {

                        retour.add(new Indication(data3.get(i), data2
                                .get(i + 1)));
                    }

                }

                if (!(retour.get(retour.size() - 1).getLatLng().equals(tableauIndic.get(tableauIndic.size() - 1)))) {
                    retour.add(new Indication(tableauIndic.get(tableauIndic.size() - 1), "fin"));
                }


            }


            direction.clear();


            for (int j = 0; j < retour.size(); j++) {

                if (j == 0) {
                    direction.add(new Direction(tableauIndic.get(0), 0.00, retour.get(j).getInstruction()));
                } else if (j != (retour.size() - 1)) {
                    testAngle = 180 - SphericalUtil.computeHeading(retour.get(j - 1).getLatLng(), retour.get(j).getLatLng()) + SphericalUtil.computeHeading(retour.get(j).getLatLng(), retour.get(j + 1).getLatLng());


                    direction.add(new Direction(retour.get(j).getLatLng(), testAngle, retour.get(j).getInstruction()));
                } else {
                    direction.add(new Direction(retour.get(j).getLatLng(), 0.00, retour.get(j).getInstruction()));
                }


            }


            // myPosition = new LatLng( retour.get(0).getLat() ,
            // retour.get(0).getLng());


            for (int p = 0; p < direction.size(); p++) {
                if (p == 0) {

                } else {
                    testAngle = direction.get(p).getAngle();

                    if (testAngle > 360) {
                        testAngle = testAngle - 360;
                    }

                    if ((testAngle <= 180 && testAngle >= 0) || (testAngle < -180 && testAngle > -360)) {

                        if ((testAngle >= 150) && (testAngle <= 180)) {

                            direction.get(p).setInfo("tout droit");


                        } else if ((testAngle <= (-180)) && (testAngle >= (-210))) {

                            direction.get(p).setInfo("tout droit");

                        } else if ((testAngle >= 0) && (testAngle <= 10)) {

                            direction.get(p).setInfo("demi-tour");


                        } else if ((testAngle >= -360) && (testAngle <= -350)) {

                            direction.get(p).setInfo("demi-tour");

                        } else {

                            direction.get(p).setInfo("gauche");


                        }
                    } else if ((testAngle < 360 && testAngle > 180) || (testAngle <= 0 && testAngle >= -180)) {

                        if ((testAngle >= 180) && (testAngle <= 210)) {

                            direction.get(p).setInfo("tout droit");


                        } else if (testAngle <= (-150) && testAngle >= (-180)) {

                            direction.get(p).setInfo("tout droit");


                        } else if ((testAngle >= 350) && (testAngle <= 360)) {

                            direction.get(p).setInfo("demi-tour");


                        } else if ((testAngle >= -10) && (testAngle <= 0)) {

                            direction.get(p).setInfo("demi-tour");


                        } else {

                            direction.get(p).setInfo("droite");

                        }
                    } else {

                        direction.get(p).setInfo("gauche");


                    }


                }

            }


            for (int y = 0; y < direction.size(); y++) {
                if (!(direction.get(y).getInfo().contains("tout droit"))) {
                    directionFinal.add(direction.get(y));
                } else {

                }

            }

            mMap.addPolyline(polyLineOptions);


            for (int i = 0; i < directionFinal.size(); i++) {

                if (i == 0) {
                    marker = mMap.addMarker(new MarkerOptions().position(directionFinal.get(i).getLatlng()).visible(true).draggable(true));
                } else {


                }
            }


            final Handler handler = new Handler();


            handler.post(new Runnable() {


                @Override
                public void run() {

                    // Post again 16ms later.
                    doStuff();

                    handler.postDelayed(this, 16);


                }
            });


        }

        private void doStuff() {

            if (!zoom) {


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));


                if ((m != directionFinal.size())) {
                    if (done) {


                        LatLngInterpolator latLngInterpolator = new LatLngInterpolator() {
                            @Override
                            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                                double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                                return new LatLng(lat, lng);
                            }
                        };


                        MarkerAnimation.animateMarkerToGB(marker, directionFinal.get(m).getLatlng(), latLngInterpolator);
                        marker.setPosition(directionFinal.get(m).getLatlng());
                        done = false;

                    } else {
                        if (SphericalUtil.computeDistanceBetween(marker.getPosition(), directionFinal.get(m).getLatlng()) < 2) {
                            done = true;
                            m++;
                        }


                    }
                } else {
                    zoom = true;
                }
            } else {

            }


        }


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
















	