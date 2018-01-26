package com.n1technology.app.exampreparationnetwork.question;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
 * Created by RAKEZ on 11/29/2017.
 */

public class AdapterAnswer extends RecyclerView.Adapter<AdapterAnswer.ViewHolder> {

    Context context;
    List<String> answers;
    Integer row_index = -1;
    private List<String> options = new ArrayList<>();

    public AdapterAnswer(Context context, List<String> answers) {
        this.context = context;
        this.answers = answers;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String answer = answers.get(position);
        holder.answerOption.setText(answer);
        holder.answerOptionNo.setText(options.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.answer_true_back));
            holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.answer_true));
            holder.answerOption.setTextColor(context.getResources().getColor(R.color.answer_true));
            holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.answerOptionLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.answerOptionNoCV.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.answerOption.setTextColor(context.getResources().getColor(R.color.default_text_color));
            holder.answerOptionNo.setTextColor(context.getResources().getColor(R.color.default_text_color));
        }

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.answer_option_layout) LinearLayout answerOptionLayout;
        @BindView(R.id.answer_option) TextView answerOption;
        @BindView(R.id.answer_option_no) TextView answerOptionNo;
        @BindView(R.id.answer_option_no_CV) CardView answerOptionNoCV;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
            //Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/unicode.ttf");
            //answerOption.setTypeface(custom_font);
        }
    }

    public Integer getSelectedAnswer(){
        return row_index;
    }
}
