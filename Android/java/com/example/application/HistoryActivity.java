package com.example.application;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

public class HistoryActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours__historique);

        setActionBar("Historique");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("EMAIL", "");

        String nomServeur = getResources().getString(R.string.nom_serveur);
        FavoriteRequestTask requete = new FavoriteRequestTask(this, 2);
        requete.execute(nomServeur + "/parcours_historique.php?email=" + email);
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