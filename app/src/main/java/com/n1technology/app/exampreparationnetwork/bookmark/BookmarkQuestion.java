package com.n1technology.app.exampreparationnetwork.bookmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Questions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkQuestion extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.bookmark_toolbar) Toolbar toolbar;
    @BindView(R.id.bookmark_question_TV) TextView questionTV;
    @BindView(R.id.bookmark_answer_RV) RecyclerView answerRV;
    @BindView(R.id.bookmark_question_previous) TextView previousTV;
    @BindView(R.id.bookmark_divider_previous) View dividerPrevious;
    @BindView(R.id.bookmark_divider_next) View dividerNext;
    @BindView(R.id.bookmark_question_next) TextView nextTV;

    List<Questions> questionsList;
    Integer currentQuestionNo;
    AdapterBookmarkAnswer adapterBookmarkAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bookmark_question);
        ButterKnife.bind(this);
        toolbar.setTitle("Bookmark Questions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        questionsList = Questions.listAll(Questions.class);
        currentQuestionNo = getIntent().getExtras().getInt("position");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        answerRV.setLayoutManager(layoutManager);
        Log.d("bookmark", "question data is "+currentQuestionNo);
        Log.d("bookmark", "question data is "+questionsList.get(currentQuestionNo).toString());
        initializeQuestion(questionsList.get(currentQuestionNo).getQuestion(), getAnswersList(), questionsList.get(currentQuestionNo).getCorrect());
        nextTV.setOnClickListener(this);
        previousTV.setOnClickListener(this);
    }

    private void initializeQuestion(String question, List<String> answers, String correct) {

        Log.d("bookmark", "question data is "+answers.toString());
        if(currentQuestionNo==0){
            togglePrevious(false);
        }else if(currentQuestionNo==questionsList.size()-1){
            toggleNext(false);
            togglePrevious(true);
        }else{
            toggleNext(true);
            togglePrevious(true);
        }
        questionTV.setText("Q. "+(currentQuestionNo+1)+". "+question);
        adapterBookmarkAnswer = new AdapterBookmarkAnswer(this, answers, Integer.parseInt(correct));
        answerRV.setAdapter(adapterBookmarkAnswer);
    }

    private void togglePrevious(boolean show){
        if(show){
            previousTV.setVisibility(View.VISIBLE);
            dividerPrevious.setVisibility(View.VISIBLE);
        }else{
            previousTV.setVisibility(View.GONE);
            dividerPrevious.setVisibility(View.GONE);
        }
    }

    private void toggleNext(boolean show){
        if(show){
            nextTV.setVisibility(View.VISIBLE);
            dividerNext.setVisibility(View.VISIBLE);
        }else{
            nextTV.setVisibility(View.GONE);
            dividerNext.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.bookmark_question_previous:
                currentQuestionNo--;
                initializeQuestion(questionsList.get(currentQuestionNo).getQuestion(), getAnswersList(), questionsList.get(currentQuestionNo).getCorrect());;
                break;
            case R.id.bookmark_question_next:
                currentQuestionNo++;
                initializeQuestion(questionsList.get(currentQuestionNo).getQuestion(), getAnswersList(), questionsList.get(currentQuestionNo).getCorrect());
                break;
            default:
                break;
        }
    }

    private List<String> getAnswersList(){
        List<String> answers = new ArrayList<>();
        answers.add(questionsList.get(currentQuestionNo).getOptiona());
        answers.add(questionsList.get(currentQuestionNo).getOptionb());
        answers.add(questionsList.get(currentQuestionNo).getOptionc());
        answers.add(questionsList.get(currentQuestionNo).getOptiond());
        return answers;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.menu_delete:
                deleteCurrentItem();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCurrentItem() {
        Questions questions = questionsList.get(currentQuestionNo);
        questions.delete();
        questionsList = Questions.listAll(Questions.class);
        Toast.makeText(this, "Question deleted...", Toast.LENGTH_SHORT).show();
        if(questionsList.size()==0){
            onBackPressed();
        }else if(currentQuestionNo==questionsList.size()){
            currentQuestionNo--;
            initializeQuestion(questionsList.get(currentQuestionNo).getQuestion(), getAnswersList(), questionsList.get(currentQuestionNo).getCorrect());
        }else{
            initializeQuestion(questionsList.get(currentQuestionNo).getQuestion(), getAnswersList(), questionsList.get(currentQuestionNo).getCorrect());
        }

    }
}
