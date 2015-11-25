package com.example.application;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class FavoriteItinerariesActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours__favoris);

        setActionBar("Parcours Favoris");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("EMAIL", "");

        String nomServeur = getResources().getString(R.string.nom_serveur);
        FavoriteRequestTask requete = new FavoriteRequestTask(this, 0);
        requete.execute(nomServeur + "/parcours_favoris.php?email=" + email);

        View messageRetour = findViewById(R.id.messageRetourF);
        Typeface typeFaceMessage = CustomFontsLoader.getTypeface(
                FavoriteItinerariesActivity.this, 4);
        if (typeFaceMessage != null)
            ((TextView) messageRetour).setTypeface(typeFaceMessage);

        View nom = findViewById(R.id.nom);
        Typeface typeFaceNom = CustomFontsLoader.getTypeface(
                FavoriteItinerariesActivity.this, 4);
        if (typeFaceNom != null)
            ((TextView) nom).setTypeface(typeFaceNom);

        View note = findViewById(R.id.note);
        Typeface typeFaceNote = CustomFontsLoader.getTypeface(
                FavoriteItinerariesActivity.this, 4);
        if (typeFaceNote != null)
            ((TextView) note).setTypeface(typeFaceNote);

        View date = findViewById(R.id.date);
        Typeface typeFaceDate = CustomFontsLoader.getTypeface(
                FavoriteItinerariesActivity.this, 4);
        if (typeFaceDate != null)
            ((TextView) date).setTypeface(typeFaceDate);
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
