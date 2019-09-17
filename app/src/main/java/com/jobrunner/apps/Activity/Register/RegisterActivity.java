package com.jobrunner.apps.Activity.Register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jobrunner.apps.Activity.Dashboard.DashboardActivity;
import com.jobrunner.apps.Activity.LoginScreen.LoginActivity;
import com.jobrunner.apps.Common.NetworkStateReceiver;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    TextView textView_Register;
    ImageView imageView_back;
    TextInputEditText et_email,et_password,et_password_two;
    Button button_signUp;
    PreferenceManagerLogin session;
    StandardProgressDialog loading,success,cancel;
    private NetworkStateReceiver networkStateReceiver;
    Context mContext;
    int click = 0;
    private int network = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new PreferenceManagerLogin(getApplicationContext());
        loading = new StandardProgressDialog(this.getWindow().getContext());
        success = new StandardProgressDialog(this.getWindow().getContext());
        cancel = new StandardProgressDialog(this.getWindow().getContext());

        textView_Register = findViewById(R.id.textView_Register);
        imageView_back = findViewById(R.id.imageView_back);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password_two = findViewById(R.id.et_password_two);
        button_signUp = findViewById(R.id.button_signUp);

        //CLICK BACK BUTTON
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email.getText().toString().equals("")){
                    et_email.setError("Required");
                }else if(et_password.getText().toString().equals("")){
                    et_password.setError("Required");
                }else if(et_password_two.getText().toString().equals("")){
                    et_password_two.setError("Required");
                }else {
                    if(et_password.getText().toString().equals(et_password_two.getText().toString())){
                        if(network == 1){
                            register();
                        }else {
                            networkUnavailable();
                        }
                    }else{
                        et_password_two.setError("Password is not same");
                    }
                }

            }
        });
    }

    private void register() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                String token =  FirebaseInstanceId.getInstance().getToken();
                UrlLink uLink = new UrlLink();
                return uLink.registerEmailAndPassword(et_email.getText().toString(), et_password.getText().toString(),token);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){

                        JSONObject result = new JSONObject(jsonObject.getString("result"));
                        session.createLoginSession(et_email.getText().toString(),et_password.getText().toString(),result.getString("jr_id"));
                        success.dialogSuccess(RegisterActivity.this,jsonObject.getString("message"));

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                success.dialogSuccessClose();
                                Intent next = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(next);
                            }
                        }, 2000);
                    }else {
                        cancel.dialogFailed(RegisterActivity.this,jsonObject.getString("message"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cancel.dialogCancelClose();
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
                loading.dismiss();
            }

        }

        new AsyncTaskRunner().execute();
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
        alertDialog = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialogTheme)
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
}
