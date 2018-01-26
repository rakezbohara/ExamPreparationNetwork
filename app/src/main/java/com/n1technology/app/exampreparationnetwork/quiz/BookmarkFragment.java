package com.n1technology.app.exampreparationnetwork.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Questions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/12/2017.
 */

public class BookmarkFragment extends Fragment {

    @BindView(R.id.bookmark_question_RV) RecyclerView bookmarkQuestionRV;
    List<Questions> questionsList;
    ActivityCommunicator activityCommunicator;
    AdapterBookmark adapterBookmark;
    static BookmarkFragment bookmarkFragment;

    public static BookmarkFragment getInstance(){
        if(bookmarkFragment==null){
            bookmarkFragment = new BookmarkFragment();
        }
        return bookmarkFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        questionsList = Questions.listAll(Questions.class);
        Log.d("from bookmark", "size is "+questionsList.size());
        initializeRV();
        activityCommunicator.hideProfile();
    }

    private void initializeRV() {
        adapterBookmark = new AdapterBookmark(getActivity(), questionsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bookmarkQuestionRV.setLayoutManager(layoutManager);
        bookmarkQuestionRV.setAdapter(adapterBookmark);
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

    @Override
    public void onResume() {
        questionsList = Questions.listAll(Questions.class);
        Log.d("from bookmark", "size is "+questionsList.size());
        initializeRV();
        super.onResume();
    }
}
