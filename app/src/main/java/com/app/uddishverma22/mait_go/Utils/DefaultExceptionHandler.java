package com.app.uddishverma22.mait_go.Utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.app.uddishverma22.mait_go.Activities.MainActivity;

import java.util.Arrays;

/**
 * Created by uddishverma on 30/07/17.
 */

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("APP crashed", throwable);
        Log.d("ERROR", "---------" + throwable.getMessage());
        Log.d("ERROR", "--------" + throwable.getCause());
        Log.d("ERROR", "--------" + Arrays.toString(throwable.getStackTrace()));
        activity.startActivity(intent);
        activity.finish();

        System.exit(0);

    }
}
