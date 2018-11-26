package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.Transactions_RecyclerView;
import com.vitartha.hisabkitab.Adapters.Divider_RecyclerView;
import com.vitartha.hisabkitab.Adapters.HisabKitabDeleteRequest;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    SharedPreference spAdap;
    TextView one, two, three, four, five, one_selected, two_selected, three_selected, four_selected, five_selected, noTransMsg,
            TotalTransaction, TotalAmount;
    EditText feedbackmsg;
    Button feedbacksbmt;
    String show_url;
    RelativeLayout trasactiondetails;
    FloatingActionButton addfab, creditfab, debitfab;
    private Boolean isFabOpen = false;
    private Animation show_fab1, hide_fab1;
    String nextpage;
    static String jwtName, jwtEmail, jwtContact;
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    public int VisibleItemCount, TotalItemCount, PastVisibleItems, count=1;
    private boolean loading = true;
    RecyclerView recyclerView;
    Transactions_RecyclerView Trans_recyclerView;
    boolean doublepress = false;
    private ArrayList<DebitDetails> Trans_HistoryList = new ArrayList<>();
    private ArrayList<DebitDetails> newHistorieslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        show_url = key.transactions.show_url;
        noTransMsg = findViewById(R.id.notransaction);
        show_fab1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
        hide_fab1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);

        spAdap = new SharedPreference(Dashboard.this);
        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);

        addfab = findViewById(R.id.addfab);
        creditfab = findViewById(R.id.fabcredit);
        debitfab = findViewById(R.id.fabdebit);
        TotalAmount = findViewById(R.id.TotalamtValue);
        TotalTransaction = findViewById(R.id.TotattransValue);
        trasactiondetails = findViewById(R.id.transactiondetaillayout);


        Trans_recyclerView = new Transactions_RecyclerView(Trans_HistoryList, Dashboard.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(Trans_recyclerView);
        recyclerView.addItemDecoration(new Divider_RecyclerView(this, LinearLayoutManager.VERTICAL));

        /* Feed back API is not working, so, commenting Feedback FAB icon **/
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feedback Form!", Snackbar.LENGTH_LONG)
                        .setAction("Feedback", null).show();

                FeedbackForm();
            }
        });*/


        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        creditfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dashboard.this, AddDebit.class);
                spAdap.saveData("category", "C");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        debitfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Dashboard.this, AddDebit.class);
                spAdap.saveData("category", "D");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
        //onNavigationItemSelected(navigationView.getMenu().getItem(0));
        // navigationView.getMenu().getItem(0).setChecked(false);


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, UpdateProfile.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        JWT jwt = new JWT(spAdap.getString("token"));
        Claim claimEmail = jwt.getClaim("email");
        Claim claimName = jwt.getClaim("name");
        Claim claimContact = jwt.getClaim("mobile");
        jwtName = claimName.asString();
        jwtEmail = claimEmail.asString();
        jwtContact = claimContact.asString();


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(Trans_recyclerView);

        //When recycler reached at bottom call to get next page data**
        Trans_recyclerView.setOnBottomReachListener(new Transactions_RecyclerView.OnBottomReachListener() {
            @Override
            public void loadMoreData(int position) {
                if(!nextpage.equals("null")){
                    fetchtransaction(nextpage);
                }else{
                    Toast.makeText(Dashboard.this, "No more transactions in this list!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard2, menu);
        return true;
    }

    /***
     * This function checks whether the back button is pressed once or twice.
     * On pressing once, it gives warning
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doublepress) {
                super.onBackPressed();
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
                return;
            }
            this.doublepress = true;
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
        } else if (id == R.id.nav_home) {
            Intent i = new Intent(Dashboard.this, LandingActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_credit) {
            Intent i = new Intent(Dashboard.this, TransactionHistory.class);
            spAdap.saveData("category", "C");
            i.putExtra("filter_url", "");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_debit) {
            Intent i = new Intent(Dashboard.this, TransactionHistory.class);
            spAdap.saveData("category", "D");
            i.putExtra("filter_url", "");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (id == R.id.nav_call) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            final View dialogView = inflater.inflate(R.layout.contactus_alertbox, null);
            alert.setView(dialogView);

            ImageView call, mail;
            call = dialogView.findViewById(R.id.callus);
            mail = dialogView.findViewById(R.id.mailus);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:01204545647"));
                    startActivity(intent);
                }
            });
            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "info@vitartha.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT,"Regarding Hisab-Kitab");
                    email.putExtra(Intent.EXTRA_TEXT, "Message:");
                    //need this to prompt email client only
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Open Email through :"));
                }
            });
            alert.show();
        }
        else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Hisab-Kitab");
            String message = "\nLet me recommend you this application\n\n";
            message = message + "https://play.google.com/store/apps/details?id=com.vitartha.hisabkitab";
            i.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(i, "Share via:"));

        } else if (id == R.id.nav_logout) {
            final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
            alertdialog.setMessage("Are you sure?");
            alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Dashboard.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    spAdap.clearData();
                    finish();
                }
            });
            alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertdialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** to show transactions url**/
    public void fetchtransaction(String urlobj) {
        progressDialog.setMessage("Fetching History...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.GET,
                urlobj, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    checkingstatus(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Dashboard.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, Dashboard.this), this);
        /*, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String errorStr = new String(error.networkResponse.data);
                try{
                    JSONObject jObj = new JSONObject(errorStr);
                    // Getting error object
                    String objError = jObj.optString("detail");
                    Toast.makeText(Dashboard.this, objError,  Toast.LENGTH_SHORT).show();
                } catch (JSONException e){
                    Toast.makeText(Dashboard.this, "Some Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        })*/
        requestQueue.add(jsonObjectRequest);
    }

    /**Getting list of transactions from server and setting it to Recyclerview **/
    public void checkingstatus(JSONObject response) throws Exception {
        progressDialog.dismiss();
        JSONArray array = response.optJSONArray("results");
        nextpage = response.optString("next");
        for(int i=0; i<array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            DebitDetails debitDetails = new DebitDetails(obj);
            Trans_HistoryList.add(debitDetails);
        }
        int count = Trans_recyclerView.getItemCount();
        // Trans_recyclerView.reloadData(Trans_HistoryList);
        Trans_recyclerView.notifyDataSetChanged();

        if(count <= 0) {
            noTransMsg.setVisibility(View.VISIBLE);
            trasactiondetails.setVisibility(View.INVISIBLE);

        }
        else {
            noTransMsg.setVisibility(View.INVISIBLE);
            String amt = response.optString("total_amount");
            TotalAmount.setText(amt);
            TotalTransaction.setText(response.optString("count"));
            trasactiondetails.setVisibility(View.VISIBLE);
        }

    }

    /** to delete transactions **/
    public void deletefromAPI(String urlobj, final ProgressDialog pd) {
        HisabKitabDeleteRequest jsonObjectRequest = new HisabKitabDeleteRequest(urlobj, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                    }
                },
                new HisabKitabErrorListener(progressDialog, Dashboard.this), this);
        requestQueue.add(jsonObjectRequest);
    }

    /**To update transaction**/
    public void updatetransaction(JSONObject jsonObject, String url) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verifyupdatestatus(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Dashboard.this, "Error while updating data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, Dashboard.this), this);
        requestQueue.add(jsonObjectRequest);
    }

    /**To show updation success message and reload activity***/
    public void verifyupdatestatus(JSONObject response) throws  JSONException{
        progressDialog.dismiss();
        Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
        Trans_HistoryList.clear();
        fetchtransaction(show_url);
    }

    /**Feedback Alert Box**/
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

        feedbacksbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedbackmsg.getText().length()<0) {
                    feedbackmsg.setError("Enter your feedback");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put(key.user_api.key_fullname, jwtName);
                        jsonObject.put(key.user_api.key_mail, jwtEmail);
                        jsonObject.put(key.user_api.key_mob, jwtContact);
                        jsonObject.put(key.user_api.key_msg, feedbackmsg.getText().toString());
                        sendfeedbackdata(jsonObject);

                    } catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(Dashboard.this, "Some error occured!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    /**Sending feedback data using POST Method**/
    public void  sendfeedbackdata(JSONObject object) {
        progressDialog.setMessage("Sending Feedback...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.POST, key.user_api.feedback_endpoint, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verifydata(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, Dashboard.this), this);
        requestQueue.add(jsonObjectRequest);
    }

    /**Showing success message of sending feedback successfully**/
    public void verifydata(JSONObject object) throws  JSONException {
        progressDialog.dismiss();
        Toast.makeText(this, "Thank you for your feedback :) Your feedback has been sent successfully!", Toast.LENGTH_SHORT).show();
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

    /*Animates credit and debit fab icon*/
    public void animateFab() {
        if (isFabOpen) {
            creditfab.startAnimation(hide_fab1);
            debitfab.startAnimation(hide_fab1);
            creditfab.setClickable(false);
            debitfab.setClickable(false);
            isFabOpen = false;
        } else {
            creditfab.startAnimation(show_fab1);
            debitfab.startAnimation(show_fab1);
            creditfab.setClickable(true);
            debitfab.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Trans_HistoryList.clear();
        try {
            fetchtransaction(show_url);
        } catch (Exception e){
            Toast.makeText(this, "Some error occured while fetching data...", Toast.LENGTH_SHORT).show();
        }
        Trans_recyclerView.reloadData(Trans_HistoryList);
    }

}
