package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Point;
import android.graphics.Typeface;
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
import com.example.uddishverma22.mait_go.Adapters.ResultAdapter;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.example.uddishverma22.mait_go.Utils.RecyclerScroller;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        rollNumber = Preferences.getPrefs("rollNo", getApplicationContext());
        url = url + rollNumber;

        Log.d(TAG, "onCreate: Roll No " + rollNumber);

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
                            mCircleView.setValueAnimated(Float.parseFloat(percentage));


                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);
                                ResultModel resultObj = new ResultModel();
                                resultObj.subName = jsonObject.getString("subjectName");
                                resultObj.intMarks = jsonObject.getString("internal");
                                resultObj.extMarks = jsonObject.getString("external");
                                resultObj.credits = jsonObject.getString("credits");
                                resultObj.totMarks = jsonObject.getString("total");
                                resultObj.percentage = String.valueOf(response.get("percentage"));
                                resultObj.univRank = String.valueOf(response.get("urank"));
                                resultObj.colRank = String.valueOf(response.get("crank"));
                                resultObj.creditPerc = String.valueOf(roundOffPerc);
                                resultObj.cgpa = String.valueOf(roundOffGpa);

                                resultList.add(resultObj);
                            }

                            /**
                             * Saving data to realm
                             */
//                            if (resultList.size() != 0) {
//                                realm.executeTransactionAsync(new Realm.Transaction() {
//                                    @Override
//                                    public void execute(Realm realm) {
//                                        //Deleting the previous result to remove repeatity
//                                        realm.where(ResultModel.class).findAll().deleteAllFromRealm();
//                                        //Adding the results in the realm database
//                                        realm.copyToRealmOrUpdate(resultList);
//                                    }
//                                }, new Realm.Transaction.OnSuccess() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//                                }, new Realm.Transaction.OnError() {
//                                    @Override
//                                    public void onError(Throwable error) {
//                                        Log.e(TAG, "onError: " + error.toString());
//                                    }
//                                });
//
//                            }

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
                avi.hide();
                noResultLayout.setVisibility(View.VISIBLE);
//                results = realm.where(ResultModel.class).findAll();
//                setting percentage in the CircleView
//                if (results.size() != 0) {
//                    mCircleView.setValueAnimated(Float.parseFloat(results.get(1).percentage));
//                    resultAdapter = new ResultAdapter(results);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Result.this);
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    recyclerView.setAdapter(resultAdapter);
//                }

            }
        });
        requestQueue.add(request);
    }
}
