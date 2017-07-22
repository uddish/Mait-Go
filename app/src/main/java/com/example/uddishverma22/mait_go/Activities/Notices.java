package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Fragments.NoticeFragment;
import com.example.uddishverma22.mait_go.Models.Notice;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.CheckInternet;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class Notices extends AppCompatActivity {

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/scrape/notices";
    Notice noticeObj, examNoticeObj;

    public static final String TAG = "Notices";

    public ArrayList<Notice> noticeList = new ArrayList<>();
    public ArrayList<Notice> examNoticeList = new ArrayList<>();
    public RecyclerView recyclerView;
    RealmResults<Notice> results;
    Toolbar toolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    JSONObject jsonNoticeObject, jsonExamNoticeObject;

    CoordinatorLayout coordinatorLayout;
    LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        fetchData(queue);

        setToolbar();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);

        tabLayout.setTabTextColors(Color.parseColor("#3D5392"), Color.parseColor("#23283D"));

        //Handling click listeners on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        Log.d(TAG, "onTabSelected: Notices");
                        break;
                    case 1:
                        Log.d(TAG, "onTabSelected: Examination Notices");
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (!CheckInternet.isNetworkAvailable(Notices.this)) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
            errorLayout.setVisibility(View.VISIBLE);
        }

    }

    private void fetchData(RequestQueue queue) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        try {

                            JSONArray noticeArray = response.getJSONArray("exams");
                            JSONArray examNoticeArray = response.getJSONArray("datesheets");

                            for (int n = 0; n < noticeArray.length(); n++) {
                                jsonNoticeObject = noticeArray.getJSONObject(n);
                                noticeObj = new Notice();
                                noticeObj.notice = jsonNoticeObject.getString("notice");
                                noticeObj.url = jsonNoticeObject.getString("url");
                                noticeList.add(noticeObj);
                            }
                            Globals.noticeList = noticeList;

                            for (int n = 0; n < examNoticeArray.length(); n++) {
                                jsonExamNoticeObject = examNoticeArray.getJSONObject(n);
                                examNoticeObj = new Notice();
                                examNoticeObj.notice = jsonExamNoticeObject.getString("datesheet");
                                examNoticeObj.url = jsonExamNoticeObject.getString("url");
                                examNoticeList.add(examNoticeObj);
                            }
                            Globals.examinationNoticeList = examNoticeList;

                            //Setting up Viewpager
                            setupViewPager(viewPager);


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
        queue.add(jsonObjReq);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_notice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        Notices.ViewPagerAdapter adapter = new Notices.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NoticeFragment("Notice"), "Notice");
        adapter.addFrag(new NoticeFragment("Examination"), "Examination");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
