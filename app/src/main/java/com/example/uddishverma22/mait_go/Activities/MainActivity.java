package com.example.uddishverma22.mait_go.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.example.uddishverma22.mait_go.Adapters.DailyScheduleListAdapter;
import com.example.uddishverma22.mait_go.Models.DailySchedule;
import com.example.uddishverma22.mait_go.Models.TempModel;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    String url = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/timetable/4I4";

    private static int IS_NET_AVAIL = 2000;

    LinearLayout linearLayout;
    ActionBarDrawerToggle toggle;

    public static Context context;

    //Navigation header items
    public static View headerView;
    public static TextView navHeaderText;
    public static CircleImageView navHeaderImage;

    Realm realm = null;

    public static NavigationView navigationView;

    //List to add all the week's list
    DailySchedule mSchedule;
    RealmResults<TempModel> result;

    Typeface tfThin;
    Typeface tfLight;
    Typeface openSansReg;
    Typeface openSansBold;

    public List<DailySchedule> mondaySchedule = new ArrayList<>();
    JSONArray mondayScheduleArray = null;
    JSONObject mondayScheduleObject = null;
    public List<DailySchedule> tuesdaySchedule = new ArrayList<>();
    JSONArray tuesdayScheduleArray = null;
    JSONObject tuesdayScheduleObject = null;
    public List<DailySchedule> wednesdaySchedule = new ArrayList<>();
    JSONArray wednesdayScheduleArray = null;
    JSONObject wednesdayScheduleObject = null;
    public List<DailySchedule> thursdaySchedule = new ArrayList<>();
    JSONArray thursdayScheduleArray = null;
    JSONObject thursdayScheduleObject = null;
    public List<DailySchedule> fridaySchedule = new ArrayList<>();
    JSONArray fridayScheduleArray = null;
    JSONObject fridayScheduleObject = null;

    AVLoadingIndicatorView mAvi;

    RequestQueue queue;

    //Views when there is no class
    RelativeLayout noClassLayout;
    TextView noClassTv;

    public RecyclerView recyclerView;
    public DailyScheduleListAdapter scheduleListAdapter;
    String currentDate, currentDay, currentYear, currentMonth;
    Toolbar toolbar;
    TextView date, day, month;
    TextView monTv, tueTv, wedTv, thuTv, friTv;
    TextView monHeading, tueHeading, wedHeading, thuHeading, friheading;

    View mainSideView;

    LinearLayout mon, tue, wed, thu, fri;
    static int monSelected = 0;
    static int tueSelected = 0;
    static int wedSelected = 0;
    static int thuSelected = 0;
    static int friSelected = 0;
    Calendar calendar;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        //Crashlytics support
        Fabric.with(this, new Crashlytics());

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navHeaderText = (TextView) headerView.findViewById(R.id.nav_textview);
        navHeaderImage = (CircleImageView) headerView.findViewById(R.id.nav_image);
        if (!Preferences.getPrefs("studentImage", getApplicationContext()).equals("notfound")) {
            Picasso.with(this).load(Preferences.getPrefs("studentImage", getApplicationContext())).into(navHeaderImage);
        }
        if (!Preferences.getPrefs("studentName", getApplicationContext()).equals("notfound")) {
            navHeaderText.setText(Preferences.getPrefs("studentName", getApplicationContext()));
        }

        //fetching data from API
        fetchData(queue);

        //Setting the fonts
        tfThin = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Thin.ttf");
        tfLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Raleway-Light.ttf");
        openSansReg = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        openSansBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Bold.ttf");

        realm = Realm.getDefaultInstance();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mAvi.show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Attaching all the fields
        attachFields();

        currentDate = getCurrentDate();
        currentDay = getCurrentDay();
        currentYear = getCurrentYear();
        currentMonth = getCurrentMonth();
        date.setText(currentDate);
        day.setText(currentDay);
        month.setText(currentMonth.substring(0, 3) + " " + currentYear);

//        Setting the actual details in all the fields
        attachDateToDays();

//        Sending the list to the adapter
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(scheduleListAdapter);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_one);


        //Opening profile page on click of image on nav drawer
        navHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfile.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        /**
         * Checking which day is selected
         */
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noClassLayout.setVisibility(View.GONE);
                mondaySchedule = new ArrayList<DailySchedule>();
                mondayScheduleFunction();
                scheduleListAdapter = new DailyScheduleListAdapter(mondaySchedule);
                recyclerView.setAdapter(scheduleListAdapter);
                monSelected = 1;
                tueSelected = 0;
                wedSelected = 0;
                thuSelected = 0;
                friSelected = 0;
                changeDayCircleColor();
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noClassLayout.setVisibility(View.GONE);
                tuesdaySchedule = new ArrayList<DailySchedule>();
                tuesdayScheduleFunction();
                scheduleListAdapter = new DailyScheduleListAdapter(tuesdaySchedule);
                recyclerView.setAdapter(scheduleListAdapter);
                monSelected = 0;
                tueSelected = 1;
                wedSelected = 0;
                thuSelected = 0;
                friSelected = 0;
                changeDayCircleColor();
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noClassLayout.setVisibility(View.GONE);
                wednesdaySchedule = new ArrayList<DailySchedule>();
                wednesdayScheduleFunction();
                scheduleListAdapter = new DailyScheduleListAdapter(wednesdaySchedule);
                recyclerView.setAdapter(scheduleListAdapter);
                monSelected = 0;
                tueSelected = 0;
                wedSelected = 1;
                thuSelected = 0;
                friSelected = 0;
                changeDayCircleColor();
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noClassLayout.setVisibility(View.GONE);
                thursdaySchedule = new ArrayList<DailySchedule>();
                thursdayScheduleFunction();
                scheduleListAdapter = new DailyScheduleListAdapter(thursdaySchedule);
                recyclerView.setAdapter(scheduleListAdapter);
                monSelected = 0;
                tueSelected = 0;
                wedSelected = 0;
                thuSelected = 1;
                friSelected = 0;
                changeDayCircleColor();
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noClassLayout.setVisibility(View.GONE);
                fridaySchedule = new ArrayList<DailySchedule>();
                fridayScheduleFunction();
                scheduleListAdapter = new DailyScheduleListAdapter(fridaySchedule);
                recyclerView.setAdapter(scheduleListAdapter);
                monSelected = 0;
                tueSelected = 0;
                wedSelected = 0;
                thuSelected = 0;
                friSelected = 1;
                changeDayCircleColor();
            }
        });


        //Setting NavBar items
        getSupportActionBar().setElevation(0);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainSideView.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        ColorStateList csl = AppCompatResources.getColorStateList(this, R.color.white);
        Drawable drawableone = getResources().getDrawable(R.drawable.hamburger_icon);
        DrawableCompat.setTintList(drawableone, csl);
        toolbar.setNavigationIcon(drawableone);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fetchData(RequestQueue queue) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            IS_NET_AVAIL = 2001;
                            mAvi.hide();
                            mondayScheduleArray = response.getJSONArray("monday");
                            tuesdayScheduleArray = response.getJSONArray("tuesday");
                            wednesdayScheduleArray = response.getJSONArray("wednesday");
                            thursdayScheduleArray = response.getJSONArray("thursday");
                            fridayScheduleArray = response.getJSONArray("friday");
                            mondayScheduleObject = mondayScheduleArray.getJSONObject(0);
                            tuesdayScheduleObject = tuesdayScheduleArray.getJSONObject(0);
                            wednesdayScheduleObject = wednesdayScheduleArray.getJSONObject(0);
                            thursdayScheduleObject = thursdayScheduleArray.getJSONObject(0);
                            fridayScheduleObject = fridayScheduleArray.getJSONObject(0);

//*************************************         Saving data to realm           ***********************************************
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(TempModel.class).findAll().deleteAllFromRealm();
                                    realm.createObjectFromJson(TempModel.class, response);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "onSuccess: Realm Object Saved");
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    Log.d(TAG, "onError: " + error.toString());
                                }
                            });
//***************************************************************************************************************************
                            switch (currentDay) {

                                case "Monday":
                                    mondayScheduleFunction();
                                    monTv.setBackgroundResource(R.drawable.circular_image);
                                    monTv.setTextColor(Color.BLACK);
                                    scheduleListAdapter = new DailyScheduleListAdapter(mondaySchedule);
                                    recyclerView.setAdapter(scheduleListAdapter);
                                    break;
                                case "Tuesday":
                                    tuesdayScheduleFunction();
                                    tueTv.setBackgroundResource(R.drawable.circular_image);
                                    tueTv.setTextColor(Color.BLACK);
                                    scheduleListAdapter = new DailyScheduleListAdapter(tuesdaySchedule);
                                    recyclerView.setAdapter(scheduleListAdapter);
                                    break;
                                case "Wednesday":
                                    wednesdayScheduleFunction();
                                    wedTv.setBackgroundResource(R.drawable.circular_image);
                                    wedTv.setTextColor(Color.BLACK);
                                    scheduleListAdapter = new DailyScheduleListAdapter(wednesdaySchedule);
                                    recyclerView.setAdapter(scheduleListAdapter);
                                    break;
                                case "Thursday":
                                    thursdayScheduleFunction();
                                    thuTv.setBackgroundResource(R.drawable.circular_image);
                                    thuTv.setTextColor(Color.BLACK);
                                    scheduleListAdapter = new DailyScheduleListAdapter(thursdaySchedule);
                                    recyclerView.setAdapter(scheduleListAdapter);
                                    break;
                                case "Friday":
                                    fridayScheduleFunction();
                                    friTv.setBackgroundResource(R.drawable.circular_image);
                                    friTv.setTextColor(Color.BLACK);
                                    scheduleListAdapter = new DailyScheduleListAdapter(fridaySchedule);
                                    recyclerView.setAdapter(scheduleListAdapter);
                                    break;
                                default:
                                    noClassLayout.setVisibility(View.VISIBLE);
                                    break;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAvi.hide();
                IS_NET_AVAIL = 2000;
                //Showing data from Realm)
                result = realm.where(TempModel.class).findAll();
                ArrayList<TempModel> list = new ArrayList<>(result);        //converting realm list into arraylist
                Log.d(TAG, "onErrorResponse: List Size " + list.size() + "\n" + list);

                //Offline JSONArray
                JSONArray monArray = null;
                JSONArray tuesArray = null;
                JSONArray wedArray = null;
                JSONArray thursArray = null;
                JSONArray friArray = null;
                if (list.size() != 0) {
                    try {
                        monArray = new JSONArray(list.get(0).getMonday());
                        tuesArray = new JSONArray(list.get(0).getTuesday());
                        wedArray = new JSONArray(list.get(0).getWednesday());
                        thursArray = new JSONArray(list.get(0).getThursday());
                        friArray = new JSONArray(list.get(0).getFriday());

                        //Saving offline data to JSONObject
                        mondayScheduleObject = monArray.getJSONObject(0);
                        tuesdayScheduleObject = tuesArray.getJSONObject(0);
                        wednesdayScheduleObject = wedArray.getJSONObject(0);
                        thursdayScheduleObject = thursArray.getJSONObject(0);
                        fridayScheduleObject = friArray.getJSONObject(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    switch (currentDay) {
                        case "Monday":
                            mondayScheduleFunction();
                            scheduleListAdapter = new DailyScheduleListAdapter(mondaySchedule);
                            recyclerView.setAdapter(scheduleListAdapter);
                            break;
                        case "Tuesday":
                            tuesdayScheduleFunction();
                            scheduleListAdapter = new DailyScheduleListAdapter(tuesdaySchedule);
                            recyclerView.setAdapter(scheduleListAdapter);
                            break;
                        case "Wednesday":
                            wednesdayScheduleFunction();
                            scheduleListAdapter = new DailyScheduleListAdapter(wednesdaySchedule);
                            recyclerView.setAdapter(scheduleListAdapter);
                            break;
                        case "Thursday":
                            thursdayScheduleFunction();
                            scheduleListAdapter = new DailyScheduleListAdapter(thursdaySchedule);
                            recyclerView.setAdapter(scheduleListAdapter);
                            break;
                        case "Friday":
                            fridayScheduleFunction();
                            scheduleListAdapter = new DailyScheduleListAdapter(fridaySchedule);
                            recyclerView.setAdapter(scheduleListAdapter);
                            break;
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "onErrorResponse: " + error.toString() + " NET_CODE " + IS_NET_AVAIL);

            }
        });
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notices) {
            startActivity(new Intent(this, Notices.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, UserProfile.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, Result.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, FacultyInformation.class));

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(this, Announcements.class));
        } else if (id == R.id.assignment) {
            startActivity(new Intent(this, Assignments.class));
        } else if (id == R.id.events) {
            startActivity(new Intent(this, UpcomingEvents.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attachFields() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mainSideView = (View) findViewById(R.id.main_view);

        date = (TextView) findViewById(R.id.date_tv);
        day = (TextView) findViewById(R.id.day_tv);
        month = (TextView) findViewById(R.id.month_tv);
        monTv = (TextView) findViewById(R.id.date_mon);
        tueTv = (TextView) findViewById(R.id.date_tue);
        wedTv = (TextView) findViewById(R.id.date_wed);
        thuTv = (TextView) findViewById(R.id.date_thu);
        friTv = (TextView) findViewById(R.id.date_fri);

        mon = (LinearLayout) findViewById(R.id.ll_mon);
        tue = (LinearLayout) findViewById(R.id.ll_tue);
        wed = (LinearLayout) findViewById(R.id.ll_wed);
        thu = (LinearLayout) findViewById(R.id.ll_thu);
        fri = (LinearLayout) findViewById(R.id.ll_fri);

        noClassLayout = (RelativeLayout) findViewById(R.id.noclass_layout);
        noClassTv = (TextView) findViewById(R.id.no_class_tv);
        noClassTv.setTypeface(openSansReg);

        date.setTypeface(openSansBold);
        day.setTypeface(openSansReg);
        month.setTypeface(openSansReg);
        monTv.setTypeface(openSansReg);
        tueTv.setTypeface(openSansReg);
        wedTv.setTypeface(openSansReg);
        thuTv.setTypeface(openSansReg);
        friTv.setTypeface(openSansReg);

        monHeading = (TextView) findViewById(R.id.mon_heading);
        tueHeading = (TextView) findViewById(R.id.tue_heading);
        wedHeading = (TextView) findViewById(R.id.wed_heading);
        thuHeading = (TextView) findViewById(R.id.thu_heading);
        friheading = (TextView) findViewById(R.id.fri_heading);

        monHeading.setTypeface(openSansReg);
        tueHeading.setTypeface(openSansReg);
        wedHeading.setTypeface(openSansReg);
        thuHeading.setTypeface(openSansReg);
        friheading.setTypeface(openSansReg);

    }

    private String getCurrentDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = mdformat.format(calendar.getTime());
        String date = strDate.substring(0, 2);
        return date;
    }

    private String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    private void attachDateToDays() {
        String dt = currentDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        calendar = Calendar.getInstance();

        if (currentDay.equals("Monday")) {

            monTv.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));
            //setting date for wedTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));
            //setting date for thuTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));
            //setting date for friTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));
        }

        if (currentDay.equals("Tuesday")) {

            tueTv.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));
            //setting date for wedTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));
            //setting date for thuTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));
            //setting date for friTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));
        }

        if (currentDay.equals("Wednesday")) {

            wedTv.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));
            //setting date for friTv
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));
            //setting date for tueTv
            calendar.add(Calendar.DATE, -3);
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));
            //setting date for monTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Thursday")) {

            thuTv.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));
            //setting date for wedTv
            calendar.add(Calendar.DATE, -2);
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));
            //setting date for tueTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));
            //setting date for monTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Friday")) {

            friTv.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));
            //setting date for wedTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));
            //setting date for tueTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));
            //setting date for monTv
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Saturday")) {
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));      //setting date for friTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));      //setting date for thuTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));      //setting date for wedTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));      //setting date for tueTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));      //setting date for monTv
        }
        if (currentDay.equals("Sunday")) {
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -2);  // number of days to add
            dt = sdf.format(calendar.getTime());
            friTv.setText(dt.substring(0, 2));      //setting date for friTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thuTv.setText(dt.substring(0, 2));      //setting date for thuTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            wedTv.setText(dt.substring(0, 2));      //setting date for wedTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tueTv.setText(dt.substring(0, 2));      //setting date for tueTv
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            monTv.setText(dt.substring(0, 2));      //setting date for monTv
        }

    }

    private String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = mdformat.format(calendar.getTime());
        String year = strDate.substring(0, 4);
        return year;
    }

    private String getCurrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        String month = (dateFormat.format(date));
        String currMonth = null;
        if (month.equals("01")) {
            currMonth = "January";
        } else if (month.equals("02")) {
            currMonth = "February";
        } else if (month.equals("03")) {
            currMonth = "March";
        } else if (month.equals("04")) {
            currMonth = "April";
        } else if (month.equals("05")) {
            currMonth = "May";
        } else if (month.equals("06")) {
            currMonth = "June";
        } else if (month.equals("07")) {
            currMonth = "July";
        } else if (month.equals("08")) {
            currMonth = "August";
        } else if (month.equals("09")) {
            currMonth = "September";
        } else if (month.equals("10")) {
            currMonth = "October";
        } else if (month.equals("11")) {
            currMonth = "November";
        } else if (month.equals("12")) {
            currMonth = "December";
        }
        return currMonth;
    }

    //Adding the monday Schedule in the list
    private void mondayScheduleFunction() {
        try {
            DailySchedule movie = new DailySchedule("8:15 - 9:15", (String) mondayScheduleObject.getJSONArray("p1").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p1").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p1").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("9:15 - 10:15", (String) mondayScheduleObject.getJSONArray("p2").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p2").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p2").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("10:15 - 11:15", (String) mondayScheduleObject.getJSONArray("p3").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p3").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p3").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("11:45 - 12:45", (String) mondayScheduleObject.getJSONArray("p4").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p4").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p4").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("12:45 - 1:45", (String) mondayScheduleObject.getJSONArray("p5").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p5").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p5").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("1:45 - 2:45", (String) mondayScheduleObject.getJSONArray("p6").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p6").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p6").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);
            movie = new DailySchedule("2:45 - 3:45", (String) mondayScheduleObject.getJSONArray("p7").getJSONObject(0).get("subject"), (String) mondayScheduleObject.getJSONArray("p7").getJSONObject(0).get("room"), (String) mondayScheduleObject.getJSONArray("p7").getJSONObject(0).get("teacher"));
            mondaySchedule.add(movie);

            scheduleListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "mondayScheduleFunction: " + e.toString());
        }
    }

    //Adding the tuesday Schedule in the list
    private void tuesdayScheduleFunction() {
        try {
            DailySchedule movie = new DailySchedule("8:15 - 9:15", (String) tuesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("9:15 - 10:15", (String) tuesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("10:15 - 11:15", (String) tuesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("11:45 - 12:45", (String) tuesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("12:45 - 1:45", (String) tuesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("1:45 - 2:45", (String) tuesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            movie = new DailySchedule("2:45 - 3:45", (String) tuesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("subject"), (String) tuesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("room"), (String) tuesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("teacher"));
            tuesdaySchedule.add(movie);
            scheduleListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "mondayScheduleFunction: " + e.toString());
        }
    }

    //Adding the wednesday Schedule in the list
    private void wednesdayScheduleFunction() {
        try {
            DailySchedule movie = new DailySchedule("8:15 - 9:15", (String) wednesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("9:15 - 10:15", (String) wednesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("10:15 - 11:15", (String) wednesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("11:45 - 12:45", (String) wednesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("12:45 - 1:45", (String) wednesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("1:45 - 2:45", (String) wednesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            movie = new DailySchedule("2:45 - 3:45", (String) wednesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("subject"), (String) wednesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("room"), (String) wednesdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("teacher"));
            wednesdaySchedule.add(movie);
            scheduleListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "mondayScheduleFunction: " + e.toString());
        }
    }

    //Adding the thursday Schedule in the list
    private void thursdayScheduleFunction() {
        try {
            DailySchedule movie = new DailySchedule("8:15 - 9:15", (String) thursdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p1").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("9:15 - 10:15", (String) thursdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p2").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("10:15 - 11:15", (String) thursdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p3").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("11:45 - 12:45", (String) thursdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p4").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("12:45 - 1:45", (String) thursdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p5").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("1:45 - 2:45", (String) thursdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p6").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            movie = new DailySchedule("2:45 - 3:45", (String) thursdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("subject"), (String) thursdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("room"), (String) thursdayScheduleObject.getJSONArray("p7").getJSONObject(0).get("teacher"));
            thursdaySchedule.add(movie);
            scheduleListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "mondayScheduleFunction: " + e.toString());
        }
    }

    //Adding the friday Schedule in the list
    private void fridayScheduleFunction() {

        try {
            DailySchedule movie = new DailySchedule("8:15 - 9:15", (String) fridayScheduleObject.getJSONArray("p1").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p1").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p1").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("9:15 - 10:15", (String) fridayScheduleObject.getJSONArray("p2").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p2").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p2").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("10:15 - 11:15", (String) fridayScheduleObject.getJSONArray("p3").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p3").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p3").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("11:45 - 12:45", (String) fridayScheduleObject.getJSONArray("p4").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p4").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p4").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("12:45 - 1:45", (String) fridayScheduleObject.getJSONArray("p5").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p5").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p5").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("1:45 - 2:45", (String) fridayScheduleObject.getJSONArray("p6").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p6").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p6").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            movie = new DailySchedule("2:45 - 3:45", (String) fridayScheduleObject.getJSONArray("p7").getJSONObject(0).get("subject"), (String) fridayScheduleObject.getJSONArray("p7").getJSONObject(0).get("room"), (String) fridayScheduleObject.getJSONArray("p7").getJSONObject(0).get("teacher"));
            fridaySchedule.add(movie);
            scheduleListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.d(TAG, "mondayScheduleFunction: " + e.toString());
        }
    }

    private void changeDayCircleColor() {
        if (monSelected == 1) {
            monTv.setBackgroundResource(R.drawable.circular_image);
            monTv.setTextColor(Color.BLACK);

            tueTv.setBackgroundResource(0);
            tueTv.setTextColor(Color.WHITE);
            wedTv.setBackgroundResource(0);
            wedTv.setTextColor(Color.WHITE);
            thuTv.setBackgroundResource(0);
            thuTv.setTextColor(Color.WHITE);
            friTv.setBackgroundResource(0);
            friTv.setTextColor(Color.WHITE);
        } else if (tueSelected == 1) {
            tueTv.setBackgroundResource(R.drawable.circular_image);
            tueTv.setTextColor(Color.BLACK);

            monTv.setBackgroundResource(0);
            monTv.setTextColor(Color.WHITE);
            wedTv.setBackgroundResource(0);
            wedTv.setTextColor(Color.WHITE);
            thuTv.setBackgroundResource(0);
            thuTv.setTextColor(Color.WHITE);
            friTv.setBackgroundResource(0);
            friTv.setTextColor(Color.WHITE);
        } else if (wedSelected == 1) {
            wedTv.setBackgroundResource(R.drawable.circular_image);
            wedTv.setTextColor(Color.BLACK);

            monTv.setBackgroundResource(0);
            monTv.setTextColor(Color.WHITE);
            tueTv.setBackgroundResource(0);
            tueTv.setTextColor(Color.WHITE);
            thuTv.setBackgroundResource(0);
            thuTv.setTextColor(Color.WHITE);
            friTv.setBackgroundResource(0);
            friTv.setTextColor(Color.WHITE);
        } else if (thuSelected == 1) {
            thuTv.setBackgroundResource(R.drawable.circular_image);
            thuTv.setTextColor(Color.BLACK);

            monTv.setBackgroundResource(0);
            monTv.setTextColor(Color.WHITE);
            tueTv.setBackgroundResource(0);
            tueTv.setTextColor(Color.WHITE);
            wedTv.setBackgroundResource(0);
            wedTv.setTextColor(Color.WHITE);
            friTv.setBackgroundResource(0);
            friTv.setTextColor(Color.WHITE);
        } else if (friSelected == 1) {
            friTv.setBackgroundResource(R.drawable.circular_image);
            friTv.setTextColor(Color.BLACK);

            monTv.setBackgroundResource(0);
            monTv.setTextColor(Color.WHITE);
            tueTv.setBackgroundResource(0);
            tueTv.setTextColor(Color.WHITE);
            thuTv.setBackgroundResource(0);
            thuTv.setTextColor(Color.WHITE);
            wedTv.setBackgroundResource(0);
            wedTv.setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (gradAnim != null && !gradAnim.isRunning())
//            gradAnim.start();
        if (UserProfile.themeColor == 101) {
            Log.d(TAG, "onCreate: Theme " + UserProfile.themeColor);
            linearLayout.setBackgroundResource(R.drawable.orange_gradient);
            toolbar.setBackgroundResource(R.drawable.orange_gradient);
            recyclerView.setAdapter(scheduleListAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (gradAnim != null && gradAnim.isRunning())
//            gradAnim.stop();
    }
}