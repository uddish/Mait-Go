package com.example.uddishverma22.mait_go.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Notices extends AppCompatActivity {

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/scrape/notices";
    JSONObject object;
    Notice noticeObj;

    private static int IS_INTERNET_AVAILABLE = 2000;

    Realm realm = null;

    public static final String TAG = "Notices";

    public List<Notice> noticeList = new ArrayList<>();
    public RecyclerView recyclerView;
    public NoticeAdapter noticeAdapter;
    TextView noticeHeading;
    View view;
    ImageView iconNotice;
    RealmResults<Notice> results;
    Toolbar toolbar;
    AVLoadingIndicatorView indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        realm = Realm.getDefaultInstance();

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        indicatorView.show();
        recyclerView = (RecyclerView) findViewById(R.id.notice_recycler_view);

        fetchData(queue);

        view = findViewById(R.id.view);
        iconNotice = (ImageView) findViewById(R.id.img_notice_icon);

        setToolbar();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (IS_INTERNET_AVAILABLE == 2009) {
                            Notice notice = noticeList.get(position);
                            Intent i = new Intent(getApplicationContext(), NoticeWebView.class);
                            i.putExtra("url", notice.url);
                            startActivity(i);
                        } else {
                            Toast.makeText(Notices.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        noticeHeading = (TextView) findViewById(R.id.notice_tv);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Regular.ttf");
        noticeHeading.setTypeface(tf);

    }

    private void fetchData(RequestQueue queue) {

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {

                        IS_INTERNET_AVAILABLE = 2009;

                        indicatorView.hide();
                        try {
                            for (int n = 0; n < response.length(); n++) {
                                object = response.getJSONObject(n);
                                noticeObj = new Notice();
                                noticeObj.notice = object.getString("notice");
                                noticeObj.url = object.getString("url");
                                Log.d(TAG, "onResponse: DJSON " + object.get("notice"));
                                noticeList.add(noticeObj);
                            }

                            /**
                             * ************************** Saving data to Realm **************************
                             */
                            if (noticeList.size() != 0) {

                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        //Deleting the previous result to remove repeatity
                                        realm.where(Notice.class).findAll().deleteAllFromRealm();
                                        //Adding the results in the realm database
                                        realm.copyToRealmOrUpdate(noticeList);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        Log.e(TAG, "onError: " + error.toString());
                                    }
                                });
                            }

                            /**
                             * ***************************************************************************
                             */

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

                IS_INTERNET_AVAILABLE = 2000;

                Log.d(TAG, "onErrorResponse: " + error.toString());
                /**If internet connectivity is not available
                 * Showing data from the realm database
                 */
                indicatorView.hide();
                results = realm.where(Notice.class).findAll();
                Log.d(TAG, "onErrorResponse: RESULT REALM SIZE " + results.size());
                noticeAdapter = new NoticeAdapter(results);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Notices.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(noticeAdapter);

            }
        });
        queue.add(jsonArrayReq);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
//        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
//        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
