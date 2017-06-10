package com.example.uddishverma22.mait_go.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by uddishverma22 on 02/05/17.
 */

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.detailsViewHolder>{

    List<Faculty> facultyList;
    String imageUrl = "http://ec2-52-66-87-230.ap-south-1.compute.amazonaws.com/";

    public static final String TAG = "FacultyListAdapter";

    public FacultyListAdapter(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView name, designation, qual, exp;
        CircleImageView image;

        public detailsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fac_name);
            designation = (TextView) itemView.findViewById(R.id.fac_desig);
            qual = (TextView) itemView.findViewById(R.id.fac_qual);
            exp = (TextView) itemView.findViewById(R.id.fac_exp);
            image = (CircleImageView) itemView.findViewById(R.id.image);
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

        Picasso.with(holder.image.getContext()).load(imageUrl + facultyObj.imageUrl).into(holder.image);
        Log.d(TAG, "onBindViewHolder: " + facultyObj.imageUrl);

    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }
}
