package com.n1technology.app.exampreparationnetwork.question;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/1/2017.
 */

public class AdapterReviewAnswer extends RecyclerView.Adapter<AdapterReviewAnswer.ViewHolder> {

    private Context context;
    private List<String> answers;
    private Integer correntOption;
    private Integer selectedOption;
    private List<String> options = new ArrayList<>();

    public AdapterReviewAnswer(Context context, List<String> answers, Integer correntOption, Integer selectedOption) {
        this.context = context;
        this.answers = answers;
        this.correntOption = correntOption;
        this.selectedOption = selectedOption;
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("D");
        options.add("E");
        options.add("F");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String answer = answers.get(position);
        holder.answerOption.setText(answer);
        holder.answerOptionNo.setText(options.get(position));
        Log.d("data", " From adapet 1");
        if(selectedOption==-1){
            if(position==correntOption){
                holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.answer_true_back));
                holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.answer_true));
                holder.answerOption.setTextColor(context.getResources().getColor(R.color.answer_true));
                holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.white));
            }
        }else if(selectedOption==correntOption && position == selectedOption){
            holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.answer_true_back));
            holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.answer_true));
            holder.answerOption.setTextColor(context.getResources().getColor(R.color.answer_true));
            holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.white));
        }else if(selectedOption!=correntOption){
            if(position==selectedOption){
                holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.answer_false_back));
                holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.answer_false));
                holder.answerOption.setTextColor(context.getResources().getColor(R.color.answer_false));
                holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.white));
            }else if(position==correntOption){
                holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.answer_true_back));
                holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.answer_true));
                holder.answerOption.setTextColor(context.getResources().getColor(R.color.answer_true));
                holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.answer_option_layout)
        LinearLayout answerOptionLayout;
        @BindView(R.id.answer_option)
        TextView answerOption;
        @BindView(R.id.answer_option_no) TextView answerOptionNo;
        @BindView(R.id.answer_option_no_CV)
        CardView answerOptionNoCV;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }
}
