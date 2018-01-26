package com.n1technology.app.exampreparationnetwork.selection;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.n1technology.app.exampreparationnetwork.FirebaseHandler.FirebaseHelper;
import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseSelector extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.course_select_RV)RecyclerView courseSelectRV;
    @BindView(R.id.course_next) Button courseNext;
    List<Course> itemCourses;
    AdapterCourse adapterCourse;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_course_selector);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Courses");
        courseNext.setEnabled(false);
        courseNext.setOnClickListener(this);
        initializeRV();
        getCourses();

    }

    private void initializeRV() {
        itemCourses = new ArrayList<>();
        adapterCourse = new AdapterCourse(this, itemCourses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        courseSelectRV.setLayoutManager(layoutManager);
        courseSelectRV.setAdapter(adapterCourse);
    }

    void showProgressDialog(){
        if(progressDialog!=null){
            progressDialog.show();
        }
    }
    void hideProgressDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    //Firebase task
    public void getCourses(){
        showProgressDialog();
        databaseReference = FirebaseHelper.getDatabase().getReference("courses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemCourses.clear();
                for(DataSnapshot contactSnapshot : dataSnapshot.getChildren()){
                    Log.d("tag", "Response from firebase " + contactSnapshot.toString());
                    Course course = contactSnapshot.getValue(Course.class);
                    itemCourses.add(course);
                }
                for(int i = 0 ; i <itemCourses.size() ; i++) {
                    Log.d("tag", "Response from firebase " + itemCourses.get(i).getId());
                    Log.d("tag", "Response from firebase " + itemCourses.get(i).getName());
                    Log.d("tag", "Response from firebase " + itemCourses.get(i).getId());
                    Log.d("tag", "Response from firebase " + itemCourses.get(i).getMax_level());
                    Log.d("tag", "Response from firebase " + itemCourses.get(i).getSyllabus());

                }
                adapterCourse.notifyDataSetChanged();
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(CourseSelector.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enableNext(boolean enable){
        if(enable){
            courseNext.setEnabled(true);
        }else{
            courseNext.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.course_next:
                adapterCourse.nextCourse();
                break;
            default:
                break;
        }
    }
}
