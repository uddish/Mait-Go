package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uddishverma22.mait_go.Adapters.FacultyListAdapter;
import com.example.uddishverma22.mait_go.Fragments.FacultyFragment;
import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacultyInformation extends AppCompatActivity implements FacultyFragment.OnFacultyChangeListener{

    public static final String TAG = "FacultyInformation";
//    TextView cse, it, eee, ece, mae;
//    View cseLine, itLine, eceLine, eeeLine ,maeLine;

    private  Toolbar toolbar;
    private  ViewPager viewPager;
    private  TabLayout tabLayout;

    RecyclerView mRecyclerView;
    FacultyListAdapter listAdapter;

    JSONArray itFaculty = null;
    JSONArray cseFaculty = null;

    JSONObject itFacObj, cseFacObj;
    Faculty itFacultyobj, cseFacultyobj;

    AVLoadingIndicatorView mAvi;

    Typeface openSansReg, openSansBold;

    ArrayList<Faculty> itFacList = new ArrayList<>();
    ArrayList<Faculty> cseFacList = new ArrayList<>();

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/faculty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_information);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

//        mRecyclerView = (RecyclerView) findViewById(R.id.fac_recycler);

//        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
//        mAvi.show();

//        cseLine = findViewById(R.id.cse_line);
//        itLine = findViewById(R.id.it_line);
//        eceLine = findViewById(R.id.ece_line);
//        eeeLine = findViewById(R.id.eee_line);
//        maeLine = findViewById(R.id.mae_line);

        openSansReg = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Bold.ttf");

//        it = (TextView) findViewById(R.id.it);
//        cse = (TextView) findViewById(R.id.cse);
//        eee = (TextView) findViewById(R.id.eee);
//        ece = (TextView) findViewById(R.id.ece);
//        mae = (TextView) findViewById(R.id.mae);
//
//        it.setTypeface(openSansReg);
//        cse.setTypeface(openSansReg);
//        eee.setTypeface(openSansReg);
//        ece.setTypeface(openSansReg);
//        mae.setTypeface(openSansReg);


//        it.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (itFacList != null) {
//                    listAdapter = new FacultyListAdapter(itFacList);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacultyInformation.this);
//                    mRecyclerView.setLayoutManager(layoutManager);
//                    mRecyclerView.setAdapter(listAdapter);
//                    itLine.setVisibility(View.VISIBLE);
//                    cseLine.setVisibility(View.INVISIBLE);
//                    eceLine.setVisibility(View.INVISIBLE);
//                    eeeLine.setVisibility(View.INVISIBLE);
//                    maeLine.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        cse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cseFacList != null) {
//                    listAdapter = new FacultyListAdapter(cseFacList);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacultyInformation.this);
//                    mRecyclerView.setLayoutManager(layoutManager);
//                    mRecyclerView.setAdapter(listAdapter);
//                    cseLine.setVisibility(View.VISIBLE);
//                    itLine.setVisibility(View.INVISIBLE);
//                    eceLine.setVisibility(View.INVISIBLE);
//                    eeeLine.setVisibility(View.INVISIBLE);
//                    maeLine.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        eee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FacultyInformation.this, "Updating Soon...", Toast.LENGTH_SHORT).show();
//            }
//        });
//        ece.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FacultyInformation.this, "Updating Soon...", Toast.LENGTH_SHORT).show();
//            }
//        });
//        mae.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(FacultyInformation.this, "Updating Soon...", Toast.LENGTH_SHORT).show();
//            }
//        });


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        mAvi.hide();

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
//                            Log.d(TAG, "onResponse: " + response.getJSONArray("IT"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        listAdapter = new FacultyListAdapter(itFacList);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacultyInformation.this);
//                        mRecyclerView.setLayoutManager(layoutManager);
//                        mRecyclerView.setAdapter(listAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        queue.add(request);

        //Handling click listeners on TabLayout

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition())  {
                    case 0:
                        onFacultyChange(cseFacList);
                        break;
                    case 1:
                        Log.d(TAG, "onTabSelected: " + itFacList);
                        onFacultyChange(itFacList);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FacultyFragment("CSE"), "CSE");
        adapter.addFrag(new FacultyFragment("IT"), "IT");
        adapter.addFrag(new FacultyFragment("ECE"), "ECE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFacultyChange(ArrayList<Faculty> list) {
        FacultyListAdapter adapter = new FacultyListAdapter(list);
        FacultyFragment.recyclerView.setAdapter(adapter);
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
