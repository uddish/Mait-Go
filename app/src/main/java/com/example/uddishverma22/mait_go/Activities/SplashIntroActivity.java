package com.example.uddishverma22.mait_go.Activities;

import android.os.Bundle;

import com.example.uddishverma22.mait_go.R;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class SplashIntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(true);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);


        addSlide(new SimpleSlide.Builder()
                .title("Daily Schedule")
                .description("Check your daily Timetable")
                .image(R.drawable.schedule_ss)
                .background(R.color.toryBlue)
                .backgroundDark(R.color.darkBlue)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Daily Schedule")
                .description("Check your daily Timetable")
                .image(R.drawable.schedule_ss)
                .background(R.color.midnightBlue)
                .backgroundDark(R.color.darkBlue)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Daily Schedule")
                .description("Check your daily Timetable")
                .image(R.drawable.schedule_ss)
                .background(R.color.resolutionBlue)
                .backgroundDark(R.color.darkBlue)
                .scrollable(false)
                .build());

    }
}
