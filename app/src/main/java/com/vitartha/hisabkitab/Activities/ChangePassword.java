package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.JsonObject;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    Button back, updatepwd;
    EditText newpwd, confpwd;
    ProgressDialog progressDialog;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        back = findViewById(R.id.backbutton);
        updatepwd = findViewById(R.id.updatebtn);
        newpwd = findViewById(R.id.newpwdtxt);
        confpwd = findViewById(R.id.confirmpwdtxt);
        progressDialog = new ProgressDialog(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePassword.this, UpdateProfile.class);
                startActivity(i);
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
            }
        });

        updatepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpwd.length() > 0 && confpwd.length() > 0) {
                    if (newpwd.getText().toString().equals(confpwd.getText().toString())) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(key.user_api.key_newpwd, confpwd.getText().toString());
                            senddata(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePassword.this, "Some error occured!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        confpwd.setError("Password does not matched!");
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void senddata(JSONObject object) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        HisabKitabJSONRequest jsonObjectRequest = new HisabKitabJSONRequest(Request.Method.PUT, key.user_api.change_pwd_endpoint, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verifystatus(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePassword.this, "Error while updating password!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, this), this);
        requestQueue.add(jsonObjectRequest);
    }

    public  void verifystatus(JSONObject response) throws JSONException{
        progressDialog.dismiss();
        if(response.optInt(key.server.key_status) == 202) {
            Toast.makeText(this, "Your password has been updated successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}