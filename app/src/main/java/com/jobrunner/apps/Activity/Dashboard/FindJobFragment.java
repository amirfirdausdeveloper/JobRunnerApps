package com.jobrunner.apps.Activity.Dashboard;


import android.content.Intent;
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
import android.widget.TextView;

import com.jobrunner.apps.Activity.LoginScreen.LoginActivity;
import com.jobrunner.apps.Adapter.FindJobAdapter;
import com.jobrunner.apps.Class.FindJobClass;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindJobFragment extends Fragment {
    public FindJobFragment() {
    }

    PreferenceManagerLogin session;
    StandardProgressDialog loading,success,failed;
    RecyclerView recyclerView;
    TextView textView_header;
    private FindJobAdapter findJobAdapter;
    List<FindJobClass> findJobClasses;
    int current_length = 0;
    int changes_length = 0;
    Handler handler;
    TextView textView_no_job;
    String changes = "changes";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_find_job2, container, false);

        session = new PreferenceManagerLogin(getContext());
        loading = new StandardProgressDialog(getActivity().getWindow().getContext());
        success = new StandardProgressDialog(getActivity().getWindow().getContext());
        failed = new StandardProgressDialog(getActivity().getWindow().getContext());

        recyclerView = v.findViewById(R.id.recycleView);
        textView_header = v.findViewById(R.id.textView_header);
        textView_no_job = v.findViewById(R.id.textView_no_job);

        TypeFaceClass.setTypeFaceTextView(textView_no_job,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_header,getActivity());

        textView_no_job.setVisibility(View.GONE);
        getJob();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getJobChanges();
                if(changes.equals("changes")){
                    if(changes_length != current_length){
                        changes = "changess";
                        getJob();
                    }
                }

                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(0);
    }

    private void getJob() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        findJobClasses = new ArrayList<>();
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getPendingJob();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){
                        JSONArray result = new JSONArray(jsonObject.getString("result"));
                        current_length = result.length();
                        if(current_length == 0){
                            textView_no_job.setVisibility(View.VISIBLE);
                            findJobClasses = new ArrayList<>();
                            recyclerView.setAdapter(null);
                        }else{
                            textView_no_job.setVisibility(View.GONE);
                            for(int i= 0; i<result.length();i++){
                                JSONObject resultOBJ = result.getJSONObject(i);

                                findJobClasses.add(new FindJobClass(
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
                                        resultOBJ.getString("job_total_comission")

                                ));
                                findJobAdapter = new FindJobAdapter(getActivity(), findJobClasses);
                                recyclerView.setAdapter(findJobAdapter);
                                changes = "changes";
                            }
                        }


                    }else {
                        failed.dialogFailed(getActivity(),jsonObject.getString("message"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                failed.dialogCancelClose();
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

    private void getJobChanges() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getPendingJob();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    if(jsonObject.getString("status").equals("true")){
                        JSONArray result = new JSONArray(jsonObject.getString("result"));
                        changes_length = result.length();

                    }else {
                        current_length = 0;

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

}
