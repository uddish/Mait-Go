package com.example.uddishverma22.mait_go.Activities;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Adapters.FacultyDetailsAdapter;
import com.example.uddishverma22.mait_go.Adapters.FacultyViewPagerAdapter;
import com.example.uddishverma22.mait_go.MainActivity;
import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacultyActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    FacultyDetailsAdapter facultyDetailsAdapter;
    ArrayList<Faculty> facultyList = new ArrayList<>();

    ViewPager viewPager;
    FacultyViewPagerAdapter pagerAdapter;
    ArrayList<Integer> imageId;

    String branch = "IT";

    String url = "http://192.168.0.111:8081/faculty/it";

    JSONObject object;

    public static final String TAG = "Faculty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);


        setupView();

        setupImageId();
        pagerAdapter = new FacultyViewPagerAdapter(FacultyActivity.this, imageId);
        viewPager.setAdapter(pagerAdapter);

        apiCall();
    }

    private void setupImageId() {
        imageId = new ArrayList<>();
        imageId.add(R.drawable.cse);
        imageId.add(R.drawable.it);
        imageId.add(R.drawable.ece);
        imageId.add(R.drawable.eee);
        imageId.add(R.drawable.mae);
    }

    private void setupView() {

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //      facultyDetailsAdapter = new FacultyDetailsAdapter(MainActivity.this, facultyList);
        //    recyclerView.setAdapter(facultyDetailsAdapter);
    }

    private void apiCall() {

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();
     //   enableDisableTouch(false);

        pd.setCanceledOnTouchOutside(true);




        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject facultyName;
                        JSONArray facultyDetails;
                        Faculty obj;
                        try {
                            facultyName = response.getJSONObject(branch);
                            facultyDetails = response.getJSONArray("details");

                            for (int i = 0; i < facultyName.length(); i++) {
                                obj = new Faculty();
                                obj.setName(facultyName.getString(String.valueOf(i + 1)));

                                object = facultyDetails.getJSONObject(i);

                                obj.setDesignation(object.getString("Designation"));
                                obj.setExperience(object.getString("exp"));
                                obj.setQualification(object.getString("Qualification"));

                                Log.d(TAG, obj.getName() + " " + obj.getDesignation() + " " + obj.getExperience() + " " + obj.getQualification());
                                facultyList.add(obj);
                            }


                            facultyDetailsAdapter = new FacultyDetailsAdapter(FacultyActivity.this, facultyList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(facultyDetailsAdapter);


                            //  recyclerView.setAdapter(facultyDetailsAdapter);
                            pd.dismiss();

                            //   enableDisableTouch(true);



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
        queue.add(jsonObjectReq);

        pd.setCanceledOnTouchOutside(false);
    }
}

