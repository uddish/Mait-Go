package com.example.uddishverma22.mait_go.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.R;

public class UserProfile extends AppCompatActivity {

    TextView name, roll;
    ImageView orangeTheme;
    public static int themeColor = 0;
    RelativeLayout relativeLayout;

    /**
     * ORANGE -> 101
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextView) findViewById(R.id.name_tv);
        roll = (TextView) findViewById(R.id.enrollment_tv);
        orangeTheme = (ImageView) findViewById(R.id.orange_theme);
        relativeLayout = (RelativeLayout) findViewById(R.id.diagonal_view);

        if(themeColor == 101)   {
            relativeLayout.setBackgroundResource(R.drawable.diagonal_background_yellow);
        }

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Raleway-Regular.ttf");
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
