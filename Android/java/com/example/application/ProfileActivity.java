package com.example.application;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        View button1 = findViewById(R.id.button1);
        Typeface typeFaceButton = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton != null) ((TextView) button1).setTypeface(typeFaceButton);

        View button2 = findViewById(R.id.button2);
        Typeface typeFaceButton2 = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton2 != null) ((TextView) button2).setTypeface(typeFaceButton2);

        View button3 = findViewById(R.id.button3);
        Typeface typeFaceButton3 = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton3 != null) ((TextView) button3).setTypeface(typeFaceButton3);

        View button4 = findViewById(R.id.button4);
        Typeface typeFaceButton4 = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton4 != null) ((TextView) button4).setTypeface(typeFaceButton4);

        View button5 = findViewById(R.id.button5);
        Typeface typeFaceButton5 = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton5 != null) ((TextView) button5).setTypeface(typeFaceButton5);

        View button6 = findViewById(R.id.button6);
        Typeface typeFaceButton6 = CustomFontsLoader.getTypeface(ProfileActivity.this, 1);
        if (typeFaceButton6 != null) ((TextView) button6).setTypeface(typeFaceButton6);

        final Button button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


            }
        });


    }
}
