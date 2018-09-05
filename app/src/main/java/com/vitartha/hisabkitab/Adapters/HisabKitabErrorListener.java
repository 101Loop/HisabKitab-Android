package com.vitartha.hisabkitab.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Activities.LoginActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HisabKitabErrorListener implements Response.ErrorListener{
    private ProgressDialog progressDialog;
    private Context act;
    private SharedPreference spAdap;


    /**
     * Customized ErrorListener to handle errors from Django REST API.
     * @param pd  A {@link ProgressDialog} that need to be closed down.
     * @param context An {@link Activity} to be use to show pop-ups.
     */
    public HisabKitabErrorListener(ProgressDialog pd, Context context){
        this.progressDialog = pd;
        this.act = context;
        spAdap = new SharedPreference(act);
    }
    /**
     * Parses the error based on StatusCode
     * @param error A {@link VolleyError} object.
     */
    public void onErrorResponse(VolleyError error) {
        this.progressDialog.dismiss();

        if (error instanceof NetworkError) {
            Toast.makeText(act, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {

            String errorResponse = new String(error.networkResponse.data);
            int errorStatusCode = error.networkResponse.statusCode;
            //Toast.makeText(this.act, err, Toast.LENGTH_SHORT).show();

            // Error Status Code
            switch (errorStatusCode) {
                case 422: {
                    try {
                        JSONObject jObj = new JSONObject(errorResponse);
                        JSONObject objError = jObj.getJSONObject("data");
                        Toast.makeText(this.act, objError.optString("non_field_errors"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 403: {
                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                    alert.setTitle("Login Session Expired!");
                    alert.setMessage("Login Again!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(act, LoginActivity.class);
                            act.startActivity(i);
                            spAdap.clearData();
                        }
                    });

                    alert.show();
                    break;
                }
                case 500: {
                    Toast.makeText(act, "Server Error Occured! Please report this to info@vitartha.com", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 504: {
                    Toast.makeText(act, "Gateway Timeout! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    Toast.makeText(act, "Default Error", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
