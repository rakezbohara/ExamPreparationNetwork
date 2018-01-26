package com.n1technology.app.exampreparationnetwork.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by RAKEZ on 11/24/2017.
 */

public class SharedPref {
    private Context context;
    private static String PREF_NAME = "basic_user_info";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String TAG = "Data from SP";

    public SharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(String key, int value) {
        editor.putInt(key, value);
        if(editor.commit()){
            Log.d(TAG,key+" Data is saved "+value);
        }
    }

    public void saveData(String key, boolean value){
        editor.putBoolean(key,value);
        if(editor.commit()){
            Log.d(TAG,key+" Data is saved "+value);
        }
    }

    public void saveData(String key, String value){
        editor.putString(key, value);
        if(editor.commit()){
            Log.d(TAG,key+" Data is saved "+value);
        }

    }

    public int getIntData(String key){
        Log.d(TAG,key+" Data is fetched "+sharedPreferences.getInt(key,0));
        return sharedPreferences.getInt(key,0);
    }

    public String getStringData(String key){
        Log.d(TAG,key+" Data is fetched "+sharedPreferences.getString(key,null));
        return sharedPreferences.getString(key, null);
    }

    public Boolean getBooleanData(String key){
        Log.d(TAG,key+" Data is fetched "+sharedPreferences.getBoolean(key,false));
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean ifExist(String key){
        return sharedPreferences.contains(key);
    }

    public boolean clearAll(){
       return sharedPreferences.edit().clear().commit();
    }

}
