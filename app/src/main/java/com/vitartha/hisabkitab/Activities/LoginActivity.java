package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;
import com.crashlytics.android.Crashlytics;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.HisabKitabErrorListener;
import com.vitartha.hisabkitab.Adapters.HisabKitabJSONRequest;
import com.vitartha.hisabkitab.Adapters.SharedPreference;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.Class.SampleClass;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class LoginActivity extends SampleClass{

    EditText email;
    TextView signup, forgotpwd;
    TextInputEditText pwd;
    Button loginbtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String user_Email, user_pwd;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    private ProgressDialog progressDialog;
    SharedPreference spAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spAdap = new SharedPreference(LoginActivity.this);
        signup = findViewById(R.id.signUPId);
        loginbtn = findViewById(R.id.loginId);
        email = findViewById(R.id.emailID);
        pwd = findViewById(R.id.password);
        forgotpwd = findViewById(R.id.forgot);
        progressDialog = new ProgressDialog(this);

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPwd.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_Email = email.getText().toString();
                user_pwd = pwd.getText().toString();
                if(user_Email.length()>0 && user_pwd.length()>0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(key.user_api.key_username, email.getText().toString());
                        jsonObject.put(key.user_api.key_pwd, pwd.getText().toString());
                        send_logindata(jsonObject);

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "error!!!!", Toast.LENGTH_SHORT).show();
                    }
                } else if(user_Email.length() == 0) {
                    email.setError("This field is required");
                } else if(user_pwd.length() == 0) {
                    pwd.setError("Password can not be null");
                }
            }
        });
    }

    /***
     * Sending login data i.e. username and password through POST Method
     * @param jsonObject
     */
    public void send_logindata(JSONObject jsonObject) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Clear data before log-in (just in case)
        spAdap.clearData();

        HisabKitabJSONRequest request = new HisabKitabJSONRequest(Request.Method.POST,
                key.user_api.login_endpoint, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verify_login(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error while sending data!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new HisabKitabErrorListener(progressDialog, LoginActivity.this), this);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,0,1f));

        requestQueue.add(request);
    }

    /***
     * Parsing Response, if status is 200 then user is redirected to Next Page
     * @param response
     * @throws Exception
     */
    public void verify_login(JSONObject response) throws Exception {
        progressDialog.dismiss();
        if(response.optInt(key.server.key_status) == 200) {
            spAdap.saveData(key.server.key_token, response.getJSONObject(key.server.key_data).optString(key.server.key_token));
            Toast.makeText(this, "LoggedIn successfully!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, Dashboard.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();

        } else {
            Toast.makeText(this, "Server Error Occured!", Toast.LENGTH_SHORT).show();
        }
    }

}
