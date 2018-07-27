package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfile extends AppCompatActivity {

    EditText name, mob, mail, add;
    Button update, back;
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        update = findViewById(R.id.updatebtn);
        name = findViewById(R.id.txtname);
        mob = findViewById(R.id.txtnumber);
        mail = findViewById(R.id.txtmail);
        add = findViewById(R.id.txtaddress);
        back = findViewById(R.id.backbutton);

        progressDialog = new ProgressDialog(this);

        setSupportActionBar(toolbar);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(key.user_api.key_fullname, name.getText().toString());
                    jsonObject.put(key.user_api.key_mob, mob.getText().toString());
                    jsonObject.put(key.user_api.key_mail, mail.getText().toString());
                    senddata(jsonObject);
                } catch (JSONException e) {

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateProfile.this, Dashboard.class);
                startActivity(i);
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
            }
        });

    }

    public void senddata(JSONObject object) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.PUT, key.user_api.update_profile_endpoint, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verifystatus(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateProfile.this, "Error while upda data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, this), this);
        requestQueue.add(jsonObjectRequest);
    }

    public void verifystatus(JSONObject response) throws JSONException {
        progressDialog.dismiss();
        if(response.optInt(key.server.key_status) == 202) {
            Toast.makeText(this, "Your Profile has been updated successfully!", Toast.LENGTH_SHORT).show();
            name.setText(response.optJSONObject("data").optString("name"));
            mob.setText(response.optJSONObject("data").optString("mobile"));
            mail.setText(response.optJSONObject("data").optString("email"));
        }
    }

}