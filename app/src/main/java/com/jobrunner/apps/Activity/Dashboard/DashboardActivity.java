
package com.jobrunner.apps.Activity.Dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jobrunner.apps.Activity.LoginScreen.LoginActivity;
import com.jobrunner.apps.Common.NetworkStateReceiver;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerCompleteProfile;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{
    PreferenceManagerLogin session;
    PreferenceManagerCompleteProfile session_completed;
    StandardProgressDialog loading;
    String jr_id,status_completed,message;
    private NetworkStateReceiver networkStateReceiver;
    Context mContext;
    int click = 0;
    private int network = 0;
    private AlertDialog alertDialog;
    BottomNavigationView navigation;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new PreferenceManagerLogin(getApplicationContext());
        session_completed  = new PreferenceManagerCompleteProfile(getApplicationContext());
        loading = new StandardProgressDialog(this.getWindow().getContext());

        if(session.checkLogin()){
            finish();
        }else{
            HashMap<String, String> user = session.getUserDetails();
            jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);

            getProfileDetailStatus();
        }

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mContext = getApplicationContext();
    }

    @Override
    public void networkAvailable() {
        network = 1;
    }

    public void networkUnavailable() {

        network = 0;
        alertDialog = new AlertDialog.Builder(DashboardActivity.this, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.no_network_notification))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            click++;
                            if(click == 1){
                                dialog.dismiss();
                            }
                        } else {
                            networkUnavailable();
                            click = 0;
                        }
                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private void getProfileDetailStatus() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getProfileJrId(jr_id);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    if(jsonObject.getString("status").equals("false")){
                        session_completed.createStatus("0");
                        navigation.setSelectedItemId(R.id.navigation_profile);
                    }else {
                        navigation.setSelectedItemId(R.id.navigation_job);
                        session_completed.createStatus("1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }
        new AsyncTaskRunner().execute();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_job:
                    HashMap<String, String> complete1 = session_completed.getUserDetails();
                    String status1 = complete1.get(PreferenceManagerCompleteProfile.KEY_STATUS);
                    if(status1.equals("1")){
                        loadFragment(new FindJobFragment());
                    }else{
                        navigation.setSelectedItemId(R.id.navigation_profile);
                    }

                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_wallet:
                    HashMap<String, String> complete2 = session_completed.getUserDetails();
                    String status2 = complete2.get(PreferenceManagerCompleteProfile.KEY_STATUS);
                    if(status2.equals("1")){
                        fragment = new WalletFragment();
                        loadFragment(fragment);
                    }else{
                        navigation.setSelectedItemId(R.id.navigation_profile);
                    }

                    return true;
                case R.id.navigation_my_job:
                    HashMap<String, String> complete3 = session_completed.getUserDetails();
                    String status3 = complete3.get(PreferenceManagerCompleteProfile.KEY_STATUS);
                    if(status3.equals("1")){
                        fragment = new MyJobFragment();
                        loadFragment(fragment);
                    }else{
                        navigation.setSelectedItemId(R.id.navigation_profile);
                    }

                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateNavigationBarState(int actionId){
        Menu menu = navigation.getMenu();

        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(item.getItemId() == actionId);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
