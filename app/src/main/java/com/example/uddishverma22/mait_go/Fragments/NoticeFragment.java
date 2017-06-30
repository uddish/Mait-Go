package com.example.uddishverma22.mait_go.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uddishverma22.mait_go.Adapters.FacultyListAdapter;
import com.example.uddishverma22.mait_go.Adapters.NoticeAdapter;
import com.example.uddishverma22.mait_go.R;
import com.example.uddishverma22.mait_go.Utils.Globals;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by uddishverma on 30/06/17.
 */

public class NoticeFragment extends Fragment {

    private static final String TAG = "NoticeFragment";
    private View view;
    private String title;
    public static NoticeAdapter adapter;
    public static RecyclerView recyclerView;
    public static AVLoadingIndicatorView indicatorView;

    public NoticeFragment() {
    }

    public NoticeFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.notice_fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        indicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        indicatorView.show();

        setupRecycler();

        return view;
    }

    private void setupRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (title.equals("Notice")) {
            adapter = new NoticeAdapter(Globals.noticeList);
                indicatorView.hide();
        }
        else if(title.equals("Examination")) {
            adapter = new NoticeAdapter(Globals.noticeList);
            indicatorView.hide();
        }
        recyclerView.setAdapter(adapter);

    }

}