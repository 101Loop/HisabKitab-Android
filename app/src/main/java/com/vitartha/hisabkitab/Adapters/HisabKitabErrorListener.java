package com.vitartha.hisabkitab.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.vitartha.hisabkitab.API.key;
import com.vitartha.hisabkitab.Activities.Dashboard;
import com.vitartha.hisabkitab.Activities.LoginActivity;
import com.vitartha.hisabkitab.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
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
            AlertDialog.Builder alert = new AlertDialog.Builder(act);
            alert.setTitle("Connection Error!");
            alert.setMessage("Please check your internet connection!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(act, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            alert.show();
        } else if( error instanceof AuthFailureError) {
            AlertDialog.Builder alert = new AlertDialog.Builder(act);
            alert.setTitle("Authentication Error");
            alert.setMessage("Authentication Failed!");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(act, "Authorization Error!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(act, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    act.startActivity(i);
                    spAdap.clearData();
                    dialog.dismiss();
                }
            });

            alert.show();
        }
        else {
            String errorResponse = new String(error.networkResponse.data);

            int errorStatusCode = error.networkResponse.statusCode;

            // Error Status Code
            switch (errorStatusCode) {
                case 401: {
                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                    alert.setTitle("Authentication Error");
                    alert.setMessage("Authentication Failed!");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(act, "Authorization Error!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(act, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            act.startActivity(i);
                            spAdap.clearData();
                            dialog.dismiss();
                        }
                    });
                    alert.show();
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
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            act.startActivity(i);
                            spAdap.clearData();
                        }
                    });

                    alert.show();
                    break;
                }
                case 404: {
                    Toast.makeText(act, "Page not found!", Toast.LENGTH_SHORT).show();
                }
                case 422: {
                    try {
                        JSONObject jObj = new JSONObject(errorResponse);
                        Iterator keys = jObj.keys();
                        JSONObject objError;
                        while (keys.hasNext()) {
                            String curr = (String) keys.next();
                            try {
                                if ( jObj.get(curr) instanceof JSONObject ) {
                                    JSONObject obj = new JSONObject(jObj.get(curr).toString());
                                    String errorkey = obj.keys().next();
                                    errorkey = obj.getJSONArray(errorkey).getString(0);

                                    AlertDialog.Builder alert = new AlertDialog.Builder(act);
                                    alert.setTitle("Error!");
                                    alert.setMessage(errorkey);
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressDialog.dismiss();
                                        }
                                    });

                                    alert.show();

                                    Toast.makeText(act, errorkey, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(act, "Some error has occurred! Try again!", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(act, "Some error occured while fetching key pair data!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    Toast.makeText(act, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
