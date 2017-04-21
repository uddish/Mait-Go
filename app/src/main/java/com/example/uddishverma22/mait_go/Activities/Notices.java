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

//    String url = "http://192.168.0.13:8081/notices";
    String url = "https://agile-hamlet-82527.herokuapp.com/scrape/notices";
    JSONObject object;
    Notice noticeObj;

    private static int IS_INTERNET_AVAILABLE = 2000;

    Realm realm;

    public static final String TAG = "Notices";

    public List<Notice> noticeList = new ArrayList<>();
    public RecyclerView recyclerView;
    public NoticeAdapter noticeAdapter;
    TextView noticeHeading;
    RelativeLayout relativeLayout;
    View view;
    ImageView iconNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        realm = Realm.getDefaultInstance();

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final AVLoadingIndicatorView indicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);
        indicatorView.show();
        recyclerView = (RecyclerView) findViewById(R.id.notice_recycler_view);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        view = findViewById(R.id.view);
        iconNotice = (ImageView) findViewById(R.id.img_notice_icon);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener()    {

                    @Override
                    public void onItemClick(View view, int position) {
                        if(IS_INTERNET_AVAILABLE == 2009) {
                            Notice notice = noticeList.get(position);
                            Intent i = new Intent(getApplicationContext(), NoticeWebView.class);
                            i.putExtra("url", notice.url);
                            startActivity(i);
                        }
                        else    {
                            Toast.makeText(Notices.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                        }
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
                    public void onResponse(final JSONArray response) {

                        IS_INTERNET_AVAILABLE = 2009;

//                        pd.dismiss();
                        indicatorView.hide();
                        try {
                            for(int n = 0; n < response.length(); n++)
                            {
                                object = response.getJSONObject(n);
                                noticeObj = new Notice();
                                noticeObj.notice = object.getString("notice");
                                noticeObj.url = object.getString("url");
                                Log.d(TAG, "onResponse: JSON " + object.get("notice"));
                                noticeList.add(noticeObj);
                            }

                            /**
                             * ************************** Saving data to Realm **************************
                             */
                            if(noticeList.size() != 0) {

                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(noticeList);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(Notices.this, "Info Stored in Realm", Toast.LENGTH_SHORT).show();
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
                RealmResults<Notice> results = realm.where(Notice.class).findAll();
                noticeAdapter = new NoticeAdapter(results);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Notices.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(noticeAdapter);
//                for(int i = 0; i < results.size(); i++) {
//                    Log.d(TAG, "NOTICE -> " + results.get(i).getNotice());
//                }

            }
        });
        queue.add(jsonArrayReq);
//        jsonArrayReq.setRetryPolicy(new DefaultRetryPolicy(
//                5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserProfile.themeColor == 101)   {
            relativeLayout.setBackgroundResource(R.drawable.yellow_gradient);
            view.setBackgroundResource(R.drawable.yellow_gradient);
            iconNotice.setImageResource(R.drawable.ic_notice_yel);
            iconNotice.setBackgroundResource(R.drawable.circular_yellow);
            recyclerView.setAdapter(noticeAdapter);
        }
    }
}
