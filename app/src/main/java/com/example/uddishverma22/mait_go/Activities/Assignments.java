package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.uddishverma22.mait_go.Adapters.AssignmentAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.Models.AssignmentModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.CheckInternet;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.example.uddishverma22.mait_go.Utils.RecyclerItemClickListener;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Assignments extends AppCompatActivity {

    public static final String TAG = "Assignment";

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/assignment/";
    JSONObject object;
    AssignmentModel assignmentObj;

    AVLoadingIndicatorView indicatorView;

    String requestClassEndpoint;

    public List<AssignmentModel> assignmentList = new ArrayList<>();
    public RecyclerView recyclerView;
    public AssignmentAdapter assignmentAdapter;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        indicatorView.show();
        setToolbar();

        if (!CheckInternet.isNetworkAvailable(Assignments.this)) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
            indicatorView.hide();
            errorLayout.setVisibility(View.VISIBLE);
        }

        requestClassEndpoint = Preferences.getPrefs("class and section", Assignments.this);
        if (!requestClassEndpoint.equals("notfound"))
            url = url + requestClassEndpoint;

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        fetchData(queue);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AssignmentModel assignment = assignmentList.get(position);
                        ArrayList<String> images = new ArrayList<String>();
                        Intent i = new Intent(getApplicationContext(), AssignmentImageViewer.class);
                        try {
                            for (int j = 0; j < assignment.images.length(); j++) {
                                images.add(assignment.images.getString(j));
                            }

                            i.putExtra("imageUrl", images);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

    }

    private void fetchData(RequestQueue queue) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        indicatorView.hide();
                        errorLayout.setVisibility(View.GONE);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                object = response.getJSONObject(i);
                                assignmentObj = new AssignmentModel();
                                assignmentObj.teacher = object.getString("name");
                                assignmentObj.subject = object.getString("subject");
                                assignmentObj.imageUrl = object.getString("files");
                                assignmentObj.marks = object.getString("marks");
                                assignmentObj.lastdate = object.getString("last");
                                //Adding array of images
                                assignmentObj.images = object.getJSONArray("files");
                                assignmentList.add(assignmentObj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        assignmentAdapter = new AssignmentAdapter(assignmentList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Assignments.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(assignmentAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        queue.add(request);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_assignments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);

    }
}
