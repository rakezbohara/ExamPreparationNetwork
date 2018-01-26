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
import com.n1technology.app.exampreparationnetwork.data.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/22/2017.
 */

public class SyllabusFragment extends Fragment {

    @BindView(R.id.no_syllabus_TV) TextView noSyllabusTV;
    @BindView(R.id.syllabus_RV) RecyclerView syllabusRV;
    String noOfSyllabus;
    SharedPref sharedPref;
    AdapterSyllabus adapterSyllabus;
    ActivityCommunicator activityCommunicator;
    private static SyllabusFragment syllabusFragment;

    public static SyllabusFragment getInstance(){
        if(syllabusFragment==null){
            syllabusFragment = new SyllabusFragment();
        }
        return syllabusFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_syllabus, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCommunicator.hideProfile();
        sharedPref = new SharedPref(getActivity());
        noOfSyllabus = sharedPref.getStringData("syllabus");
        if(!noOfSyllabus.equals("0")){
            noSyllabusTV.setVisibility(View.GONE);
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

    private void initializeRV(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapterSyllabus = new AdapterSyllabus(getActivity(), Integer.parseInt(noOfSyllabus));
        syllabusRV.setLayoutManager(layoutManager);
        syllabusRV.setAdapter(adapterSyllabus);
    }
}
