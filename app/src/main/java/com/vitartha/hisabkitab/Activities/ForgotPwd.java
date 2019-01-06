package com.vitartha.hisabkitab.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.Class.SampleClass;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPwd extends SampleClass {
    LinearLayout ll_mobile, ll_otp;
    EditText otp, mail;
    Button verify, getOTP;
    ProgressDialog progressDialog;
    Boolean isOTP;
    SharedPreference spAdap;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();

    String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    String mobilePattern = "^[0][1-9]\\d{9}$|^[1-9]\\d{9}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //For back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        otp = findViewById(R.id.otp);
        mail = findViewById(R.id.emailID);
        verify = findViewById(R.id.verify);
        getOTP = findViewById(R.id.getOtp);
        ll_mobile = findViewById(R.id.ll_mobile);
        ll_otp = findViewById(R.id.ll_otp);
        spAdap = new SharedPreference(this);
        progressDialog = new ProgressDialog(this);

        /**For back button**/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /**To receive OTP user clicks on this button**/
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mail.getText().toString().matches(mobilePattern) || mail.getText().toString().matches(emailPattern)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(key.user_api.key_otp, mail.getText().toString());
                        isOTP = false;
                        getotp(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mail.setError("Enter valid value");
                }
            }
        });

        /**To verify OTP user click on Verify Button**/
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().length() > 0){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(key.user_api.key_otp, mail.getText().toString());
                        jsonObject.put(key.user_api.key_otpvalue, otp.getText().toString());
                        isOTP = true;
                        getotp(jsonObject);
                    } catch (JSONException e){
                        Toast.makeText(ForgotPwd.this, "Some error occured!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    otp.setError("This field is required");
                }
            }
        });
    }

    /**Sending mail/mobile i.e user input to otp API to receive OTP**/
    public void getotp(JSONObject object) {
        if(isOTP)
            progressDialog.setMessage("Verifying OTP...");
        else
            progressDialog.setMessage("Sending OTP...");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, key.user_api.otp_endpoint, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (isOTP) {
                    try {
                        verifyOTP(response);
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPwd.this, "Error!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    try {
                        checkstatusOTP(response);
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPwd.this, "Error!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof NetworkError) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPwd.this);
                    alert.setTitle("Connection Error!");
                    alert.setMessage("Please check your internet connection!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ForgotPwd.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1295c9"));
                } else {
                    String erroobj = new String(error.networkResponse.data);
                    try {
                        JSONObject jObj = new JSONObject(erroobj);
                        // Getting error object
                        JSONObject objError = jObj.getJSONObject("data");
                        if (isOTP) {
                            Toast.makeText(ForgotPwd.this, erroobj, Toast.LENGTH_SHORT).show();
                            otp.setError(objError.optString("OTP"));
                        } else {
                            mail.setError(objError.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,0,1f));

        requestQueue.add(jsonObjectRequest);
    }


    /**Checking status whether otp has been sent or not**/
    public void checkstatusOTP(JSONObject jsonObject) throws JSONException {
        progressDialog.dismiss();
        if(jsonObject.optInt(key.server.key_status) == 201){
            ll_otp.setVisibility(View.VISIBLE);
            ll_mobile.setVisibility(View.INVISIBLE);
            mail.setEnabled(false);
            Toast.makeText(this, "OTP has been sent successfully to your mail ID!", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "Server Error Occured!", Toast.LENGTH_SHORT).show();
        }
    }

    public void verifyOTP(JSONObject jsonObject) throws JSONException {
        progressDialog.dismiss();
        if(jsonObject.optInt(key.server.key_status)==200){
            Intent i = new Intent(ForgotPwd.this, Dashboard.class);
            startActivity(i);
            spAdap.saveData(key.server.key_token, jsonObject.getJSONObject(key.server.key_data).optString(key.server.key_token));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            Toast.makeText(this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else if(jsonObject.optInt(key.server.key_status)==401){
            otp.setError("Enter valid OTP");
            Toast.makeText(this, "OTP Validation Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Server Error Occured!", Toast.LENGTH_SHORT).show();
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


