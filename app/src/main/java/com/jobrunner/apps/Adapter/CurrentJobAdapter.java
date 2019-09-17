package com.jobrunner.apps.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobrunner.apps.Class.CurrentJobClass;
import com.jobrunner.apps.Class.HistoryClass;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.R;

import java.util.List;

public class CurrentJobAdapter extends RecyclerView.Adapter<CurrentJobAdapter.ProductViewHolder> {


    private Context mCtx;
    public static List<CurrentJobClass> currentJobClassList;

    public CurrentJobAdapter(Context mCtx, List<CurrentJobClass> currentJobClassList) {
        this.mCtx = mCtx;
        this.currentJobClassList = currentJobClassList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_current_job_adapter, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final CurrentJobClass currentJobClass = currentJobClassList.get(position);


        holder.textView_jobid.setText(currentJobClass.getJob_id());
        holder.textView_time.setText(currentJobClass.getDate_created());
        holder.textView_job_name.setText(currentJobClass.getJob_name());
        holder.textView_employer_name.setText(currentJobClass.getEmployer_name());
        holder.textView_working_date.setText(currentJobClass.getDate_from()+" - "+currentJobClass.getDate_to());
        holder.textView_working_hour.setText(currentJobClass.getWorking_time_from()+" - "+currentJobClass.getWorking_time_to());
        holder.textView_location.setText(currentJobClass.getJob_location_address());
        holder.textView_details.setText(currentJobClass.getJob_details());
        holder.textView_price.setText(currentJobClass.getJob_salary_hour());

        holder.imageView_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TEMP_PICKUP = currentJobClass.getJob_location_latlng().split(",");
                double TEMP_LAN_PICKUP = Double.parseDouble(TEMP_PICKUP[0]);
                double TEMP_LOG_PICKUP = Double.parseDouble(TEMP_PICKUP[1]);
                String strUri = "http://maps.google.com/maps?q=loc:" + TEMP_LAN_PICKUP + "," + TEMP_LOG_PICKUP + " (" + "PickUp Point" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                mCtx.startActivity(intent);
            }
        });

        if(currentJobClass.getStatus_job().equals("1")){
            holder.textView_status_job.setText("Job accepted");
        }
        if(currentJobClass.getStatus_job().equals("0")){
            holder.textView_status_job.setText("Not accepted");
        }
        if(currentJobClass.getStatus_job().equals("2")){
            holder.textView_status_job.setText("Job on going");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(mCtx);
        dialog.setContentView(R.layout.dialog_enter_code);


        dialog.show();
    }

    @Override
    public int getItemCount() {
        return currentJobClassList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView textView_job_name_title,textView_employer_name_title,textView_working_date_title,textView_working_hour_title,textView_location_title,
                textView_details_title,textView_price_title,textView_status_job_title;
        TextView textView_job_name,textView_employer_name,textView_working_date,textView_working_hour,textView_location,
                textView_details,textView_price,textView_status_job;
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

            textView_status_job_title = itemView.findViewById(R.id.textView_status_job_title);
            textView_status_job = itemView.findViewById(R.id.textView_status_job);

            TypeFaceClass.setTypeFaceTextView(textView_job_name_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_job_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_employer_name_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_employer_name,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_working_date_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_working_date,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_working_hour_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_working_hour,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_location_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_location,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_details_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_details,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_price_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_price,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_jobid,mCtx);TypeFaceClass.setTypeFaceTextView(textView_time,mCtx);
            TypeFaceClass.setTypeFaceTextView(textView_status_job_title,mCtx);TypeFaceClass.setTypeFaceTextView(textView_status_job,mCtx);

        }
    }

}
