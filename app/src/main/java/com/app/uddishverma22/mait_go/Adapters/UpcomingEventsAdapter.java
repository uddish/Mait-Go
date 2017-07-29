package com.app.uddishverma22.mait_go.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.uddishverma22.mait_go.Models.UpcomingEventsModel;
import com.app.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by uddishverma22 on 07/05/17.
 */

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingEventsAdapter.detailsViewHolder>{

    private List<UpcomingEventsModel> eventsList;

    public UpcomingEventsAdapter(List<UpcomingEventsModel> eventsList) {
        this.eventsList = eventsList;
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView name, date, society, organiser, organiserTitle, societyTitle;

        public detailsViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.event_img);
            name = (TextView) itemView.findViewById(R.id.event_name);
            society = (TextView) itemView.findViewById(R.id.society);
            date = (TextView) itemView.findViewById(R.id.event_date);
            organiser = (TextView) itemView.findViewById(R.id.organiser);
            organiserTitle = (TextView) itemView.findViewById(R.id.oraniser_head);
            societyTitle = (TextView) itemView.findViewById(R.id.society_head);


            Typeface tf = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Raleway-Regular.ttf");
            name.setTypeface(tf);
            society.setTypeface(tf);
            date.setTypeface(tf);
            organiser.setTypeface(tf);
            organiserTitle.setTypeface(tf);
            societyTitle.setTypeface(tf);
        }
    }

    @Override
    public UpcomingEventsAdapter.detailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_layout, parent, false);
        return new detailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UpcomingEventsAdapter.detailsViewHolder holder, int position) {

        UpcomingEventsModel model = eventsList.get(position);
        Picasso.with(holder.name.getContext()).load(model.imageUrl).into(holder.image);
        holder.name.setText(model.eventName);
        holder.society.setText(model.society);
        holder.organiser.setText(model.organiser);
        holder.date.setText(model.eventDate.substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
