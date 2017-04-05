package com.example.uddishverma22.mait_go.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.uddishverma22.mait_go.Adapters.DailyScheduleListAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.Models.DailySchedule;
import com.example.uddishverma22.mait_go.Models.Notice;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.RecyclerItemClickListener;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notices extends AppCompatActivity {

    String url = "http://192.168.0.13:8081/notices";
    JSONObject object;

    public static final String TAG = "Notices";

    public List<Notice> noticeList = new ArrayList<>();
    public RecyclerView recyclerView;
    public NoticeAdapter noticeAdapter;
    TextView noticeHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();
        recyclerView = (RecyclerView) findViewById(R.id.notice_recycler_view);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener()    {

                    @Override
                    public void onItemClick(View view, int position) {
                        Notice notice = noticeList.get(position);
                        Intent i = new Intent(getApplicationContext(), NoticeWebView.class);
                        i.putExtra("url", notice.url);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        noticeHeading = (TextView) findViewById(R.id.notice_tv);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Raleway-Regular.ttf");
        noticeHeading.setTypeface(tf);

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pd.dismiss();
                        try {
                            for(int n = 0; n < response.length(); n++)
                            {
                                object = response.getJSONObject(n);
                                Notice noticeObj = new Notice();
                                noticeObj.notice = object.getString("name");
                                noticeObj.url = object.getString("url");
                                Log.d(TAG, "onResponse: JSON " + object.get("name"));
                                noticeList.add(noticeObj);
                            }

                            noticeAdapter = new NoticeAdapter(noticeList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Notices.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(noticeAdapter);

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
        queue.add(jsonArrayReq);
//        jsonArrayReq.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
