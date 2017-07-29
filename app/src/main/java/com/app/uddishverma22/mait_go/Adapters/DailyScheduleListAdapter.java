package com.app.uddishverma22.mait_go.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.uddishverma22.mait_go.Models.DailySchedule;
import com.app.uddishverma22.mait_go.R;

import java.util.List;
import java.util.Random;

/**
 * Created by uddishverma22 on 04/04/17.
 */

public class DailyScheduleListAdapter extends RecyclerView.Adapter<DailyScheduleListAdapter.detailsViewHolder> {

    public List<DailySchedule> schedule;
    private int lastPosition = -1;

    public static final String TAG = "DailySchedule";

    public DailyScheduleListAdapter(List<DailySchedule> schedule) {
        this.schedule = schedule;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        private TextView time, subject, room, teacher;
        private ImageView dotColor;
        private LinearLayout mContainer;

        Typeface tfThin, tfLight, openSansReg, openSansBold, openSansLight;

        public detailsViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            subject = (TextView) itemView.findViewById(R.id.subject);
            room = (TextView) itemView.findViewById(R.id.room);
            teacher = (TextView) itemView.findViewById(R.id.teacher);
            dotColor = (ImageView) itemView.findViewById(R.id.dot_design);
            mContainer = (LinearLayout) itemView.findViewById(R.id.container);

            //Setting the fonts
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Bold.ttf");
        }
    }

    @Override
    public detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_schedule_layout, parent, false);
        return new detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(detailsViewHolder holder, int position) {

        setAnimation(holder.itemView, position);

        DailySchedule schedulelist = schedule.get(position);
        holder.time.setText(schedulelist.getTime());
        holder.subject.setText(schedulelist.getSubject());
        holder.room.setText(schedulelist.getRoom());
        holder.teacher.setText(schedulelist.getTeacher());

        holder.time.setTypeface(holder.openSansLight);
        holder.room.setTypeface(holder.openSansLight);
        holder.subject.setTypeface(holder.openSansBold);
        holder.teacher.setTypeface(holder.openSansReg);

    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
}
