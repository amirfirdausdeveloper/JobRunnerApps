package com.jobrunner.apps.Activity.LoginScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jobrunner.apps.Activity.Dashboard.DashboardActivity;
import com.jobrunner.apps.Activity.IntroductionScreen.IntroductionActivity;
import com.jobrunner.apps.Activity.Register.RegisterActivity;
import com.jobrunner.apps.Activity.SplashScreen.SplashScreenActivity;
import com.jobrunner.apps.Common.NetworkStateReceiver;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.FirebaseNotification.MyFirebaseInstanceIdService;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerCompleteProfile;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    TextView textView_signUp;
    EditText editText_email,editText_password;
    Button button_login;
    StandardProgressDialog standardProgressDialog,successDialog,failedDialog;
    private NetworkStateReceiver networkStateReceiver;
    Context mContext;
    int click = 0;
    private int network = 0;
    private AlertDialog alertDialog;
    PreferenceManagerLogin session;
    private static long back_pressed;
    PreferenceManagerCompleteProfile completeProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new PreferenceManagerLogin(getApplicationContext());
        completeProfile = new PreferenceManagerCompleteProfile(getApplicationContext());
        standardProgressDialog = new StandardProgressDialog(this.getWindow().getContext());
        successDialog = new StandardProgressDialog(this.getWindow().getContext());
        failedDialog = new StandardProgressDialog(this.getWindow().getContext());

        textView_signUp = findViewById(R.id.textView_signUp);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_login = findViewById(R.id.button_login);

        editText_email.setText("amirfirdausoff@gmail.com");
        editText_password.setText("fuckAcikko93@");

        TypeFaceClass.setTypeFaceEditText(editText_email,getApplicationContext());
        TypeFaceClass.setTypeFaceEditText(editText_password,getApplicationContext());
        TypeFaceClass.setTypeFaceButton(button_login,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_signUp,getApplicationContext());

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_email.getText().toString().equals("")){
                    editText_email.setError("Required");
                }else if(editText_password.getText().toString().equals("")){
                    editText_password.setError("Required");
                }else{
                    if(network ==0){
                        networkUnavailable();
                    }else{
                        login();
                    }
                }
            }
        });

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(next);
            }
        });
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

    private void login() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                standardProgressDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                String token =  FirebaseInstanceId.getInstance().getToken();
                UrlLink uLink = new UrlLink();
                return uLink.loginEmailAndPasswordAndToken(editText_email.getText().toString(), editText_password.getText().toString(), token);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                standardProgressDialog.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){

                        JSONObject result = new JSONObject(jsonObject.getString("result"));
                        session.createLoginSession(editText_email.getText().toString(),editText_password.getText().toString(),result.getString("jr_id"));
                        successDialog.dialogSuccess(LoginActivity.this,jsonObject.getString("message"));
                        completeProfile.createStatus(result.getString("complete_status"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successDialog.dialogSuccessClose();
                                Intent next = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(next);
                            }
                        }, 2000);
                    }else {
                        failedDialog.dialogFailed(LoginActivity.this,jsonObject.getString("message"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                failedDialog.dialogCancelClose();
                            }
                        }, 2000);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                standardProgressDialog.dismiss();
            }

        }

        new AsyncTaskRunner().execute();
    }

    @Override
    public void networkAvailable() {
        network = 1;
    }

    public void networkUnavailable() {

        network = 0;
        alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme)
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

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())  moveTaskToBack(true);
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
