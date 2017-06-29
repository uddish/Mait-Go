package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Adapters.FacultyListAdapter;
import com.example.uddishverma22.mait_go.Fragments.FacultyFragment;
import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FacultyInformation extends AppCompatActivity {

    public static final String TAG = "FacultyInformation";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    JSONArray itFaculty = null;
    JSONArray cseFaculty = null;

    JSONObject itFacObj, cseFacObj;
    Faculty itFacultyobj, cseFacultyobj;

    Typeface openSansReg, openSansBold;

    ArrayList<Faculty> itFacList = new ArrayList<>();
    ArrayList<Faculty> cseFacList = new ArrayList<>();

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/faculty";

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_information);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        fetchData(queue);

        setToolbar();

        openSansReg = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Bold.ttf");

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        //Handling click listeners on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        Globals.title = "CSE";
                        Log.d(TAG, "onTabSelected: " + cseFacList);
                        break;
                    case 1:
                        Globals.title = "IT";
                        Log.d(TAG, "onTabSelected: " + itFacList);
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
    }

    private void fetchData(RequestQueue queue) {

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            itFaculty = response.getJSONArray("IT");
                            cseFaculty = response.getJSONArray("CSE");
                            for (int i = 0; i < itFaculty.length(); i++) {
                                itFacObj = itFaculty.getJSONObject(i);
                                itFacultyobj = new Faculty();
                                itFacultyobj.name = itFacObj.getString("name");
                                itFacultyobj.designation = itFacObj.getString("designation");
                                itFacultyobj.qualification = itFacObj.getString("qualification");
                                itFacultyobj.experience = itFacObj.getString("exp");
                                itFacultyobj.imageUrl = itFacObj.getString("img");
                                itFacList.add(itFacultyobj);
                            }
                            Globals.itFacList = itFacList;

                            for (int i = 0; i < cseFaculty.length(); i++) {
                                cseFacObj = cseFaculty.getJSONObject(i);
                                cseFacultyobj = new Faculty();
                                cseFacultyobj.name = cseFacObj.getString("name");
                                cseFacultyobj.designation = cseFacObj.getString("designation");
                                cseFacultyobj.qualification = cseFacObj.getString("qualification");
                                cseFacultyobj.experience = cseFacObj.getString("exp");
                                cseFacultyobj.imageUrl = cseFacObj.getString("img");
                                cseFacList.add(cseFacultyobj);
                            }
                            Globals.cseFacList = cseFacList;
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
        queue.add(request);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FacultyFragment("CSE"), "CSE");
        adapter.addFrag(new FacultyFragment("IT"), "IT");
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

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_faculty);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
