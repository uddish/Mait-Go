package com.example.uddishverma22.mait_go.Utils;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Activities.Login;
import com.example.uddishverma22.mait_go.Activities.MainActivity;
import com.example.uddishverma22.mait_go.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    Typeface tf;
    TextView title;
    public static FirebaseUser currentUser = null;
    private FirebaseAuth mAuth;

    private final Handler waitHandler = new Handler();
    private final Runnable waitCallback = new Runnable() {
        @Override
        public void run() {
            if (currentUser != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            else    {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        waitHandler.postDelayed(waitCallback, 2000);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/OpenSans-SemiBold.ttf");
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(tf);

    }

    @Override
    protected void onDestroy() {
        waitHandler.removeCallbacks(waitCallback);
        super.onDestroy();
    }
}
