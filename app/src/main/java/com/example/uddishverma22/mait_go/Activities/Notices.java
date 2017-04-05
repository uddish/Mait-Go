package com.example.uddishverma22.mait_go.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notices extends AppCompatActivity {

    String url = "http://192.168.0.13:8081/notices";
    JSONObject object;

    public static final String TAG = "Notices";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                // we got the response, now our job is to handle it
//                Log.d(TAG, "onResponse: " + response);
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //something happened, treat the error.
//                Log.d(TAG, "onErrorResponse: ERROR " + error.toString());
//            }
//        });
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


//        JsonObjectRequest request = new JsonObjectRequest(url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, "onResponse: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, "onErrorResponse: " + error.toString());
//            }
//        });
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
//

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
                        try {
                            for(int n = 0; n < response.length(); n++)
                            {
                                object = response.getJSONObject(n);
                                Log.d(TAG, "onResponse: JSON " + object.get("name"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        queue.add(jsonArrayReq);
//        jsonArrayReq.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
