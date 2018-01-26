package com.n1technology.app.exampreparationnetwork.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.notice_date) TextView noticeDateTV;
    @BindView(R.id.notice_title) TextView noticeTitleTV;
    @BindView(R.id.notice_body) WebView noticeBodyWV;
    @BindView(R.id.notice_toolbar) Toolbar toolbar;
    String noticeDate, noticeTitle, noticeBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        toolbar.setTitle("Notice");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        noticeTitle = getIntent().getExtras().getString("title");
        noticeDate = getIntent().getExtras().getString("date");
        noticeBody = getIntent().getExtras().getString("body");
        noticeTitleTV.setText(noticeTitle);
        noticeDateTV.setText(noticeDate);
        noticeBodyWV.loadData(noticeBody, "text/html","UTF-8");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
