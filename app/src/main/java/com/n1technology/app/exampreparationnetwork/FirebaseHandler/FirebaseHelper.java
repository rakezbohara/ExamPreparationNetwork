package com.n1technology.app.exampreparationnetwork.FirebaseHandler;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by RAKEZ on 12/2/2017.
 */

public class FirebaseHelper {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
