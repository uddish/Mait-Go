package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.R;


public class UserProfile extends AppCompatActivity{

    TextView name, roll, branch, semester,className, branchHeading, classHeading, semesterHeading;
    ImageView orangeTheme;
    public static int themeColor = 0;
    RelativeLayout relativeLayout;
    LinearLayout branchSelector, semesterSelector, classSelector;
    Typeface tf;

    AlertDialog.Builder alert;
    View alertLayout;
    TextView alertCs, alertIt, alertEce, alertEee, alertMae;            //tv for the popup branch menu
    TextView first, second, third, fourth, fifth, sixth, seventh, eighth;            //tv for the popup semester menu
    EditText classEditText; TextView classAlertHeading; ImageView classImg;                            //tv and et for the popup class menu


    /**
     * ORANGE -> 101
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Raleway-Regular.ttf");

        name = (TextView) findViewById(R.id.name_tv);
        roll = (TextView) findViewById(R.id.enrollment_tv);
        orangeTheme = (ImageView) findViewById(R.id.orange_theme);
        relativeLayout = (RelativeLayout) findViewById(R.id.diagonal_view);


        branch = (TextView) findViewById(R.id.branch_tv);
        semester = (TextView) findViewById(R.id.semester_tv);
        className = (TextView) findViewById(R.id.class_tv);
        branch.setTypeface(tf);
        semester.setTypeface(tf);
        className.setTypeface(tf);


        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);
        semesterSelector = (LinearLayout) findViewById(R.id.semester_selector);
        classSelector = (LinearLayout) findViewById(R.id.class_selector);


        branchHeading = (TextView) findViewById(R.id.branch_heading);
        classHeading = (TextView) findViewById(R.id.class_heading);
        semesterHeading = (TextView) findViewById(R.id.semester_heading);
        branchHeading.setTypeface(tf);
        classHeading.setTypeface(tf);
        semesterHeading.setTypeface(tf);

        //Custom alert dialog to pick the branch
        alert = new AlertDialog.Builder(UserProfile.this);


        branchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBranchDialog();
            }
        });
        semesterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertSemesterDialog();
            }
        });
        classSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertClassDialog();
            }
        });


        //Checking for theme
        if(themeColor == 101)   {
            relativeLayout.setBackgroundResource(R.drawable.diagonal_background_yellow);
        }

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Raleway-Regular.ttf");
        name.setTypeface(tf);

        orangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themeColor = 101;
                relativeLayout.setBackgroundResource(R.drawable.diagonal_background_yellow);

            }
        });
    }

    private void alertBranchDialog()    {

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.branch_selector_layout, null);
        alert.setView(alertLayout);

        alertCs = (TextView) alertLayout.findViewById(R.id.alert_cs);
        alertIt = (TextView) alertLayout.findViewById(R.id.alert_it);
        alertEce = (TextView) alertLayout.findViewById(R.id.alert_ece);
        alertEee = (TextView) alertLayout.findViewById(R.id.alert_eee);
        alertMae = (TextView) alertLayout.findViewById(R.id.alert_mae);

        final AlertDialog dialog = alert.create();
        dialog.show();

        alertCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertCs.getText().toString());
                dialog.dismiss();
            }
        });
        alertIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertIt.getText().toString());
                dialog.dismiss();
            }
        });
        alertEce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertEce.getText().toString());
                dialog.dismiss();
            }
        });
        alertEee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertEee.getText().toString());
                dialog.dismiss();
            }
        });
        alertMae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setText(alertMae.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void alertSemesterDialog()    {

        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.semester_selector_layout, null);
        alert.setView(alertLayout);

        first = (TextView) alertLayout.findViewById(R.id.alert_first);
        second = (TextView) alertLayout.findViewById(R.id.alert_second);
        third = (TextView) alertLayout.findViewById(R.id.alert_third);
        fourth = (TextView) alertLayout.findViewById(R.id.alert_fourth);
        fifth = (TextView) alertLayout.findViewById(R.id.alert_fifth);
        sixth = (TextView) alertLayout.findViewById(R.id.alert_sixth);
        seventh = (TextView) alertLayout.findViewById(R.id.alert_seventh);
        eighth = (TextView) alertLayout.findViewById(R.id.alert_eighth);

        final AlertDialog dialog = alert.create();
        dialog.show();

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(first.getText().toString());
                dialog.dismiss();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(second.getText().toString());
                dialog.dismiss();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(third.getText().toString());
                dialog.dismiss();
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fourth.getText().toString());
                dialog.dismiss();
            }
        });
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(fifth.getText().toString());
                dialog.dismiss();
            }
        });
        sixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(sixth.getText().toString());
                dialog.dismiss();
            }
        });
        seventh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(seventh.getText().toString());
                dialog.dismiss();
            }
        });
        eighth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semester.setText(eighth.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void alertClassDialog() {
        LayoutInflater inflater = getLayoutInflater();
        alertLayout = inflater.inflate(R.layout.class_selector_layout, null);
        alert.setView(alertLayout);

        classAlertHeading = (TextView) alertLayout.findViewById(R.id.alert_class_heading);
        classEditText = (EditText) alertLayout.findViewById(R.id.alert_class);
        classImg = (ImageView) alertLayout.findViewById(R.id.class_img);

        final AlertDialog dialog = alert.create();
        dialog.show();

        classImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                className.setText(classEditText.getText().toString());
            }
        });

    }

}

