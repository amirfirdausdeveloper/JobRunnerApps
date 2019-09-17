package com.jobrunner.apps.PreferanceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jobrunner.apps.Activity.LoginScreen.LoginActivity;

import java.util.HashMap;


public class PreferenceManagerCompleteProfile {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "JobRunnerComplete";
    private static final String IS_LOGIN = "Complete";
    public static final String KEY_STATUS = "0";

    public PreferenceManagerCompleteProfile(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createStatus(String status){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_STATUS, status);
        // commit changes
        editor.commit();
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));
        return user;
    }



    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}