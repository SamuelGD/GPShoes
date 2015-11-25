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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaveItineraryActivity extends ActionBarActivity {

    private RadioGroup reponse = null;
    private Spinner notes = null;
    private Button bouton = null;
    private EditText nomParcours = null;
    private CheckBox favori = null;
    private ArrayList<LatLng> parcours;
    public final static String key = "key_to_SavePath";
    public final static String keyGuide = "key_to_Guide";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrer__parcours);

        setActionBar("Enregistrer le parcours");


        final View question = findViewById(R.id.question);
        Typeface typeFaceQ = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceQ != null)
            ((TextView) question).setTypeface(typeFaceQ);

        final View oui = findViewById(R.id.oui);
        Typeface typeFaceOui = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceOui != null)
            ((TextView) oui).setTypeface(typeFaceOui);

        final View non = findViewById(R.id.non);
        Typeface typeFaceNon = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceNon != null)
            ((TextView) non).setTypeface(typeFaceNon);

        final View tv1 = findViewById(R.id.tv1);
        Typeface typeFaceTv1 = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceTv1 != null)
            ((TextView) tv1).setTypeface(typeFaceTv1);

        final View tv2 = findViewById(R.id.tv2);
        Typeface typeFaceTv2 = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceTv2 != null)
            ((TextView) tv2).setTypeface(typeFaceTv2);

        final View nom = findViewById(R.id.nomParcours);
        Typeface typeFaceNom = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceNom != null)
            ((TextView) nom).setTypeface(typeFaceNom);


        final View favori1 = findViewById(R.id.favori);
        Typeface typeFaceFavori1 = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceFavori1 != null)
            ((TextView) favori1).setTypeface(typeFaceFavori1);

        final View sauv = findViewById(R.id.sauvegarder);
        Typeface typeFaceSauv = CustomFontsLoader.getTypeface(
                SaveItineraryActivity.this, 6);
        if (typeFaceSauv != null)
            ((TextView) sauv).setTypeface(typeFaceSauv);



        reponse = (RadioGroup) findViewById(R.id.reponse);
        notes = (Spinner) findViewById(R.id.notes);
        bouton = (Button) findViewById(R.id.sauvegarder);
        nomParcours = (EditText) findViewById(R.id.nomParcours);
        favori = (CheckBox) findViewById(R.id.favori);


        // On ajoute les notes possibles au bouton de vote: de 1 a 5

        ArrayList<String> listeNotes = new ArrayList<String>();
        for (int i = 1; i <= 5; i++)
            listeNotes.add(Integer.toString(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listeNotes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notes.setAdapter(adapter);

        reponse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.non:
                        // Si l'utilisateur ne sauvegarde pas le parcours, retour accueil

                        Intent activiteAccueil = new Intent(
                                SaveItineraryActivity.this, MainMenuActivity.class);

                        startActivity(activiteAccueil);
                        finish();
                        break;
                }
            }
        });

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nomParcours.getText().toString();
                if (nom.isEmpty()) {
                    Toast.makeText(SaveItineraryActivity.this, R.string.nomVide,
                            Toast.LENGTH_SHORT).show();
                } else {

                    Intent retrieveIntent = getIntent();

                    parcours = retrieveIntent.getParcelableArrayListExtra(keyGuide);

                    double longueur = retrieveIntent.getDoubleExtra("longueur", 0);
                    double vitesse = retrieveIntent.getDoubleExtra("vitesse", 0);
                    String temps = retrieveIntent.getStringExtra("temps");


                    // verifier parcours non vide


                    SharedPreferences preferences = PreferenceManager
                            .getDefaultSharedPreferences(SaveItineraryActivity.this);
                    String email = preferences.getString("EMAIL", "");
                    String note = notes.getSelectedItem().toString();
                    String date = new SimpleDateFormat("dd/MM kk:mm")
                            .format(new Date());

                    String nomServeur = getResources().getString(
                            R.string.nom_serveur);

                    System.out.println("DBG : vitesse : "+vitesse + " date : " + date + " longueur : "+longueur+" temps : "+temps);

                    new SaveItineraryRequestTask(email, nom, parcours, note,
                            favori.isChecked(), vitesse, date, longueur, temps)
                            .execute(nomServeur + "/enregistrer_parcours.php");

                    // Retour accueil

                    Intent activiteAccueil = new Intent(
                            SaveItineraryActivity.this, MainMenuActivity.class);

                    startActivity(activiteAccueil);
                    finish();
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


}