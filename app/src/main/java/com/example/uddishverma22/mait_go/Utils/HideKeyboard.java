package com.example.uddishverma22.mait_go.Utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.uddishverma22.mait_go.MainActivity;

/**
 * Created by uddishverma on 13/06/17.
 */

public class HideKeyboard {

    public static void hideKeyboard(View root) {
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    View view = v.getRootView().findFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) MainActivity.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return false;
                }
                return false;
            }
        });
    }

}
