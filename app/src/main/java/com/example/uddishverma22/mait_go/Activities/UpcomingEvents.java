package com.example.uddishverma22.mait_go.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uddishverma22.mait_go.Adapters.AssignmentAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.Adapters.UpcomingEventsAdapter;
import com.example.uddishverma22.mait_go.Models.AssignmentModel;
import com.example.uddishverma22.mait_go.Models.UpcomingEventsModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEvents extends AppCompatActivity {

    String url = "https://agile-hamlet-82527.herokuapp.com/upcoming";
    JSONObject object;
    UpcomingEventsModel eventsModel;

    private List<UpcomingEventsModel> eventsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UpcomingEventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        recyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);


        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
            }
        });
        queue.add(request);


    }
}
