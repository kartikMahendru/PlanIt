package com.example.kartik.foodmanagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UploadActivity extends AppCompatActivity {
    private EditText description, suffFor;
    private ImageView imageView;
    private ImageView choose, upload;
    private CircleButton circleButton;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    // private ProgressBar progressBar;
    private Uri ImageUri;
    private long time = System.currentTimeMillis();
    private LatLng latLng;
    private String MOB_NUMBER="ashu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        description = findViewById(R.id.editText6);
        suffFor = findViewById(R.id.num);
        imageView = findViewById(R.id.imageView);
        // progressBar = findViewById(R.id.progressBar2);
        choose = findViewById(R.id.button2);
        upload = findViewById(R.id.button4);
        circleButton = findViewById(R.id.goBack);
        Intent intent = getIntent();
        //MOB_NUMBER = intent.getStringExtra("MOB_NUMBER");
        double[] location=intent.getDoubleArrayExtra("Location");
        assert location != null;
        latLng=new LatLng(location[0],location[1]);
        FirebaseApp.initializeApp(this);

        storageReference = FirebaseStorage.getInstance().getReference("food-request");
        databaseReference = FirebaseDatabase.getInstance().getReference("food-request");
        choose.setOnClickListener(view -> openFileChooser());
        upload.setOnClickListener(view -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
            imageView.setBackgroundColor(Color.WHITE);
        }
    }

    private void uploadFile()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if(ImageUri!=null){
            StorageReference fileReference = storageReference.child(time+"."+getFileExtension(ImageUri));
            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();

                        new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("File uploaded successfully!")
                                .show();

                        storageReference.child(time+"."+getFileExtension(ImageUri)).getDownloadUrl().addOnSuccessListener(uri -> {
                            String url = uri.toString();
                            Food food = new Food(description.getText().toString().trim(), latLng.latitude,latLng.longitude,suffFor.getText().toString().trim(),url);
                            String uploadID = time+"";
                            databaseReference.child(uploadID).setValue(food);
                        });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        new SweetAlertDialog(UploadActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Upload failed!")
                                .setContentText("Something went wrong!")
                                .show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded "+ (int)progress + "%...");
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

    public void createAndShare(View view) {
        Intent x = new Intent(UploadActivity.this, MapsMarkLocation.class);
        startActivity(x);
        finish();
    }
}