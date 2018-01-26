package com.n1technology.app.exampreparationnetwork.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Questions;
import com.n1technology.app.exampreparationnetwork.bookmark.BookmarkQuestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/12/2017.
 */

public class AdapterBookmark extends RecyclerView.Adapter<AdapterBookmark.MyViewHolder> {

    private Context context;
    private List<Questions> questionsList;

    public AdapterBookmark(Context context, List<Questions> questionsList) {
        this.context = context;
        this.questionsList = questionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark_question, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Questions question = questionsList.get(position);
        holder.question.setText(question.getQuestion());
        holder.questionNo.setText(String.valueOf(position+1));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, BookmarkQuestion.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        @BindView(R.id.item_bookmark_CV) CardView cardView;
        @BindView(R.id.item_bookmark_question_no) TextView questionNo;
        @BindView(R.id.item_bookmark_question) TextView question;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
    }
}
