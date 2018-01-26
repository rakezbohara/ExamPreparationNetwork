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
 * Created by RAKEZ on 12/21/2017.
 */

public class AboutUsFragment extends Fragment {

    @BindView(R.id.about_us_WV)
    WebView aboutUsWV;

    ActivityCommunicator activityCommunicator;
    private static AboutUsFragment aboutUsFragment;

    public static AboutUsFragment getInstance(){
        if(aboutUsFragment==null){
            aboutUsFragment = new AboutUsFragment();
        }
        return aboutUsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCommunicator.hideProfile();
        String data = "<p>EPN is Exam Preparation Network a Product of <b>N1technology Pvt. Ltd.</b>, a team focused on providing various kind of web and mobile based services to people.</p>\n" +
                "<p>For any queries, contact us via feedback form or write us at <p style=\"color:#3498db\"><u>epn@n1technology.com</u></p><br />\n" +
                "Developed and Maintained By</p>\n" +
                "<p><strong>N1technology Pvt. Ltd.<br />\n" +
                "Shantinaggar, Kathmandu<br/>Email:<span style=\"color:#3498db\"><u>admin@n1technology.com</u></span><br />\n" +
                "Web:<span style=\"color:#3498db\"><u>http://www.n1technology.com</u></span></strong></p>";
        aboutUsWV.loadData(data,"text/html","UTF-8");
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
