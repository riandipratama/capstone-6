package com.example.capstone.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.capstone.SignIn;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor prefEdit;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RencaraPref";
    public static final String LOGGED_IN = "isLoggedIn";
    public static final String KEY_EMAIL = "loggedEmail";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        prefEdit = pref.edit();
    }

    public void createLoginSession (String email) {
        prefEdit.putBoolean(LOGGED_IN, true);
        prefEdit.putString(KEY_EMAIL, email);

        prefEdit.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, SignIn.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        return user;
    }

    public void logoutUser(){
        prefEdit.clear();
        prefEdit.commit();

        Intent i = new Intent(context, SignIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(LOGGED_IN, false);
    }

}
