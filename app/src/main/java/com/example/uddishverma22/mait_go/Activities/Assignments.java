package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uddishverma22.mait_go.Adapters.AssignmentAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.Models.AssignmentModel;
import com.example.uddishverma22.mait_go.R;
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

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/assignment/4I4";
    JSONObject object;
    AssignmentModel assignmentObj;

    AVLoadingIndicatorView indicatorView;

    public List<AssignmentModel> assignmentList = new ArrayList<>();
    public RecyclerView recyclerView;
    public AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        indicatorView.show();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AssignmentModel assignment = assignmentList.get(position);
                        Intent i = new Intent(getApplicationContext(), AssignmentImageViewer.class);
                        i.putExtra("imageUrl", assignment.imageUrl);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        indicatorView.hide();
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                object = response.getJSONObject(i);
                                assignmentObj = new AssignmentModel();
                                assignmentObj.teacher = object.getString("name");
                                assignmentObj.subject = object.getString("subject");
                                assignmentObj.imageUrl = object.getString("files");
                                assignmentObj.marks = object.getString("marks");
                                assignmentObj.lastdate = object.getString("last");
                                assignmentObj.imageUrl = object.getJSONArray("files").getString(0);
                                Log.d(TAG, "onResponse: Image Url " + assignmentObj.imageUrl);
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
}