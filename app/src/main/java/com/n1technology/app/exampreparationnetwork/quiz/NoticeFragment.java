package com.n1technology.app.exampreparationnetwork.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Notice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/28/2017.
 */

public class NoticeFragment extends Fragment {
    ActivityCommunicator activityCommunicator;
    private static NoticeFragment noticeFragment;
    List<Notice> noticeList;
    AdapterNotice adapterNotice;

    @BindView(R.id.no_notice_TV) TextView noNoticeTV;
    @BindView(R.id.notice_RV) RecyclerView noticeRV;

    public static NoticeFragment getInstance(){
        if(noticeFragment==null){
            noticeFragment = new NoticeFragment();
        }
        return noticeFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCommunicator.hideProfile();
        noticeList = Notice.listAll(Notice.class);
        if(noticeList.size()>0){
            noNoticeTV.setVisibility(View.GONE);
            initializeRV();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCommunicator = (ActivityCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void initializeRV(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        adapterNotice = new AdapterNotice(getActivity(), noticeList);
        noticeRV.setLayoutManager(layoutManager);
        noticeRV.setAdapter(adapterNotice);
    }
}
