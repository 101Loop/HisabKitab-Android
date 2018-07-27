package com.vitartha.hisabkitab.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.R;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    RelativeLayout debithistory, credithistory;
    SharedPreference spAdap;
    TextView one, two, three, four, five, one_selected, two_selected, three_selected, four_selected, five_selected;
    EditText feedbackmsg;
    Button feedbacksbmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        debithistory = findViewById(R.id.layout_debit);
        credithistory = findViewById(R.id.layout_credit);
        spAdap = new SharedPreference(Dashboard.this);

      //  JWT jwt = (JWT) getIntent().getParcelableExtra("jwt");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feedback Form!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                FeedbackForm();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, UpdateProfile.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        debithistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, TransactionHistory.class);
                i.putExtra("category", "D");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        credithistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, TransactionHistory.class);
                i.putExtra("category", "C");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(Dashboard.this, LandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            // Handle the camera action
        } else if (id == R.id.nav_dashboard) {
            Intent i = new Intent(Dashboard.this, Dashboard.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_credit) {
            Intent i = new Intent(Dashboard.this, TransactionHistory.class);
            i.putExtra("category", "C");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_debit) {
            Intent i = new Intent(Dashboard.this, TransactionHistory.class);
            i.putExtra("category", "D");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(Dashboard.this, LandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            spAdap.clearData();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void FeedbackForm() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.feedback_form_layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        one = view.findViewById(R.id.first);
        two = view.findViewById(R.id.second);
        three = view.findViewById(R.id.third);
        four = view.findViewById(R.id.fourth);
        five = view.findViewById(R.id.five);
        one_selected = view.findViewById(R.id.first_selected);
        two_selected = view.findViewById(R.id.second_selected);
        three_selected = view.findViewById(R.id.third_selected);
        four_selected = view.findViewById(R.id.fourth_selected);
        five_selected = view.findViewById(R.id.five_selected);
        feedbackmsg = view.findViewById(R.id.feedback);
        feedbacksbmt = view.findViewById(R.id.submit);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);

        one_selected.setOnClickListener(this);
        two_selected.setOnClickListener(this);
        three_selected.setOnClickListener(this);
        four_selected.setOnClickListener(this);
        five_selected.setOnClickListener(this);

      /*  feedbacksbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedbackmsg.getText().length()<0) {
                    feedbackmsg.setError("Enter your feedback");
                } else {
                }
            }
        });*/

    }

    /**On Click Listener of 5 stars of Feedback**/
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.first:
                one.setVisibility(View.INVISIBLE);
                one_selected.setVisibility(View.VISIBLE);

                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.INVISIBLE);
                three_selected.setVisibility(View.INVISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("Hated it :(");
                break;
            case  R.id.second:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.INVISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);
                feedbackmsg.setText("Disliked it :/");
                break;

            case  R.id.third:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("It's ok!");

                break;

            case  R.id.fourth:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.VISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("Liked it :)");

                break;

            case  R.id.five:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.VISIBLE);
                five_selected.setVisibility(View.VISIBLE);

                feedbackmsg.setText("Loved it ^_^");

                break;


            case R.id.first_selected:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.INVISIBLE);
                three_selected.setVisibility(View.INVISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("Hated it :(");

                break;

            case R.id.second_selected:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.INVISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("Disliked it :/");

                break;
            case R.id.third_selected:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.VISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.INVISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("It's ok!");

                break;
            case R.id.fourth_selected:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.VISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.VISIBLE);
                five_selected.setVisibility(View.INVISIBLE);

                feedbackmsg.setText("Liked it :)");

                break;
            case R.id.five_selected:
                one.setVisibility(View.INVISIBLE);
                two.setVisibility(View.INVISIBLE);
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.INVISIBLE);
                five.setVisibility(View.INVISIBLE);
                one_selected.setVisibility(View.VISIBLE);
                two_selected.setVisibility(View.VISIBLE);
                three_selected.setVisibility(View.VISIBLE);
                four_selected.setVisibility(View.VISIBLE);
                five_selected.setVisibility(View.VISIBLE);

                feedbackmsg.setText("Loved it ^_^");

                break;
        }
    }
}
