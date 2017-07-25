package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.uddishverma22.mait_go.R;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class SplashIntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    private static final int REQUEST_CODE_INTRO = 9001;
    public static final String TAG = "SplashIntroActivity";
    public static final String EXTRA_FINISH_ENABLED = "com.heinrichreimersoftware.materialintro.EXTRA_FINISH_ENABLED";

    Boolean finishEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(true);
        setButtonNextVisible(true);
        setButtonCtaVisible(false);
        setFullscreen(true);
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == RESULT_OK) {
                // Finished the intro
                Log.d(TAG, "onActivityResult: finish" );
                startActivity(new Intent(SplashIntroActivity.this, MainActivity.class));
                finish();
            } else {
                // Cancelled the intro. You can then e.g. finish this activity too.
                finish();
                Log.d(TAG, "onActivityResult: Cancel");
            }
        }
    }
}
