package com.example.uddishverma22.mait_go.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by uddishverma22 on 02/05/17.
 */

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.detailsViewHolder>{

    List<Faculty> facultyList;

    public FacultyListAdapter(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView name, designation, qual, exp;

        public detailsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fac_name);
            designation = (TextView) itemView.findViewById(R.id.fac_desig);
            qual = (TextView) itemView.findViewById(R.id.fac_qual);
            exp = (TextView) itemView.findViewById(R.id.fac_exp);
        }
    }

    @Override
    public FacultyListAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faculty_layout, parent, false);
        return new detailsViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(FacultyListAdapter.detailsViewHolder holder, int position) {

        Faculty facultyObj = facultyList.get(position);
        holder.name.setText(facultyObj.name);
        holder.designation.setText(facultyObj.designation);
        holder.qual.setText(facultyObj.qualification);
        holder.exp.setText(facultyObj.experience);

    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }
}
