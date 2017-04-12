package com.example.uddishverma22.mait_go.Activities;

import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.uddishverma22.mait_go.R;

public class Result extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        progressBar = (ProgressBar) findViewById(R.id.prog_bar);
        Drawable draw = getResources().getDrawable(R.drawable.custom_progressbar);
        progressBar.setProgressDrawable(draw);
        progressBar.setProgress(50);

    }
}
