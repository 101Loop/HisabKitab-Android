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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.DebitT_RecyclerView;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.Class.DebitDetails;
import com.vitartha.hisabkitab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebitHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DebitT_RecyclerView debitT_recyclerView;
    private ArrayList<DebitDetails> debitHistorieslist = new ArrayList<>();
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    SharedPreference spAdap;
    String url;
    String category_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debithistory_content);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Debit Transactions History");
        toolbar.setTitleTextColor(Color.WHITE);
        spAdap = new SharedPreference(DebitHistory.this);

        recyclerView = findViewById(R.id.recyclerView);

        debitT_recyclerView = new DebitT_RecyclerView(debitHistorieslist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(debitT_recyclerView);
        progressDialog = new ProgressDialog(this);

        /**For back button**/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category_value = extras.getString("category");
            //The key argument here must match that used in the other activity
        }

        url = key.transactions.show_url + "?&category=" + category_value;

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DebitHistory.this, AddDebit.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        try {
            fetchtransaction(url);
        } catch (Exception e) {
            Toast.makeText(this, "Some error occured while fetching data...", Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Toast.makeText(DebitHistory.this, "Touch event!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(debitT_recyclerView);

    }

    /** to show transactions url**/
    public void fetchtransaction(String jsonObject) {
        progressDialog.setMessage("Fetching History...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    checkingstatus(response);
                    Toast.makeText(DebitHistory.this, "get method", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DebitHistory.this, "Error!"+ e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },new HisabKitabErrorListener(progressDialog, this), this);

        requestQueue.add(jsonObjectRequest);
    }

    public void checkingstatus(JSONObject response) throws Exception {
        progressDialog.dismiss();
        JSONArray array = response.optJSONArray("results");


        for(int i=0; i<array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            DebitDetails debitDetails = new DebitDetails(obj);
            debitHistorieslist.add(debitDetails);
        }
        int count = debitT_recyclerView.getItemCount();
        Toast.makeText(this, "count:" + count, Toast.LENGTH_SHORT).show();
        debitT_recyclerView.notifyDataSetChanged();
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
        return true;
    }
}
