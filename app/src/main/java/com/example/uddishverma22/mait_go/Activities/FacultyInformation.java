package com.example.uddishverma22.mait_go.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Adapters.FacultyListAdapter;
import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacultyInformation extends AppCompatActivity {

    public static final String TAG = "FacultyInformation";
    TextView cse, it, eee, ece, mae;

    RecyclerView mRecyclerView;
    FacultyListAdapter listAdapter;

    JSONArray itFaculty = null;
    JSONArray cseFaculty = null;

    JSONObject itFacObj, cseFacObj;
    Faculty itFacultyobj, cseFacultyobj;

    ArrayList<Faculty> itFacList = new ArrayList<>();
    ArrayList<Faculty> cseFacList = new ArrayList<>();

    String url = "https://agile-hamlet-82527.herokuapp.com/faculty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_information);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        mRecyclerView = (RecyclerView) findViewById(R.id.fac_recycler);


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            itFaculty = response.getJSONArray("IT");
                            cseFaculty = response.getJSONArray("CSE");
                            for(int i = 0; i < itFaculty.length(); i++) {
                                itFacObj = itFaculty.getJSONObject(i);
                                itFacultyobj = new Faculty();
                                itFacultyobj.name = itFacObj.getString("name");
                                itFacultyobj.designation = itFacObj.getString("designation");
                                itFacultyobj.qualification = itFacObj.getString("qualification");
                                itFacultyobj.experience = itFacObj.getString("exp");
                                itFacList.add(itFacultyobj);
                            }
                            for(int i = 0; i < cseFaculty.length(); i++) {
                                cseFacObj = cseFaculty.getJSONObject(i);
                                cseFacultyobj = new Faculty();
                                cseFacultyobj.name = cseFacObj.getString("name");
                                cseFacultyobj.designation = cseFacObj.getString("designation");
                                cseFacultyobj.qualification = cseFacObj.getString("qualification");
                                cseFacultyobj.experience = cseFacObj.getString("exp");
                                cseFacList.add(cseFacultyobj);
                            }
//                            Log.d(TAG, "onResponse: " + response.getJSONArray("IT"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listAdapter = new FacultyListAdapter(itFacList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacultyInformation.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setAdapter(listAdapter);

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
