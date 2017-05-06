package com.example.uddishverma22.mait_go.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uddishverma22.mait_go.R;

public class Login extends AppCompatActivity {

    Animation studentUp, studentDown;
    EditText name, roll;
    TextView signinBtn, studentTrigger, teacherTrigger;
    ViewGroup group;
    LinearLayout studentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        name = (EditText) findViewById(R.id.name);
//        roll = (EditText) findViewById(R.id.roll);
//        signinBtn = (TextView) findViewById(R.id.sign_in);
//        studentTrigger = (TextView) findViewById(R.id.student_trigger);
//        teacherTrigger = (TextView) findViewById(R.id.teacher_trigger);
//
//        studentLayout = (LinearLayout) findViewById(R.id.student_layout);
//
//        studentUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_anim);
//        studentDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_anim);
//        studentUp.setFillAfter(true);
////        group = (ViewGroup) findViewById(R.id.container);
////        group.startAnimation(bottomUp);
////        group.setVisibility(View.VISIBLE);
//
//        teacherTrigger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                studentLayout.startAnimation(studentUp);
//                studentTrigger.setVisibility(View.VISIBLE);
//            }
//        });
//
//        studentTrigger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Login.this, "YOOO", Toast.LENGTH_SHORT).show();
//                studentLayout.startAnimation(studentDown);
//                studentUp.setFillAfter(false);
//            }
//        });

    }
}
