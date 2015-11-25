package com.example.application;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItineraryVicinityActivity extends ActionBarActivity implements
        android.location.LocationListener {
    private double lat;
    private double lng;
    private LocationManager lm;
    private boolean locationReceived = false;
    private float rayonRecherche = 2;
    private EditText et_rayonRecherche;
    private String email;
    private String nomServeur;
    private ImageButton ib_chercher;
    private TextView messageRetour;
    private ListView lv_favoris;

    @Override
    protected void onResume() {
        super.onResume();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
                    this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
                this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours__alentours);

        setActionBar("Parcours alentours");


        View tv = findViewById(R.id.tv_rayonRecherche);
        Typeface typeFaceTv = CustomFontsLoader.getTypeface(
                ItineraryVicinityActivity.this, 6);
        if (typeFaceTv != null)
            ((TextView) tv).setTypeface(typeFaceTv);

        final View et = findViewById(R.id.et_rayonRecherche);
        Typeface typeFaceEt = CustomFontsLoader.getTypeface(
               ItineraryVicinityActivity.this, 6);
        if (typeFaceEt!= null)
            ((TextView) et).setTypeface(typeFaceEt);

        messageRetour = (TextView) findViewById(R.id.messageRetourA);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        email = preferences.getString("EMAIL", "");
        nomServeur = getResources().getString(R.string.nom_serveur);

        et_rayonRecherche = (EditText) findViewById(R.id.et_rayonRecherche);
        ib_chercher = (ImageButton) findViewById(R.id.b_chercher);
        lv_favoris = (ListView) findViewById(R.id.listeFavoris);

        ib_chercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationReceived) {
                    ItineraryVicinityActivity.this.rayonRecherche = Float.parseFloat(et_rayonRecherche.getText().toString());

                    // Nettoyer la ListView
                    ItineraryAdapter adapter = (ItineraryAdapter) lv_favoris.getAdapter();
                    if (adapter != null) {
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                    }


                    FavoriteRequestTask requete = new FavoriteRequestTask(ItineraryVicinityActivity.this, 1);
                    requete.execute(nomServeur + "/parcours_alentours.php?lat=" + lat
                            + "&lng=" + lng + "&email=" + email + "&rayon=" + rayonRecherche);

                    System.out.println("DBG "+"/parcours_alentours.php?lat=" + lat
                            + "&lng=" + lng + "&email=" + email + "&rayon=" + rayonRecherche);
                    messageRetour.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ItineraryVicinityActivity.this, "Le GPS n'est pas active", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        locationReceived = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

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
}
