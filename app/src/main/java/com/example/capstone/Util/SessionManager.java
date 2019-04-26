package com.example.capstone.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.capstone.SignInActivity;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private Context context;
    private static final String PREF_NAME = "RencaraPref";
    private static final String LOGGED_IN_VENDOR = "isLoggedIn";
    private static final String LOGGED_IN_CUSTOMER = "isLoggedIn";
    public static final String KEY_EMAIL = "loggedEmail";
    public static final String KEY_ID = "loggedUserID";
    public static final String CART = "CartSession";
    public static final String KEY_CART = "cartId";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefEdit = pref.edit();
    }

    public void createLoginSession (String id, String email, int type) {
        if (type == 1) {
            prefEdit.putBoolean(LOGGED_IN_CUSTOMER, true);
        } else if (type == 2) {
            prefEdit.putBoolean(LOGGED_IN_VENDOR, true);
        }
        prefEdit.putString(KEY_ID, id);
        prefEdit.putString(KEY_EMAIL, email);

        prefEdit.commit();
    }

    public void createCartSession(String cart_id) {
        prefEdit.putBoolean(CART,true);
        prefEdit.putString(KEY_CART,cart_id);
        prefEdit.commit();
    }

    public void checkLogin(int type){
        if(!this.isLoggedIn(type)){
            Intent i = new Intent(context, SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }

    public boolean checkCartSession() {
        return pref.getBoolean(CART,false);
    }

    public HashMap<String, String> getCartSession() {
        HashMap<String, String> order = new HashMap<>();

        order.put(KEY_CART, pref.getString(KEY_CART, null));

        return order;
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

        Intent i = new Intent(context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public void deleteCartSession() {
        prefEdit.putBoolean(CART,false);
        prefEdit.remove(KEY_CART);
        prefEdit.commit();
    }

    public boolean isLoggedIn(int type){
        Boolean whoLoggedIn = false;
        if(type == 1) {
            whoLoggedIn = pref.getBoolean(LOGGED_IN_CUSTOMER, false);
        }else if (type == 2) {
            whoLoggedIn = pref.getBoolean(LOGGED_IN_VENDOR, false);
        }
        return whoLoggedIn;
    }

}
