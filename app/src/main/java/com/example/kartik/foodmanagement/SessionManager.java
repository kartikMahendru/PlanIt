package com.example.kartik.foodmanagement;

import java.util.HashMap;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Details";
    private static final String UNIQ_ID = "emailID";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id){
        editor.putString(UNIQ_ID, id);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(UNIQ_ID, pref.getString(UNIQ_ID, "Not Found"));
        return user;
    }
    public void logoutUser(){

        editor.clear();
        editor.commit();
    }
}