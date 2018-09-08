package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.DebitT_RecyclerView;
import com.vitartha.hisabkitab.Adapters.Divider_RecyclerView;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.HisabKitabDeleteRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vitartha.hisabkitab.R.*;

public class TransactionHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DebitT_RecyclerView debitT_recyclerView;
    private ArrayList<DebitDetails> debitHistorieslist = new ArrayList<>();
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    SharedPreference spAdap;
    TextView morefilteroption, alpha_ascend, alpha_descend, noTransMsg, TotalTransaction, TotalAmount;
    String  filtered_url, url, sorturl, nextpage;
    ImageView filter, price_ascend, price_descend;
    LinearLayout filter_layout;
    RelativeLayout trasactiondetails;
    Boolean loading_url = true;
    public int VisibleItemCount, TotalItemCount, PastVisibleItems, count=1;
    private boolean loading = true, filterscreenvisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        spAdap = new SharedPreference(TransactionHistory.this);

        Toolbar toolbar = findViewById(id.toolbar);
        if(spAdap.getString("category").equals("C"))
            toolbar.setTitle("Credit Transactions");
        else if(spAdap.getString("category").equals("D"))
            toolbar.setTitle("Debit Transactions");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(id.recyclerView);

        filter_layout = findViewById(id.filterlayout);
        trasactiondetails = findViewById(R.id.transactiondetaillayout);
        morefilteroption = findViewById(id.morefilter);
        TotalAmount = findViewById(id.TotalamtValue);
        TotalTransaction = findViewById(id.TotattransValue);
        price_ascend = findViewById(id.Pricesort_ascending);
        price_descend = findViewById(id.Pricesort_descending);
        alpha_ascend = findViewById(id.Alphasort_ascending);
        alpha_descend = findViewById(id.Alphasort_descending);
        noTransMsg = findViewById(id.notransaction);
        debitT_recyclerView = new DebitT_RecyclerView(debitHistorieslist, TransactionHistory.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(debitT_recyclerView);
        progressDialog = new ProgressDialog(this);
        recyclerView.addItemDecoration(new Divider_RecyclerView(this, LinearLayoutManager.VERTICAL));

        filter = toolbar.findViewById(id.filterid);

        //For back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filtered_url = extras.getString("filter_url");

            //The key argument here must match that used in the other activity
        }
        url = key.transactions.show_url + "?&category=" + spAdap.getString("category");

        if(!filtered_url.equals(""))
            url = filtered_url;

        price_descend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price_descend.setBackgroundResource(drawable.ic_incoming_black);
                price_ascend.setBackgroundResource(drawable.ic_outgoing);
                alpha_descend.setTextColor(Color.parseColor("#1295c9"));
                alpha_ascend.setTextColor(Color.parseColor("#1295c9"));

                sorturl = url + "&ordering=-amount";
                url = sorturl;
                debitHistorieslist.clear();
                fetchtransaction(url);
            }
        });
        price_ascend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price_ascend.setBackgroundResource(drawable.ic_outgoing_black);
                price_descend.setBackgroundResource(drawable.ic_incoming);
                alpha_descend.setTextColor(Color.parseColor("#1295c9"));
                alpha_ascend.setTextColor(Color.parseColor("#1295c9"));

                sorturl = url +"&ordering=+amount";
                url = sorturl;
                debitHistorieslist.clear();
                fetchtransaction(url );
            }
        });
        alpha_descend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alpha_descend.setTextColor(Color.BLACK);
                price_descend.setBackgroundResource(drawable.ic_incoming);
                price_ascend.setBackgroundResource(drawable.ic_outgoing);
                alpha_ascend.setTextColor(Color.parseColor("#1295c9"));

                sorturl = url + "&ordering=-contact_name";
                url = sorturl;
                debitHistorieslist.clear();
                fetchtransaction(url);
            }
        });
        alpha_ascend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alpha_ascend.setTextColor(Color.BLACK);
                price_descend.setBackgroundResource(drawable.ic_incoming);
                price_ascend.setBackgroundResource(drawable.ic_outgoing);
                alpha_descend.setTextColor(Color.parseColor("#1295c9"));

                sorturl = url +"&ordering=+contact__name";
                url = sorturl;
                debitHistorieslist.clear();
                fetchtransaction(url);

            }
        });




        FloatingActionButton fab = (FloatingActionButton)findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionHistory.this, AddDebit.class);
                startActivity(i);
                overridePendingTransition(anim.slide_in, anim.slide_out);
            }
        });

        try {
            fetchtransaction(url);
        } catch (Exception e){
            Toast.makeText(this, "Some error occured while fetching data...", Toast.LENGTH_SHORT).show();
        }

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

        recyclerView.setAdapter(debitT_recyclerView);
        debitT_recyclerView.notifyDataSetChanged();



        /***
         * To make Filter options visible and invisible
         */
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterscreenvisible){
                    filter_layout.setVisibility(View.GONE);
                    filterscreenvisible = false;
                    overridePendingTransition(anim.slide_in_down, anim.stay_anim);
                } else {
                    filter_layout.setVisibility(View.VISIBLE);
                    filterscreenvisible = true;
                    overridePendingTransition(anim.slide_in_up, anim.stay_anim);
                }
            }
        });
        /*
         * Shows more filter options
         */
        morefilteroption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionHistory.this, FilterActivity.class);
                startActivity(i);
                overridePendingTransition(anim.slide_in, anim.slide_out);
            }
        });


        /*Recyclerview onscroll listener*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    VisibleItemCount = linearLayoutManager.getChildCount();
                    TotalItemCount = linearLayoutManager.getItemCount();
                    PastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if(!nextpage.equals("null")) {
                        if ((VisibleItemCount + PastVisibleItems) >= TotalItemCount) {
                            fetchtransaction(nextpage);
                       }
                    } else
                        Toast.makeText(TransactionHistory.this, "No more transactions in this list!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    Toast.makeText(TransactionHistory.this, "Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, TransactionHistory.this), this);
        /*new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String errorStr = new String(error.networkResponse.data);
                try{
                    JSONObject jObj = new JSONObject(errorStr);
                    // Getting error object
                    String objError = jObj.optString("detail");
                    loading = false;
                    Toast.makeText(TransactionHistory.this, objError,  Toast.LENGTH_SHORT).show();
                } catch (JSONException e){
                    Toast.makeText(TransactionHistory.this, "Some Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        })*/

        requestQueue.add(jsonObjectRequest);
    }

    /**Getting list of transactions from server and setting it to Recyclerview **/
    public void checkingstatus(JSONObject response) throws Exception {
        progressDialog.dismiss();

        if(!filtered_url.equals("")) {
            debitHistorieslist.clear();
        }
        //debitHistorieslist.clear();
        nextpage = response.optString("next");
        JSONArray array = response.optJSONArray("results");
        for(int i=0; i<array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            DebitDetails debitDetails = new DebitDetails(obj);
            debitHistorieslist.add(debitDetails);

        }
        debitT_recyclerView.notifyDataSetChanged();
        int count = debitT_recyclerView.getItemCount();

      // debitT_recyclerView.reloadData(debitHistorieslist);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(TransactionHistory.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i = new Intent(TransactionHistory.this, Dashboard.class);
        startActivity(i);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
        return true;
    }

    /** to delete transactions **/
    public void deletefromAPI(final String urlobj, final ProgressDialog pd) {
        HisabKitabDeleteRequest jsonObjectRequest = new  HisabKitabDeleteRequest(urlobj, null,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
              //  debitHistorieslist.clear();
            }
        }, new HisabKitabErrorListener(progressDialog, TransactionHistory.this), this);
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
                    Toast.makeText(TransactionHistory.this, "Error while updating data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, TransactionHistory.this), this);
        requestQueue.add(jsonObjectRequest);
    }

    /**To show updation success message and reload activity***/
    public void verifyupdatestatus(JSONObject response) throws  JSONException{
        progressDialog.dismiss();
        Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
        debitHistorieslist.clear();
        fetchtransaction(url);
    }

   /* public void onResume(){
        super.onResume();
        debitT_recyclerView.notifyDataSetChanged();
        android.widget.Toast.makeText(this, "onresume", Toast.LENGTH_SHORT).show();
    }*/
}
