package com.example.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ItineraryAdapter extends BaseAdapter {
    private final Context context;
    private final List<Itinerary> values;
    private int position;
    private int numeroActivite; // 0 Favoris, 1 Alentours, 2 Historique
    public final static String keyGuide = "key_to_Guide";

    public ItineraryAdapter(Context context, List<Itinerary> values, int numeroActivite) {
        this.context = context;
        this.values = values;
        this.numeroActivite = numeroActivite;
    }

    public int getPosition() {
        return position;
    }

    public void clearData() {
        values.clear();
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favoris, parent, false);
        }

        TextView nom = (TextView) convertView.findViewById(R.id.nomFavoris);
        TextView note = (TextView) convertView.findViewById(R.id.noteFavoris);
        TableLayout tl_view = (TableLayout) convertView.findViewById(R.id.tl_view);
        ImageButton star = (ImageButton) convertView.findViewById(R.id.checkFavoris);
        TextView date = (TextView) convertView.findViewById(R.id.dateFavoris);
        TableLayout tl_alentours = (TableLayout) ((Activity) context).findViewById(R.id.tl_parcours);
        TableLayout tl_legendeF = (TableLayout) ((Activity) context).findViewById(R.id.tl_legendeF);
        TableLayout tl_legendeH = (TableLayout) ((Activity) context).findViewById(R.id.tl_legendeH);





        Typeface typeFaceNom = CustomFontsLoader.getTypeface(  nom.getContext() , 6 );
        if (typeFaceNom != null)
            (nom).setTypeface(typeFaceNom);

        Typeface typeFaceNote = CustomFontsLoader.getTypeface( date.getContext() , 6);
        if (typeFaceNote != null)
            (date).setTypeface(typeFaceNote);

        if (numeroActivite == 1) { // si activite parcours alentours, on desactive les checkboxs et la date
            star.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            tl_alentours.setVisibility(View.VISIBLE);

        } else if (numeroActivite == 2) {// si activite historique, on desactive les checkboxs
            star.setVisibility(View.GONE);
            tl_legendeH.setVisibility(View.VISIBLE);
        } else if (numeroActivite == 0) { // Si activité favoris
            tl_legendeF.setVisibility(View.VISIBLE);
        }

        this.position = position;
        final int position2 = position;


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String email = preferences.getString("EMAIL", "");

                String nomServeur = context.getResources().getString(R.string.nom_serveur);
                int idParcours = values.get(position2).getId();

                String variables = "/favoris.php?email=" + email + "&idParcours=" + idParcours + "&favori=";

                boolean selected = v.isSelected();
                if (selected) { // on ajoute le parcours aux favoris
                    new ProfileRequestTask(null, 2).execute(nomServeur + variables + "true");
                    ((ImageButton) v).setImageResource(R.drawable.star_filled);
                } else { // On supprime le parcours des favoris
                    new ProfileRequestTask(null, 2).execute(nomServeur + variables + "false");
                    ((ImageButton) v).setImageResource(R.drawable.star_empty);
                }

                v.setSelected(!selected);
            }
        });

        tl_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idParcours = values.get(position2).getId();

                // Aller a l'activite de statistiques avant le guidage
                // Passer en argument les points GPS
                ArrayList<LatLng> points = values.get(position2).getPoints();
                Intent activiteStatistiques = new Intent(context, ItineraryStatisticsActivity.class);

                activiteStatistiques.putParcelableArrayListExtra(keyGuide, points);
                activiteStatistiques.putExtra("idParcours", idParcours);

                context.startActivity(activiteStatistiques);

                ((Activity) context).finish();
            }
        });

        nom.setText(values.get(position).getNom());
        note.setText(Integer.toString(values.get(position).getNote()) + " / 5");
        date.setText(values.get(position).getDate());

        return convertView;
    }

}