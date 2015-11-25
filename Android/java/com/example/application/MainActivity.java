package com.example.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

public class MainActivity extends FragmentActivity implements
        android.location.LocationListener {

    private static final double EARTH_RADIUS = 6371000;
    private GoogleMap mMap;
    private LatLng Paris = new LatLng(48.8259167, 2.3463); //LatLng pointant sur Paris
    ArrayList<LatLng> monTableau = new ArrayList<LatLng>(); //tableau où l'on va ranger nos points de passage (waypoints)
    ArrayList<Marker> monTableauMarker = new ArrayList<Marker>(); //tableau où l'on va ranger nos markers sur les waypoints (pour pouvoir les effacer au cas où)
    private Polyline polylineRandom;
    private int i = 0; // simplement un compteur pour nous aider à traiter les cas
    public final static String key = "key_to_SavePath"; //clef pour se passer des tableaux dans les intents
    public final static String keyGuide = "key_to_Guide"; //de même mais pour une autre activité
    private ArrayList<LatLng> data3 = new ArrayList<LatLng>(); //on va récupérer les informations du fichier JSON qui est renvoyé après une requête
    private LocationManager lm;
    private LatLng myPosition; //on va ranger notre position avec
    private boolean done = false; //boolean pour ne pas accéder à des éléments avant que d'autres tâches soient effectuées
    private Marker myMarkerPosition; //on va pointer sur notre position avec
    private double length = 0;
    private double distanceChosen = 0; // disatnce que l'on choisit dans le EditText


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**** Ici on va mettre en place la Map et lui ajouter des choses ****/
        setUpMapIfNeeded();

        mMap.addMarker(new MarkerOptions().position(Paris).draggable(true));

        mMap.setMyLocationEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 13));
        /*******************************************************************/


        /*********** Ici on change les fonts des boutons ***********/
        final View buttonText = findViewById(R.id.button1);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(
                MainActivity.this, 4);
        if (typeFaceButton != null)
            ((TextView) buttonText).setTypeface(typeFaceButton);

        final View buttonText1 = findViewById(R.id.button2);
        Typeface typeFaceButton1 = CustomFontsLoader.getTypeface(
                MainActivity.this, 4);
        if (typeFaceButton1 != null)
            ((TextView) buttonText1).setTypeface(typeFaceButton1);
        /************************************************************/


        /**************** Ici on ajoute un ClickListener sur la Map **********************/
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {

				/*
                 * mMap.addMarker(new
				 * MarkerOptions().position(latlng).draggable( true));
				 */
                //Pour l'instant on ne fait rien...

            }
        });
        /***********************************************************************************/


        // lorsqu'on clique sur le bouton d'id 1, bouton random
        final Button button = (Button) findViewById(R.id.button1);
        // lorsqu'on clique sur le bouton d'id 2, bouton back
        final Button button1 = (Button) findViewById(R.id.button2);


        /**************** Ici on gère le click du bouton Random ***********/
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                EditText text = (EditText) findViewById(R.id.editText1);

                if (i > 0) {

                    //Si i est différent de 0, c'est qu'on a ce qu'il faut pour envoyer une activité de guidage

                    Intent intent = new Intent(MainActivity.this, Guide.class);

                    intent.putParcelableArrayListExtra(keyGuide, monTableau);

                    startActivity(intent);

                    finish();

                } else {

                    myPosition = Paris;//pour l'émulateur, car le LocationListener !!!NE MARCHE PAS!!! avec l'émulateur

                    if (TextUtils.isEmpty(text.getText())) //Si le EditText est nul, on envoie un Toast pour avertir l'utilisateur...
                    {

                        Toast.makeText(MainActivity.this, R.string.addlong,
                                Toast.LENGTH_SHORT).show();

                    } else {


                        double longueurAleatoire = Double.valueOf(text.getText().toString()); //on récupère la longueur entrée par l'utilisateur

                        distanceChosen = longueurAleatoire * 1000; //on la convertie en metre

                        if (longueurAleatoire <= 50 && longueurAleatoire > 1) { //On ajoute des conditions sur cette longueur

                            if (monTableau.isEmpty()) {
                                /******************* Ici on va traiter les différents cas empiriquement, car plus la longueur choisie est grande, plus la longueur du parcours va grandir **********************/
                                if (longueurAleatoire > 2.6 && longueurAleatoire < 10) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 1.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 10 && (longueurAleatoire < 20)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 2));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 20 && (longueurAleatoire < 29)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 3));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 29 && (longueurAleatoire < 35)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 4.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if ((longueurAleatoire) >= 35 && (longueurAleatoire < 45)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if ((longueurAleatoire) >= 45 && (longueurAleatoire <= 50)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 5.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        randomPath(Paris, 1000 * (longueurAleatoire));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                button.setText("CHOISIR");
                                button1.setText("RECOMMENCER");
                                i++;

                                /******************************************************************************/


                            } else {

                                //Ici on sait qu'on a dejà des markers sur la Map et donc on est dans le cas où on clique sur RECOMMENCER
                                /*********** On efface les objets de la map *********************/
                                for (int j = 0; j < 8; j++) {
                                    monTableauMarker.get(j).remove();
                                }
                                monTableauMarker.clear();
                                monTableau.clear();
                                polylineRandom.remove();
                                /*******************************************************************/

                                /************************** On va triater les mêmes cas que plus haut ***************************************/
                                if (longueurAleatoire > 2.6 && longueurAleatoire < 10) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 1.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 10 && (longueurAleatoire < 20)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 2));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 20 && (longueurAleatoire < 29)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 3));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if (longueurAleatoire >= 29 && (longueurAleatoire < 35)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 4.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if ((longueurAleatoire) >= 35 && (longueurAleatoire < 45)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else if ((longueurAleatoire) >= 45 && (longueurAleatoire <= 50)) {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire - 5.5));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        randomPath(myPosition, 1000 * (longueurAleatoire));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                /******************************************************************************/

                                /************* On change la valeur des textes des boutons **********************/
                                button.setText("CHOISIR");
                                button1.setText("RECOMMENCER");
                                /********************************************************************************/

                                i++; // si i>0 on sait qu'on a appuyé sur random

                            }

                        } else if (longueurAleatoire >= 50) {
                            Toast.makeText(MainActivity.this, //on borne de même la longueur choisie
                                    R.string.distancemax, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, //on borne de même la longueur choisie
                                    R.string.distancemin, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
            }
        });
        /******************************************************************************************/


        /************************* Ici on gère le click du bouton back ou recommencer **************/
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (i > 0) {
                    for (int j = 0; j < 8; j++) {
                        monTableauMarker.get(j).remove();
                    }
                    monTableauMarker.clear();
                    monTableau.clear();
                    polylineRandom.remove();
                    button.setText("RANDOM");
                    button1.setText("BACK");

                    i = 0;
                } else {

                    Intent intent = new Intent(MainActivity.this, MainMenuActivity.class); //si on clique sur back on revient en arrière sur secondScreen
                    startActivity(intent);
                    finish();

                }
            }
        });
        /*********************************************************************************************/
    }

    protected void onResume() {

        super.onResume();

        /************** Ici on va gérer les requêtes de localisation, en tout cas leurs fréquences ****************/
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
        /**********************************************************************************************************/
    }

    public void showAlert(Activity activity, String message, String title) {
        /*************** Cette méthode sert à créer des alertDialog avec deux boutons **************/
        /************** On va gérer avec cela le fait qu'il peut y avoir des bugs pour**************/
        /************** générer des parcours de bonne taille, on informe alors l'utilisateur********/
        /************** de la situation, et de ce qu'il peut décider de faire***********************/


        TextView title1 = new TextView(activity);
        title1.setText(title);
        title1.setPadding(10, 10, 10, 10);
        title1.setGravity(Gravity.CENTER);
        title1.setTextColor(Color.BLACK);
        title1.setTextSize(20);


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // builder.setTitle("Title");
        builder.setCustomTitle(title1);
        // builder.setIcon(R.drawable.alert_36);

        builder.setMessage(message);


        builder.setCancelable(false);
        builder.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                //Si la longueur n'est pas bonne, il peut quand même tenter de trouver un autre parcours aléatoire avec ou non la même longueur...
            }

        });

        builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                Intent intent = new Intent(MainActivity.this, EditItineraryActivity.class);
                startActivity(intent);
                finish();
                //Ici la longueur n'est pas bonne, et donc il va décider de l'éditer lui même à la main...

            }

        });


        AlertDialog alert = builder.create();
        alert.show(); //on affiche à l'écran le message...
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
                "Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {

        /*************** Ici on met à jour la position de l'utilisateur ***************/
        myPosition = new LatLng(location.getLatitude(), location.getLongitude());

        if (done) {

            myMarkerPosition.remove();
            done = false;

        }

        myMarkerPosition = mMap.addMarker(new MarkerOptions().position(myPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        done = true; //On utilise done pour savoir quand est-ce qu'il faut supprimer les éléments de la carte ou non
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
        /************************************************************************************/
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
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider, newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String provider) {

        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }


    public void randomPath(LatLng positionDepart, double distance) throws InterruptedException {
        /**************** méthode permettant la génération d'un parcours aléatoires **************/

        /****************** Ici on génère un angle random entre 0 et 360 degrés ******************/
        int max = 360;
        int min = 0;
        Random rand = new Random();
        double myRandomAngle = (double) rand.nextInt(max - min + 1) + min;
        /*****************************************************************************************/

        LatLng myCenter = SphericalUtil.computeOffset(positionDepart, (distance) / (2 * Math.PI), myRandomAngle); // On trouve le centre du cercle fictif qui passe par notre postion, qui est incliné d'un angle myRandomAngle, et
        // qui est de rayon (distance)/(2 * Math.PI) pour avoir un cercle de rayon la disatnce que l'on veut courrir c'est à dire
        // de longueur distance...

        for (int i = 0; i < 8; i++) {
            monTableau.add(SphericalUtil.computeOffset(myCenter, (distance)
                    / (2 * Math.PI), myRandomAngle - (3 * 180) / 2 + i * 45));  //On remplie le tableau de 8 LatLng repartie uniformement sur le cercle fictif ...
        }

        for (int i = 0; i < 8; i++) {
            monTableauMarker.add(mMap.addMarker(new MarkerOptions().position(monTableau.get(i)).draggable(true))); //On ajoute les markers qui correspondent ...
        }

        monTableau.add(monTableau.get(0)); //On ajoute à notre tableau son permier élément élément pour pouvoir former une boucle parfaite

        /******************* Ici on génère l'url et on la traite, On récupère ce qu'il faut pour pourvoir tracer le parcours aléatoire... **********************************************************/
        String url = ConnectionPath.getURLConnection(monTableau);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
        /*******************************************************************************************************************************************************************************************/


    }

    public double getLength() {
        //méthode permettant de trouver la longueur du parcours
        for (int n = 0; n < data3.size(); n++) {
            if (n < (data3.size() - 1))
                length = length + SphericalUtil.computeDistanceBetween(data3.get(n), data3.get(n + 1)); //data3 récupère le parcours aléatoire découpé en chackpoint
            // entre chaque checkpoint on a quasiment une ligne droite.
            //En sommant les distances consécutives on trouve la longueur
            // du parcours avec grande précision
        }

        length = length * (0.1);

        length = Math.round(length);

        length = length * (0.1);

        length = Math.round(length);

        length = length * (0.1);

        return length;
    }


    /**
     * ******************** Ici on génère la requête et on la triate, avec notamment, la classe ConnexionPath qui parse l'url, puis les classes
     * Ci-dessous qui s'occupent de traiter ce que l'on récupère en utilisant d'autres de nos classes comme PathJSONParser qui va lire le JSON
     */
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

            polylineRandom = mMap.addPolyline(polyLineOptions);

            System.out.println("votre parcours fait " + " " + getLength(data3));

            Toast toast = Toast.makeText(MainActivity.this, "votre parcours fait" + " " + getLength(data3) + "km environ",
                    Toast.LENGTH_SHORT);
            toast.show();


            if (Math.abs(Math.abs(getLength(data3)) - distanceChosen * 0.001) > 2.00) {
                showAlert(MainActivity.this, "Nous avons du mal à vous fournir un parcours de la longueur demandée.", "INFORMATION");
            }


        }

        public double getLength(ArrayList<LatLng> retour) {
            for (int n = 0; n < retour.size(); n++) {
                if (n < (retour.size() - 1))
                    length = length + SphericalUtil.computeDistanceBetween(retour.get(n), retour.get(n + 1));
            }

            length = length * (0.1);

            length = Math.round(length);

            length = length * (0.1);

            length = Math.round(length);

            length = length * (0.1);

            return length;
        }

        /*******************************************************************************************************************************/
    }


}