package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class RateItineraryActivity extends ActionBarActivity {

    private RadioGroup reponse = null;
    private Spinner notes = null;
    private Button bouton = null;
    private CheckBox favori = null;
    private int idParcours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noter__parcours);

        setActionBar("Noter le parcours");

        final View question2 = findViewById(R.id.question2);
        Typeface typeFaceQ2 = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceQ2 != null)
            ((TextView) question2).setTypeface(typeFaceQ2);

        final View oui1 = findViewById(R.id.oui1);
        Typeface typeFaceOui1 = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceOui1 != null)
            ((TextView) oui1).setTypeface(typeFaceOui1);

        final View non2 = findViewById(R.id.non2);
        Typeface typeFaceNon2 = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceNon2 != null)
            ((TextView) non2).setTypeface(typeFaceNon2);

        final View tv1 = findViewById(R.id.note2);
        Typeface typeFaceTv1 = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceTv1 != null)
            ((TextView) tv1).setTypeface(typeFaceTv1);

        final View tv2 = findViewById(R.id.favori2);
        Typeface typeFaceTv2 = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceTv2 != null)
            ((TextView) tv2).setTypeface(typeFaceTv2);



        final View sauv = findViewById(R.id.noter);
        Typeface typeFaceSauv = CustomFontsLoader.getTypeface(
                RateItineraryActivity.this, 6);
        if (typeFaceSauv != null)
            ((TextView) sauv).setTypeface(typeFaceSauv);

        reponse = (RadioGroup) findViewById(R.id.reponse2);
        notes = (Spinner) findViewById(R.id.notes2);
        bouton = (Button) findViewById(R.id.noter);
        favori = (CheckBox) findViewById(R.id.favori2);

        Intent intent = getIntent();
        idParcours = intent.getIntExtra("idParcours", 0);

        // On ajoute les notes possibles au bouton de vote: de 1 a 5
        ArrayList<String> listeNotes = new ArrayList<String>();
        for (int i = 1; i <= 5; i++)
            listeNotes.add(Integer.toString(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeNotes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notes.setAdapter(adapter);

        reponse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.non2:
                        // Si l'utilisateur ne sauvegarde pas le parcours, retour accueil

                        Intent activiteAccueil = new Intent(RateItineraryActivity.this, MainMenuActivity.class);

                        startActivity(activiteAccueil);
                        finish();
                        break;
                }
            }
        });

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RateItineraryActivity.this);
                String email = preferences.getString("EMAIL", "");
                String note = notes.getSelectedItem().toString();

                String nomServeur = getResources().getString(R.string.nom_serveur);

                String s_favori = "false";
                if (favori.isChecked())
                    s_favori = "true";

                ProfileRequestTask requete = new ProfileRequestTask(null, 2);
                requete.execute(nomServeur + "/noter_parcours.php?email=" + email + "&idParcours=" + idParcours + "&favori=" + s_favori + "&note=" + note);

                // Retour accueil
                Intent activiteAccueil = new Intent(RateItineraryActivity.this, MainMenuActivity.class);
                startActivity(activiteAccueil);
                finish();
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