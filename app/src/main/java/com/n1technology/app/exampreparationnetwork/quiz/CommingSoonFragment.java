package com.n1technology.app.exampreparationnetwork.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n1technology.app.exampreparationnetwork.R;

import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/27/2017.
 */

public class CommingSoonFragment extends Fragment {

    ActivityCommunicator activityCommunicator;

    private static CommingSoonFragment commingSoonFragment;

    public static CommingSoonFragment getInstance(){
        if(commingSoonFragment==null){
            commingSoonFragment = new CommingSoonFragment();
        }
        return commingSoonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commingsoon, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCommunicator.hideProfile();
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
}
