package com.vitartha.hisabkitab.Adapters;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vitartha.hisabkitab.API.key;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HisabKitabJSONRequest extends JsonObjectRequest {
    private Activity act;

    /**
     * Creates a new request.
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *   indicates no parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     * @param activity A {@link Activity} to handle create {@link SharedPreference} object for token.
     */
    public HisabKitabJSONRequest(int method, String url, JSONObject jsonRequest,
                                 Response.Listener<JSONObject> listener,
                                 HisabKitabErrorListener errorListener, Activity activity) {
        super(method, url, jsonRequest, listener, errorListener);
        this.act = activity;
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     */
    public HisabKitabJSONRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                                 HisabKitabErrorListener errorListener, Activity activity) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener, activity);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreference shaPre;
        shaPre = new SharedPreference(this.act);
        if(shaPre.isLoggedIn() != null){
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", shaPre.getString(key.server.key_token));
            return headers;
        }
        return super.getHeaders();
    }
}
