package com.example.uddishverma22.mait_go.Utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by uddishverma on 14/06/17.
 */

public abstract class RecyclerScroller extends RecyclerView.OnScrollListener {

    int scrollDist = 0;
    boolean isVisible = true;
    static final float MINIMUM = 25;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (isVisible && scrollDist > MINIMUM) {
            hide();
            scrollDist = 0;
            isVisible = false;
        } else if (!isVisible && scrollDist < -MINIMUM) {
            show();
            scrollDist = 0;
            isVisible = true;
        }
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }
    }

    public abstract void show();

    public abstract void hide();
}
