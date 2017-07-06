package com.example.uddishverma22.mait_go.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AssignmentImageViewer extends AppCompatActivity {

    public static final String TAG = "AssignmentImageViewer";

    Intent i;
    ListView assignmentList;
    DetailsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_image_list);

        assignmentList = (ListView) findViewById(R.id.assign_list);

        i = getIntent();

        ArrayList<String> myList = (ArrayList<String>) i.getSerializableExtra("imageUrl");

        adapter = new DetailsListAdapter(myList);
        assignmentList.setAdapter(adapter);

    }

    private class DetailsListAdapter extends BaseAdapter {

        class DetailsViewHolder {
            ImageView imageView;
        }

        public DetailsListAdapter(ArrayList<String> mDetails) {
            this.mDetails = mDetails;
        }

        private ArrayList<String> mDetails;

        @Override
        public int getCount() {
            return mDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return mDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = getLayoutInflater();
            DetailsViewHolder detailsViewHolder;
            detailsViewHolder = new DetailsViewHolder();

            if (convertView == null) {
                convertView = li.inflate(R.layout.activity_assignment_image_viewer, null);
                detailsViewHolder.imageView = (ImageView) convertView.findViewById(R.id.assign_image);
                convertView.setTag(detailsViewHolder);
            }
            else    {
                detailsViewHolder = (DetailsViewHolder) convertView.getTag();
            }

            String thisDetails = (String) getItem(position);
            Picasso.with(getApplicationContext()).load(thisDetails).into(detailsViewHolder.imageView);
            return convertView;
        }
    }
}
