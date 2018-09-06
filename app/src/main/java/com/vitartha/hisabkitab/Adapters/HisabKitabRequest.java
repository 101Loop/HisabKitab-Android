package com.vitartha.hisabkitab.Adapters;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.vitartha.hisabkitab.API.key;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class HisabKitabRequest<T> extends JsonRequest<T> {
    private Context cont;

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public HisabKitabRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<T> listener,
                             HisabKitabErrorListener errorListener, Context context) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        cont = context;
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreference shaPre;
        shaPre = new SharedPreference(cont);
        if(shaPre.getString(key.server.key_token) != null){
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", shaPre.getString(key.server.key_token));
            return headers;
        }
        return super.getHeaders();
    }
}
