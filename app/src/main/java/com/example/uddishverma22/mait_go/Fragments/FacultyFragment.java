package com.example.uddishverma22.mait_go.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uddishverma22.mait_go.R;

/**
 * Created by uddishverma on 20/06/17.
 */

public class FacultyFragment extends Fragment {

    private View view;
    private String title;

    private static RecyclerView recyclerView;

    public FacultyFragment() {
    }

    public FacultyFragment(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faculty_recyclerview_test, container, false);
        setupRecycler();
        return view;
    }

    private void setupRecycler() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
