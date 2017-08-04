package com.app.uddishverma22.mait_go.Activities;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.uddishverma22.mait_go.Adapters.ResultAdapter;
import com.app.uddishverma22.mait_go.Models.ResultHeader;
import com.app.uddishverma22.mait_go.Models.ResultModel;
import com.app.uddishverma22.mait_go.R;
import com.app.uddishverma22.mait_go.Utils.CheckInternet;
import com.app.uddishverma22.mait_go.Utils.DefaultExceptionHandler;
import com.app.uddishverma22.mait_go.Utils.Globals;
import com.app.uddishverma22.mait_go.Utils.Preferences;
import com.app.uddishverma22.mait_go.Utils.VolleySingleton;
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
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;

public class Result extends AppCompatActivity {

    String rollNumber = null;
    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/result/";
    JSONObject jsonObject;
    public static String percentage = null;
    public String resultSemester = null;

    public static final String TAG = "Result";

    CircleProgressView mCircleView;

    CollapsingToolbarLayout collapsingToolbar;

    AVLoadingIndicatorView avi;

    public List<ResultModel> resultList = new ArrayList<>();
    RecyclerView recyclerView;
    ResultAdapter resultAdapter;

    RequestQueue requestQueue;

    LinearLayout noResultLayout;

    int screenHeight, screenWidth;
    AppBarLayout appBarLayout;
    Toolbar toolbar;

    //Result Header
    ResultHeader resultHeader;

    CoordinatorLayout mainLayout;

    BottomSheetBehavior semesterBottomSheetBehavior;
    NestedScrollView semesterBottomSheet;
    TextView semTitle, semesterBtn;
    Typeface tf;
    TextView first, second, third, fourth, fifth, sixth, seventh, eight;
    String semester = null;

    TextView migrationTitle;
    EditText migratedRollNo;
    ImageView migratedSelectDone;
    LinearLayout migratedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rollNumber = Preferences.getPrefs("rollNo", getApplicationContext());
        url = url + rollNumber;

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        fetchData(requestQueue);

        //Setting text on collapsible toolbar according to screen size
        initCollapsibleToolbar();

        attachViews();

        if (!CheckInternet.isNetworkAvailable(Result.this)) {
            Snackbar snackbar = Snackbar.make(mainLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

        //To handle CRASH
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        semesterBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                semesterSelector();
                return false;
            }
        });


        migratedSelectDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                migratedRollEnter();
            }
        });

    }

    private void attachViews() {

        recyclerView = (RecyclerView) findViewById(R.id.result_list);
        mCircleView = (CircleProgressView) findViewById(R.id.circle_progress);
        noResultLayout = (LinearLayout) findViewById(R.id.no_result_layout);
        semesterBtn = (TextView) findViewById(R.id.semester_btn);
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        semesterBottomSheet = (NestedScrollView) findViewById(R.id.semester_bottomsheet);
        semesterBottomSheetBehavior = BottomSheetBehavior.from(semesterBottomSheet);
        semesterBottomSheetBehavior.setPeekHeight(0);

        migrationTitle = (TextView) findViewById(R.id.migration_title);
        migratedRollNo = (EditText) findViewById(R.id.migrated_rollno);
        migratedSelectDone = (ImageView) findViewById(R.id.migrated_input_done);

        migratedLayout = (LinearLayout) findViewById(R.id.migrated_layout);

        semTitle = (TextView) findViewById(R.id.semester_title);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Light.ttf");
        semTitle.setTypeface(tf);
        migrationTitle.setTypeface(tf);
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

        resultList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            noResultLayout.setVisibility(View.GONE);

                            avi.hide();

                            JSONArray jsonArray = response.getJSONArray("marks");
                            percentage = String.valueOf((response.get("percentage")));
                            semester = String.valueOf(response.get("sem"));

                            semesterBtn.setText(semester);

                            double creditp = Double.parseDouble(String.valueOf(response.get("creditp")));
                            double roundOffPerc = Math.round(creditp * 100.0) / 100.0;
                            double gpa = Double.parseDouble(String.valueOf(response.get("gpa")));
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
                            new saveDataAsync().execute(resultHeader.univRank, resultHeader.colRank, resultHeader.creditPerc, resultHeader.cgpa, String.valueOf(response.get("percentage")), resultJson, semester);

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

                if (!(Preferences.getPrefs("resultPercentage", Result.this).equals("notfound")))
                    mCircleView.setValueAnimated(Float.parseFloat((Preferences.getPrefs("resultPercentage", Result.this))));

                Gson gson = new Gson();
                String resultStr = Preferences.getPrefs("result", Result.this);
                Log.d(TAG, "onErrorResponse: " + resultStr);
                Type type = new TypeToken<ArrayList<ResultModel>>() {
                }.getType();
                if (!resultStr.equals("notfound")) {
                    ArrayList<ResultModel> arrayList = gson.fromJson(resultStr, type);
                    if (arrayList != null) {
                        try {
                            String listString = gson.toJson(arrayList, new TypeToken<ArrayList<ResultModel>>() {
                            }.getType());
                            JSONArray jsonArray = new JSONArray(listString);

                            semesterBtn.setText(Preferences.getPrefs("resultSem", Result.this));

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
                    }
                } else {

                    if (semester != null) {
                        if (semester.equals("1") || semester.equals("2")) {
                            migrationTitle.setVisibility(View.VISIBLE);
                        }
                    } else {
                        noResultLayout.setVisibility(View.VISIBLE);
                        migrationTitle.setVisibility(View.GONE);
                    }

                }
                resultAdapter = new ResultAdapter(resultList, resultHeader);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Result.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(resultAdapter);

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("x-access-token", Globals.X_ACCESS_TOKEN);
                return headers;
            }

        };
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
            Preferences.setPrefs("resultSem", params[6], Result.this);

            return null;
        }
    }

    private void migratedRollEnter() {

        String migratedRoll = migratedRollNo.getText().toString();
        if (!TextUtils.isEmpty(migratedRoll)) {
            avi.show();
            url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/result/" + migratedRoll;
            fetchSemData(semester);
        } else {
            Toast.makeText(this, "Enter the Roll Number", Toast.LENGTH_SHORT).show();
        }
        InputMethodManager imm = (InputMethodManager) MainActivity.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(migratedSelectDone.getWindowToken(), 0);

    }

    private void semesterSelector() {

        semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        semesterBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        avi.show();
                        url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/result/" + rollNumber;
                        fetchSemData(semester);
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
                semester = fourth.getText().toString();
                semesterBtn.setText(fourth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = fifth.getText().toString();
                semesterBtn.setText(fifth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = sixth.getText().toString();
                semesterBtn.setText(sixth.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = seventh.getText().toString();
                semesterBtn.setText(seventh.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester = eight.getText().toString();
                semesterBtn.setText(eight.getText().toString());
                semesterBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    private void fetchSemData(final String semester) {

        resultList = new ArrayList<>();
        resultHeader = new ResultHeader();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "?sem=" + semester, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            noResultLayout.setVisibility(View.GONE);
                            migratedLayout.setVisibility(View.GONE);
                            appBarLayout.setExpanded(true);

                            avi.hide();
                            JSONArray jsonArray = response.getJSONArray("marks");
                            percentage = String.valueOf((response.get("percentage")));

                            //converting perc and gpa upto 2 decimal places
                            double creditp = 0;
                            double roundOffPerc = 0;
                            double roundOffGpa = 0;
                            double gpa = 0;
//                                if (response.get("percentage") instanceof Integer) {
//                                    resultHeader.creditPerc = String.valueOf(response.get("creditp"));
//                                } else {
                            creditp = Double.parseDouble(String.valueOf(response.get("creditp")));
                            roundOffPerc = Math.round(creditp * 100.0) / 100.0;
//                                }

                            gpa = Double.parseDouble(String.valueOf(response.get("gpa")));
                            roundOffGpa = Math.round(gpa * 100.0) / 100.0;

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
                resultList = new ArrayList<>();
                resultHeader = new ResultHeader();
                resultAdapter = new ResultAdapter(resultList, resultHeader);
                recyclerView.setAdapter(resultAdapter);
                mCircleView.setValue(0);
                if (semester.equals("1") || semester.equals("2")) {
                    appBarLayout.setExpanded(false);
                    migratedLayout.setVisibility(View.VISIBLE);
                    noResultLayout.setVisibility(View.GONE);
                } else {
                    noResultLayout.setVisibility(View.VISIBLE);
                    migratedLayout.setVisibility(View.GONE);
                }

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("x-access-token", Globals.X_ACCESS_TOKEN);
                return headers;
            }

        };
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }
}
