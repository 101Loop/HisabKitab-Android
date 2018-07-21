package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
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
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebitHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DebitT_RecyclerView debitT_recyclerView;
    private ArrayList<DebitHistory> debitHistories = new ArrayList<>();
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    SharedPreference spAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debithistory);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Debit Transactions History");
        toolbar.setTitleTextColor(Color.WHITE);
        spAdap = new SharedPreference(DebitHistory.this);

        recyclerView = findViewById(R.id.recyclerView);

        debitT_recyclerView = new DebitT_RecyclerView(debitHistories);
        // recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(debitT_recyclerView);
        progressDialog = new ProgressDialog(this);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key.server.key_paginator, 0);
            fetchtransaction(jsonObject);
        } catch (JSONException e) {
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

    }

    /**Hitting to show transactions url**/
    public void fetchtransaction(JSONObject jsonObject) {
        progressDialog.setMessage("Fetching History...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.GET,
                key.transactions.show_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    checkingstatus(response);
                    Toast.makeText(DebitHistory.this, "get method", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DebitHistory.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        },new HisabKitabErrorListener(progressDialog, this), this);

        requestQueue.add(jsonObjectRequest);
    }

    public void checkingstatus(JSONObject object) throws Exception {
        progressDialog.dismiss();
        if(object.optInt(key.server.key_status) == 200) {

            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
    }
}
