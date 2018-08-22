package com.vitartha.hisabkitab.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.R;

public class SplashScreen extends AppCompatActivity {

    private int SPLASH_OUT_TIME = 3000;
    SharedPreference spAdap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        spAdap = new SharedPreference(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spAdap.getString(key.server.key_token) == null) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, Dashboard.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_OUT_TIME);
    }
}

