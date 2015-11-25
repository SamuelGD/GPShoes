package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyItinerariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_parcours);


        View button1 = findViewById(R.id.button1);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(MyItinerariesActivity.this, 4);
        if (typeFaceButton != null) ((TextView) button1).setTypeface(typeFaceButton);

        View button2 = findViewById(R.id.button2);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(MyItinerariesActivity.this, 4);
        if (typeFaceButton2 != null) ((TextView) button2).setTypeface(typeFaceButton2);

        final Button buttonGot2 = (Button) findViewById(R.id.button2);
        final Button buttonGot3 = (Button) findViewById(R.id.button1);


        buttonGot2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyItinerariesActivity.this, FavoriteItinerariesActivity.class);
                startActivity(intent);
            }

        });

        buttonGot3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyItinerariesActivity.this, HistoryActivity.class);
                startActivity(intent);
            }

        });

    }

}
