package com.vitartha.hisabkitab.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitartha.hisabkitab.R;

public class LandingActivity extends AppCompatActivity {

    ImageView fb, twitter, gplus;
    TextView privacy, call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hisab-Kitab(हिसाब-किताब)");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        /* For back button */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fb = findViewById(R.id.fbID);
        twitter = findViewById(R.id.twitID);
        gplus = findViewById(R.id.GPLusID);
        privacy = findViewById(R.id.privacy);
        call = findViewById(R.id.call);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/vitarthaa"));
                startActivity(browserIntent);
            }
        });

        gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+Vitartha"));
                startActivity(browserIntent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/vitartha"));
                startActivity(browserIntent);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hisabkitab.in/policy"));
                startActivity(browserIntent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01204545647"));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        finish();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        return true;
    }
}

