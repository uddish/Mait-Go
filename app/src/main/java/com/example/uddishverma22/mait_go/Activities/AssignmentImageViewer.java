package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AssignmentImageViewer extends AppCompatActivity {

    public static final String TAG = "AssignmentImageViewer";

    Intent i;
    ListView assignmentList;
    DetailsListAdapter adapter;
    RequestQueue requestQueue;
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_image_list);

        assignmentList = (ListView) findViewById(R.id.assign_list);

        i = getIntent();

        ArrayList<String> myList = (ArrayList<String>) i.getSerializableExtra("imageUrl");

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        setToolbar();

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

//        FileOutputStream outStream = null;
//        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsolutePath() + "/ipugo");
//        dir.mkdirs();
//        String fileName = String.format("%d.jpg", System.currentTimeMillis());
//        File outFile = new File(dir, fileName);
//        if (outFile.exists ()) outFile.delete ();
//
//        try {
//            outFile.createNewFile();
//            outStream = new FileOutputStream(outFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//            refreshGallery(outFile);
//            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Image Downloaded!", Snackbar.LENGTH_SHORT);
//            snackbar.show();
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        FileOutputStream outStream = null;

        bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/ipugo");
        dir.mkdirs();
        File file = new File(dir + fileName);
        try {
            file.createNewFile();
            outStream = new FileOutputStream(file);
            outStream.write(bytearrayoutputstream.toByteArray());
            outStream.close();
            refreshGallery(file);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Image Downloaded!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_assign);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
