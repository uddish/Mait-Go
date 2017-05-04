package com.example.uddishverma22.mait_go.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.ClassAnnouncementsModel;
import com.example.uddishverma22.mait_go.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uddishverma22 on 03/05/17.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.detailsViewHolder> {

    List<ClassAnnouncementsModel> list;

    public AnnouncementAdapter(ArrayList<ClassAnnouncementsModel> list) {
        this.list = list;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView teacherName, announ, time, date, imageLetter;

        public detailsViewHolder(View itemView) {
            super(itemView);
            teacherName = (TextView) itemView.findViewById(R.id.teacher_name);
            announ = (TextView) itemView.findViewById(R.id.announcement);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);
            imageLetter = (TextView) itemView.findViewById(R.id.img_letter);

        }
    }

    @Override
    public AnnouncementAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_layout, parent, false);
        return new detailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapter.detailsViewHolder holder, int position) {
        ClassAnnouncementsModel model = list.get(position);
        holder.teacherName.setText(model.teacherName);
        holder.announ.setText(model.announcement);
        holder.imageLetter.setText(model.teacherName.substring(0,1));
        holder.date.setText(model.msgDate.substring(0, 10));
        holder.time.setText(model.msgDate.substring(11, 16));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
