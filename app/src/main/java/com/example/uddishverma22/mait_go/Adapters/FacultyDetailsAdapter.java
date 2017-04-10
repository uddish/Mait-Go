package com.example.uddishverma22.mait_go.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;

import java.util.ArrayList;

/**
 * Created by naman on 11-Apr-17.
 */

public class FacultyDetailsAdapter extends RecyclerView.Adapter<FacultyDetailsAdapter.FacultyDetailsViewHolder> {

    ArrayList<Faculty> facultyList;
    Context context;


    public FacultyDetailsAdapter(Context context, ArrayList<Faculty> facultyList) {
        this.context = context;
        this.facultyList = facultyList;
    }

    public void setFacultyList(ArrayList<Faculty> facultyList) {
        this.facultyList.clear();
        this.facultyList.addAll(facultyList);
        notifyDataSetChanged();
    }


    @Override
    public FacultyDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_layout_faculty, parent, false);
        return new FacultyDetailsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(FacultyDetailsViewHolder holder, int position) {

        holder.name.setText(facultyList.get(position).getName());
        holder.experience.setText(facultyList.get(position).getExperience());
        holder.qualification.setText(facultyList.get(position).getQualification());
        holder.designation.setText(facultyList.get(position).getDesignation());
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    public class FacultyDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView name, designation, qualification, experience;

        public FacultyDetailsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            designation = (TextView) itemView.findViewById(R.id.designation);
            qualification = (TextView) itemView.findViewById(R.id.qualification);
            experience = (TextView) itemView.findViewById(R.id.experience);
        }
    }
}
