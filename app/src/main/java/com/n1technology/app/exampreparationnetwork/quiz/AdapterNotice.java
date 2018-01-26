package com.n1technology.app.exampreparationnetwork.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Notice;
import com.n1technology.app.exampreparationnetwork.notice.NoticeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/28/2017.
 */

public class AdapterNotice extends RecyclerView.Adapter<AdapterNotice.MyViewHolder> {

    private Context context;
    private List<Notice> noticeList;

    public AdapterNotice(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Notice itemNotice = noticeList.get(position);
        holder.noticeName.setText(itemNotice.getNoticeTitle());
        holder.noticeDate.setText(itemNotice.getNoticeDate());
        holder.noticeNo.setText(String.valueOf(position+1));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeActivity.class);
                intent.putExtra("title", itemNotice.getNoticeTitle());
                intent.putExtra("date", itemNotice.getNoticeDate());
                intent.putExtra("body", itemNotice.getNoticeBody());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.item_notice_name) TextView noticeName;
        @BindView(R.id.item_notice_no) TextView noticeNo;
        @BindView(R.id.item_notice_date) TextView noticeDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
