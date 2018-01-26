package com.n1technology.app.exampreparationnetwork.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by RAKEZ on 11/24/2017.
 */

public class ImageUtils {
    private static String TAG = "Image util class";
    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
            Log.d(TAG, "Image saved successfully");
        } catch (Exception e) {
            Log.d(TAG, "Exception 2, Something went wrong! "+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }
    public boolean imageExist(Context context,String imageName){
        File file = context.getFileStreamPath(imageName);
        if (file.exists()){
            Log.d("file", "my_image.jpeg exists!");
            return true;
        }else{
            return false;
        }
    }
    public boolean deleteImage(Context context, String imageName){
        File file = context.getFileStreamPath(imageName);
        if (file.delete()){
            Log.d("file", "my_image.jpeg deleted!");
            return true;
        }else{
            return false;
        }
    }
}
