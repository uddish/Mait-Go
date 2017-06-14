package com.example.uddishverma22.mait_go.Adapters;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uddishverma22.mait_go.Models.ResultModel;
import com.example.uddishverma22.mait_go.R;

import java.util.List;

/**
 * Created by uddishverma22 on 12/04/17.
 */

//TODO complete result shit
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
        Typeface tf, tfThin, tfLight;
        //TextViews for Result Headers
        TextView cgpaTv, cgpaTitle, creditPercTv, creditPercTitle;
        TextView univRankTv, univRankTitle, colRankTv, colRankTitle;

        public detailsViewHolder(View itemView) {
            super(itemView);
            subName = (TextView) itemView.findViewById(R.id.sub_name);
            inMarks = (TextView) itemView.findViewById(R.id.inter_marks);
            extMarks = (TextView) itemView.findViewById(R.id.exter_marks);
            totMarks = (TextView) itemView.findViewById(R.id.tot_marks);
            credits = (TextView) itemView.findViewById(R.id.credits);
            progressBar = (ProgressBar) itemView.findViewById(R.id.prog_bar);
            draw = itemView.getResources().getDrawable(R.drawable.custom_progressbar);
            intHead = (TextView) itemView.findViewById(R.id.int_head);
            extHead = (TextView) itemView.findViewById(R.id.ext_head);
            credHead = (TextView) itemView.findViewById(R.id.credits_head);
            totMarksHead = (TextView) itemView.findViewById(R.id.totalmarks_head);

            cgpaTv = (TextView) itemView.findViewById(R.id.cgpa);
            cgpaTitle = (TextView) itemView.findViewById(R.id.cgpa_heading);
            creditPercTv = (TextView) itemView.findViewById(R.id.credit_perc);
            creditPercTitle = (TextView) itemView.findViewById(R.id.creditp_heading);
            univRankTitle = (TextView) itemView.findViewById(R.id.uv_rank_heading);
            univRankTv = (TextView) itemView.findViewById(R.id.uv_rank);
            colRankTitle = (TextView) itemView.findViewById(R.id.col_rank_heading);
            colRankTv = (TextView) itemView.findViewById(R.id.col_rank);

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Medium.ttf");
            tfThin = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Thin.ttf");
            tfLight = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Light.ttf");

        }
    }

    @Override
    public ResultAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.result_list_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.result_head, parent, false);
        }
        return new detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.detailsViewHolder holder, int position) {
        ResultModel obj = list.get(position);

//        holder.setIsRecyclable(false);
        if (position != 0) {
            holder.progressBar.setProgressDrawable(holder.draw);
            holder.subName.setTypeface(holder.tf);
            holder.inMarks.setTypeface(holder.tfThin);
            holder.extMarks.setTypeface(holder.tfThin);
            holder.credits.setTypeface(holder.tfThin);
            holder.totMarks.setTypeface(holder.tf);
            holder.intHead.setTypeface(holder.tfLight);
            holder.extHead.setTypeface(holder.tfLight);
            holder.credHead.setTypeface(holder.tfLight);
            holder.totMarksHead.setTypeface(holder.tfLight);

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
        else    {
            holder.cgpaTv.setTypeface(holder.tfThin);
            holder.cgpaTitle.setTypeface(holder.tfLight);
            holder.creditPercTv.setTypeface(holder.tfThin);
            holder.creditPercTitle.setTypeface(holder.tfLight);

            holder.univRankTitle.setTypeface(holder.tfLight);
            holder.univRankTv.setTypeface(holder.tfThin);
            holder.colRankTv.setTypeface(holder.tfThin);
            holder.colRankTitle.setTypeface(holder.tfLight);

            holder.cgpaTv.setText(obj.cgpa);
            holder.creditPercTv.setText(obj.creditPerc);

            holder.colRankTv.setText(obj.colRank);
            holder.univRankTv.setText(obj.univRank);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return position;
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
