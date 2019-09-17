package com.jobrunner.apps.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobrunner.apps.Class.FindJobClass;
import com.jobrunner.apps.Class.HistoryClass;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class HistoryJobAdapter extends RecyclerView.Adapter<HistoryJobAdapter.ProductViewHolder> {


    private Context mCtx;
    public static List<HistoryClass> historyClassList;

    public HistoryJobAdapter(Context mCtx, List<HistoryClass> historyClassList) {
        this.mCtx = mCtx;
        this.historyClassList = historyClassList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_history_job_adapter, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final HistoryClass historyClass = historyClassList.get(position);

        holder.textView_jobid.setText(historyClass.getJob_id());
        holder.textView_price.setText("RM "+historyClass.getJob_salary_total());
        holder.textView_job_name.setText(historyClass.getJob_name());
        holder.textView_employer_name.setText(historyClass.getEmployer_name());

        if(historyClass.getStatus_job().equals("3")){
            holder.textView_job_status.setText("Job completed");
        }else if(historyClass.getStatus_job().equals("4")){
            holder.textView_job_status.setText("Job Cancelled");
        }else if(historyClass.getStatus_job().equals("5")){
            holder.textView_job_status.setText("Job failed");
        }

        if(historyClass.getStatus_payment().equals("0")){
            holder.textView_job_payment_status.setText("On going");
        }else if(historyClass.getStatus_payment().equals("1")){
            holder.textView_job_payment_status.setText("Payment completed");
        }else if(historyClass.getStatus_payment().equals("2")){
            holder.textView_job_payment_status.setText("Payment failed");
        }


        holder.textView_feedback.setText("on the way");


    }

    @Override
    public int getItemCount() {
        return historyClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView textView_jobid,textView_price;
        TextView textView_job_name_title,textView_employer_name_title,textView_job_status_title,textView_job_payment_status_title,textView_feedback_title;
        TextView textView_job_name,textView_employer_name,textView_job_status,textView_job_payment_status,textView_feedback;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textView_jobid = itemView.findViewById(R.id.textView_jobid);
            textView_price = itemView.findViewById(R.id.textView_price);

            textView_job_name_title = itemView.findViewById(R.id.textView_job_name_title);
            textView_employer_name_title = itemView.findViewById(R.id.textView_employer_name_title);
            textView_job_status_title = itemView.findViewById(R.id.textView_job_status_title);
            textView_job_payment_status_title = itemView.findViewById(R.id.textView_job_payment_status_title);
            textView_feedback_title = itemView.findViewById(R.id.textView_feedback_title);

            textView_job_name = itemView.findViewById(R.id.textView_job_name);
            textView_employer_name = itemView.findViewById(R.id.textView_employer_name);
            textView_job_status = itemView.findViewById(R.id.textView_job_status);
            textView_job_payment_status = itemView.findViewById(R.id.textView_job_payment_status);
            textView_feedback = itemView.findViewById(R.id.textView_feedback);

            TypeFaceClass.setTypeFaceTextView(textView_jobid,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_price,mCtx);

            TypeFaceClass.setTypeFaceTextView(textView_job_name_title,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_employer_name_title,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_job_status_title,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_job_payment_status_title,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_feedback_title,mCtx);

            TypeFaceClass.setTypeFaceTextView(textView_job_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_employer_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_job_status,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_job_payment_status,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_feedback,mCtx);
        }
    }

}
