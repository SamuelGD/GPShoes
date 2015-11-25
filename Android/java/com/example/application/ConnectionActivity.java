package com.example.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

public class ConnectionActivity extends ActionBarActivity {

    private CheckBox checkBox;
    private Button button;
    private EditText email;
    private EditText password;
    private TableRow tr_poids;
    private EditText et_poids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        setActionBar("Connexion");



        // On recupere les identifiants sauvegardes si ils existent
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String emailSauvegarde = preferences.getString("EMAIL", "");
        String passwordSauvegarde = preferences.getString("PASSWORD", "");

        if (!emailSauvegarde.isEmpty() && !passwordSauvegarde.isEmpty()) { // Si deja des identifiants
            // Changer d'activite

            Intent activiteAccueil = new Intent(this, MainMenuActivity.class);

            startActivity(activiteAccueil);
            finish();
        }

        // Changer le nom du bouton si l'utilisateur coche la case d'inscription
        button = (Button) findViewById(R.id.boutonConnexion);
        checkBox = (CheckBox) findViewById(R.id.creation);
        tr_poids = (TableRow) findViewById(R.id.tr_poids);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    button.setText(R.string.inscription);
                    tr_poids.setVisibility(View.VISIBLE);

                } else {
                    button.setText(R.string.connexion);
                    tr_poids.setVisibility(View.GONE);
                }
            }
        });

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        et_poids = (EditText) findViewById(R.id.et_poids);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordChaine = password.getText().toString();
                String emailChaine = email.getText().toString();

                // Contraintes email et password
                if (emailChaine.isEmpty() || passwordChaine.isEmpty() || !emailChaine.contains("@")) {
                    Toast.makeText(ConnectionActivity.this, R.string.invalides, Toast.LENGTH_SHORT).show();
                } else {
                    String nomServeur = getResources().getString(R.string.nom_serveur);
                    if (checkBox.isChecked()) {
                        // Cas de l'inscription

                        int poids = Integer.parseInt(et_poids.getText().toString());

                        String lien = nomServeur + "/creation.php?email=" + emailChaine + "&password=" + passwordChaine + "&poids=" + poids;
                        new LogInRequestTask(1, ConnectionActivity.this, emailChaine, passwordChaine).execute(lien);
                    } else {
                        String lien = nomServeur + "/connexion.php?email=" + emailChaine + "&password=" + passwordChaine;
                        new LogInRequestTask(2, ConnectionActivity.this, emailChaine, passwordChaine).execute(lien);
                    }
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