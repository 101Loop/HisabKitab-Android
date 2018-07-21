package com.vitartha.hisabkitab.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class HisabKitabErrorListener implements Response.ErrorListener {
    private ProgressDialog progressDialog;
    private Activity act;

    /**
     * Customized ErrorListener to handle errors from Django REST API.
     * @param pd  A {@link ProgressDialog} that need to be closed down.
     * @param activity An {@link Activity} to be use to show pop-ups.
     */
    public HisabKitabErrorListener(ProgressDialog pd, Activity activity){
        this.progressDialog = pd;
        this.act = activity;
    }

    /**
     * Parses the error based on StatusCode
     * @param error A {@link VolleyError} object.
     */
    public void onErrorResponse(VolleyError error){
        this.progressDialog.dismiss();
        String errorResponse = new String(error.networkResponse.data);

        // Error Status Code
        int errorStatusCode = error.networkResponse.statusCode;
        Toast.makeText(this.act, errorStatusCode, Toast.LENGTH_SHORT).show();
        try {
            JSONObject jObj = new JSONObject(errorResponse);
            JSONObject objError = jObj.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
