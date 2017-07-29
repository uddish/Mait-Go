package com.app.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.app.uddishverma22.mait_go.R;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class SplashIntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    public static final String TAG = "SplashIntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(true);
        setButtonNextVisible(true);
        setButtonCtaVisible(false);
        setFullscreen(true);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.toryBlue)
                .backgroundDark(R.color.midnightBlue)
                .fragment(R.layout.intro_fragment_one)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.resolutionBlue)
                .backgroundDark(R.color.midnightBlue)
                .fragment(R.layout.intro_fragment_two)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.midnightBlue)
                .backgroundDark(R.color.midnightBlue)
                .fragment(R.layout.intro_fragment_three)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.darkBlue)
                .backgroundDark(R.color.midnightBlue)
                .fragment(R.layout.intro_fragment_four)
                .build());

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
