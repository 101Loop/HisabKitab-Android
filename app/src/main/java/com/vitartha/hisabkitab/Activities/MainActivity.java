package com.vitartha.hisabkitab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Adapters.VolleySingleton;
import com.vitartha.hisabkitab.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText Uname, email, mobile, pwd, c_pwd;
    TextView  loginhere;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pass, confirm_pass, user_Name;
    Button registerbtn;
    Boolean isemail = false, isname = false, ismobile = false, ispass = false, isconfpass = false;
    private VolleySingleton volleySingleton = VolleySingleton.getsInstance();
    private RequestQueue requestQueue = volleySingleton.getRequestQueue();
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Signup");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Uname = findViewById(R.id.nameID);
        email = findViewById(R.id.emailID);
        mobile = findViewById(R.id.mobileID);
        pwd = findViewById(R.id.password);
        c_pwd = findViewById(R.id.confirm_pass);
        registerbtn = findViewById(R.id.registerbtn);
        loginhere = findViewById(R.id.loginHere);
        progressDialog = new ProgressDialog(this);

        /**For back button**/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user_Name = Uname.getText().toString();
                if(user_Name.length() <= 0) {
                    Uname.setError("This field can't be empty");
                    isname = false;
                } else
                    isname = true;
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String Email = email.getText().toString();
                if(!Email.matches(emailPattern)){
                    email.setError("Enter valid Email-ID");
                    isemail = false;
                } else
                    isemail = true;
            }
        });

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String contact = mobile.getText().toString();
                if(contact.length() < 9) {
                    mobile.setError("Enter valid Mobile Number");
                    ismobile = false;
                } else
                    ismobile = true;
            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pass = pwd.getText().toString();
                if(pass.length() < 3) {
                    pwd.setError("Password must be 3 characters long!");
                    ispass = false;
                } else
                    ispass = true;
            }
        });

        c_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirm_pass = c_pwd.getText().toString();
                if(!confirm_pass.equals(pass)) {
                    c_pwd.setError("Password & confirm password does not match!");
                    isconfpass = false;
                } else
                    isconfpass = true;
            }
        });


        /**If already a member, redirect to login page**/
        loginhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
            }
        });

        /**On click on registration button**/
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isemail && isname && ismobile && ispass && isconfpass) {

                    JSONObject jsonObject = new JSONObject();
                    try{
                        jsonObject.put(key.user_api.key_username, mobile.getText().toString());
                        jsonObject.put(key.user_api.key_fullname, Uname.getText().toString());
                        jsonObject.put(key.user_api.key_mail, email.getText().toString());
                        jsonObject.put(key.user_api.key_mob, mobile.getText().toString());
                        jsonObject.put(key.user_api.key_pwd, pwd.getText().toString());

                        send_registrationdetails(jsonObject);
                    } catch(JSONException e) {
                        Toast.makeText(MainActivity.this, "Error while doing Registration!", Toast.LENGTH_SHORT).show();
                    }

               } else  if(!isname)
                    Uname.setError("This field can't be empty");
                else if (!isemail)
                    email.setError("Enter valid Email-ID");
                else if (!ismobile)
                    mobile.setError("Enter valid mobile number");
                else if(!ispass)
                    pwd.setError("Password must be 3 characters long!");
                else if(!isconfpass)
                    c_pwd.setError("Password & confirm password does not match");
            }
        });
    }

    /**Sending user details using POST Method gor signup process**/
    public void send_registrationdetails(JSONObject jsonObject) {
        progressDialog.setMessage("Processing.... Please Wait!");
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, key.user_api.registration_endpoint, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    verify_register(response);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error while sending data!"+ e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String errorStr = new String(error.networkResponse.data);
               // Toast.makeText(MainActivity.this, errorStr, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jObj = new JSONObject(errorStr);
                    // Getting error object
                    JSONObject objError = jObj.getJSONObject("data");

                    if (!objError.isNull("email")) {
                        email.setError("User with this Email-ID already exists");
                    }
                    if (!objError.isNull("mobile")) {
                        mobile.setError("User with this mobile number already exists");
                    }
                    if (!objError.isNull("username")) {
                        mobile.setError("User with this mobile number already exists");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestQueue.add(request);
    }


    /**Checking status received from server**/
    public void verify_register(JSONObject object) throws Exception {
     if(object.optInt(key.server.key_status) == 201) {
         progressDialog.dismiss();
         Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
         Intent i = new Intent(MainActivity.this, LoginActivity.class);
         startActivity(i);
         overridePendingTransition(R.anim.back_in, R.anim.back_out);
         finish();
     } else {
         email.setError("Server Error occured!");
     }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
        finish();
        return true;
    }
}