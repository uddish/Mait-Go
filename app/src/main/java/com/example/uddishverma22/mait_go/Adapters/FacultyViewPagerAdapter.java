package com.example.uddishverma22.mait_go.Adapters;

/**
 * Created by naman on 11-Apr-17.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.uddishverma22.mait_go.R;

import java.util.ArrayList;



public class FacultyViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Integer> imageId;

    LayoutInflater inflater;

    public FacultyViewPagerAdapter(Context context,ArrayList<Integer> imageId) {
        this.context = context;
        this.imageId=imageId;
    }

    @Override
    public int getCount() {
        return imageId.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView iv;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.view_pager_single_page_faculty, container, false);

        // Locate the ImageView in viewpager_item.xml

        iv= (ImageView) itemView.findViewById(R.id.view_pager_faculty_iv);

        // Capture position and set to the TextViews
        iv.setImageResource(imageId.get(position));

        // Locate the ImageView in viewpager_item.xml


        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}