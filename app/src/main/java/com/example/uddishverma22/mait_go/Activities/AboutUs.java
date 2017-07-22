package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.R;

public class AboutUs extends AppCompatActivity {

    Toolbar toolbar;
    TextView developer, devOne, devTwo, devOneEmail, devTwoEmail;
    Typeface tfBold, tfReg, tfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        attachViews();

        setToolbar();

    }

    private void attachViews() {
        developer = (TextView) findViewById(R.id.developer_title);
        devOne = (TextView) findViewById(R.id.dev_one);
        devTwo = (TextView) findViewById(R.id.dev_two);
        devOneEmail = (TextView) findViewById(R.id.one_email);
        devTwoEmail = (TextView) findViewById(R.id.two_email);

        tfLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Light.ttf");
        tfReg = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Regular.ttf");
        tfBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-Bold.ttf");

        developer.setTypeface(tfBold);
        devOne.setTypeface(tfReg);
        devTwo.setTypeface(tfReg);
        devOneEmail.setTypeface(tfLight);
        devTwoEmail.setTypeface(tfLight);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_aboutus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }

}
