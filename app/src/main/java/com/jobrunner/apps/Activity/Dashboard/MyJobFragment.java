package com.jobrunner.apps.Activity.Dashboard;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobrunner.apps.Adapter.CurrentJobAdapter;
import com.jobrunner.apps.Adapter.FindJobAdapter;
import com.jobrunner.apps.Adapter.HistoryJobAdapter;
import com.jobrunner.apps.Class.CurrentJobClass;
import com.jobrunner.apps.Class.FindJobClass;
import com.jobrunner.apps.Class.HistoryClass;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJobFragment extends Fragment implements View.OnClickListener {

    public MyJobFragment() {}

    PreferenceManagerLogin session;
    StandardProgressDialog loading;
    TextView textView_header,textView_current,textView_history,textView_words_history;
    LinearLayout linear_current,linear_history;
    RecyclerView recyclerView_current,recyclerView_history;

    private HistoryJobAdapter historyJobAdapter;
    List<HistoryClass> historyClasses;

    private CurrentJobAdapter currentJobAdapter;
    List<CurrentJobClass> currentJobClasses;

    String jr_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_job, container, false);

        session = new PreferenceManagerLogin(getContext());
        loading = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);

        textView_header = v.findViewById(R.id.textView_header);
        textView_current = v.findViewById(R.id.textView_current);
        textView_history = v.findViewById(R.id.textView_history);
        textView_words_history = v.findViewById(R.id.textView_words_history);
        linear_history = v.findViewById(R.id.linear_history);
        linear_current = v.findViewById(R.id.linear_current);
        recyclerView_current = v.findViewById(R.id.recyclerView_current);
        recyclerView_history = v.findViewById(R.id.recyclerView_history);

        //CHANGE FONT TYPE
        TypeFaceClass.setTypeFaceTextView(textView_header,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_current,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_history,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_words_history,getActivity());

        textView_words_history.setVisibility(View.GONE);
        recyclerView_current.setVisibility(View.GONE);
        recyclerView_history.setVisibility(View.GONE);

        linear_history.setOnClickListener(this);
        linear_current.setOnClickListener(this);

        //SET LINEAR ONCLICK FIRSTTIME
        linear_current.performClick();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.linear_history:
                linear_current.setBackgroundColor(Color.parseColor("#FFFFFF"));
                linear_history.setBackgroundColor(Color.parseColor("#EEEEEE"));
                recyclerView_current.setVisibility(View.GONE);
                recyclerView_history.setVisibility(View.VISIBLE);
                getHistoryJob();
                textView_header.setText("HISTORY JOB");
                break;
            case R.id.linear_current:
                linear_current.setBackgroundColor(Color.parseColor("#EEEEEE"));
                linear_history.setBackgroundColor(Color.parseColor("#FFFFFF"));
                recyclerView_current.setVisibility(View.VISIBLE);
                recyclerView_history.setVisibility(View.GONE);
                getJobCurrent();
                textView_header.setText("CURRENT JOB");
                break;
        }
    }

    private void getJobCurrent() {
        recyclerView_current.setLayoutManager(new LinearLayoutManager(getActivity()));
        currentJobClasses = new ArrayList<>();
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getCurrentJob(jr_id);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){
                        JSONArray result = new JSONArray(jsonObject.getString("result"));
                        if(result.length() == 0){
                           textView_words_history.setText("No current job");
                           textView_words_history.setVisibility(View.VISIBLE);
                        }else{
                            textView_words_history.setVisibility(View.GONE);
                            for(int i= 0; i<result.length();i++){
                                JSONObject resultOBJ = result.getJSONObject(i);
                                Log.d("value",resultOBJ.toString());
                                currentJobClasses.add(new CurrentJobClass(
                                        resultOBJ.getString("date_created"),
                                        resultOBJ.getString("job_id").toUpperCase(),
                                        resultOBJ.getString("job_type"),
                                        resultOBJ.getString("employee_job_id"),
                                        resultOBJ.getString("employer_id").toUpperCase(),
                                        resultOBJ.getString("employer_name"),
                                        resultOBJ.getString("job_name"),
                                        resultOBJ.getString("date_from").toUpperCase(),
                                        resultOBJ.getString("date_to"),
                                        resultOBJ.getString("working_time_from"),
                                        resultOBJ.getString("working_time_to").toUpperCase(),
                                        resultOBJ.getString("working_hour"),
                                        resultOBJ.getString("job_location_address"),
                                        resultOBJ.getString("job_location_latlng").toUpperCase(),
                                        resultOBJ.getString("job_details"),
                                        resultOBJ.getString("working_job_salary_hourday"),
                                        resultOBJ.getString("job_salary_day").toUpperCase(),
                                        resultOBJ.getString("job_salary_total"),
                                        resultOBJ.getString("job_salary_hour"),
                                        resultOBJ.getString("status_payment").toUpperCase(),
                                        resultOBJ.getString("job_total_comission"),
                                        resultOBJ.getString("status_job")

                                ));
                                currentJobAdapter = new CurrentJobAdapter(getActivity(), currentJobClasses);
                                recyclerView_current.setAdapter(currentJobAdapter);
                            }
                        }


                    }else {

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

    private void getHistoryJob() {
        recyclerView_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyClasses = new ArrayList<>();
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getCurrentHistoryJob(jr_id);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){
                        JSONArray result = new JSONArray(jsonObject.getString("result"));
                        if(result.length() == 0){
                            textView_words_history.setText("No history job");
                            textView_words_history.setVisibility(View.VISIBLE);
                        }else{
                            textView_words_history.setVisibility(View.GONE);
                            for(int i= 0; i<result.length();i++){
                                JSONObject resultOBJ = result.getJSONObject(i);

                                historyClasses.add(new HistoryClass(
                                        resultOBJ.getString("date_created"),
                                        resultOBJ.getString("job_id").toUpperCase(),
                                        resultOBJ.getString("job_type"),
                                        resultOBJ.getString("employee_job_id"),
                                        resultOBJ.getString("employer_id").toUpperCase(),
                                        resultOBJ.getString("employer_name"),
                                        resultOBJ.getString("job_name"),
                                        resultOBJ.getString("date_from").toUpperCase(),
                                        resultOBJ.getString("date_to"),
                                        resultOBJ.getString("working_time_from"),
                                        resultOBJ.getString("working_time_to").toUpperCase(),
                                        resultOBJ.getString("working_hour"),
                                        resultOBJ.getString("job_location_address"),
                                        resultOBJ.getString("job_location_latlng").toUpperCase(),
                                        resultOBJ.getString("job_details"),
                                        resultOBJ.getString("working_job_salary_hourday"),
                                        resultOBJ.getString("job_salary_day").toUpperCase(),
                                        resultOBJ.getString("job_salary_total"),
                                        resultOBJ.getString("job_salary_hour"),
                                        resultOBJ.getString("status_payment").toUpperCase(),
                                        resultOBJ.getString("job_total_comission"),
                                        resultOBJ.getString("status_job")

                                ));
                                historyJobAdapter = new HistoryJobAdapter(getActivity(), historyClasses);
                                recyclerView_history.setAdapter(historyJobAdapter);
                            }
                        }


                    }else {

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
