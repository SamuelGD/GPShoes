package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends Activity{

    private boolean done = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);






        View button1 = findViewById(R.id.button1);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton != null)
            ((TextView) button1).setTypeface(typeFaceButton);

        View button3 = findViewById(R.id.button3);
        Typeface typeFaceButton3 = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton3 != null)
            ((TextView) button3).setTypeface(typeFaceButton3);

        View button4 = findViewById(R.id.button4);
        Typeface typeFaceButton4 = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton4 != null)
            ((TextView) button4).setTypeface(typeFaceButton4);

        View button5 = findViewById(R.id.button5);
        Typeface typeFaceButton5 = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton5 != null)
            ((TextView) button5).setTypeface(typeFaceButton5);

        View button6 = findViewById(R.id.button6);
        Typeface typeFaceButton6 = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton6 != null)
            ((TextView) button6).setTypeface(typeFaceButton6);

        View button7 = findViewById(R.id.button7);
        Typeface typeFaceButton7 = CustomFontsLoader.getTypeface(
                MainMenuActivity.this, 4);
        if (typeFaceButton7 != null)
            ((TextView) button7).setTypeface(typeFaceButton7);

        final Button button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(MainMenuActivity.this,
                        MainActivity.class);
                startActivity(intent);


            }
        });

        final Button buttonGot1 = (Button) findViewById(R.id.button1);
        buttonGot1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                Intent intent = new Intent(MainMenuActivity.this,
                        MyItinerariesActivity.class);
                startActivity(intent);


            }
        });

        final Button buttonGotOption = (Button) findViewById(R.id.button5);
        buttonGotOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                Intent intent = new Intent(MainMenuActivity.this,
                        BlindActivity.class);
                startActivity(intent);


            }
        });

        final Button buttonGot2 = (Button) findViewById(R.id.button4);
        buttonGot2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(MainMenuActivity.this,
                        EditItineraryActivity.class);
                startActivity(intent);


            }
        });

        final Button buttonGot7 = (Button) findViewById(R.id.button7);
        buttonGot7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(MainMenuActivity.this,
                        SettingsActivity.class);

                startActivity(intent);

            }
        });

        final Button buttonGot3 = (Button) findViewById(R.id.button3);
        buttonGot3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(MainMenuActivity.this,
                        ItineraryVicinityActivity.class);

                startActivity(intent);


            }
        });


    }



}