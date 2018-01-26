package com.n1technology.app.exampreparationnetwork.syllabus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyllabusActivity extends AppCompatActivity {

    @BindView(R.id.syllabus_toolbar) Toolbar toolbar;
    @BindView(R.id.syllabusView) PDFView syllabusView;
    int syllabusNo;
    String courseId;
    SharedPref sharedPref;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_syllabus);
        ButterKnife.bind(this);
        syllabusNo = getIntent().getExtras().getInt("syllabus_no");
        toolbar.setTitle("Paper "+syllabusNo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPref = new SharedPref(this);
        pDialog = new ProgressDialog(this);
        courseId = sharedPref.getStringData("course_id");
        if(fileExist(courseId+syllabusNo+".pdf")){
            showPDF(new File(getFilesDir(), courseId+syllabusNo+".pdf"));
        }else{
            downloadFile();
        }
    }

    private void downloadFile() {
        try {
            pDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("syllabus/"+courseId+syllabusNo+".pdf");
            final File localFile = File.createTempFile(courseId+syllabusNo, "pdf");
            storageReference.getFile(localFile).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("File Data", "File Progress "+taskSnapshot.getBytesTransferred()+"/"+taskSnapshot.getTotalByteCount());
                }
            });
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("File Data", "File successfully saved");
                    File file = new File(getFilesDir(), courseId+syllabusNo+".pdf");
                    try {
                        FileInputStream fileInputStream = new FileInputStream(localFile);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte b[] = new byte[(int)localFile.length()];
                        fileInputStream.read(b);
                        fileOutputStream.write(b);
                        fileInputStream.close();
                        fileOutputStream.close();
                        Log.d("File Data", "File permanent "+file.compareTo(localFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showPDF(new File(getFilesDir(), courseId+syllabusNo+".pdf"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pDialog!=null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }


    }

    public boolean fileExist(String imageName){
        File file = new File(getFilesDir(), courseId+syllabusNo+".pdf");
        if (file.exists()){
            Log.d("file", "file does exist");
            return true;
        }else{
            Log.d("file", "file does not exist");
            return false;
        }
    }

    private void showPDF(File file){
        pDialog.show();
        syllabusView.fromFile(file)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        if(pDialog!=null && pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                    }
                })
                .load();
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
