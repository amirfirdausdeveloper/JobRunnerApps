package com.jobrunner.apps.Activity.Dashboard;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.jobrunner.apps.Activity.Dashboard.Wallet.StatiscticsIncomeActivity;
import com.jobrunner.apps.Activity.Dashboard.Wallet.TopUpActivity;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {
    PreferenceManagerLogin session;

    String jr_id,balance_topup;

    TextView textView_header;

    TextView textView_income,textView_incomeBalanceTitle,textView_cashOutTitle,textView_topap_title,textView_cashoutHistoryTitle,textView_statisticsTitle;

    LinearLayout linear_cashOut,linear_topap,linear_cashOutHistory,linear_statisticIncome;

    StandardProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);

        session = new PreferenceManagerLogin(getContext());
        loading = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);

        declaration(v);


        //CASH OUT LINEAR ONCLICK
        linear_cashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","cashout");
            }
        });

        //TOP UP LINEAR ONCLICK
        linear_topap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), TopUpActivity.class);
                next.putExtra("balance",balance_topup);
                startActivity(next);
            }
        });


        //CASH OUT HISTORY LINEAR ONCLICK
        linear_cashOutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","history");
            }
        });

        //STATISTIC LINEAR ONCLICK
        linear_statisticIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getActivity(), StatiscticsIncomeActivity.class);
                startActivity(next);
            }
        });

        getIncome();
        return v;
    }

    private void getIncome(){
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getIncome(jr_id);
            }
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));
                    for (int i =0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        textView_income.setText("RM "+obj.getString("income"));
                        balance_topup = obj.getString("balance");

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

    private void declaration(View v) {
        textView_header = v.findViewById(R.id.textView_header);
        textView_income = v.findViewById(R.id.textView_income);
        textView_incomeBalanceTitle = v.findViewById(R.id.textView_incomeBalanceTitle);
        textView_cashOutTitle = v.findViewById(R.id.textView_cashOutTitle);
        textView_topap_title = v.findViewById(R.id.textView_topap_title);
        textView_cashoutHistoryTitle = v.findViewById(R.id.textView_cashoutHistoryTitle);
        textView_statisticsTitle = v.findViewById(R.id.textView_statisticsTitle);

        linear_cashOut = v.findViewById(R.id.linear_cashOut);
        linear_topap = v.findViewById(R.id.linear_topap);
        linear_cashOutHistory = v.findViewById(R.id.linear_cashOutHistory);
        linear_statisticIncome = v.findViewById(R.id.linear_statisticIncome);

        TypeFaceClass.setTypeFaceTextView(textView_header,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_income,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_incomeBalanceTitle,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_cashOutTitle,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_topap_title,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_cashoutHistoryTitle,getContext());
        TypeFaceClass.setTypeFaceTextView(textView_statisticsTitle,getContext());
    }



}
