package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Adapters.ResultAdapter;
import com.example.uddishverma22.mait_go.Models.ResultHeader;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    BottomSheetBehavior semesterBottomSheetBehavior;
    NestedScrollView semesterBottomSheet;
    TextView semTitle, semesterBtn;
    Typeface tf;
    TextView first, second, third, fourth, fifth, sixth, seventh, eight;
    String semester;

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

        semesterBtn = (TextView) findViewById(R.id.semester_btn);

        semesterBottomSheet = (NestedScrollView) findViewById(R.id.semester_bottomsheet);
        semesterBottomSheetBehavior = BottomSheetBehavior.from(semesterBottomSheet);
        semesterBottomSheetBehavior.setPeekHeight(0);

        semTitle = (TextView) findViewById(R.id.semester_title);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Light.ttf");
        semTitle.setTypeface(tf);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

        semesterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterSelector();
            }
        });

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
                            resultList= new ArrayList<>();
                            noResultLayout.setVisibility(View.GONE);

                            avi.hide();

                            JSONArray jsonArray = response.getJSONArray("marks");
                            percentage = String.valueOf((response.get("percentage")));

//                            Float f = Float.valueOf(decimalFormat.format(response.get("percentage")));
//                            Float dec = f - Float.parseFloat(percentage.substring(0,2));

                            //converting perc and gpa upto 2 decimal places
                            double creditp = (double) response.get("creditp");
                            double roundOffPerc = Math.round(creditp * 100.0) / 100.0;
                            double gpa = (double) response.get("gpa");
                            double roundOffGpa = Math.round(gpa * 100.0) / 100.0;

                            //Setting the percentage in the circleView and the cgpa and creditPercentage
                            mCircleView.setValueAnimated(Float.parseFloat(percentage));


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
                                resultObj.percentage = percentage;
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

    private void semesterSelector() {

        semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        semesterBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fetchSemData(semester);
                        Log.d(TAG, "onStateChanged: " + semester);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        first = (TextView) findViewById(R.id.semester_one);
        second = (TextView) findViewById(R.id.semester_two);
        third = (TextView) findViewById(R.id.semester_three);
        fourth = (TextView) findViewById(R.id.semester_four);
        fifth = (TextView) findViewById(R.id.semester_five);
        sixth = (TextView) findViewById(R.id.semester_six);
        seventh = (TextView) findViewById(R.id.semester_seven);
        eight = (TextView) findViewById(R.id.semester_eight);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = first.getText().toString();
                semesterBtn.setText(semester);
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = second.getText().toString();
                semesterBtn.setText(semester);
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = third.getText().toString();
                semesterBtn.setText(semester);
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterBtn.setText(fourth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterBtn.setText(fifth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterBtn.setText(sixth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterBtn.setText(seventh.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterBtn.setText(eight.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    private void fetchSemData(final String semester) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "?sem=" + semester, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "URL ********: " + url + "?sem=" + semester);
                        try {

                            noResultLayout.setVisibility(View.GONE);
                            avi.hide();
                            JSONArray jsonArray = response.getJSONArray("marks");
                            percentage = String.valueOf((response.get("percentage")));

                            //converting perc and gpa upto 2 decimal places
                            double creditp = (double) response.get("creditp");
                            double roundOffPerc = Math.round(creditp * 100.0) / 100.0;
                            double gpa = (double) response.get("gpa");
                            double roundOffGpa = Math.round(gpa * 100.0) / 100.0;

                            //Setting the percentage in the circleView and the cgpa and creditPercentage
                            mCircleView.setValueAnimated(Float.parseFloat(percentage));

                            resultHeader = new ResultHeader();
                            resultHeader.univRank = String.valueOf(response.get("urank"));
                            resultHeader.colRank = String.valueOf(response.get("crank"));
                            resultHeader.creditPerc = String.valueOf(roundOffPerc);
                            resultHeader.cgpa = String.valueOf(roundOffGpa);

                            resultList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);
                                ResultModel resultObj = new ResultModel();
                                resultObj.subName = jsonObject.getString("subjectName");
                                resultObj.intMarks = jsonObject.getString("internal");
                                resultObj.extMarks = jsonObject.getString("external");
                                resultObj.credits = jsonObject.getString("credits");
                                resultObj.totMarks = jsonObject.getString("total");
                                resultObj.percentage = percentage;
                                resultList.add(resultObj);
                            }

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
                Log.d(TAG, "URL ********: " + url + "?sem=" + semester);
                Log.d(TAG, "onErrorResponse: " + error.toString());
                avi.hide();
                noResultLayout.setVisibility(View.VISIBLE);

            }
        });
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }
}
