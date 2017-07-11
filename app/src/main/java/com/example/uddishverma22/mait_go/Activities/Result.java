package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.uddishverma22.mait_go.Adapters.AnnouncementAdapter;
import com.example.uddishverma22.mait_go.Adapters.ResultAdapter;
import com.example.uddishverma22.mait_go.Models.ClassAnnouncementsModel;
import com.example.uddishverma22.mait_go.Models.ResultHeader;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.example.uddishverma22.mait_go.Utils.RecyclerScroller;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import io.realm.Realm;
import io.realm.RealmResults;

public class Result extends AppCompatActivity {

    String rollNumber = null;
    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/result/";
    JSONObject jsonObject;
    public static String percentage = null;

    public static final String TAG = "Result";

    CircleProgressView mCircleView;

    CollapsingToolbarLayout collapsingToolbar;

    AVLoadingIndicatorView avi;

    public List<ResultModel> resultList = new ArrayList<>();
    RecyclerView recyclerView;
    ResultAdapter resultAdapter;

    Realm realm = null;
    RealmResults<ResultModel> results;
    RequestQueue requestQueue;

    LinearLayout noResultLayout;

    int screenHeight, screenWidth;
    AppBarLayout appBarLayout;
    Toolbar toolbar;

    //Result Header
    ResultHeader resultHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rollNumber = Preferences.getPrefs("rollNo", getApplicationContext());
        url = url + rollNumber;

        realm = Realm.getDefaultInstance();

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        fetchData(requestQueue);

        //Setting text on collapsible toolbar according to screen size
        initCollapsibleToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.result_list);

        mCircleView = (CircleProgressView) findViewById(R.id.circle_progress);

        noResultLayout = (LinearLayout) findViewById(R.id.no_result_layout);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

    }

    private void initCollapsibleToolbar() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
        toolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_result);
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(screenWidth, screenHeight / 3));
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Result");

    }

    private void fetchData(RequestQueue requestQueue) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            noResultLayout.setVisibility(View.GONE);

                            avi.hide();

                            JSONArray jsonArray = response.getJSONArray("marks");
                            percentage = String.valueOf(Double.parseDouble(String.valueOf((response.get("percentage")))));

                            //converting perc and gpa upto 2 decimal places
                            double creditp = (double) response.get("creditp");
                            double roundOffPerc = Math.round(creditp * 100.0) / 100.0;
                            double gpa = (double) response.get("gpa");
                            double roundOffGpa = Math.round(gpa * 100.0) / 100.0;

                            //Setting the percentage in the circleView and the cgpa and creditPercentage
                            mCircleView.setValueAnimated(Float.parseFloat((percentage)));

                            resultHeader = new ResultHeader();
                            resultHeader.univRank = String.valueOf(response.get("urank"));
                            resultHeader.colRank = String.valueOf(response.get("crank"));
                            resultHeader.creditPerc = String.valueOf(roundOffPerc);
                            resultHeader.cgpa = String.valueOf(roundOffGpa);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);
                                ResultModel resultObj = new ResultModel();
                                resultObj.subName = jsonObject.getString("subjectName");
                                resultObj.intMarks = jsonObject.getString("internal");
                                resultObj.extMarks = jsonObject.getString("external");
                                resultObj.credits = jsonObject.getString("credits");
                                resultObj.totMarks = jsonObject.getString("total");
                                resultObj.percentage = String.valueOf(response.get("percentage"));
                                resultList.add(resultObj);
                            }

                            /**
                             * Saving data to shared prefs
                             */
                            Gson gson = new Gson();
                            String resultJson = gson.toJson(resultList);
                            new saveDataAsync().execute(resultHeader.univRank, resultHeader.colRank, resultHeader.creditPerc, resultHeader.cgpa, String.valueOf(response.get("percentage")), resultJson);

                            resultAdapter = new ResultAdapter(resultList, resultHeader);
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
                avi.hide();

                resultHeader = new ResultHeader();
                resultHeader.univRank = Preferences.getPrefs("resultUrank", Result.this);
                resultHeader.colRank = Preferences.getPrefs("resultCrank", Result.this);
                resultHeader.creditPerc = Preferences.getPrefs("resultCreditPerc", Result.this);
                resultHeader.cgpa = Preferences.getPrefs("resultCgpa", Result.this);

                mCircleView.setValueAnimated(Float.parseFloat((Preferences.getPrefs("resultPercentage", Result.this))));

                Gson gson = new Gson();
                String resultStr = Preferences.getPrefs("result", Result.this);
                Log.d(TAG, "onErrorResponse: " + resultStr);
                Type type = new TypeToken<ArrayList<ResultModel>>() {
                }.getType();
                ArrayList<ResultModel> arrayList = gson.fromJson(resultStr, type);
                if (arrayList != null) {
                    try {
                        String listString = gson.toJson(arrayList, new TypeToken<ArrayList<ResultModel>>() {
                        }.getType());
                        JSONArray jsonArray = new JSONArray(listString);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ResultModel resultObj = new ResultModel();
                            resultObj.subName = jsonObject.getString("subName");
                            resultObj.intMarks = jsonObject.getString("intMarks");
                            resultObj.extMarks = jsonObject.getString("extMarks");
                            resultObj.credits = jsonObject.getString("credits");
                            resultObj.totMarks = jsonObject.getString("totMarks");
                            resultObj.percentage = Preferences.getPrefs("resultPercentage", Result.this);
                            resultList.add(resultObj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    noResultLayout.setVisibility(View.VISIBLE);
                }
                resultAdapter = new ResultAdapter(resultList, resultHeader);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Result.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(resultAdapter);

            }
        });
        requestQueue.add(request);
    }

    private class saveDataAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Preferences.setPrefs("resultUrank", params[0], Result.this);
            Preferences.setPrefs("resultCrank", params[1], Result.this);
            Preferences.setPrefs("resultCreditPerc", params[2], Result.this);
            Preferences.setPrefs("resultCgpa", params[3], Result.this);
            Preferences.setPrefs("resultPercentage", params[4], Result.this);
            Preferences.setPrefs("result", params[5], Result.this);

            return null;
        }
    }
}
