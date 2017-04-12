package com.example.uddishverma22.mait_go.Activities;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uddishverma22.mait_go.Adapters.ResultAdapter;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity {

    String url = "https://agile-hamlet-82527.herokuapp.com/scrape/result/40514803115";
    JSONObject jsonObject;

    public static final String TAG = "Result";

    public List<ResultModel> resultList = new ArrayList<>();
    RecyclerView recyclerView;
    ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = (RecyclerView) findViewById(R.id.result_list);

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                jsonObject = response.getJSONObject(i);
                                ResultModel resultObj = new ResultModel();
                                resultObj.subName = jsonObject.getString("subjectName");
                                resultObj.intMarks = jsonObject.getString("internal");
                                resultObj.extMarks = jsonObject.getString("external");
                                resultObj.credits = jsonObject.getString("credits");
                                resultObj.totMarks = jsonObject.getString("total");
                                resultList.add(resultObj);
                            }
                            Log.d(TAG, "onResponse: " + resultList);

                            resultAdapter = new ResultAdapter(resultList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Result.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(resultAdapter);

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
        requestQueue.add(jsonArrayReq);

    }
}
