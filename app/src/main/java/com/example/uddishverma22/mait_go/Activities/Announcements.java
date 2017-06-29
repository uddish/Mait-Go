package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uddishverma22.mait_go.Adapters.AnnouncementAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.Models.ClassAnnouncementsModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Announcements extends AppCompatActivity {

    public static final String TAG = "Announcements";

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/announcement/4I4";
    JSONObject object;
    ClassAnnouncementsModel announcement;
    public ArrayList<ClassAnnouncementsModel> list = new ArrayList<>();

    public RecyclerView recyclerView;
    public AnnouncementAdapter mAdapter;

    AVLoadingIndicatorView avi;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        fetchData(queue);

        setToolbar();

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

    }

    private void fetchData(RequestQueue queue) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        avi.hide();
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                object = response.getJSONObject(i);
                                announcement = new ClassAnnouncementsModel();
                                announcement.teacherName = object.getString("name");
                                announcement.announcement = object.getString("message");
                                announcement.msgDate = object.getString("createdAt");
                                list.add(announcement);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mAdapter = new AnnouncementAdapter(list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Announcements.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_announcement);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
