package com.example.uddishverma22.mait_go.Adapters;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Activities.Result;
import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by uddishverma22 on 12/04/17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.detailsViewHolder> {

    private static final String TAG = "ResultAdapter";

    List<ResultModel> list;
    ObjectAnimator mAnimator;
    static int isProgressAnimated = 101;

    public ResultAdapter(List<ResultModel> list) {
        this.list = list;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder {

        TextView subName, inMarks, extMarks, totMarks, credits;
        TextView intHead, extHead, credHead, totMarksHead;                            //Headings to change their font
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

            intHead = (TextView) itemView.findViewById(R.id.int_head);
            extHead = (TextView) itemView.findViewById(R.id.ext_head);
            credHead = (TextView) itemView.findViewById(R.id.credits_head);
            totMarksHead = (TextView) itemView.findViewById(R.id.totalmarks_head);

            progressBar.setProgressDrawable(draw);
            Typeface tf = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Medium.ttf");
            Typeface tfThin = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Thin.ttf");
            Typeface tfLight = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Light.ttf");

            subName.setTypeface(tf);
            inMarks.setTypeface(tfThin);
            extMarks.setTypeface(tfThin);
            credits.setTypeface(tfThin);
            totMarks.setTypeface(tf);

            intHead.setTypeface(tfLight);
            extHead.setTypeface(tfLight);
            credHead.setTypeface(tfLight);
            totMarksHead.setTypeface(tfLight);
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

//        holder.setIsRecyclable(false);

        holder.subName.setText(obj.subName);
        holder.inMarks.setText(obj.intMarks);
        holder.extMarks.setText(obj.extMarks);
        holder.credits.setText(obj.credits);
        holder.totMarks.setText(obj.totMarks + "/100");
        //Animation for progress bar
        mAnimator = ObjectAnimator.ofInt(holder.progressBar, "progress", Integer.parseInt(obj.totMarks));
        mAnimator.setDuration(1000);     //0.5 sec
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.start();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
