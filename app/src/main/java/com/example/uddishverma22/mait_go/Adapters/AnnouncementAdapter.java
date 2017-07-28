package com.example.uddishverma22.mait_go.Adapters;

import android.graphics.Typeface;
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

    public static final String TAG = "AnnouncementAdapter";

    public AnnouncementAdapter(ArrayList<ClassAnnouncementsModel> list) {
        this.list = list;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView teacherName, announ, time, date, imageLetter;
        Typeface openSansReg,openSansBold, openSansLight;

        public detailsViewHolder(View itemView) {
            super(itemView);
            teacherName = (TextView) itemView.findViewById(R.id.teacher_name);
            announ = (TextView) itemView.findViewById(R.id.announcement);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);
            imageLetter = (TextView) itemView.findViewById(R.id.img_letter);

            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Bold.ttf");
            openSansLight = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Light.ttf");

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
        try {

            holder.imageLetter.setText(model.teacherName.substring(0, 1));
            holder.date.setText(model.msgDate);
            holder.time.setText(model.msgTime);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        holder.teacherName.setTypeface(holder.openSansBold);
        holder.announ.setTypeface(holder.openSansReg);
        holder.imageLetter.setTypeface(holder.openSansBold);
        holder.date.setTypeface(holder.openSansLight);
        holder.time.setTypeface(holder.openSansLight);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
