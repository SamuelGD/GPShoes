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
import android.widget.EditText;
import android.widget.TextView;


public class SettingsActivity extends ActionBarActivity {
    private EditText et_poids;
    private Button b_deconnexion;
    private Button b_maj;
    private String email;
    private String nomServeur;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setActionBar("Options");


        View poids = findViewById(R.id.poids);
        Typeface typeFacePoids = CustomFontsLoader.getTypeface(
                SettingsActivity.this, 6);
        if (typeFacePoids != null)
            ((TextView) poids).setTypeface(typeFacePoids);

        final View maj = findViewById(R.id.b_maj);
        Typeface typeFaceMaj = CustomFontsLoader.getTypeface(
                SettingsActivity.this, 6);
        if (typeFaceMaj!= null)
            ((TextView) maj).setTypeface(typeFaceMaj);

        final View deco = findViewById(R.id.b_deconnexion);
        Typeface typeFaceDeco = CustomFontsLoader.getTypeface(
                SettingsActivity.this, 6);
        if (typeFaceDeco!= null)
            ((TextView) deco).setTypeface(typeFaceDeco);


        et_poids = (EditText) findViewById(R.id.et_poids_options);
        b_deconnexion = (Button) findViewById(R.id.b_deconnexion);
        b_maj = (Button) findViewById(R.id.b_maj);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        email = preferences.getString("EMAIL", "");

        nomServeur = getResources().getString(R.string.nom_serveur);

        ProfileRequestTask requete = new ProfileRequestTask(this, 0);
        requete.execute(nomServeur + "/maj_profil.php?email=" + email);

        b_maj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int poids = Integer.parseInt(et_poids.getText().toString());
                ProfileRequestTask requete = new ProfileRequestTask(SettingsActivity.this, 1);
                requete.execute(nomServeur + "/maj_profil.php?email=" + email + "&poids=" + poids);
            }
        });

        b_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On supprime les identifiants en local
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("EMAIL");
                editor.remove("PASSWORD");
                editor.commit();

                // Retour a la page de connexion
                Intent activiteConnexion = new Intent(SettingsActivity.this,
                        ConnectionActivity.class);
                startActivity(activiteConnexion);
                finish(); //on ne peut plus revenir sur secondScreen sans se connecter
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
