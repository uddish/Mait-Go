package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uddishverma22.mait_go.Adapters.AnnouncementAdapter;
import com.example.uddishverma22.mait_go.Models.ClassAnnouncementsModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.CheckInternet;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.Preferences;
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
import java.util.Map;

public class Announcements extends AppCompatActivity {

    public static final String TAG = "Announcements";

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/announcement/";
    JSONObject object;
    ClassAnnouncementsModel announcement;
    public ArrayList<ClassAnnouncementsModel> list = new ArrayList<>();

    public RecyclerView recyclerView;
    public AnnouncementAdapter mAdapter;

    String requestClassEndpoint;

    AVLoadingIndicatorView avi;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    LinearLayout errorLayout;
    TextView fetchingText;
    RelativeLayout fetchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        requestClassEndpoint = Preferences.getPrefs("class and section", Announcements.this);
        if (!requestClassEndpoint.equals("notfound"))
            url = url + requestClassEndpoint;

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.announcement_main_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        fetchLayout = (RelativeLayout) findViewById(R.id.fetch_layout);

        fetchData(queue);

        setToolbar();

        if (!CheckInternet.isNetworkAvailable(Announcements.this)) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        startLoading();

    }

    private void fetchData(RequestQueue queue) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        stopLoading();
                        try {

                            if (response.length() == 0) {
                                errorLayout.setVisibility(View.VISIBLE);
                            } else
                                errorLayout.setVisibility(View.GONE);

                            for (int i = 0; i < response.length(); i++) {
                                object = response.getJSONObject(i);
                                announcement = new ClassAnnouncementsModel();
                                announcement.teacherName = object.getString("name");
                                announcement.announcement = object.getString("message");
                                announcement.msgDate = object.getString("date");
                                announcement.msgTime = object.getString("time");
                                list.add(announcement);
                            }

                            //Saving offline
                            Gson gson = new Gson();
                            String announcJson = gson.toJson(list);
                            Preferences.setPrefs("announcements", announcJson, Announcements.this);

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

                stopLoading();

                Gson gson = new Gson();
                String announcementStr = Preferences.getPrefs("announcements", Announcements.this);
                Type type = new TypeToken<ArrayList<ClassAnnouncementsModel>>() {
                }.getType();

                ArrayList<ClassAnnouncementsModel> arrayList;
                if (!announcementStr.equals("notfound")) {
                    arrayList = gson.fromJson(announcementStr, type);
//

                    if (arrayList.size() == 0) {
                        errorLayout.setVisibility(View.VISIBLE);
                    }
                    if (arrayList.size() != 0) {
                        Log.d(TAG, "onErrorResponse: " + arrayList.size());
                        try {
                            String listString = gson.toJson(arrayList, new TypeToken<ArrayList<ClassAnnouncementsModel>>() {
                            }.getType());
                            JSONArray jsonArray = new JSONArray(listString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                object = jsonArray.getJSONObject(i);
                                announcement = new ClassAnnouncementsModel();
                                announcement.teacherName = object.getString("teacherName");
                                announcement.announcement = object.getString("announcement");
                                announcement.msgDate = object.getString("msgDate");
                                list.add(announcement);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    arrayList = new ArrayList<>();
                    errorLayout.setVisibility(View.VISIBLE);
                }
                mAdapter = new AnnouncementAdapter(list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Announcements.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);

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
        queue.add(request);
    }

    private void startLoading() {
        avi.show();
    }

    private void stopLoading() {
        avi.hide();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_announcement);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

}
