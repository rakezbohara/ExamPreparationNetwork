package com.n1technology.app.exampreparationnetwork.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.EntryPointActivity;
import com.n1technology.app.exampreparationnetwork.FirebaseHandler.FirebaseHelper;
import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.SugarModel.Notice;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.n1technology.app.exampreparationnetwork.model.LeaderBoard;
import com.n1technology.app.exampreparationnetwork.selection.CourseSelector;
import com.n1technology.app.exampreparationnetwork.util.ImageUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainContainer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCommunicator, View.OnClickListener {

    private static String TAG = "Main Caontainer";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    View navHeaderView;
    ImageView navImage;
    TextView navName;
    TextView navEmail;

    SharedPref sharedPref;
    ImageUtils imageUtils;

    //Elements of profile
    @BindView(R.id.profile_layout) RelativeLayout profileLayout;
    @BindView(R.id.profile_image_IV) ImageView profileImage;
    @BindView(R.id.display_name_TV) TextView userNameTV;
    @BindView(R.id.course_name_TV) TextView courseNameTV;
    @BindView(R.id.score_val_TV) TextView scoreValueTV;
    @BindView(R.id.level_val_TV) TextView levelValueTV;
    @BindView(R.id.scoreboard_fab) FloatingActionButton scoreboardFAB;
    @BindView(R.id.theory_fab) FloatingActionButton theoryFAB;
    String userName, courseId, courseName, uId, email, loginProvider;
    Bitmap userImage;
    boolean imageExist;

    //Signout elements
    FirebaseAuth firebaseAuth;

    //Fragment elements
    FragmentTransaction fragmentTransaction;

    DatabaseReference databaseReference;

    //LeaderBoard Component
    Dialog leaderDialog;
    RecyclerView dialogLeaderRV;
    Button dialogLeaderDone;
    List<LeaderBoard> listLeaderBoard;
    AdapterLeader adapterLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_container);
        ButterKnife.bind(this);
        scoreboardFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab1)));
        theoryFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab2)));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_quiz);
        navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main_container);
        navImage = navHeaderView.findViewById(R.id.nav_image);
        navName = navHeaderView.findViewById(R.id.nav_name);
        navEmail = navHeaderView.findViewById(R.id.nav_email);
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(this);
        imageUtils = new ImageUtils();
        databaseReference = FirebaseHelper.getDatabase().getReference();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, LevelFragment.getInstance());
        fragmentTransaction.commit();
        initializeUserProfile();
        scoreboardFAB.setOnClickListener(this);
        theoryFAB.setOnClickListener(this);
        //Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/unicode.ttf");
        //courseNameTV.setTypeface(custom_font);
        //userNameTV.setTypeface(custom_font);
    }

    private void initializeUserProfile(){
        userName = sharedPref.getStringData("name");
        uId  =sharedPref.getStringData("uid");
        courseName = sharedPref.getStringData("course_name");
        courseId = sharedPref.getStringData("course_id");
        email = sharedPref.getStringData("email");
        loginProvider = sharedPref.getStringData("login_provider");
        imageExist = sharedPref.getBooleanData("image_exist");
        Log.d(TAG,"course info "+courseId);

        if(imageExist){
            userImage = imageUtils.loadImageBitmap(this,uId);
            profileImage.setImageBitmap(userImage);
            navImage.setImageBitmap(userImage);
        }else{
            navImage.setImageResource(R.drawable.ic_user_image);
        }
        navEmail.setText(email);
        navName.setText(userName);

        userNameTV.setText(userName);
        courseNameTV.setText(courseName);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         switch (id){
             case R.id.nav_bookmark:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, BookmarkFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_logout:
                 signOut();
                 break;
             case R.id.nav_quiz:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, LevelFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_leaderboard:
                 if(sharedPref.getStringData("login_provider").equals("facebook")){
                     showScorecardFB();
                 }else{
                     showError("Please Login using Facebook");
                 }
                 break;
             case R.id.nav_switch_course:
                 Intent intent = new Intent(this, CourseSelector.class);
                 startActivity(intent);
                 finish();
             case R.id.nav_about_us:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, AboutUsFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_syllabus:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, SyllabusFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_theory:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, CommingSoonFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_notice:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, NoticeFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             case R.id.nav_faq:
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container, FAQFragment.getInstance());
                 fragmentTransaction.commit();
                 break;
             default:
                 break;
         }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void signOut(){
        if(loginProvider.equals("google")){
            signOutGoogle();
        }else if(loginProvider.equals("facebook")){
            signOutFacebook();
        }else if(loginProvider.equals("email")){
            signOutEmail();
        }
        sharedPref.clearAll();
    }

    private void signOutEmail() {
        firebaseAuth.signOut();
        Intent intent = new Intent(this, EntryPointActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOutFacebook() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, EntryPointActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOutGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Google Account signed out");
                        Intent intent = new Intent(MainContainer.this, EntryPointActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void hideProfile() {
        if(scoreboardFAB.isShown()){
            scoreboardFAB.hide();
        }
        if(theoryFAB.isShown()){
            theoryFAB.hide();
        }
        if(profileLayout.getVisibility()==View.VISIBLE){
            profileLayout.animate()
                    .alpha(0.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            profileLayout.setVisibility(View.GONE);
                        }
                    });
        }

    }

    @Override
    public void showProfile() {
        if(profileLayout.getVisibility()==View.GONE){
            profileLayout.animate()
                    .alpha(1.0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            profileLayout.setVisibility(View.VISIBLE);
                            if(!scoreboardFAB.isShown()){
                                scoreboardFAB.show();
                            }
                            if(!theoryFAB.isShown()){
                                theoryFAB.show();
                            }
                        }
                    });
        }
    }

    @Override
    public void updateScoreandLevel(Integer obtainedscore,Integer totalScore, Integer level) {
        scoreValueTV.setText(obtainedscore+"/"+totalScore);
        levelValueTV.setText(String.valueOf(level));
        sharedPref.saveData("obtained_score",obtainedscore);
        sharedPref.saveData("total_score", totalScore);
        updateScoreFB(obtainedscore);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.scoreboard_fab:
                if(sharedPref.getStringData("login_provider").equals("facebook")){
                    showScorecardFB();
                }else{
                    showError("Please Login using Facebook");
                }
                break;
            case R.id.theory_fab:
                //Crashlytics.getInstance().crash();
                break;
            case R.id.leader_okay:
                if(leaderDialog.isShowing()){
                    leaderDialog.dismiss();
                }
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        LevelFragment.levelFragment = null;
        super.onDestroy();
    }

    //Facebook Score task start here
    public void showScorecardFB(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
        {
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(!token.getPermissions().contains("publish_actions")){
                LoginManager manager = LoginManager.getInstance();
                manager.logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            }
            GraphRequest graphRequest = GraphRequest.newGraphPathRequest(token, "/"+getResources().getString(R.string.facebook_app_id)+"/scores", new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.d(TAG, response.toString());
                    if(response.getError()==null) {
                        handleResponseFB(response);
                        try {
                            Log.d(TAG, response.getJSONObject().getJSONArray("data").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getLocalizedMessage());
                        }
                    }
                }
            });
            graphRequest.executeAsync();
        }
        else
        {
            showError("Please check the internet connectivity");
        }
    }

    private void handleResponseFB(GraphResponse response) {
        try {
            listLeaderBoard = new ArrayList<>();
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(!token.getPermissions().contains("publish_actions")){
                LoginManager manager = LoginManager.getInstance();
                manager.logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            }
            final JSONArray topScoreData = response.getJSONObject().getJSONArray("data");
            showLeaderBoardDialog();
            final LinkedHashMap<String,String> leaderNames = new LinkedHashMap<>();
            final LinkedHashMap<String,String> leaderPhotoURLs = new LinkedHashMap<>();
            final LinkedHashMap<String,String> leaderScores = new LinkedHashMap<>();
            for(int i = 0 ; i < topScoreData.length() ; i++){
                JSONObject userData = topScoreData.getJSONObject(i);
                leaderNames.put(userData.getJSONObject("user").getString("id"),userData.getJSONObject("user").getString("name"));
                leaderScores.put(userData.getJSONObject("user").getString("id"),userData.getString("score"));
                final String itemUserId = userData.getJSONObject("user").getString("id");
                Log.d(TAG, itemUserId);
                Log.d(TAG,"Total Len"+topScoreData.length());
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        token,
                        "/"+itemUserId+"?fields=id,picture,name&redirect=false",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.e(TAG,"Response error "+response.toString());
                                if(response.getError()==null){
                                    try {
                                        leaderPhotoURLs.put(response.getJSONObject().getString("id"),response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));
                                        //Log.d(TAG, response.getJSONObject().getJSONObject("data").getString("url"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG,"Photo error "+e.getLocalizedMessage());
                                    }
                                }else{
                                }
                                //Log.d(TAG,"Photo Len"+leaderPhotoURLs.size()+isLast);
                                if(leaderPhotoURLs.size()==topScoreData.length()){
                                    Log.d(TAG,"Name Len"+leaderNames.size());
                                    Log.d(TAG,"Photo Len"+leaderPhotoURLs.size());
                                    Log.d(TAG,"Score Len"+leaderScores.size());
                                    adapterLeader.updateData(leaderNames, leaderPhotoURLs, leaderScores);
                                }
                            }
                        });
                request.executeAsync();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateScoreFB(Integer currentScore){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
        {
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(!token.getPermissions().contains("publish_actions")){
                LoginManager manager = LoginManager.getInstance();
                manager.logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            }
            JSONObject object = new JSONObject();
            try {
                object.put("score", currentScore);
            } catch (JSONException e) {
                Log.w(TAG, "Error publishing score to Facebook");
            }
            GraphRequest graphRequest = GraphRequest.newPostRequest(token, "me/scores", object, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.d(TAG, response.toString());
                }
            });
            graphRequest.executeAsync();
        }
        else
        {
            showError("Please check the internet connectivity");
        }

    }
    //Facebook Score task start here

    //Leaderboard Dialog
    public void showLeaderBoardDialog(){
        leaderDialog = new Dialog(this);
        leaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        leaderDialog.setContentView(R.layout.dialog_leaderboard);
        dialogLeaderRV = leaderDialog.findViewById(R.id.leader_RV);
        dialogLeaderDone = leaderDialog.findViewById(R.id.leader_okay);
        adapterLeader = new AdapterLeader(this, listLeaderBoard);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dialogLeaderRV.setLayoutManager(layoutManager);
        dialogLeaderRV.setAdapter(adapterLeader);
        leaderDialog.show();
        dialogLeaderDone.setOnClickListener(this);
    }

    void showError(String error){
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OKAY", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
        Log.d(TAG, "Error : "+error);
    }


}
