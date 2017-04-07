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

    TextView name, roll, branch, branchHeading, classHeading, semesterHeading;
    ImageView orangeTheme;
    public static int themeColor = 0;
    RelativeLayout relativeLayout;
    LinearLayout branchSelector;
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
        branchSelector = (LinearLayout) findViewById(R.id.branch_selector);

        branchHeading = (TextView) findViewById(R.id.branch_heading);
        classHeading = (TextView) findViewById(R.id.class_heading);
        semesterHeading = (TextView) findViewById(R.id.semester_heading);
        branchHeading.setTypeface(tf);
        classHeading.setTypeface(tf);
        semesterHeading.setTypeface(tf);


        branchSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                return true;
                            case R.id.ece:
                                branch.setText("ECE");
                                return true;
                            case R.id.eee:
                                branch.setText("EEE");
                                return true;
                            case R.id.mae:
                                branch.setText("MAE");
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

}

