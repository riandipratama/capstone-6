package com.example.capstone.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.capstone.SignIn;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private Context context;
    private static final String PREF_NAME = "RencaraPref";
    private static final String LOGGED_IN = "isLoggedIn";
    public static final String KEY_EMAIL = "loggedEmail";
    public static final String KEY_ID = "loggedUserID";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefEdit = pref.edit();
    }

    public void createLoginSession (String id, String email) {
        prefEdit.putBoolean(LOGGED_IN, true);
        prefEdit.putString(KEY_ID, id);
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
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_ID, pref.getString(KEY_ID,null));
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
