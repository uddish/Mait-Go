package com.example.uddishverma22.mait_go.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.uddishverma22.mait_go.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomNavDrawer extends Fragment {

    public static LinearLayout profileLayout, resultLayout, noticeLayout, announcLayout, assignLayout, eventsLayout, facultyLayout;
    public static CircleImageView logoutBtn;

    public CustomNavDrawer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_custom_nav_drawer, container, false);

        profileLayout = (LinearLayout) view.findViewById(R.id.profile_ll);
        resultLayout = (LinearLayout) view.findViewById(R.id.result_ll);
        noticeLayout = (LinearLayout) view.findViewById(R.id.notice_ll);
        announcLayout = (LinearLayout) view.findViewById(R.id.announcement_ll);
        assignLayout = (LinearLayout) view.findViewById(R.id.assignment_ll);
        eventsLayout = (LinearLayout) view.findViewById(R.id.events_ll);
        facultyLayout = (LinearLayout) view.findViewById(R.id.faculty_ll);
        logoutBtn = (CircleImageView) view.findViewById(R.id.logout);

        return view;
    }

}
