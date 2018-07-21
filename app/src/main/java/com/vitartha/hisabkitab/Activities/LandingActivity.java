package com.vitartha.hisabkitab.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.vitartha.hisabkitab.R;

public class LandingActivity extends AppCompatActivity {

    ImageView person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hisab-Kitab(हिसाब-किताब)");
        toolbar.setTitleTextColor(Color.WHITE);

        person = findViewById(R.id.loginPerson);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

}
