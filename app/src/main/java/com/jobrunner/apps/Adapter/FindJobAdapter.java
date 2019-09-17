package com.jobrunner.apps.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobrunner.apps.Activity.Dashboard.DashboardActivity;
import com.jobrunner.apps.Class.FindJobClass;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.MainActivity;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindJobAdapter extends RecyclerView.Adapter<FindJobAdapter.ProductViewHolder> {


    private Context mCtx;
    public static List<FindJobClass> findJobClassList;
    StandardProgressDialog loading,success,failed;
    PreferenceManagerLogin session;
    public FindJobAdapter(Context mCtx, List<FindJobClass> findJobClassList) {
        this.mCtx = mCtx;
        this.findJobClassList = findJobClassList;

        loading = new StandardProgressDialog(mCtx);
        success = new StandardProgressDialog(mCtx);
        failed = new StandardProgressDialog(mCtx);
        session = new PreferenceManagerLogin(mCtx);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_find_job_adapter, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final FindJobClass findJobClass = findJobClassList.get(position);

        holder.textView_jobid.setText(findJobClass.getJob_id());
        holder.textView_time.setText(findJobClass.getDate_created());
        holder.textView_job_name.setText(findJobClass.getJob_name());
        holder.textView_employer_name.setText(findJobClass.getEmployer_name());
        holder.textView_working_date.setText(findJobClass.getDate_from()+" - "+findJobClass.getDate_to());
        holder.textView_working_hour.setText(findJobClass.getWorking_time_from()+" - "+findJobClass.getWorking_time_to());
        holder.textView_location.setText(findJobClass.getJob_location_address());
        holder.textView_details.setText(findJobClass.getJob_details());
        holder.textView_price.setText(findJobClass.getJob_salary_hour());

        holder.imageView_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TEMP_PICKUP = findJobClass.getJob_location_latlng().split(",");
                double TEMP_LAN_PICKUP = Double.parseDouble(TEMP_PICKUP[0]);
                double TEMP_LOG_PICKUP = Double.parseDouble(TEMP_PICKUP[1]);
                String strUri = "http://maps.google.com/maps?q=loc:" + TEMP_LAN_PICKUP + "," + TEMP_LOG_PICKUP + " (" + "PickUp Point" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                mCtx.startActivity(intent);
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mCtx);
                dialog.setContentView(R.layout.dialog_custom_accept);

                Button button_accept = dialog.findViewById(R.id.button_accept);
                Button button_cancel = dialog.findViewById(R.id.button_cancel);

                TypeFaceClass.setTypeFaceButton(button_accept,mCtx.getApplicationContext());
                TypeFaceClass.setTypeFaceButton(button_cancel,mCtx.getApplicationContext());

                button_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        acceptOrder(findJobClass.getJob_id(), "JR 1");
                    }
                });

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return findJobClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView textView_job_name_title,textView_employer_name_title,textView_working_date_title,textView_working_hour_title,textView_location_title,
                textView_details_title,textView_price_title;
        TextView textView_job_name,textView_employer_name,textView_working_date,textView_working_hour,textView_location,
                textView_details,textView_price;
        TextView textView_jobid,textView_time;
        ImageView imageView_maps;
        LinearLayout view;
        public ProductViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view);
            imageView_maps = itemView.findViewById(R.id.imageView_maps);
            textView_job_name_title = itemView.findViewById(R.id.textView_job_name_title);
            textView_employer_name_title = itemView.findViewById(R.id.textView_employer_name_title);
            textView_working_date_title = itemView.findViewById(R.id.textView_working_date_title);
            textView_working_hour_title = itemView.findViewById(R.id.textView_working_hour_title);
            textView_location_title = itemView.findViewById(R.id.textView_location_title);
            textView_details_title = itemView.findViewById(R.id.textView_details_title);
            textView_price_title = itemView.findViewById(R.id.textView_price_title);

            textView_job_name = itemView.findViewById(R.id.textView_job_name);
            textView_employer_name = itemView.findViewById(R.id.textView_employer_name);
            textView_working_date = itemView.findViewById(R.id.textView_working_date);
            textView_working_hour = itemView.findViewById(R.id.textView_working_hour);
            textView_location = itemView.findViewById(R.id.textView_location);
            textView_details = itemView.findViewById(R.id.textView_details);
            textView_price = itemView.findViewById(R.id.textView_price);

            textView_jobid = itemView.findViewById(R.id.textView_jobid);
            textView_time = itemView.findViewById(R.id.textView_time);

            TypeFaceClass.setTypeFaceTextView(textView_job_name_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_job_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_employer_name_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_employer_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_working_date_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_working_date,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_working_hour_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_working_hour,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_location_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_location,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_details_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_details,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_price_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_price,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_jobid,mCtx);TypeFaceClass.setTypeFaceTextView(textView_time,mCtx);
        }
    }


    private void acceptOrder(final String job_id, final String jrs_id){
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();

                HashMap<String, String> user = session.getUserDetails();
                String jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);
                return uLink.acceptJob(job_id,jr_id);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();

                try {
                    if(jsonObject.getString("status").equals("true")){

                        success.dialogSuccess(mCtx,jsonObject.getString("message"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                success.dialogSuccessClose();
                            }
                        }, 2000);

                        notifyDataSetChanged();
                    }else {
                        failed.dialogFailed(mCtx,jsonObject.getString("message"));
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

}
