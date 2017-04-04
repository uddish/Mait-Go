package com.example.uddishverma22.mait_go.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.DailySchedule;
import com.example.uddishverma22.mait_go.R;

import java.util.List;

/**
 * Created by uddishverma22 on 04/04/17.
 */

public class DailyScheduleListAdapter extends RecyclerView.Adapter<DailyScheduleListAdapter.detailsViewHolder> {

    public List<DailySchedule> schedule;

    public DailyScheduleListAdapter(List<DailySchedule> schedule) {
        this.schedule = schedule;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        private TextView time, subject, room;
        private ImageView dotColor;

        public detailsViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            subject = (TextView) itemView.findViewById(R.id.subject);
            room = (TextView) itemView.findViewById(R.id.room);
            dotColor = (ImageView) itemView.findViewById(R.id.dot_design);
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
        DailySchedule schedulelist = schedule.get(position);
        holder.time.setText(schedulelist.getTime());
        holder.subject.setText(schedulelist.getSubject());
        holder.room.setText(schedulelist.getRoom());
        //Theme based fields
//        holder.dotColor.setImageResource(R.drawable.green_circle);
    }


    @Override
    public int getItemCount() {
        return schedule.size();
    }
}
