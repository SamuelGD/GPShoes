package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Guide extends FragmentActivity implements
        android.location.LocationListener {

    public final static String keyGuide = "key_to_Guide";

    private ArrayList<LatLng> tableauIndic = new ArrayList<LatLng>();

    private LatLng myPosition;
    private boolean isFull = false;
    private LocationManager lm;
    private GoogleMap mMap;
    private Marker myMarkerPosition;
    private boolean done = false;
    private ArrayList<Indication> retour = new ArrayList<Indication>();
    private ArrayList<String> data2 = new ArrayList<String>();
    private ArrayList<LatLng> data3 = new ArrayList<LatLng>();
    private Polyline polylineRandom;
    private boolean isInstructionSet = false;
    private LatLng currentCheckPoint;
    private boolean start = false;
    private boolean out = false;
    private int indiceNotLost = 1;
    private double currentTolerance = 0.00;
    private double currentToleranceLost = 0.00;
    private LatLng Paris = new LatLng(48.8534100, 2.3488000);
    private double testAngle = 0;
    private double length = 0;
    private ArrayList<Direction> directionFinal = new ArrayList<Direction>();
    private boolean flag = true;
    private int i = 0;
    ArrayList<Direction> direction = new ArrayList<Direction>();
    private int countdown = 2;
    private ArrayList<Marker> tableauMarker = new ArrayList<Marker>();
    private boolean need = true;
    private Date dateStart;
    private Date dateEnd;
    private int idParcours;
    private double vitesseVisee;
    private double longueur = 0;
    private boolean tailleDeux = false;
    private double vitesseCourante = 0;
    private double vitessePast = 0;
    private Date maDateCourante;
    private Date maDatePast;
    private LatLng oldPosition;
    private boolean isZero = false;

    // Bluetooth
    private BluetoothHandler myBluetoothHandler1;
    private BluetoothHandler myBluetoothHandler2;
    private Vibrator vibrateur = null;
    private boolean dateDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vibrateur = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);



        try{
            myBluetoothHandler1 = new BluetoothHandler("RNBT-1F69");
        }
        catch (Exception e)
        {
            Log.d("Cannot create 1", e.toString());
        }

        try{
            myBluetoothHandler2 =  new BluetoothHandler("RNBT-E538");
        }
        catch (Exception e)
        {
            Log.d("Cannot create 2", e.toString());
        }

        try {
            if(myBluetoothHandler1.isConnected())
                myBluetoothHandler1.close();

        } catch (Exception e) {
            Log.d("Bluetooth test 1", e.toString());
        }

        try {

            if(myBluetoothHandler2.isConnected())
                myBluetoothHandler2.close();

        } catch (Exception e) {
            Log.d("Bluetooth test 2", e.toString());
        }

        try {
            myBluetoothHandler1.Connect();
            myBluetoothHandler2.Connect();

        } catch (Exception e) {
            Log.d("Bluetooth test Connect", e.toString());
        }

        setUpMapIfNeeded();

        dateStart = new Date();

        maDatePast = new Date();

        Intent intent = getIntent();

        tableauIndic = intent.getParcelableArrayListExtra(keyGuide);
        idParcours = intent.getIntExtra("idParcours", 0);
        vitesseVisee = intent.getDoubleExtra("vitesse", 0);
        length = intent.getDoubleExtra("longueur", 0);

        if( Math.abs(vitesseVisee) < 1 )
        {
            isZero = true;
        }



        mMap.setMyLocationEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 13));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                i++;

                if( !isZero ) {


                    if (!dateDone) {
                        dateDone = true;
                        oldPosition = latLng;
                        i++;
                    } else {

                        if (i % 3 == 0) {

                            maDateCourante = new Date();

                            vitesseCourante = ((SphericalUtil.computeDistanceBetween(myPosition, oldPosition) * (0.001)) / ((maDateCourante.getTime() - maDatePast.getTime()) * (0.001 / 3600)));


                            if (vitesseCourante < vitesseVisee) {

                                Toast toast1 = Toast.makeText(Guide.this, "Vous allez trop lentement" + " V = " + " " + Math.floor(vitesseCourante) + " "  + "Km/h environ", Toast.LENGTH_SHORT);
                                toast1.show();
                                try {
                                    vibrateur.vibrate(2000);
                                } catch (Exception e) {

                                    System.out.println("ATTENTION: pas de vibrations");
                                }

                            }

                            maDatePast = maDateCourante;
                            oldPosition = myPosition;

                        }

                    }

                }













                try {
                    if (!myBluetoothHandler1.isConnected())
                        myBluetoothHandler1.Connect();
                } catch (Exception e)
                {
                    Log.d("Cannot connect to 1", e.toString());
                }

                try {

                    if (!myBluetoothHandler2.isConnected())
                        myBluetoothHandler2.Connect();
                } catch (Exception e)
                {
                    Log.d("Cannot connect to 2", e.toString());

                }


                myPosition = new LatLng(latLng.latitude, latLng.longitude);

                if (done) {
                    LatLngInterpolator latLngInterpolator = new LatLngInterpolator() {
                        @Override
                        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                            double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                            return new LatLng(lat, lng);
                        }
                    };

                    MarkerAnimation.animateMarkerToGB(myMarkerPosition, myPosition, latLngInterpolator);

                    guidage(myPosition);

                } else {

                    myMarkerPosition = mMap.addMarker(new MarkerOptions().position(
                            myPosition).icon(
                            BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    done = true;
                }

            }
        });



        if (tableauIndic.size() == 2) {
            tailleDeux = true;
        }

        String url = ConnectionPath.getURLConnection(tableauIndic);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

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
    protected void onResume() {
        super.onResume();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0,
                    this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0,
                this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!myBluetoothHandler1.isConnected())
            myBluetoothHandler1.Connect();

        if(!myBluetoothHandler2.isConnected())
            myBluetoothHandler2.Connect();

        /*

        myPosition = new LatLng(location.getLatitude(), location.getLongitude());

        if( out )
        {
            if( SphericalUtil.computeDistanceBetween( directionFinal.get(1).getLatlng() , myPosition) < 30)
            {
                start = true;
                //dateStart = new Date();
                Toast toast = Toast.makeText(this, "Vous pouvez commençez à courir!!", Toast.LENGTH_SHORT);
                toast.show();

                //BIATCH : ici on va vibrer pour dire que la course commence
            }
        }

        if (done) {

            myMarkerPosition.setPosition(myPosition);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18));
            if(start) {
                guidage(myPosition);
            }

        } else {

            myMarkerPosition = mMap.addMarker(new MarkerOptions().position(
                    myPosition).icon(
                    BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            done = true;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 19));
        }

        */


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

        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    private class ReadTask extends AsyncTask<String, Integer, String> {

		/*
         * @Override protected void onPreExecute() { super.onPreExecute();
		 * Toast.makeText(getApplicationContext(),
		 * "Début du traitement asynchrone", Toast.LENGTH_LONG).show(); }
		 *
		 * @Override protected void onProgressUpdate(Integer... values){
		 * super.onProgressUpdate(values); bar.setVisibility(View.VISIBLE); //
		 * Mise à jour de la ProgressBar bar.setProgress(values[0]); }
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

                longueur = getLength(data3);


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


                if (need) {
                    System.out.println("ici on ajoute le début pour la première requete");
                    retour.add(new Indication(tableauIndic.get(0), "début "));
                } else {
                    retour.add(new Indication(myPosition, "début "));
                }


                for (int i = 0; i < data2.size() - 1; i++) {

                    if (SphericalUtil.computeDistanceBetween(retour.get(retour.size() - 1).getLatLng(), data3.get(i)) < 10) {

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


            isFull = true;

            isInstructionSet = true;

            polylineRandom = mMap.addPolyline(polyLineOptions);


            currentCheckPoint = directionFinal.get(1).getLatlng();

            currentTolerance = 1.3 * SphericalUtil.computeDistanceBetween(directionFinal.get(0).getLatlng(), currentCheckPoint);


            for (int i = 0; i < directionFinal.size(); i++) {
                System.out.println(directionFinal.get(i).getInfo());
                tableauMarker.add(mMap.addMarker(new MarkerOptions().position(directionFinal.get(i).getLatlng()).visible(true).draggable(true)));
                System.out.println(directionFinal.get(i).getInfo());
            }

            out = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tableauIndic.get(0), 19));


        }
    }


    public void guidage(LatLng position) {

        if (SphericalUtil.computeDistanceBetween(position, currentCheckPoint) < 30 ) {

            if (!tailleDeux) {


                if ( (countdown <= (tableauIndic.size() - 1))  && SphericalUtil.computeDistanceBetween(currentCheckPoint, tableauIndic.get(countdown)) < 30 ) {
                    countdown++;
                }
            }


            if (isFull) {

                if (indiceNotLost == (directionFinal.size() - 1)) {
                    flag = false;
                    Intent intent = null;
                    dateEnd = new Date();

                    try {

                        if (myBluetoothHandler1.isConnected())
                            myBluetoothHandler1.close();

                        if (myBluetoothHandler2.isConnected())
                            myBluetoothHandler2.close();
                    } catch (Exception e)
                    {
                        Log.d("Cannot connect 1", e.toString());
                    }



                    int minute = 0;
                    int seconde = 0;
                    int heure = 0;

                    long diff = dateEnd.getTime() - dateStart.getTime();

                    heure = (int) Math.floor((diff * 0.001) / (3600));

                    minute = (int) Math.floor(((diff * 0.001) / (3600) - heure) * 60);

                    seconde = (int) Math.floor(((diff * 0.001) / (60) - minute) * 60);

                    double vitesse = Math.floor((length) / ((diff * 0.001) / (3600)));
                    String temps = heure + ":" + minute + ":" + seconde;

                    if (idParcours == 0) {
                        intent = new Intent(Guide.this, SaveItineraryActivity.class);
                        intent.putParcelableArrayListExtra(keyGuide, tableauIndic);
                        intent.putExtra("vitesse", vitesse);
                        intent.putExtra("longueur", longueur);
                        intent.putExtra("temps", temps);
                    } else {
                        intent = new Intent(Guide.this, RateItineraryActivity.class);
                        intent.putExtra("idParcours", idParcours);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                        String email = preferences.getString("EMAIL", "");


                        String nomServeur = getResources().getString(R.string.nom_serveur);
                        String date = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
                                .format(new Date());

                        ProfileRequestTask requete = new ProfileRequestTask(null, 2);
                        requete.execute(nomServeur + "/maj_parcours.php?email=" + email + "&idParcours=" + idParcours + "&date=" + date + "&vitesse=" + vitesse + "&temps=" + temps);

                    }

                    startActivity(intent);
                    finish();


                }
                if (flag) {

                    String indication = directionFinal.get(indiceNotLost).getInfo();
                    vibrer(indication);
                    indiceNotLost++;
                    System.out.println("Mon checkPoint que je veux rejoindre est le suivant" + " " + currentCheckPoint);
                    currentCheckPoint = directionFinal.get(indiceNotLost).getLatlng();
                    currentTolerance = 1.3 * SphericalUtil.computeDistanceBetween(position, currentCheckPoint); // on change de tolérance

                }


            }
        } else if (SphericalUtil.computeDistanceBetween(position, currentCheckPoint) > currentTolerance) {

            directionFinal.clear();
            direction.clear();
            retour.clear();
            ArrayList<LatLng> tableauLost = new ArrayList<LatLng>();
            tableauLost.add(position);

            if (!tailleDeux) {
                for (int p = countdown; p < tableauIndic.size(); p++) {
                    tableauLost.add(tableauIndic.get(p));
                }

                polylineRandom.remove();
                for (int u = 0; u < tableauMarker.size(); u++) {
                    tableauMarker.get(u).remove();
                }
                tableauMarker.clear();
                indiceNotLost = 1;
                need = false;

                Toast toast = Toast.makeText(this, "vous êtes perdu", Toast.LENGTH_SHORT);
                toast.show();

                String url2 = ConnectionPath.getURLConnection(tableauLost);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url2);
            } else {
                tableauLost.add(tableauIndic.get(1));
                polylineRandom.remove();
                for (int u = 0; u < tableauMarker.size(); u++) {
                    tableauMarker.get(u).remove();
                }

                tableauMarker.clear();
                indiceNotLost = 1;
                need = false;

                Toast toast = Toast.makeText(this, "vous êtes perdu", Toast.LENGTH_SHORT);
                toast.show();

                try {
                    vibrateur.vibrate(4000);
                } catch( Exception e)
                {
                    Log.d("Cannot vibrate/Emulator", e.toString());
                }


                String url3 = ConnectionPath.getURLConnection(tableauLost);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url3);

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

    public void vibrer(String instruction) {

        if (instruction.contains("droite")) {
            Toast toast = Toast.makeText(this, "droite", Toast.LENGTH_SHORT);
            toast.show();
            try {
                vibrerChaussureD();

            } catch (Exception e)
            {
                Log.d("Cannot vibrate right", e.toString());
            }
        } else if (instruction.contains("gauche")) {
            Toast toast = Toast.makeText(this, "gauche", Toast.LENGTH_SHORT);
            toast.show();

            try {
                vibrerChaussureG();

            } catch (Exception e)
            {
                Log.d("Cannot vibrate left", e.toString());
            }
        } else if (instruction.contains("fin")) {
            Toast toast = Toast.makeText(this, "fin", Toast.LENGTH_SHORT);
            toast.show();

            try {
                vibrerChaussureG();
                vibrerChaussureD();

            } catch (Exception e)
            {
                Log.d("Cannot vibrate to end", e.toString());
            }


        }
    }

    public void vibrerChaussureG() { // Vibreur chaussure gauche
        vibrateur.vibrate(1000);
        myBluetoothHandler2.writeData(65);
        myBluetoothHandler2.writeData(66);
    }

    public void vibrerChaussureD() { // Vibreur chaussure droite
        vibrateur.vibrate(1000);
        myBluetoothHandler1.writeData(65);
        myBluetoothHandler1.writeData(66);

    }
}







   