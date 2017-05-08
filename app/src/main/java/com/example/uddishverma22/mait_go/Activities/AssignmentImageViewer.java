package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.uddishverma22.mait_go.R;
import com.squareup.picasso.Picasso;

public class AssignmentImageViewer extends AppCompatActivity {

    Intent i;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_image_viewer);

        image = (ImageView) findViewById(R.id.assign_img);

        i = getIntent();

        Picasso.with(this).load(i.getStringExtra("imageUrl")).into(image);
    }
}
