package com.example.uddishverma22.mait_go;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.example.uddishverma22.mait_go.Activities.Announcements;
import com.example.uddishverma22.mait_go.Activities.Assignments;
import com.example.uddishverma22.mait_go.Activities.FacultyInformation;
import com.example.uddishverma22.mait_go.Activities.Login;
import com.example.uddishverma22.mait_go.Activities.Notices;
import com.example.uddishverma22.mait_go.Activities.Result;
import com.example.uddishverma22.mait_go.Activities.UpcomingEvents;
import com.example.uddishverma22.mait_go.Activities.UserProfile;
import com.example.uddishverma22.mait_go.Adapters.DailyScheduleListAdapter;
import com.example.uddishverma22.mait_go.Models.DailySchedule;
import com.example.uddishverma22.mait_go.Models.Notice;
import com.example.uddishverma22.mait_go.Models.TempModel;
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
    AnimationDrawable gradAnim;
    ActionBarDrawerToggle toggle;

    CircleImageView navDrawerImage;

    Realm realm = null;


    //List to add all the week's list
    DailySchedule mSchedule;
    RealmResults<TempModel> result;

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


    public RecyclerView recyclerView;
    public DailyScheduleListAdapter scheduleListAdapter;
    String currentDate, currentDay, currentYear, currentMonth;
    Toolbar toolbar;
    TextView date, day, month;
    TextView mon, tue, wed, thu, fri;
    static int monSelected = 0;
    static int tueSelected = 0;
    static int wedSelected = 0;
    static int thuSelected = 0;
    static int friSelected = 0;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Crashlytics support
        Fabric.with(this, new Crashlytics());

        realm = Realm.getDefaultInstance();

        mAvi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mAvi.show();

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

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
                            mondayScheduleFunction();


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
                    mondayScheduleFunction();
                } else {
                    Toast.makeText(MainActivity.this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, "onErrorResponse: " + error.toString() + " NET_CODE " + IS_NET_AVAIL);

            }
        });
        queue.add(request);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //Sending the list to the adapter
        scheduleListAdapter = new DailyScheduleListAdapter(mondaySchedule);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(scheduleListAdapter);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_one);

        //Attaching all the fields
        attachFields();

        currentDate = getCurrentDate();
        currentDay = getCurrentDay();
        currentYear = getCurrentYear();
        currentMonth = getCurrentMonth();
        date.setText(currentDate);
        day.setText(currentDay);
        month.setText(currentMonth.substring(0, 3) + " " + currentYear);

        //Setting the actual details in all the fields
        attachDateToDays();

//        if(UserProfile.themeColor == 101) {
//            Log.d(TAG, "onCreate: Theme " + UserProfile.themeColor);
//            linearLayout.setBackgroundResource(R.drawable.orange_gradient);
//        }

        /**
         * Checking which day is selected
         */
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        getSupportActionBar().setElevation(0);

        //Gradient animation
//        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_one);
//        gradAnim = (AnimationDrawable) linearLayout.getBackground();
//        gradAnim.setEnterFadeDuration(2000);
//        gradAnim.setExitFadeDuration(2000);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, Login.class));

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(this, FacultyInformation.class));

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(this, Announcements.class));
        } else if (id == R.id.assignment) {
            startActivity(new Intent(this, Assignments.class));
        } else if (id == R.id.events) {
            startActivity(new Intent(this, UpcomingEvents.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attachFields() {
        date = (TextView) findViewById(R.id.date_tv);
        day = (TextView) findViewById(R.id.day_tv);
        month = (TextView) findViewById(R.id.month_tv);
        mon = (TextView) findViewById(R.id.date_mon);
        tue = (TextView) findViewById(R.id.date_tue);
        wed = (TextView) findViewById(R.id.date_wed);
        thu = (TextView) findViewById(R.id.date_thu);
        fri = (TextView) findViewById(R.id.date_fri);
        navDrawerImage = (CircleImageView) findViewById(R.id.nav_image);
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
            mon.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));
            //setting date for wed
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));
            //setting date for thu
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));
            //setting date for fri
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));
        }

        if (currentDay.equals("Tuesday")) {
            tue.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));
            //setting date for wed
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));
            //setting date for thu
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));
            //setting date for fri
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));
        }

        if (currentDay.equals("Wednesday")) {
            wed.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));
            //setting date for fri
            calendar.add(Calendar.DATE, 1);
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));
            //setting date for tue
            calendar.add(Calendar.DATE, -3);
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));
            //setting date for mon
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Thursday")) {
            thu.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));
            //setting date for wed
            calendar.add(Calendar.DATE, -2);
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));
            //setting date for tue
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));
            //setting date for mon
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Friday")) {
            fri.setText(currentDate);
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));
            //setting date for wed
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));
            //setting date for tue
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));
            //setting date for mon
            calendar.add(Calendar.DATE, -1);
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));
        }
        if (currentDay.equals("Saturday")) {
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));      //setting date for fri
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));      //setting date for thu
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));      //setting date for wed
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));      //setting date for tue
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));      //setting date for mon
        }
        if (currentDay.equals("Sunday")) {
            try {
                calendar.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, -2);  // number of days to add
            dt = sdf.format(calendar.getTime());
            fri.setText(dt.substring(0, 2));      //setting date for fri
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            thu.setText(dt.substring(0, 2));      //setting date for thu
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            wed.setText(dt.substring(0, 2));      //setting date for wed
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            tue.setText(dt.substring(0, 2));      //setting date for tue
            calendar.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(calendar.getTime());
            mon.setText(dt.substring(0, 2));      //setting date for mon
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
            mon.setBackgroundResource(R.drawable.circular_image);
            mon.setTextColor(Color.BLACK);

            tue.setBackgroundResource(0);
            tue.setTextColor(Color.WHITE);
            wed.setBackgroundResource(0);
            wed.setTextColor(Color.WHITE);
            thu.setBackgroundResource(0);
            thu.setTextColor(Color.WHITE);
            fri.setBackgroundResource(0);
            fri.setTextColor(Color.WHITE);
        } else if (tueSelected == 1) {
            tue.setBackgroundResource(R.drawable.circular_image);
            tue.setTextColor(Color.BLACK);

            mon.setBackgroundResource(0);
            mon.setTextColor(Color.WHITE);
            wed.setBackgroundResource(0);
            wed.setTextColor(Color.WHITE);
            thu.setBackgroundResource(0);
            thu.setTextColor(Color.WHITE);
            fri.setBackgroundResource(0);
            fri.setTextColor(Color.WHITE);
        } else if (wedSelected == 1) {
            wed.setBackgroundResource(R.drawable.circular_image);
            wed.setTextColor(Color.BLACK);

            mon.setBackgroundResource(0);
            mon.setTextColor(Color.WHITE);
            tue.setBackgroundResource(0);
            tue.setTextColor(Color.WHITE);
            thu.setBackgroundResource(0);
            thu.setTextColor(Color.WHITE);
            fri.setBackgroundResource(0);
            fri.setTextColor(Color.WHITE);
        } else if (thuSelected == 1) {
            thu.setBackgroundResource(R.drawable.circular_image);
            thu.setTextColor(Color.BLACK);

            mon.setBackgroundResource(0);
            mon.setTextColor(Color.WHITE);
            tue.setBackgroundResource(0);
            tue.setTextColor(Color.WHITE);
            wed.setBackgroundResource(0);
            wed.setTextColor(Color.WHITE);
            fri.setBackgroundResource(0);
            fri.setTextColor(Color.WHITE);
        } else if (friSelected == 1) {
            fri.setBackgroundResource(R.drawable.circular_image);
            fri.setTextColor(Color.BLACK);

            mon.setBackgroundResource(0);
            mon.setTextColor(Color.WHITE);
            tue.setBackgroundResource(0);
            tue.setTextColor(Color.WHITE);
            thu.setBackgroundResource(0);
            thu.setTextColor(Color.WHITE);
            wed.setBackgroundResource(0);
            wed.setTextColor(Color.WHITE);
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
