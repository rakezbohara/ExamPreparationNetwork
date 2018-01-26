package com.n1technology.app.exampreparationnetwork.quiz;

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

import com.n1technology.app.exampreparationnetwork.FirebaseHandler.FirebaseHelper;
import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.data.DataRepository;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.n1technology.app.exampreparationnetwork.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 11/28/2017.
 */

public class LevelFragment extends Fragment {

    @BindView(R.id.level_RV) RecyclerView levelRV;

    AdapterLevel adapterLevel;
    List<ItemLevel> itemLevels;

    Integer currentLevel=0;
    Integer currentObtainedScore=0;
    Integer currentTotalScore=0;
    Integer totalLevel=0;
    SharedPref sharedPref;
    String courseId;
    String userId;
    DataRepository dataRepository;

    ActivityCommunicator activityCommunicator;

    DatabaseReference databaseReference;
    static LevelFragment levelFragment;
    public static LevelFragment getInstance(){
        if(levelFragment==null){
            levelFragment = new LevelFragment();
        }
        return levelFragment;
    }
    public LevelFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        dataRepository = DataRepository.getInstance();
        courseId = sharedPref.getStringData("course_id");
        userId = sharedPref.getStringData("uid");
        databaseReference = FirebaseHelper.getDatabase().getReference();
        updateMaxCourse();
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
    public void onPause() {
        super.onPause();
    }

    private void initializeRV() {
        itemLevels = new ArrayList<>();
        //totalLevel = dataRepository.getTotalLevel(courseId);
        for(int i = 0 ; i <= totalLevel ; i++){
            if(i<=currentLevel){
                itemLevels.add(new ItemLevel(String.valueOf(i),true));
            }else{
                itemLevels.add(new ItemLevel(String.valueOf(i),false));
            }
        }
        adapterLevel = new AdapterLevel(getActivity(), itemLevels, currentLevel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        levelRV.setLayoutManager(layoutManager);
        levelRV.setAdapter(adapterLevel);
        levelRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisible = llm.findFirstCompletelyVisibleItemPosition();
                if(firstVisible!=0){
                    activityCommunicator.hideProfile();
                }else{
                    activityCommunicator.showProfile();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void updateMaxCourse(){
        databaseReference.child("courses/"+courseId+"/max_level").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer maxLevel=0;
                Course course=null;
                maxLevel = Integer.valueOf(dataSnapshot.getValue(String.class));
                Log.d("From fragment", "data is "+maxLevel);
                totalLevel = maxLevel;
                updateCurrentLevel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*public void updateCurrentLevel(){
        databaseReference.child("scores/"+userId+"/"+courseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("level").getValue(String.class)!=null){
                    currentLevel = Integer.valueOf(dataSnapshot.child("level").getValue(String.class));
                }
                if(dataSnapshot.child("obtained_score").getValue(String.class)!=null){
                    currentObtainedScore = Integer.valueOf(dataSnapshot.child("obtained_score").getValue(String.class));
                }
                if(dataSnapshot.child("total_score").getValue(String.class)!=null){
                    currentTotalScore = Integer.valueOf(dataSnapshot.child("total_score").getValue(String.class));
                }
                Log.d("From fragment", "data is "+currentLevel+":"+currentObtainedScore);
                initializeRV();
                activityCommunicator.updateScoreandLevel(currentObtainedScore,currentTotalScore,currentLevel);
                loadPrimaryLevel();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
    public void updateCurrentLevel(){
        Log.d("from fragment","course id b "+courseId);
        databaseReference.child("scores/"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(courseId+"/level").getValue(String.class)!=null){
                    currentLevel = Integer.valueOf(dataSnapshot.child(courseId+"/level").getValue(String.class));
                }
                if(dataSnapshot.child("obtained_score").getValue(String.class)!=null){
                    currentObtainedScore = Integer.valueOf(dataSnapshot.child("obtained_score").getValue(String.class));
                }
                if(dataSnapshot.child("total_score").getValue(String.class)!=null){
                    currentTotalScore = Integer.valueOf(dataSnapshot.child("total_score").getValue(String.class));
                }
                Log.d("From fragment", "data is "+currentLevel+":"+currentObtainedScore);
                initializeRV();
                activityCommunicator.updateScoreandLevel(currentObtainedScore,currentTotalScore,currentLevel);
                loadPrimaryLevel();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadPrimaryLevel(){
        databaseReference = databaseReference.child("questions/"+courseId);
        int initialQuery = 1;
        if(totalLevel<5){
            initialQuery = totalLevel;
        }else{
            initialQuery = 5;
        }
        for(int i = currentLevel ; i < currentLevel+initialQuery ; i++){
            Query query = databaseReference.orderByChild("level").equalTo(String.valueOf(i));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot itemQuestion : dataSnapshot.getChildren()){
                        Log.d("from fragment","data is "+itemQuestion.getValue());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}
