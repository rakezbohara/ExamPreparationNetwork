package com.n1technology.app.exampreparationnetwork;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n1technology.app.exampreparationnetwork.FirebaseHandler.AlarmReceiver;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.n1technology.app.exampreparationnetwork.quiz.MainContainer;
import com.n1technology.app.exampreparationnetwork.selection.CourseSelector;
import com.n1technology.app.exampreparationnetwork.util.ImageUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EntryPointActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.button_facebook_login) LoginButton fblogin;
    @BindView(R.id.login_fb) Button login_fb;
    @BindView(R.id.login_gp) Button login_gp;
    @BindView(R.id.user_email) EditText userEmail;
    @BindView(R.id.user_password) EditText userPassword;
    @BindView(R.id.login_button) Button loginButton;
    @BindView(R.id.new_user_email) EditText newUserEmail;
    @BindView(R.id.new_user_password) EditText newUserPassword;
    @BindView(R.id.new_user_password_confirm) EditText newUserPasswordConfirm;
    @BindView(R.id.register_button) Button registerButton;

    @BindView(R.id.layout_social_media_login) RelativeLayout socialMediaLayout;
    @BindView(R.id.layout_email_login) RelativeLayout emailLoginLayout;
    @BindView(R.id.layout_email_register) RelativeLayout emailRegisterLayout;


    @BindView(R.id.email_login_tv) TextView emailLoginTV;
    @BindView(R.id.register_tv) TextView userRegisterTV;

    @BindView(R.id.logo_1)ImageView logo1;
    @BindView(R.id.logo_2) ImageView logo2;
    @BindView(R.id.logo_3) ImageView logo3;
    @BindView(R.id.logo_4) ImageView logo4;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    ProgressDialog pDialog;
    //Google Auth
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    //Facebook auth
    private CallbackManager mCallbackManager;

    ImageUtils imageUtils;
    SharedPref sharedPref;
    String loginProvider=null;


    private static String TAG = "Entry Point Activitry";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_entry_point);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        imageUtils = new ImageUtils();
        sharedPref = new SharedPref(this);
        //auth starts
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            Intent nextActivity;
            if(!sharedPref.ifExist("uid")){
                saveUserData(currentUser);
            }
            if(sharedPref.ifExist("course_id")){
                nextActivity = new Intent(this, MainContainer.class);
            }else{
                nextActivity = new Intent(this , CourseSelector.class);
            }
            startActivity(nextActivity);
            finish();
        }else{
            enableNotification();
        }
        animateLogo();
        emailLoginLayout.setVisibility(View.GONE);
        emailRegisterLayout.setVisibility(View.GONE);

        mCallbackManager = CallbackManager.Factory.create();
        fblogin.setReadPermissions("email", "public_profile" ,"user_friends");
        fblogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                errorLogin("Login cancelled!!!");
                // [END_EXCLUDE]
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                errorLogin(error.getLocalizedMessage());
                // [END_EXCLUDE]
            }
        });
        emailLoginTV.setOnClickListener(this);
        userRegisterTV.setOnClickListener(this);
        login_gp.setOnClickListener(this);
        login_fb.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void animateLogo() {
        Log.d(TAG, "animation should start");
        logo2.setAlpha(0.0f);
        logo3.setAlpha(0.0f);
        logo4.setAlpha(0.0f);
        logo2.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        logo2.setVisibility(View.VISIBLE);
                    }
                });
        logo3.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setStartDelay(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        logo3.setVisibility(View.VISIBLE);
                    }
                });
        logo4.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setStartDelay(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        logo4.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void enableNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("epn");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(EntryPointActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EntryPointActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) EntryPointActivity.this.getSystemService(EntryPointActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.email_login_tv){
            toggleView(socialMediaLayout,emailLoginLayout);
        }
        if(id==R.id.register_tv){
            toggleView(emailLoginLayout,emailRegisterLayout);
        }
        if(id==R.id.login_gp){
            loginProvider = "google";
            googleSignin();
        }
        if(id==R.id.login_fb){
            loginProvider = "facebook";
            facebookSignin();
        }
        if(id==R.id.login_button){
            if(validateInput(userEmail, userPassword)){
                loginProvider = "email";
                emailSignin(userEmail.getText().toString(), userPassword.getText().toString());
            }
        }
        if(id==R.id.register_button){
            if(newUserPassword.getText().toString().equals(newUserPasswordConfirm.getText().toString())){
                if(validateInput(newUserEmail, newUserPassword)){
                    emailRegister(newUserEmail.getText().toString(), newUserPassword.getText().toString());
                }
            }else{
                newUserPasswordConfirm.setError("This field should be equal with above password!");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(socialMediaLayout.getVisibility()==View.VISIBLE){
            super.onBackPressed();
        }
        if(emailLoginLayout.getVisibility()==View.VISIBLE){
            Log.d(TAG,"email login is visible");
            toggleView(emailLoginLayout,socialMediaLayout);
        }
        if(emailRegisterLayout.getVisibility()==View.VISIBLE){
            Log.d(TAG,"email registration is visible");
            toggleView(emailRegisterLayout,emailLoginLayout);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void toggleView(final RelativeLayout toHide, final RelativeLayout toShow){
        toHide.animate()
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toHide.setVisibility(View.GONE);
                        toHide.setAlpha(1.0f);
                    }
                });
        toShow.animate()
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toShow.setVisibility(View.VISIBLE);
                    }
                });

    }

    //Google login starts here
    private void googleSignin(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void googleSignout(){
        firebaseAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //do something
                    }
                });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(EntryPointActivity.this,"Successful",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            successLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Toast.makeText(EntryPointActivity.this,"unsuccessful",Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(EntryPointActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            errorLogin(task.getException().getLocalizedMessage());
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    //Google login ends here

    //Facebook login code starts here
    void facebookSignin(){
        fblogin.performClick();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            successLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(FacebookLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            errorLogin(task.getException().getLocalizedMessage());
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    public void facebookSignout() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        //success signout
    }
    //Facebook login code ends here


    //Email login starts here
    private void emailSignin(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        showProgressDialog();
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                successLogin(user);
                            }else{
                                emailNotVerified(user);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            errorLogin(task.getException().getLocalizedMessage());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void emailNotVerified(final FirebaseUser user) {
        Log.d(TAG, "Error : Email not verified");
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please verify the email", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESEND EMAIL", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        sendEmailVerification(user);
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    //Email login ends here

    //Email registration starts here
    private void emailRegister(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        showProgressDialog();
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            successLogin(user);
                            sendEmailVerification(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                            errorLogin(task.getException().getLocalizedMessage());
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private void sendEmailVerification(final FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(EntryPointActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EntryPointActivity.this,
                                    task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();

                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    //Email registration ends here
    void successLogin(FirebaseUser currentUser){
        saveUserData(currentUser);
        Log.d(TAG,"Name : "+ currentUser.getDisplayName());
        Log.d(TAG,"Photo : "+ currentUser.getPhotoUrl());
        Log.d(TAG,"Email : "+ currentUser.getEmail());
        Log.d(TAG,"UID : "+ currentUser.getUid());
        Log.d(TAG, "Provider :"+loginProvider);
        Intent in = new Intent(this, CourseSelector.class);
        startActivity(in);
    }

    void saveUserData(FirebaseUser user){
        if(user.getPhotoUrl()!=null){
            new DownloadImage().execute(user.getPhotoUrl().toString(),user.getUid());
        }
        sharedPref.saveData("name",user.getDisplayName());
        sharedPref.saveData("uid",user.getUid());
        sharedPref.saveData("email",user.getEmail());
        sharedPref.saveData("login_provider",loginProvider);
        if(user.getPhotoUrl()==null){
            sharedPref.saveData("image_exist",false);
        }else{
            sharedPref.saveData("image_exist",true);
        }
    }

    void errorLogin(String error){
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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        String userId = null;
        private Bitmap downloadImageBitmap(String sUrl,String filename) {
            Bitmap bitmap = null;
            userId = filename;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0], params[1]);
        }

        protected void onPostExecute(Bitmap result) {
            imageUtils.saveImage(getApplicationContext(), result, userId);
        }
    }
    void hideProgressDialog(){
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }

    }
    void showProgressDialog(){
        if(pDialog!=null){
            pDialog.show();
        }

    }
    boolean validateInput(EditText email, EditText password){
        if(email.getText().toString().isEmpty()){
            email.setError("Email cannot be empty!");
            return false;
        }else if(password.getText().toString().isEmpty()){
            password.setError("Password cannot be empty!");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please enter the valid email!");
            return false;
        }else if(password.getText().toString().length()<8){
            password.setError("Min. length of password is 8!");
            return false;
        }else{
            return true;
        }
    }

}
