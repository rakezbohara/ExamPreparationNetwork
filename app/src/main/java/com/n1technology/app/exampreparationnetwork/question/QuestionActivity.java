package com.n1technology.app.exampreparationnetwork.question;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.n1technology.app.exampreparationnetwork.FirebaseHandler.FirebaseHelper;
import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Questions;
import com.n1technology.app.exampreparationnetwork.data.DataRepository;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.n1technology.app.exampreparationnetwork.model.Question;
import com.n1technology.app.exampreparationnetwork.quiz.MainContainer;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{


    private static String TAG = "Q/A Activity Log";
    @BindView(R.id.question_exit) TextView exitTV;
    @BindView(R.id.question_bookmark) ImageButton bookmarkQuestion;
    @BindView(R.id.question_timeout) TextView timeOutTV;
    @BindView(R.id.question_no_RV) RecyclerView questionNoRV;
    @BindView(R.id.question_TV) TextView questionTV;
    @BindView(R.id.answer_RV) RecyclerView answerRV;
    @BindView(R.id.question_next) TextView nextQuestionTV;
    @BindView(R.id.question_previous) TextView previousQuestionTV;
    @BindView(R.id.divider_previous) View dividerPrevious;
    @BindView(R.id.quiz_level) TextView levelTV;
    @BindView(R.id.question_banner_ad) AdView bannerAd;

    //Dialog componenets
    TextView dialogScoreTV;
    TextView dialogSkippedTV;
    TextView dialogAvgTimeTV;
    Button dialogReviewButton;
    Button dialogRetryButton;
    Button dialogNextLevelButton;
    Button dialogShareFB;
    ShareDialog shareDialog;
    CallbackManager callbackManager;

    DataRepository dataRepository;

    //Q/A elements
    List<String> questionId;
    List<String> questionList;
    List<List<String>> answersList;
    List<Integer> correctAnswer;
    //Current Question
    List<String> currentAnswerList;
    Integer currentCorrectAnswer;
    String currentQuestion;
    String currentQuestionId;
    SharedPref sharedPref;
    String courseId;
    String courseName;
    String userId;
    Integer obtainedScore;
    Integer totalScore;
    DatabaseReference databaseReference;

    Integer currentQuestionNo = 0;
    List<Integer> selectedAnswersList;

    RecyclerView.LayoutManager linearLayoutManager;
    AdapterAnswer adapterAnswer;
    AdapterReviewAnswer adapterReviewAnswer;

    Long answerTime;
    List<Long> answerTimeList;

    int currentLevel=0;
    //Timer
    CountDownTimer countDownTimer;

    public boolean isReview = false;
    public boolean isCompleted;
    private Dialog resultDialog;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);
        MobileAds.initialize(this, getResources().getString(R.string.banner_add_id));
        ButterKnife.bind(this);
        currentLevel = getIntent().getExtras().getInt("level");
        isCompleted = getIntent().getExtras().getBoolean("is_completed");
        displayBannerAd();
        loadIntrestitialAd();
        initialize();
        loadLevel();
        //initializeQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo),answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo));
        nextQuestionTV.setOnClickListener(this);
        previousQuestionTV.setOnClickListener(this);
        exitTV.setOnClickListener(this);
        bookmarkQuestion.setOnClickListener(this);
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/unicode.ttf");
        //questionTV.setTypeface(custom_font);
    }

    private void displayBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_add_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void loadIntrestitialAd(){
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_add_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void displayIntrestitialAd(){
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }else{
            if(!resultDialog.isShowing() && resultDialog!=null){
                resultDialog.show();
            }
        }
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(QuestionActivity.this,
                        "The interstitial is loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                if(!resultDialog.isShowing() && resultDialog!=null){
                    resultDialog.show();
                }
            }
        });
    }

    private void initialize() {
        //dataRepository = DataRepository.getInstance();
        questionId = new ArrayList<>();
        questionList = new ArrayList<>();
        answersList = new ArrayList<>();
        correctAnswer = new ArrayList<>();
        currentAnswerList = new ArrayList<>();
        selectedAnswersList = new ArrayList<>();
        answerTimeList = new ArrayList<>();
        sharedPref = new SharedPref(this);
        courseId = sharedPref.getStringData("course_id");
        courseName = sharedPref.getStringData("course_name");
        userId = sharedPref.getStringData("uid");
        obtainedScore = sharedPref.getIntData("obtained_score");
        totalScore = sharedPref.getIntData("total_score");
        levelTV.setText("Level : "+currentLevel);
        databaseReference = FirebaseHelper.getDatabase().getReference();
        countDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                answerTime = (30000-l)/1000;
                if(l<10000){
                    timeOutTV.setTextColor(Color.RED);
                    timeOutTV.setText("00:0"+l/1000);
                }else{
                    timeOutTV.setTextColor(Color.WHITE);
                    timeOutTV.setText("00:"+l/1000);
                }
            }
            @Override
            public void onFinish() {
                timeOutTV.setText("00:00");
                nextQuestion();
            }
        };
        /*questionId = dataRepository.getQuestionId(currentLevel);
        questionList = dataRepository.getQuestions(currentLevel);
        answersList = dataRepository.getAnswers(currentLevel);
        correctAnswer = dataRepository.getcorrectAnswers(currentLevel);*/
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        answerRV.setLayoutManager(linearLayoutManager);
    }

    private void initializeQuestion(String questionId, String question, List<String> answers, Integer correctAnswer) {
        Log.d(TAG,"question content: "+question);
        Log.d(TAG,"question content: "+answers.toString());
        Log.d(TAG,"question content: "+correctAnswer);
        currentQuestionId = questionId;
        currentQuestion = question;
        currentAnswerList = answers;
        currentCorrectAnswer = correctAnswer;
        questionTV.setText("Q. "+(currentQuestionNo+1)+". "+currentQuestion);
        adapterAnswer = new AdapterAnswer(this, currentAnswerList);
        answerRV.setAdapter(adapterAnswer);
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id){
            case R.id.question_exit:
                onBackPressed();
                break;
            case R.id.question_next:
                nextQuestion();
                break;
            case R.id.question_previous:
                previousQuestion();
                break;
            case R.id.question_bookmark:
                saveQuestionBookmark();
                break;
            case R.id.result_retry:
                if(resultDialog.isShowing()){
                    resultDialog.dismiss();
                }
                retryLevel();
                loadIntrestitialAd();
                break;
            case R.id.result_review:
                if(resultDialog.isShowing()){
                    resultDialog.dismiss();
                }
                reviewAnswers();
                loadIntrestitialAd();
                break;
            case R.id.result_next_level:
                intent = new Intent(this, MainContainer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            case R.id.dialog_share_to_fb:
                shareToFB();
                finish();
            default:
                break;
        }
    }



    private void saveQuestionBookmark() {
        //Questions.deleteAll(Questions.class);
        if(!questionExist(questionId.get(currentQuestionNo))){
            Questions questions = new Questions(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), String.valueOf(correctAnswer.get(currentQuestionNo)),String.valueOf(currentLevel), answersList.get(currentQuestionNo).get(0),answersList.get(currentQuestionNo).get(1),answersList.get(currentQuestionNo).get(2),answersList.get(currentQuestionNo).get(3));
            questions.save();
            Toast.makeText(this, "Question successfully bookmarked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Question already bookmarked", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean questionExist(String questionId){
        Log.d(TAG, "question id is "+questionId);
        List<Questions> questions = Questions.find(Questions.class, "questionid = ?", questionId);
        if(questions.size()>0){
            return true;
        }else{
            return false;
        }
    }


    private void reviewAnswers() {
        nextQuestionTV.setEnabled(true);
        isReview = true;
        tooglePrevious(true);
        currentQuestionNo = 0;
        timeOutTV.setText("00:00");
        initializeReviewQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo), correctAnswer.get(currentQuestionNo), selectedAnswersList.get(currentQuestionNo));
    }
    private void retryLevel(){
        nextQuestionTV.setEnabled(true);
        isReview = false;
        tooglePrevious(false);
        currentQuestionNo = 0;
        answerTimeList.clear();
        selectedAnswersList.clear();
        initializeQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo));
    }
    private void initializeReviewQuestion(String questionId, String question, List<String> answers, Integer correctAnswer, Integer selectedAnswer){
        Log.d(TAG,"question content: "+question);
        Log.d(TAG,"question content: "+answers.toString());
        Log.d(TAG,"question content: "+correctAnswer);
        Log.d(TAG,"question content: "+selectedAnswer);
        questionTV.setText("Q. "+(currentQuestionNo+1)+". "+question);
        adapterReviewAnswer = new AdapterReviewAnswer(this, answers, correctAnswer, selectedAnswer);
        answerRV.setAdapter(adapterReviewAnswer);
    }


    void nextQuestion(){
        if(isReview){
            currentQuestionNo++;
            if(currentQuestionNo==1){
                previousQuestionTV.setEnabled(true);
            }
            if(currentQuestionNo==19){
                nextQuestionTV.setText("FINISH");
                initializeReviewQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo), selectedAnswersList.get(currentQuestionNo));
            }else{
                nextQuestionTV.setText("NEXT");
                if(currentQuestionNo<20){
                    initializeReviewQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo), selectedAnswersList.get(currentQuestionNo));
                }else{
                    showResultDialog();
                    nextQuestionTV.setEnabled(false);
                }
            }
        }else{
            selectedAnswersList.add(adapterAnswer.getSelectedAnswer());
            answerTimeList.add(answerTime);
            currentQuestionNo++;
            if(currentQuestionNo==19){
                nextQuestionTV.setText("FINISH");
                initializeQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo));
            }else{
                nextQuestionTV.setText("NEXT");
                if(currentQuestionNo<20){
                    initializeQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo));
                }else{
                    countDownTimer.cancel();
                    showResultDialog();
                    nextQuestionTV.setEnabled(false);
                }
            }
        }
    }

    void previousQuestion(){
        if(isReview){
            if(currentQuestionNo!=0){
                currentQuestionNo--;
                initializeReviewQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo), answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo), selectedAnswersList.get(currentQuestionNo));
            }else{
                previousQuestionTV.setEnabled(false);
            }
        }
    }

    public void showResultDialog(){
        long sum=0;
        int correctAnswered=0;
        int skippedAnsweres=0;
        for(int i = 0 ; i < answerTimeList.size() ; i++){
            sum = sum + answerTimeList.get(i);
        }
        int avgTime = (int) (sum/answerTimeList.size());
        for(int i = 0 ; i < correctAnswer.size() ; i++){
            if(selectedAnswersList.get(i)==-1){
                skippedAnsweres++;
            }else if(selectedAnswersList.get(i)==correctAnswer.get(i)){
                correctAnswered++;
            }
        }
        if(!isReview && !isCompleted){
            updateScore(correctAnswered);
        }
        resultDialog = new Dialog(this);
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resultDialog.setContentView(R.layout.dialog_result);
        resultDialog.setCancelable(false);
        resultDialog.setCanceledOnTouchOutside(false);
        dialogScoreTV = resultDialog.findViewById(R.id.result_score);
        dialogSkippedTV = resultDialog.findViewById(R.id.result_skipped);
        dialogAvgTimeTV = resultDialog.findViewById(R.id.result_avg_time);
        dialogReviewButton = resultDialog.findViewById(R.id.result_review);
        dialogRetryButton = resultDialog.findViewById(R.id.result_retry);
        dialogNextLevelButton = resultDialog.findViewById(R.id.result_next_level);
        dialogShareFB = resultDialog.findViewById(R.id.dialog_share_to_fb);

        //setting values
        dialogScoreTV.setText(correctAnswered+"/20");
        dialogSkippedTV.setText("("+skippedAnsweres+" skipped"+")");
        if(avgTime<10){
            dialogAvgTimeTV.setText("00:0"+avgTime+" sec");
        }else{
            dialogAvgTimeTV.setText("00:"+avgTime+" sec");
        }
        if(correctAnswered==20){
            if(!isReview && !isCompleted){
                upgradeLevel();
            }
            dialogNextLevelButton.setVisibility(View.VISIBLE);
            dialogRetryButton.setVisibility(View.GONE);
            dialogShareFB.setVisibility(View.VISIBLE);
        }else {
            dialogNextLevelButton.setVisibility(View.GONE);
            dialogRetryButton.setVisibility(View.VISIBLE);
            dialogShareFB.setVisibility(View.GONE);
        }
        dialogReviewButton.setOnClickListener(this);
        dialogRetryButton.setOnClickListener(this);
        dialogNextLevelButton.setOnClickListener(this);
        dialogShareFB.setOnClickListener(this);
        displayIntrestitialAd();
    }

    private void tooglePrevious(Boolean show){
        if (show){
            previousQuestionTV.setVisibility(View.VISIBLE);
            dividerPrevious.setVisibility(View.VISIBLE);
        }else{
            previousQuestionTV.setVisibility(View.GONE);
            dividerPrevious.setVisibility(View.GONE);
        }
    }

    //Firebase Data
    public void loadLevel(){
        databaseReference = databaseReference.child("questions/"+courseId);
            Query query = databaseReference.orderByChild("level").equalTo(String.valueOf(currentLevel));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Question> questionList = new ArrayList<>();
                    for(DataSnapshot itemQuestion : dataSnapshot.getChildren()){
                        Log.d("from fragment","data is "+itemQuestion.getValue());
                        Question question = itemQuestion.getValue(Question.class);
                        questionList.add(question);
                    }
                    initializeLevelData(questionList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    public void initializeLevelData(List<Question> currentQuestionList){
        Collections.shuffle(currentQuestionList);
        for(int i = 0 ; i < 20 ; i++){
            questionId.add(currentQuestionList.get(i).getQuestion_id());
            questionList.add(currentQuestionList.get(i).getQuestion());
            answersList.add(currentQuestionList.get(i).getAnswers());
            correctAnswer.add(Integer.parseInt(currentQuestionList.get(i).getCorrect()));
        }
        initializeQuestion(questionId.get(currentQuestionNo), questionList.get(currentQuestionNo),answersList.get(currentQuestionNo),correctAnswer.get(currentQuestionNo));
    }

    public void upgradeLevel(){
        databaseReference = FirebaseHelper.getDatabase().getReference();
        int nextLevel = currentLevel+1;
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("/scores/"+userId+"/"+courseId+"/level",String.valueOf(nextLevel));
        databaseReference.updateChildren(updateData);
    }

    /*public void updateScore(Integer score){
        totalScore = totalScore + 20;
        obtainedScore = obtainedScore+score;
        databaseReference = FirebaseHelper.getDatabase().getReference();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("/scores/"+userId+"/"+courseId+"/obtained_score",String.valueOf(obtainedScore));
        updateData.put("/scores/"+userId+"/"+courseId+"/total_score",String.valueOf(totalScore));
        databaseReference.updateChildren(updateData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Log.d(TAG, "Event Occured "+databaseError.getMessage());
                }else{
                    Log.d(TAG, "Event Occured success "+" scores/"+userId+"/"+courseId);
                }
            }
        });
    }*/
    public void updateScore(Integer score){
        totalScore = totalScore + 20;
        obtainedScore = obtainedScore+score;
        databaseReference = FirebaseHelper.getDatabase().getReference();
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("/scores/"+userId+"/obtained_score",String.valueOf(obtainedScore));
        updateData.put("/scores/"+userId+"/total_score",String.valueOf(totalScore));
        databaseReference.updateChildren(updateData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Log.d(TAG, "Event Occured "+databaseError.getMessage());
                }else{
                    Log.d(TAG, "Event Occured success "+" scores/"+userId+"/"+courseId);
                }
            }
        });
    }

    public void shareToFB(){
        shareDialog = new ShareDialog(this);
        // this part is optional
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(TAG, result.toString());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, error.getLocalizedMessage());
            }});
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareHashtag hashTag = new ShareHashtag.Builder()
                    .setHashtag("#PlayAndLearn #EPN #ExamPreparationNepal")
                    .build();
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .setQuote("I have completed level "+(currentLevel+1)+" Level for "+courseName)
                    .setShareHashtag(hashTag)
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onPause() {
        if (resultDialog != null && resultDialog.isShowing()) {
            resultDialog.dismiss();
        }
        super.onPause();
    }

}
