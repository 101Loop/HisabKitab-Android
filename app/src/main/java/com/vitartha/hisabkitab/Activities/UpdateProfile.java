package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.vitartha.hisabkitab.Activities.Dashboard.jwtContact;
import static com.vitartha.hisabkitab.Activities.Dashboard.jwtEmail;
import static com.vitartha.hisabkitab.Activities.Dashboard.jwtName;

public class UpdateProfile extends AppCompatActivity {

    EditText name, mob, mail, add;
    Button update;
    TextView pwd;
    ProgressDialog progressDialog;
    SharedPreference spAdap;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* For back button */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        update = findViewById(R.id.updatebtn);
        name = findViewById(R.id.txtname);
        mob = findViewById(R.id.txtnumber);
        mail = findViewById(R.id.txtmail);
        add = findViewById(R.id.txtaddress);
        pwd = findViewById(R.id.changepwd);
        spAdap = new SharedPreference(UpdateProfile.this);


        progressDialog = new ProgressDialog(this);

        setSupportActionBar(toolbar);

        name.setText(jwtName);
        mob.setText(jwtContact);
        mail.setText(jwtEmail);

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
                    Toast.makeText(UpdateProfile.this, "Some error occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateProfile.this, ChangePassword.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                    Toast.makeText(UpdateProfile.this, "Error while updating data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, UpdateProfile.this), this);
        /* new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String errorStr = new String(error.networkResponse.data);
                try{
                    JSONObject jObj = new JSONObject(errorStr);
                    // Getting error object
                    JSONObject objError = jObj.getJSONObject("data");
                    Toast.makeText(UpdateProfile.this, objError.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e){
                    Toast.makeText(UpdateProfile.this, "Server Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        })*/
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,0,1f));

        requestQueue.add(jsonObjectRequest);
    }

    public void verifystatus(JSONObject response) throws JSONException {
        progressDialog.dismiss();
        if(response.optInt(key.server.key_status) == 202) {
            Toast.makeText(this, "Your Profile has been updated successfully!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(UpdateProfile.this);
            alert.setMessage("Login Again!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(UpdateProfile.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.back_in, R.anim.back_out);
                    spAdap.clearData();
                    finish();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1295c9"));
        }
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
