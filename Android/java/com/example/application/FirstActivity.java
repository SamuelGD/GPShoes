package com.example.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final View affichageButton = findViewById(R.id.button1);
        Typeface typeFaceButton1 = CustomFontsLoader.getTypeface(FirstActivity.this, 6);
        if (typeFaceButton1 != null) ((TextView) affichageButton).setTypeface(typeFaceButton1);

        final View affichageText1 = findViewById(R.id.textView1);
        Typeface typeFaceText1 = CustomFontsLoader.getTypeface(FirstActivity.this, 6);
        if (typeFaceText1 != null) ((TextView) affichageText1).setTypeface(typeFaceText1);


        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(FirstActivity.this, ConnectionActivity.class);
                startActivity(intent);

            }
        });


    }


}
