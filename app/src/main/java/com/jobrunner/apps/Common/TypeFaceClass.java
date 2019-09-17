package com.jobrunner.apps.Common;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TypeFaceClass {

    public static void setTypeFaceEditText(EditText editText, Context context){
        editText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf"));
    }

    public static void setTypeFaceTextView(TextView textView, Context context){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf"));
    }
    public static void setTypeFaceButton(Button button, Context context){
        button.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf"));
    }

    public static void setTypeFaceTextInputEditText(TextInputEditText textInputEditText, Context context){
        textInputEditText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf"));
    }
}
