package com.n1technology.app.exampreparationnetwork.selection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.n1technology.app.exampreparationnetwork.model.Course;
import com.n1technology.app.exampreparationnetwork.quiz.MainContainer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 11/27/2017.
 */

public class AdapterCourse extends RecyclerView.Adapter<AdapterCourse.MyViewHolder> {

    private Context context;
    private List<Course> itemCourses;
    SharedPref sharedPref;
    Integer row_index = -1;

    public AdapterCourse(Context context, List<Course> itemCourses) {
        this.context = context;
        this.itemCourses = itemCourses;
        sharedPref = new SharedPref(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        final Course itemCourse  =itemCourses.get(position);
        holder.bigLetterTV.setText(String.valueOf(position+1));
        holder.courseName.setText(itemCourse.getName());
        holder.maxLevelTV.setText(itemCourse.getMax_level()+" Levels");
        holder.courseName.setSelected(true);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                ((CourseSelector)context).enableNext(true);
            }
        });
        if(row_index==position && !itemCourse.getMax_level().equals("0")){
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.answer_true_back));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.answer_true));
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.answer_true));
            holder.bigLetterTV.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.default_text_color));
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.bigLetterTV.setTextColor(context.getResources().getColor(R.color.default_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return itemCourses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public View view;
        @BindView(R.id.item_course_CV) CardView  cardView;
        @BindView(R.id.item_course_big_letter) TextView bigLetterTV;
        @BindView(R.id.item_course_course_name) TextView courseName;
        @BindView(R.id.item_course_max_level) TextView maxLevelTV;
        @BindView(R.id.item_course_layout)
        RelativeLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
            //Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/unicode.ttf");
            //courseName.setTypeface(custom_font);
        }
    }

    public void nextCourse(){
        sharedPref.saveData("course_id",itemCourses.get(row_index).getId());
        sharedPref.saveData("course_name",itemCourses.get(row_index).getName());
        sharedPref.saveData("syllabus",itemCourses.get(row_index).getSyllabus());
        Log.d("Response from Course", itemCourses.get(row_index).toString());
        if(itemCourses.get(row_index).getMax_level().equals("0")){
            Toast.makeText(context,"This course currently does not have any level", Toast.LENGTH_SHORT).show();
        }else{
            Intent in = new Intent(context, MainContainer.class);
            context.startActivity(in);
            ((CourseSelector)context).finish();
        }
    }
}
