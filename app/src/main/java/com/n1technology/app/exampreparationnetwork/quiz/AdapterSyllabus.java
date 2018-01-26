package com.n1technology.app.exampreparationnetwork.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.syllabus.SyllabusActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/22/2017.
 */

public class AdapterSyllabus extends RecyclerView.Adapter<AdapterSyllabus.MyViewHolder> {

    Context context;
    int itemCount;

    public AdapterSyllabus(Context context, int itemCount) {
        this.context = context;
        this.itemCount = itemCount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_syllabus, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.syllabusName.setText("Paper "+(position+1));
        holder.syllabusNameNo.setText(String.valueOf(position+1));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SyllabusActivity.class);
                intent.putExtra("syllabus_no", position+1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.item_syllabus_name) TextView syllabusName;
        @BindView(R.id.item_syllabus_no) TextView syllabusNameNo;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
