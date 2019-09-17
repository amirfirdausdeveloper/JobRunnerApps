package com.jobrunner.apps.Activity.Dashboard.Wallet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
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

public class StatiscticsIncomeActivity extends AppCompatActivity {

    PreferenceManagerLogin session;

    StandardProgressDialog loading;

    String jr_id;

    TextView textView_header;

    ImageView imageView_back;

    Spinner spinner_year;

    ArrayList income,month,year;

    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisctics_income);

        session = new PreferenceManagerLogin(getApplicationContext());
        loading = new StandardProgressDialog(this.getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);

        textView_header = findViewById(R.id.textView_header);
        imageView_back = findViewById(R.id.imageView_back);
        spinner_year = findViewById(R.id.spinner_year);
        chart = findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

        TypeFaceClass.setTypeFaceTextView(textView_header,getApplicationContext());

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getYear();

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_year.getSelectedItem().toString().equals("Please choose year")){
                    chart.setVisibility(View.GONE);
                }else{
                    getData(spinner_year.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getYear(){
        year = new ArrayList();
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getIncomeYear(jr_id);
            }
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));
                    year.add("Please choose year");
                    for (int i =0; i < jsonArray.length(); i++){
                        JSONObject obj = jsonArray.getJSONObject(i);
                        year.add(obj.getString("year"));
                    }
                    spinner_year.setAdapter(new ArrayAdapter<String>(StatiscticsIncomeActivity.this, android.R.layout.simple_spinner_dropdown_item, year));

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

    private void getData(final String year) {
        income = new ArrayList();
        month = new ArrayList();

        month.add("01");
        month.add("02");
        month.add("03");
        month.add("04");
        month.add("05");
        month.add("06");
        month.add("07");
        month.add("08");
        month.add("09");
        month.add("10");
        month.add("11");
        month.add("12");

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getIncomeByMonth(jr_id,year);
            }
            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                loading.dismiss();
                chart.setVisibility(View.VISIBLE);
                try {
                    JSONArray resultARR = new JSONArray(result.getString("result"));
                    if(resultARR.length() == 0){
                        AlertDialog alertDialog = new AlertDialog.Builder(StatiscticsIncomeActivity.this)
                                .setMessage("No income history")
                                .setCancelable(false)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .show();
                    }else{
                        for (int i =0; i < resultARR.length(); i++){
                            JSONObject object = resultARR.getJSONObject(i);
                            if(object.getString("month").equals("01")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 0));
                            }
                            if(object.getString("month").equals("02")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 1));
                            }
                            if(object.getString("month").equals("03")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 2));
                            }
                            if(object.getString("month").equals("04")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 3));
                            }
                            if(object.getString("month").equals("05")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 4));
                            }
                            if(object.getString("month").equals("06")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 5));
                            }
                            if(object.getString("month").equals("07")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 6));
                            }
                            if(object.getString("month").equals("08")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 7));
                            }
                            if(object.getString("month").equals("09")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 8));
                            }
                            if(object.getString("month").equals("10")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 9));
                            }
                            if(object.getString("month").equals("11")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 10));
                            }
                            if(object.getString("month").equals("12")){
                                income.add(new BarEntry(Float.parseFloat(object.getString("income")), 11));
                            }
                        }

                        BarDataSet bardataset = new BarDataSet(income, "Income");
                        bardataset.setValueFormatter(new DefaultValueFormatter(0));

                        BarData data = new BarData(month, bardataset);
                        bardataset.setColors(Collections.singletonList(Color.parseColor("#3EB978")));

                        chart.animateY(5000);
                        chart.setDescription("");    // Hide the description
                        chart.getAxisLeft().setDrawLabels(false);
                        chart.getAxisRight().setDrawLabels(false);
                        chart.getLegend().setEnabled(false);
                        chart.getXAxis().setDrawGridLines(false);
                        chart.getAxisLeft().setDrawGridLines(false);
                        chart.getAxisRight().setDrawGridLines(false);
                        chart.setGridBackgroundColor(Color.WHITE);
                        chart.getAxisRight().setEnabled(false);
                        chart.getXAxis().setDrawAxisLine(false);
                        chart.getAxisLeft().setEnabled(false);
                        chart.setScaleEnabled(false);
                        chart.setDoubleTapToZoomEnabled(false);

                        XAxis xAxis = chart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setSpaceBetweenLabels(0);
                        chart.setData(data);

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
}
