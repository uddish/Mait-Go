package com.app.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.uddishverma22.mait_go.Adapters.UpcomingEventsAdapter;
import com.app.uddishverma22.mait_go.Models.UpcomingEventsModel;
import com.app.uddishverma22.mait_go.R;
import com.app.uddishverma22.mait_go.Utils.CheckInternet;
import com.app.uddishverma22.mait_go.Utils.DefaultExceptionHandler;
import com.app.uddishverma22.mait_go.Utils.Globals;
import com.app.uddishverma22.mait_go.Utils.RecyclerItemClickListener;
import com.app.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpcomingEvents extends AppCompatActivity {

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/upcoming";
    JSONObject object;
    UpcomingEventsModel eventsModel;

    AVLoadingIndicatorView indicatorView;

    private List<UpcomingEventsModel> eventsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UpcomingEventsAdapter eventsAdapter;
    Toolbar toolbar;
    LinearLayout errorLayout;
    CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        attachViews();

        indicatorView.show();

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        fetchData(queue);

        setToolbar();

        if (!CheckInternet.isNetworkAvailable(UpcomingEvents.this)) {
            Snackbar snackbar = Snackbar.make(mainLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
            indicatorView.hide();
            errorLayout.setVisibility(View.VISIBLE);
        }

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        UpcomingEventsModel eventsModel = eventsList.get(position);
                        Intent i = new Intent(getApplicationContext(), EventsDetailsPage.class);
                        i.putExtra("imageUrl", eventsModel.imageUrl);
                        i.putExtra("eventName", eventsModel.eventName);
                        i.putExtra("organiser", eventsModel.organiser);
                        i.putExtra("email", eventsModel.organiserEmail);
                        i.putExtra("number", eventsModel.organiserNumber);
                        i.putExtra("fee", eventsModel.regisFee);
                        i.putExtra("society", eventsModel.society);
                        i.putExtra("date", eventsModel.eventDate);
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

    }

    private void attachViews() {
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        recyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);
        errorLayout = (LinearLayout) findViewById(R.id.no_event_layout);
        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
    }

    private void fetchData(RequestQueue queue) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        indicatorView.hide();
                        errorLayout.setVisibility(View.GONE);
                        Toast.makeText(UpcomingEvents.this, "Click to expand", Toast.LENGTH_SHORT).show();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                object = response.getJSONObject(i);
                                eventsModel = new UpcomingEventsModel();
                                eventsModel.eventName = object.getString("eventName");
                                eventsModel.eventDate = object.getString("eventDate");
                                eventsModel.organiserEmail = object.getString("organiserEmail");
                                eventsModel.organiser = object.getString("organiser");
                                eventsModel.organiserNumber = object.getString("organiserNumber");
                                eventsModel.regisFee = object.getString("regisFee");
                                eventsModel.society = object.getString("society");
                                eventsModel.imageUrl = object.getString("imageUrl");
                                eventsList.add(eventsModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        eventsAdapter = new UpcomingEventsAdapter(eventsList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UpcomingEvents.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(eventsAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                indicatorView.hide();
                errorLayout.setVisibility(View.VISIBLE);
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

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
