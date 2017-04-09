package com.example.uddishverma22.mait_go;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Transition;
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
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Activities.Notices;
import com.example.uddishverma22.mait_go.Activities.UserProfile;
import com.example.uddishverma22.mait_go.Adapters.DailyScheduleListAdapter;
import com.example.uddishverma22.mait_go.BarcodeGenerator.Generation;
import com.example.uddishverma22.mait_go.Models.DailySchedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    LinearLayout linearLayout;
    AnimationDrawable gradAnim;
    ActionBarDrawerToggle toggle;
    public List<DailySchedule> mondaySchedule = new ArrayList<>();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        scheduleListAdapter = new DailyScheduleListAdapter(mondaySchedule);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(scheduleListAdapter);
        mondayScheduleFunction();

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout_one);

        //Attaching all the fields
        attachFields();

        currentDate = getCurrentDate();
        currentDay = getCurrentDay();
        currentYear = getCurrentYear();
        currentMonth = getCurrentMonth();
        date.setText(currentDate);
        day.setText(currentDay);
        month.setText(currentMonth.substring(0,3) + " " + currentYear);

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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
            startActivity(new Intent(this, Generation.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void attachFields()  {
        date = (TextView) findViewById(R.id.date_tv);
        day = (TextView) findViewById(R.id.day_tv);
        month = (TextView) findViewById(R.id.month_tv);
        mon = (TextView) findViewById(R.id.date_mon);
        tue = (TextView) findViewById(R.id.date_tue);
        wed = (TextView) findViewById(R.id.date_wed);
        thu = (TextView) findViewById(R.id.date_thu);
        fri = (TextView) findViewById(R.id.date_fri);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = mdformat.format(calendar.getTime());
        String date = strDate.substring(0,2);
        return date;
    }

    private String getCurrentDay()    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    private String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = mdformat.format(calendar.getTime());
        String year = strDate.substring(0,4);
        return year;
    }

    private String getCurrentMonth()    {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        String month = (dateFormat.format(date));
        String currMonth = null;
        if(month.equals("01")) {
            currMonth = "January";
        }else if(month.equals("02")) {
            currMonth = "February";
        }else if(month.equals("03")) {
            currMonth = "March";
        }else if(month.equals("04")) {
            currMonth = "April";
        }else if(month.equals("05")) {
            currMonth = "May";
        }else if(month.equals("06")) {
            currMonth = "June";
        }else if(month.equals("07")) {
            currMonth = "July";
        }else if(month.equals("08")) {
            currMonth = "August";
        }else if(month.equals("09")) {
            currMonth = "September";
        }else if(month.equals("10")) {
            currMonth = "October";
        }else if(month.equals("11")) {
            currMonth = "November";
        }else if(month.equals("12")) {
            currMonth = "December";
        }
        return currMonth;
    }

    private void mondayScheduleFunction() {
        DailySchedule movie = new DailySchedule("8:15 - 9:15", "Theory of Computation", "841");
        mondaySchedule.add(movie);
        movie = new DailySchedule("8:15 - 10:15", "Control System", "841");
        mondaySchedule.add(movie);
        movie = new DailySchedule("10:15 - 11:15", "Maths", "843");
        mondaySchedule.add(movie);
        movie = new DailySchedule("11:15 - 12:15", "COA", "841");
        mondaySchedule.add(movie);
        movie = new DailySchedule("12:15 - 1:15", "OOPS", "842");
        mondaySchedule.add(movie);
        movie = new DailySchedule("1:15 - 2:15", "DBMS", "841");
        mondaySchedule.add(movie);

        scheduleListAdapter.notifyDataSetChanged();
    }

    private void changeDayCircleColor()   {
        if(monSelected == 1)    {
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
        }
        else if(tueSelected == 1)    {
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
        }
        else if(wedSelected == 1)   {
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
        }
        else if(thuSelected == 1)   {
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
        }
        else if(friSelected == 1)   {
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
        if(UserProfile.themeColor == 101) {
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
