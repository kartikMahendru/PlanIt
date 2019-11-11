package com.example.kartik.foodmanagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Welcome extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private byte[] imageDataInByte;
    private ImageView imageView;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    EditText editText;
    private FirebaseAuth firebaseAuth;
    String emailId;
    String resultemail;
    private String downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap<String, String> details = sm.getUserDetails();
        String emailId = details.get("emailID");
        resultemail = emailId.replaceAll("[-+.^:,@*]","");

        editText=findViewById(R.id.editText);
        imageView=findViewById(R.id.imageView);
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void chooseImage(View v)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try {

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                imageDataInByte = byteArrayOutputStream.toByteArray();
                Log.d("TAG :: ","Image selected :: " + filePath.toString());
                Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.fitCenterTransform()).into(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadImage(View v)
    {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final String fileName = STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath);
            final StorageReference sRef = storageReference.child(fileName);

            sRef.putBytes(imageDataInByte)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            new SweetAlertDialog(Welcome.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("File uploaded successfully!")
                                    .show();

                            Task<Uri> s= sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    downloadUrl=task.getResult().toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            new SweetAlertDialog(Welcome.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Upload failed!")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded "+ (int)progress + "%...");
                        }
                    });
        }
        else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("No image selected!")
                    .show();
        }
    }

    public void createAndShare (View v)
    {
        // get event name
        final String eventName = editText.getText().toString();

        // generate a unique 4 digit id for an event
        String id = String.format("%04d", new Random().nextInt(10000));

        if(eventName.equals(""))
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter event name!")
                    .show();
        }
        else if(downloadUrl.equals(""))
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please upload an image!")
                    .show();
        }
        else
        {
            Event event = new Event(eventName, downloadUrl, 0, id);
            mDatabase.child("users").child(resultemail).child("events").child(id).setValue(event);
            mDatabase.child("events").child(id).setValue(event);

            Intent i = new Intent(getApplicationContext(), EventManager.class);
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("emailId", resultemail);
            startActivity(i);
            finish();
        }
    }
}