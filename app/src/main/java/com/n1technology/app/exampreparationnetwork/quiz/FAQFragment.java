package com.n1technology.app.exampreparationnetwork.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.n1technology.app.exampreparationnetwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 1/2/2018.
 */

public class FAQFragment extends Fragment {

    @BindView(R.id.faq_WV) WebView faqWV;
    ActivityCommunicator activityCommunicator;
    private static FAQFragment faqFragment;

    public static FAQFragment getInstance(){
        if(faqFragment==null){
            faqFragment = new FAQFragment();
        }
        return faqFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCommunicator.hideProfile();
        faqWV.loadUrl("http://epn.n1technology.com/faq/");
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
