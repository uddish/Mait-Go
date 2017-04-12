package com.example.uddishverma22.mait_go.Adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Activities.Result;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;

import java.util.List;

/**
 * Created by uddishverma22 on 12/04/17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.detailsViewHolder> {

    private static final String TAG = "ResultAdapter";

    List<ResultModel> list;

    public ResultAdapter(List<ResultModel> list) {
        this.list = list;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView subName, inMarks, extMarks, totMarks, credits;
        ProgressBar progressBar;
        Drawable draw;

        public detailsViewHolder(View itemView) {
            super(itemView);
            subName = (TextView) itemView.findViewById(R.id.sub_name);
            inMarks = (TextView) itemView.findViewById(R.id.inter_marks);
            extMarks = (TextView) itemView.findViewById(R.id.exter_marks);
            totMarks = (TextView) itemView.findViewById(R.id.tot_marks);
            credits = (TextView) itemView.findViewById(R.id.credits);
            progressBar = (ProgressBar) itemView.findViewById(R.id.prog_bar);
            draw  = itemView.getResources().getDrawable(R.drawable.custom_progressbar);
            progressBar.setProgressDrawable(draw);
        }
    }

    @Override
    public ResultAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_list_layout, parent, false);
        return new detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.detailsViewHolder holder, int position) {
        ResultModel obj = list.get(position);
        holder.subName.setText(obj.subName);
        holder.inMarks.setText(obj.intMarks);
        holder.extMarks.setText(obj.extMarks);
        holder.credits.setText(obj.credits);
        holder.totMarks.setText(obj.totMarks + "/100");
        holder.progressBar.setProgress(Integer.parseInt(obj.totMarks));

        Log.d(TAG, "onBindViewHolder: " + list);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
