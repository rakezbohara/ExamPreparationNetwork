package com.n1technology.app.exampreparationnetwork.FirebaseHandler;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.n1technology.app.exampreparationnetwork.EntryPointActivity;
import com.n1technology.app.exampreparationnetwork.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.n1technology.app.exampreparationnetwork.SugarModel.Notice;
import com.n1technology.app.exampreparationnetwork.data.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RAKEZ on 12/14/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static String TAG = "Data From Firebase Service";
    JSONObject data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data Message Body: " + remoteMessage.getData().toString());
        super.onMessageReceived(remoteMessage);
        try {
            data = new JSONObject(remoteMessage.getData().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Cannot convert Data to JSON Object");
        }
        if(remoteMessage.getData()!=null){
            if(remoteMessage.getData().get("type").equals("info")){
                try {
                    handleInfo(data.getJSONObject("info"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Cannot convert info to JSON Object");
                }
            }else if(remoteMessage.getData().get("type").equals("reset")){
                handleReset();
            }else if(remoteMessage.getData().get("type").equals("notice")){
                try {
                    handleNotice(data.getJSONObject("notice"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Cannot convert notice to JSON Object");
                }
            }
        }
    }

    private void handleReset() {
        Log.d(TAG, "Reset is handled");
        SharedPref sharedPref = new SharedPref(this);
        if(sharedPref.ifExist("uid")){
            final String userId = sharedPref.getStringData("uid");
            DatabaseReference databaseReference = FirebaseHelper.getDatabase().getReference();
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("/scores/"+userId+"/obtained_score","0");
            updateData.put("/scores/"+userId+"/total_score","0");
            databaseReference.updateChildren(updateData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Log.d(TAG, "Event Occured "+databaseError.getMessage());
                    }else{
                        Log.d(TAG, "Event Occured success "+" scores/"+userId);
                    }
                }
            });
        }

    }

    private void handleInfo(JSONObject info) throws JSONException{
        Log.d(TAG, "Info is handled"+info.get("title")+info.get("content"));
        String messageTitle = info.getString("title");
        String messageBody = info.getString("content");
        Intent resultIntent = new Intent(this, EntryPointActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this)
                .setSound(soundUri)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }

    private void handleNotice(JSONObject notice) throws JSONException{
        Log.d(TAG, "Notice is handled"+notice.toString());
        Notice noticeItem  = new Notice(notice.getString("title"), notice.getString("content"), notice.getString("date"));
        noticeItem.save();
        List<Notice> noticeList = Notice.listAll(Notice.class);
        Log.d(TAG, "NoticeList Size is a: "+noticeList.size());
        Log.d(TAG, "NoticeList a : "+noticeList.toString());
        if(noticeList.size()>25){
            Notice lastNotice = noticeList.get(noticeList.size()-1);
            lastNotice.delete();
            Log.d(TAG, "NoticeList Size is b: "+noticeList.size());
            Log.d(TAG, "NoticeList b: "+noticeList.toString());
        }
        Intent resultIntent = new Intent(this, EntryPointActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this)
                .setSound(soundUri)
                .setContentTitle("Notice...")
                .setContentText(notice.getString("title"))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(124, notification);
    }

}
