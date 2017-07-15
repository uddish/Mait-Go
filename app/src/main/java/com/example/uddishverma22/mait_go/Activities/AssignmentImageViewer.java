package com.example.uddishverma22.mait_go.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class AssignmentImageViewer extends AppCompatActivity {

    public static final String TAG = "AssignmentImageViewer";

    Intent i;
    ListView assignmentList;
    DetailsListAdapter adapter;
    RequestQueue requestQueue;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_image_list);

        assignmentList = (ListView) findViewById(R.id.assign_list);

        i = getIntent();

        ArrayList<String> myList = (ArrayList<String>) i.getSerializableExtra("imageUrl");

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.assign_coord_layout);

        adapter = new DetailsListAdapter(myList);
        assignmentList.setAdapter(adapter);

    }

    private class DetailsListAdapter extends BaseAdapter {

        class DetailsViewHolder {
            ImageView imageView;
            ImageView downloadBtn;
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
                detailsViewHolder.downloadBtn = (ImageView) convertView.findViewById(R.id.download_img);
                convertView.setTag(detailsViewHolder);
            } else {
                detailsViewHolder = (DetailsViewHolder) convertView.getTag();
            }

            final String thisDetails = (String) getItem(position);

            detailsViewHolder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadImage(thisDetails);
                    Toast.makeText(AssignmentImageViewer.this, "Downloading", Toast.LENGTH_SHORT).show();
                }
            });

            Picasso.with(getApplicationContext()).load(thisDetails).into(detailsViewHolder.imageView);
            return convertView;
        }
    }

    private void downloadImage(final String url) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                saveImage(response, url);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Can not download image!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
        requestQueue.add(imageRequest);
    }

    private void saveImage(Bitmap bitmap, String url) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + url.substring(41) + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Image Downloaded!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
