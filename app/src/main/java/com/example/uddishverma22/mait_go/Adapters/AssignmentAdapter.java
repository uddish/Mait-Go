package com.example.uddishverma22.mait_go.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.uddishverma22.mait_go.Models.AssignmentModel;
import com.example.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

/**
 * Created by uddishverma22 on 06/05/17.
 */

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.detailsViewHolder> {

    List<AssignmentModel> list;

    public static final String TAG = "AssignmentAdapter";

    public AssignmentAdapter(List<AssignmentModel> notice) {
        this.list = notice;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder  {

        ImageView assignmentImage;
        TextView subject, teacher, lastDate, marks;
        TextView subHeading, teacherHeading, lastDateHeading, marksHeading;

        public detailsViewHolder(View itemView) {
            super(itemView);

            Typeface tf = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Regular.ttf");

            assignmentImage = (ImageView) itemView.findViewById(R.id.assign_img);
            subject = (TextView) itemView.findViewById(R.id.assign_subject);
            teacher = (TextView) itemView.findViewById(R.id.assign_teacher);
            lastDate = (TextView) itemView.findViewById(R.id.assign_lastdate);
            marks = (TextView) itemView.findViewById(R.id.assign_marks);

            subject.setTypeface(tf);
            teacher.setTypeface(tf);
            lastDate.setTypeface(tf);
            marks.setTypeface(tf);


            subHeading = (TextView) itemView.findViewById(R.id.sub_tv);
            teacherHeading = (TextView) itemView.findViewById(R.id.teacher_tv);
            lastDateHeading = (TextView) itemView.findViewById(R.id.lastdate_tv);
            marksHeading = (TextView) itemView.findViewById(R.id.marks_tv);

            marksHeading.setTypeface(tf);
            subHeading.setTypeface(tf);
            teacherHeading.setTypeface(tf);
            lastDateHeading.setTypeface(tf);
        }
    }


    @Override
    public AssignmentAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignment_layout, parent, false);
        return new AssignmentAdapter.detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignmentAdapter.detailsViewHolder holder, int position) {
        AssignmentModel obj = list.get(position);
        holder.subject.setText(obj.subject);
        holder.teacher.setText(obj.teacher);
        holder.lastDate.setText(obj.lastdate.substring(0, 10));
        holder.marks.setText(obj.marks);
        try {
            Picasso.with(holder.marks.getContext()).load(obj.images.getString(0)).into(holder.assignmentImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
