package com.example.uddishverma22.mait_go.Utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.uddishverma22.mait_go.Activities.MainActivity;
import com.example.uddishverma22.mait_go.R;

public class Splash extends AppCompatActivity {

    private final Handler waitHandler = new Handler();
    private final Runnable waitCallback = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        waitHandler.postDelayed(waitCallback, 2000);

    }

    @Override
    protected void onDestroy() {
        waitHandler.removeCallbacks(waitCallback);
        super.onDestroy();
    }
}
