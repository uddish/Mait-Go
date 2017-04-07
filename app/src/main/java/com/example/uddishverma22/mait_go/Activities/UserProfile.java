package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uddishverma22.mait_go.R;

import rebus.bottomdialog.BottomDialog;


public class UserProfile extends AppCompatActivity {

    TextView name, roll, branch, semester, branchHeading, classHeading, semesterHeading;
    ImageView orangeTheme;
    public static int themeColor = 0;
    RelativeLayout relativeLayout;
    LinearLayout branchSelector, semesterSelector;
    BottomDialog dialog;
    Typeface tf;

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

        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);
        semesterSelector = (LinearLayout) findViewById(R.id.semester_selector);

        branchHeading = (TextView) findViewById(R.id.branch_heading);
        classHeading = (TextView) findViewById(R.id.class_heading);
        semesterHeading = (TextView) findViewById(R.id.semester_heading);
        branchHeading.setTypeface(tf);
        classHeading.setTypeface(tf);
        semesterHeading.setTypeface(tf);


        branchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchSelectorClicked();
            }
        });
        semesterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semesterSelectorClicked();
            }
        });


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

    private void branchSelectorClicked()    {

        dialog = new BottomDialog(UserProfile.this);
        dialog.title("Select your branch...");
        dialog.canceledOnTouchOutside(true);
        dialog.cancelable(true);
        dialog.inflateMenu(R.menu.branch_menu);
        dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
            @Override
            public boolean onItemSelected(int id) {
                switch (id) {
                    case R.id.cse:
                        branch.setText("CSE");
                        branch.setTypeface(tf);
                        return true;
                    case R.id.it:
                        branch.setText("IT");
                        branch.setTypeface(tf);
                        return true;
                    case R.id.ece:
                        branch.setText("ECE");
                        branch.setTypeface(tf);
                        return true;
                    case R.id.eee:
                        branch.setText("EEE");
                        branch.setTypeface(tf);
                        return true;
                    case R.id.mae:
                        branch.setText("MAE");
                        branch.setTypeface(tf);
                        return true;
                    case 100:
                        return false;
                    default:
                        return false;
                }
            }
        });
        dialog.show();
    }

    private void semesterSelectorClicked()  {
        dialog = new BottomDialog(UserProfile.this);
        dialog.title("Select your semester...");
        dialog.canceledOnTouchOutside(true);
        dialog.cancelable(true);
        dialog.inflateMenu(R.menu.semester_menu);
        dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
            @Override
            public boolean onItemSelected(int id) {
                switch (id) {
                    case R.id.first:
                        semester.setText("I");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.second:
                        semester.setText("II");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.third:
                        semester.setText("III");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.fourth:
                        semester.setText("IV");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.fifth:
                        semester.setText("V");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.sixth:
                        semester.setText("VI");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.seventh:
                        semester.setText("VII");
                        semester.setTypeface(tf);
                        return true;
                    case R.id.eighth:
                        semester.setText("VIII");
                        semester.setTypeface(tf);
                        return true;
                    case 100:
                        return false;
                    default:
                        return false;
                }
            }
        });
        dialog.show();
    }

}

