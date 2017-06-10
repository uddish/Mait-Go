package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.uddishverma22.mait_go.MainActivity;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Preferences;

import io.fabric.sdk.android.Fabric;

public class Login extends AppCompatActivity {

    EditText name, roll, section;
    Button signinBtn;

    public static String NAME = null;
    public static String ENROLL_NO = null;
    public static String SECTION = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Preferences.getPrefs("isLogin", getApplicationContext()).equals("YES"))  {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        Fabric.with(this, new Crashlytics());

        name = (EditText) findViewById(R.id.name);
        roll = (EditText) findViewById(R.id.enroll);
        section = (EditText) findViewById(R.id.section);
        signinBtn = (Button) findViewById(R.id.submit_btn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NAME = name.getText().toString();
                ENROLL_NO = roll.getText().toString();
                SECTION = section.getText().toString();
                checkForNullValues();

            }
        });

    }

    private void checkForNullValues() {
        if(name.getText().toString().equals("") || roll.getText().toString().equals("") || section.getText().toString().equals(""))    {
            Toast.makeText(this, "Fill the details", Toast.LENGTH_SHORT).show();
        }
        else    {
            Preferences.setPrefs("studentName", NAME, getApplicationContext());
            Preferences.setPrefs("studentRollNo", ENROLL_NO, getApplicationContext());
            Preferences.setPrefs("studentSection", SECTION, getApplicationContext());
            //Setting in the prefs if the student is registered or not
            Preferences.setPrefs("isLogin", "YES", getApplicationContext());
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
