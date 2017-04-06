package com.example.uddishverma22.mait_go.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Activities.UserProfile;
import com.example.uddishverma22.mait_go.Models.Notice;
import com.example.uddishverma22.mait_go.R;

import java.util.List;

import static com.example.uddishverma22.mait_go.Activities.NoticeWebView.TAG;

/**
 * Created by uddishverma22 on 05/04/17.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.detailsViewHolder> {

    List<Notice> noticeList;

    public NoticeAdapter(List<Notice> notice) {
        this.noticeList = notice;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder  {

        private TextView notice;
        ImageView bulletColor;

        public detailsViewHolder(View itemView) {
            super(itemView);
            notice = (TextView) itemView.findViewById(R.id.notice);
            bulletColor = (ImageView) itemView.findViewById(R.id.bullets);
            Typeface tf = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Regular.ttf");
            notice.setTypeface(tf);
        }
    }


    @Override
    public NoticeAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list_layout, parent, false);
        return new detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.detailsViewHolder holder, int position) {

        Notice noticeObj = noticeList.get(position);
        holder.notice.setText(noticeObj.notice);
        if(UserProfile.themeColor == 101)   {
            Log.d(TAG, "onBindViewHolder: THEME CHANGED ");
            holder.bulletColor.setImageResource(R.drawable.yellow_circle_notice);
        }

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
