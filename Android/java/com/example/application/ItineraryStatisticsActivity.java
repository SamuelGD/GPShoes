package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class ItineraryStatisticsActivity extends ActionBarActivity {
    private CheckBox suivi = null;
    private Button courir = null;
    private Button look = null;
    private EditText vitesseVisee = null;
    private ArrayList<LatLng> tableauCoordonnees = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques__parcours);

        setActionBar("Vos performances");

        View vitesse = findViewById(R.id.vitesse);
        Typeface typeFaceVitesse = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceVitesse != null)
            ((TextView) vitesse).setTypeface(typeFaceVitesse);

        View temps = findViewById(R.id.temps);
        Typeface typeFaceTemps = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceTemps != null)
            ((TextView) temps).setTypeface(typeFaceTemps);

        View kalo = findViewById(R.id.kalo);
        Typeface typeFaceKalo = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceKalo != null)
            ((TextView) kalo).setTypeface(typeFaceKalo);

        View longueur = findViewById(R.id.longueur);
        Typeface typeFaceLongueur = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceLongueur != null)
            ((TextView) longueur).setTypeface(typeFaceLongueur);

        View vitesseVisee1 = findViewById(R.id.vitesseVisee);
        Typeface typeFaceVisee1 = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceVisee1 != null)
            ((TextView) vitesseVisee1).setTypeface(typeFaceVisee1);

        View activer = findViewById(R.id.activer);
        Typeface typeFaceActiver = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceActiver != null)
            ((TextView) activer).setTypeface(typeFaceActiver);

        final View b_courir = findViewById(R.id.b_courir);
        Typeface typeFaceBcourir = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceBcourir!= null)
            ((TextView) b_courir).setTypeface(typeFaceBcourir);

        final View button = findViewById(R.id.button);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(
                ItineraryStatisticsActivity.this, 6);
        if (typeFaceButton!= null)
            ((TextView) button).setTypeface(typeFaceButton);



        suivi = (CheckBox) findViewById(R.id.cb_guider_vitesse);
        courir = (Button) findViewById(R.id.b_courir);
        vitesseVisee = (EditText) findViewById(R.id.vitesse_visee);
        look = (Button) findViewById(R.id.button);

        // On recupere l'id du parcours
        Intent intent = getIntent();
        final int idParcours = intent.getIntExtra("idParcours", 0);
        tableauCoordonnees = intent.getParcelableArrayListExtra(Guide.keyGuide);

        // On met a jour les statistiques de ce parcours pour l'utilisateur
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("EMAIL", "");

        String nomServeur = getResources().getString(R.string.nom_serveur);
        StatisticsRequestTask requete = new StatisticsRequestTask(this);
        requete.execute(nomServeur + "/statistiques.php?email=" + email + "&idParcours=" + idParcours);

        // Suivi de la vitesse et courir
        courir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activiteGuidage = new Intent(ItineraryStatisticsActivity.this, Guide.class);

                activiteGuidage.putParcelableArrayListExtra(Guide.keyGuide, tableauCoordonnees);

                if (suivi.isChecked()) { // Si le suivi est active on transmet la vitesse
                    double vitesse = 0;

                    try {
                        vitesse = Double.parseDouble(vitesseVisee.getText().toString());
                    } catch (NumberFormatException e) { // Si le champ est vide par exemple
                        vitesse = 0;
                    }

                    activiteGuidage.putExtra("vitesse", vitesse);
                }

                activiteGuidage.putExtra("idParcours", idParcours);

                ItineraryStatisticsActivity.this.startActivity(activiteGuidage);
                finish();
            }
        });

        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activiteLook = new Intent(ItineraryStatisticsActivity.this, MapActivity.class);

                activiteLook.putParcelableArrayListExtra(Guide.keyGuide, tableauCoordonnees);
                activiteLook.putExtra("idParcours", idParcours);

                if (suivi.isChecked()) { // Si le suivi est active on transmet la vitesse
                    double vitesse = 0;

                    try {
                        vitesse = Double.parseDouble(vitesseVisee.getText().toString());
                    } catch (NumberFormatException e) { // Si le champ est vide par exemple
                        vitesse = 0;
                    }

                    activiteLook.putExtra("vitesse", vitesse);
                }

                ItineraryStatisticsActivity.this.startActivity(activiteLook);
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
}
