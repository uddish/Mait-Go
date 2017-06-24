package com.example.uddishverma22.mait_go.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uddishverma22.mait_go.Activities.FacultyInformation;
import com.example.uddishverma22.mait_go.Adapters.FacultyListAdapter;
import com.example.uddishverma22.mait_go.Interface.FragmentInterface;
import com.example.uddishverma22.mait_go.Models.Faculty;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by uddishverma on 20/06/17.
 */

public class FacultyFragment extends Fragment {

    private static final String TAG = "FacultyFragment";
    private View view;
    private String title;
    ArrayList<Faculty> list;
    public static FacultyListAdapter adapter;

    public static RecyclerView recyclerView;

    public FacultyFragment() {
    }

    public FacultyFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.faculty_recyclerview_test, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setupRecycler();

        return view;
    }

    private void setupRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (title.equals("CSE")) {
            if (Globals.cseFacList != null && Globals.cseFacList.size() > 0)
                adapter = new FacultyListAdapter(Globals.cseFacList);
        } else {
            if (Globals.itFacList != null && Globals.itFacList.size() > 0)
                adapter = new FacultyListAdapter(Globals.itFacList);
        }
        recyclerView.setAdapter(adapter);

    }

}
