package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

import static com.example.uddishverma22.mait_go.R.id.event_image;
import static com.example.uddishverma22.mait_go.R.id.event_img;

public class EventsDetailsPage extends AppCompatActivity {

    ImageView eventImg;
    BottomSheetBehavior mBottomSheetBehavior;
    NestedScrollView bottomSheet;
    TextView eventName, organiser, date, fee, email, number, society;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details_page);

        eventImg = (ImageView) findViewById(event_image);

        organiser = (TextView) findViewById(R.id.ev_name);
        eventName = (TextView) findViewById(R.id.ev_event);
        date = (TextView) findViewById(R.id.ev_date);
        fee = (TextView) findViewById(R.id.ev_fee);
        email = (TextView) findViewById(R.id.ev_email);
        number = (TextView) findViewById(R.id.ev_no);
        society = (TextView) findViewById(R.id.ev_society);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent i = getIntent();

        if (i != null) {
            Picasso.with(getApplicationContext()).load(i.getStringExtra("imageUrl")).into(eventImg);
            organiser.setText(i.getStringExtra("organiser"));
            eventName.setText(i.getStringExtra("eventName"));
            date.setText(i.getStringExtra("date").substring(0, 10));
            fee.setText(i.getStringExtra("fee"));
            email.setText(i.getStringExtra("email"));
            number.setText(i.getStringExtra("number"));
            society.setText(i.getStringExtra("society"));
        }

        eventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        bottomSheet = (NestedScrollView) findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mBottomSheetBehavior.setPeekHeight(0);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });
    }
}
