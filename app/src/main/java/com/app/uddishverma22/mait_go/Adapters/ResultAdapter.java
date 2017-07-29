package com.app.uddishverma22.mait_go.Adapters;

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

import com.app.uddishverma22.mait_go.Models.ResultHeader;
import com.app.uddishverma22.mait_go.Models.ResultModel;
import com.app.uddishverma22.mait_go.R;

import java.util.List;

/**
 * Created by uddishverma22 on 12/04/17.
 */

//TODO complete result shit
public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    List<ResultModel> list;
    ObjectAnimator mAnimator;
    ResultHeader header;

    int cx, cy;

    public ResultAdapter(List<ResultModel> list, ResultHeader resultHeader) {
        this.list = list;
        this.header = resultHeader;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_head, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_layout, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        holder.setIsRecyclable(false);

        if (holder instanceof VHHeader) {

            VHHeader VHheader = (VHHeader) holder;
            VHheader.cgpaTv.setTypeface(VHheader.tfThin);
            VHheader.cgpaTitle.setTypeface(VHheader.tfLight);
            VHheader.creditPercTv.setTypeface(VHheader.tfThin);
            VHheader.creditPercTitle.setTypeface(VHheader.tfLight);

            VHheader.univRankTitle.setTypeface(VHheader.tfLight);
            VHheader.univRankTv.setTypeface(VHheader.tfThin);
            VHheader.colRankTv.setTypeface(VHheader.tfThin);
            VHheader.colRankTitle.setTypeface(VHheader.tfLight);

            VHheader.cgpaTv.setText(header.cgpa);
            VHheader.creditPercTv.setText(header.creditPerc);

            VHheader.colRankTv.setText(header.colRank);
            VHheader.univRankTv.setText(header.univRank);
        } else if (holder instanceof VHItem) {

            ResultModel currentItem = getItem(position - 1);
            VHItem VHitem = (VHItem) holder;

            VHitem.progressBar.setProgressDrawable(VHitem.draw);

            VHitem.subName.setTypeface(VHitem.tf);
            VHitem.inMarks.setTypeface(VHitem.tfThin);
            VHitem.extMarks.setTypeface(VHitem.tfThin);
            VHitem.credits.setTypeface(VHitem.tfThin);
            VHitem.totMarks.setTypeface(VHitem.tf);
            VHitem.intHead.setTypeface(VHitem.tfLight);
            VHitem.extHead.setTypeface(VHitem.tfLight);
            VHitem.credHead.setTypeface(VHitem.tfLight);
            VHitem.totMarksHead.setTypeface(VHitem.tfLight);

            VHitem.subName.setText(currentItem.subName);
            VHitem.inMarks.setText(currentItem.intMarks);
            VHitem.extMarks.setText(currentItem.extMarks);
            VHitem.credits.setText(currentItem.credits);
            VHitem.totMarks.setText(currentItem.totMarks + "/100");
            //Animation for progress bar

            mAnimator = ObjectAnimator.ofInt(VHitem.progressBar, "progress", Integer.parseInt(currentItem.totMarks));
            mAnimator.setDuration(1000);     //0.5 sec
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.start();
        }

    }

    private ResultModel getItem(int position) {
        return list.get(position);
    }

    private class VHHeader extends RecyclerView.ViewHolder {

        TextView cgpaTv, cgpaTitle, creditPercTv, creditPercTitle;
        TextView univRankTv, univRankTitle, colRankTv, colRankTitle;
        Typeface tf, tfThin, tfLight;

        public VHHeader(View itemView) {
            super(itemView);
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

    private class VHItem extends RecyclerView.ViewHolder {


        TextView subName, inMarks, extMarks, totMarks, credits;
        TextView intHead, extHead, credHead, totMarksHead;                            //Headings to change their font
        ProgressBar progressBar;
        Drawable draw;
        Typeface tf, tfThin, tfLight;

        public VHItem(View itemView) {

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
            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Medium.ttf");
            tfThin = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Thin.ttf");
            tfLight = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Light.ttf");

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Medium.ttf");
            tfThin = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Thin.ttf");
            tfLight = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Raleway-Light.ttf");

        }
    }

    @Override

    public int getItemCount() {
        return list.size() + 1;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }


}
