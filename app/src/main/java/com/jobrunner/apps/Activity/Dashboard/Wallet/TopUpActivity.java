package com.jobrunner.apps.Activity.Dashboard.Wallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

public class TopUpActivity extends AppCompatActivity {

    TextView textView_header,textView_income_topUp,textView_incomeBalanceTitle,textView_warning,textView_cashOutTitle,textView_topap_title;

    ImageView imageView_back;

    LinearLayout linear_topUp,linear_history;

    PreferenceManagerLogin session;

    StandardProgressDialog loading;

    String balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        session = new PreferenceManagerLogin(getApplicationContext());
        loading = new StandardProgressDialog(this.getWindow().getContext());

        declaration();

        balance = getIntent().getStringExtra("balance");

        if(Double.parseDouble(balance) < 20){
            textView_warning.setVisibility(View.VISIBLE);
        }else{
            textView_warning.setVisibility(View.GONE);
        }

        textView_income_topUp.setText("RM "+balance);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linear_topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void declaration() {
        textView_header = findViewById(R.id.textView_header);
        textView_income_topUp = findViewById(R.id.textView_income_topUp);
        textView_incomeBalanceTitle = findViewById(R.id.textView_incomeBalanceTitle);
        textView_warning = findViewById(R.id.textView_warning);
        textView_cashOutTitle = findViewById(R.id.textView_cashOutTitle);
        textView_topap_title = findViewById(R.id.textView_topap_title);

        TypeFaceClass.setTypeFaceTextView(textView_header,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_income_topUp,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_incomeBalanceTitle,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_warning,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_cashOutTitle,getApplicationContext());
        TypeFaceClass.setTypeFaceTextView(textView_topap_title,getApplicationContext());

        imageView_back = findViewById(R.id.imageView_back);

        linear_topUp = findViewById(R.id.linear_topUp);
        linear_history = findViewById(R.id.linear_history);
    }
}
