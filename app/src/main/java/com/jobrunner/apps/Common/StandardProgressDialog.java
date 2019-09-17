package com.jobrunner.apps.Common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.jobrunner.apps.R;

/**
 * Created by iqbalbaharum on 23/01/2018.
 */

public class StandardProgressDialog extends ProgressDialog {
    AlertDialog mDialogSuccess,mDialogCancel;
    public StandardProgressDialog(Context context) {
        super(context);

        setMessage("Loading");
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setCancelable(true);

    }

    public void dialogSuccess(Context activity, String words){
        AlertDialog.Builder mBuilderSuccess = new AlertDialog.Builder(activity, R.style.CustomDialog);
        final View mViews = getLayoutInflater().inflate(R.layout.dialog_custom_success, null);
        mBuilderSuccess.setView(mViews);
        mDialogSuccess = mBuilderSuccess.create();
        TextView textView_success = mViews.findViewById(R.id.textView_success);
        textView_success.setText(words);

        mDialogSuccess.setCancelable(false);
        mDialogSuccess.show();
    }

    public void dialogFailed(Context activity, String words){
        AlertDialog.Builder mBuilderSuccess = new AlertDialog.Builder(activity, R.style.CustomDialog);
        final View mViews = getLayoutInflater().inflate(R.layout.dialog_custom_cancel, null);
        mBuilderSuccess.setView(mViews);
        mDialogCancel = mBuilderSuccess.create();

        TextView textView_success = mViews.findViewById(R.id.textView_success);
        textView_success.setText(words);

        mDialogCancel.setCancelable(false);
        mDialogCancel.show();
    }

    public void dialogCancelClose(){
        mDialogCancel.dismiss();
    }
    public void dialogSuccessClose(){
        mDialogSuccess.dismiss();
    }
}
