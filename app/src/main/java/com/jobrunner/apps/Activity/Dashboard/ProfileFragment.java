package com.jobrunner.apps.Activity.Dashboard;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jobrunner.apps.Common.StandardProgressDialog;
import com.jobrunner.apps.Common.TypeFaceClass;
import com.jobrunner.apps.Connection.UrlLink;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerCompleteProfile;
import com.jobrunner.apps.PreferanceManager.PreferenceManagerLogin;
import com.jobrunner.apps.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView textView_header,textView_gender,textView_maritial_status,textView_bank_name;
    Spinner spinner_gender,spinner_maritial_status,spinner_bank_name;
    TextInputEditText et_firstName,et_secondName,et_address,et_postCode,et_phoneNo,et_emergencyNo,et_age,et_account_bank_no,et_skill_one,
            et_skill_two,et_skill_three;
    CircleImageView profile_image;
    String jr_id,email,downloadUrl = "",status_profile;
    PreferenceManagerLogin session;
    StandardProgressDialog loading,success,failed;
    private Uri outPutfileUri;
    private int GALLERY_PROFILE = 1,CAMERA_PROFILE = 2;
    public static Task<Uri> fbSignatureURL;
    private FirebaseAuth mAuth;
    public static StorageReference mStorage;
    private String storageLocation;
    PreferenceManagerCompleteProfile session_completed;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


        session = new PreferenceManagerLogin(getContext());
        session_completed = new PreferenceManagerCompleteProfile(getContext());
        loading = new StandardProgressDialog(getActivity().getWindow().getContext());
        success = new StandardProgressDialog(getActivity().getWindow().getContext());
        failed = new StandardProgressDialog(getActivity().getWindow().getContext());

        HashMap<String, String> user = session.getUserDetails();
        jr_id = user.get(PreferenceManagerLogin.KEY_JR_ID);
        email = user.get(PreferenceManagerLogin.KEY_EMAIL);

        declare(v);
        setFont();

        getProfileDetailStatus();

        mStorage = FirebaseStorage.getInstance().getReference();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog("P");
            }
        });
        Button button_save = v.findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_firstName.getText().toString().equals("")){
                    et_firstName.setError("Required");
                }else if(et_secondName.getText().toString().equals("")){
                    et_secondName.setError("Required");
                }else if(et_address.getText().toString().equals("")){
                    et_address.setError("Required");
                }else if(et_postCode.getText().toString().equals("")){
                    et_postCode.setError("Required");
                }else if(et_age.getText().toString().equals("")){
                    et_age.setError("Required");
                }else if(et_account_bank_no.getText().toString().equals("")){
                    et_account_bank_no.setError("Required");
                }else if(et_phoneNo.getText().toString().equals("")){
                    et_phoneNo.setError("Required");
                }else if(et_emergencyNo.getText().toString().equals("")){
                    et_emergencyNo.setError("Required");
                }else if(et_skill_one.getText().toString().equals("")){
                    et_skill_one.setError("Required");
                }else if(et_skill_two.getText().toString().equals("")){
                    et_skill_two.setError("Required");
                }else if(et_skill_three.getText().toString().equals("")){
                    et_skill_three.setError("Required");
                }else if(spinner_gender.getSelectedItem().toString().equals("Please choose gender")){
                    Toast.makeText(getActivity(),"Please choose gender",Toast.LENGTH_SHORT).show();
                }else if(spinner_maritial_status.getSelectedItem().toString().equals("Please choose maritial status")){
                    Toast.makeText(getActivity(),"Please choose maritial status",Toast.LENGTH_SHORT).show();
                }else if(spinner_bank_name.getSelectedItem().toString().equals("Please choose bank names")){
                    Toast.makeText(getActivity(),"Please choose bank names",Toast.LENGTH_SHORT).show();
                }else if(downloadUrl.equals("")){
                    Toast.makeText(getActivity(),"Please Upload profile picture",Toast.LENGTH_SHORT).show();
                }else{
                    loading.show();

                    save();

                }
            }
        });

        return v;
    }

    private void save() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.updateProfile(
                        jr_id,
                        email,
                        et_firstName.getText().toString(),
                        et_secondName.getText().toString(),
                        spinner_gender.getSelectedItem().toString(),
                        et_address.getText().toString(),
                        et_postCode.getText().toString(),
                        spinner_maritial_status.getSelectedItem().toString(),
                        et_age.getText().toString(),
                        spinner_bank_name.getSelectedItem().toString(),
                        et_account_bank_no.getText().toString(),
                        et_phoneNo.getText().toString(),
                        et_emergencyNo.getText().toString(),
                        et_skill_one.getText().toString(),
                        et_skill_two.getText().toString(),
                        et_skill_three.getText().toString(),
                        downloadUrl);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                loading.dismiss();
                Log.d("json",jsonObject.toString());
                try {
                    if(jsonObject.getString("status").equals("true")){
                        session_completed.createStatus("1");
                        success.dialogSuccess(getActivity(),jsonObject.getString("message"));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                success.dialogSuccessClose();
                            }
                        }, 2000);
                    }else{
                        session_completed.createStatus("0");
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

    private void getProfileDetailStatus() {
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getProfileJrId(jr_id);
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                loading.dismiss();

                try {
                    if(result.getString("status").equals("true")){
                        JSONObject jsonObject = new JSONObject(result.getString("result"));
                        if(!jsonObject.getString("firstname").equals(null)){
                            et_firstName.setText(jsonObject.getString("firstname"));
                        }
                        if(!jsonObject.getString("lastname").equals(null)){
                            et_secondName.setText(jsonObject.getString("lastname"));
                        }
                        if(!jsonObject.getString("address").equals(null)){
                            et_address.setText(jsonObject.getString("address"));
                        }
                        if(!jsonObject.getString("postcode").equals(null)){
                            et_postCode.setText(jsonObject.getString("postcode"));
                        }
                        if(!jsonObject.getString("age").equals(null)){
                            et_age.setText(jsonObject.getString("age"));
                        }
                        if(!jsonObject.getString("bank_account").equals(null)){
                            et_account_bank_no.setText(jsonObject.getString("bank_account"));
                        }
                        if(!jsonObject.getString("phone_no").equals(null)){
                            et_phoneNo.setText(jsonObject.getString("phone_no"));
                        }
                        if(!jsonObject.getString("emergency_no").equals(null)){
                            et_emergencyNo.setText(jsonObject.getString("emergency_no"));
                        }
                        if(!jsonObject.getString("addtional_skill_one").equals(null)){
                            et_skill_one.setText(jsonObject.getString("addtional_skill_one"));
                        }
                        if(!jsonObject.getString("addtional_skill_two").equals(null)){
                            et_skill_two.setText(jsonObject.getString("addtional_skill_two"));
                        }
                        if(!jsonObject.getString("addtional_skill_three").equals(null)){
                            et_skill_three.setText(jsonObject.getString("addtional_skill_three"));
                        }
                        if(!jsonObject.getString("gender").equals(null)){
                            spinner_gender.setSelection(getIndex(spinner_gender,jsonObject.getString("gender")));
                        }
                        if(!jsonObject.getString("martial_status").equals(null)){
                            spinner_maritial_status.setSelection(getIndex(spinner_maritial_status,jsonObject.getString("martial_status")));
                        }
                        if(!jsonObject.getString("bank_name").equals(null)){
                            spinner_bank_name.setSelection(getIndex(spinner_bank_name,jsonObject.getString("bank_name")));
                        }

                        if(!jsonObject.getString("profile_url").equals(null)){
                            Picasso.get().load(jsonObject.getString("profile_url")).into(profile_image);
                            downloadUrl = jsonObject.getString("profile_url");
                        }

                    }else{
                        session_completed.createStatus("0");
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                .setMessage(result.getString("message"))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                    status_profile = result.getString("status");
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

    private void declare(View v) {
        textView_header = v.findViewById(R.id.textView_header);
        textView_gender = v.findViewById(R.id.textView_gender);
        textView_maritial_status = v.findViewById(R.id.textView_maritial_status);
        textView_bank_name = v.findViewById(R.id.textView_bank_name);

        spinner_gender = v.findViewById(R.id.spinner_gender);
        spinner_maritial_status = v.findViewById(R.id.spinner_maritial_status);
        spinner_bank_name = v.findViewById(R.id.spinner_bank_name);

        et_firstName = v.findViewById(R.id.et_firstName);
        et_secondName = v.findViewById(R.id.et_secondName);
        et_address = v.findViewById(R.id.et_address);
        et_postCode = v.findViewById(R.id.et_postCode);
        et_phoneNo = v.findViewById(R.id.et_phoneNo);
        et_emergencyNo = v.findViewById(R.id.et_emergencyNo);
        et_age = v.findViewById(R.id.et_age);
        et_account_bank_no = v.findViewById(R.id.et_account_bank_no);
        et_skill_one = v.findViewById(R.id.et_skill_one);
        et_skill_two = v.findViewById(R.id.et_skill_two);
        et_skill_three = v.findViewById(R.id.et_skill_three);
        profile_image = v.findViewById(R.id.profile_image);
    }

    private void setFont() {
        TypeFaceClass.setTypeFaceTextView(textView_header,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_gender,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_maritial_status,getActivity());
        TypeFaceClass.setTypeFaceTextView(textView_bank_name,getActivity());

        TypeFaceClass.setTypeFaceTextInputEditText(et_firstName,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_secondName,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_address,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_postCode,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_phoneNo,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_emergencyNo,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_age,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_account_bank_no,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_skill_one,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_skill_two,getActivity());
        TypeFaceClass.setTypeFaceTextInputEditText(et_skill_three,getActivity());
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private void showPictureDialog(final String s) {
        android.support.v7.app.AlertDialog.Builder pictureDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new android.content.DialogInterface.OnClickListener() {
                    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                String ss = s + "G";
                                choosePhotoFromGallery(ss);
                                break;
                            case 1:
                                String sss = s + "C";
                                takePhotoFromCamera(sss);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera(String s) {
        Intent intent = null;
        int SEND = 0;
        if (s.equals("PC")) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            android.content.ContentValues values = new android.content.ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "profile.jpg");
            outPutfileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            SEND = CAMERA_PROFILE;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, SEND);
    }

    public void choosePhotoFromGallery(String s) {
        int SEND = 0;
        if (s.equals("PG")) {
            SEND = GALLERY_PROFILE;
        }
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, SEND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PROFILE) {
            if (data != null) {
                Uri contentURI = data.getData();
                outPutfileUri = contentURI;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    profile_image.setImageBitmap(bitmap);
                    uploadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA_PROFILE) {
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), outPutfileUri);
                Bitmap scaled = Bitmap.createScaledBitmap(thumbnail, 920, 576, true);
                profile_image.setImageBitmap(scaled);
                uploadImage();
                File file = new File(getFilePath(outPutfileUri));
                file.delete();
                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(getFilePath(outPutfileUri)))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
            final StorageReference ref = mStorage.child(jr_id+"/"+ UUID.randomUUID().toString());
            ref.putFile(outPutfileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downloadUrl = task.getResult().toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });

    }

    private String getFilePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            return picturePath;
        }
        return null;
    }


}
