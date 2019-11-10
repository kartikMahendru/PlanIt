package com.example.kartik.foodmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    DatabaseReference databaseUser;
    private EditText editTextName;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText editTextcPassword;
    // Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextcPassword=(EditText)findViewById(R.id.editTextcPassword);
        progressDialog = new ProgressDialog(this);
        databaseUser= FirebaseDatabase.getInstance().getReference("Users");

//        spinner=(Spinner)findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.types,R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
    }

    public void SignUpButton(View v){
        final String semail = editTextEmail.getText().toString().trim();
        String spassword  = editTextPassword.getText().toString().trim();
        String scpassword= editTextcPassword.getText().toString().trim();
        // final String sprofession = spinner.getSelectedItem().toString();
        final String sname=editTextName.getText().toString();

        if(TextUtils.isEmpty(semail)){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter email!")
                    .show();
            return;
        }

        if(TextUtils.isEmpty(spassword)){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter password!")
                    .show();
            return;
        }

        if (spassword.equals(scpassword)==false){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please re-enter password!")
                    .show();
            return;
        }

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(semail, spassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            Toast.makeText(SignupActivity.this,"Registered successfully!",Toast.LENGTH_LONG).show();
                            FirebaseAuth currentFirebaseUser = FirebaseAuth.getInstance();

                            String resultemail = semail.replaceAll("[-+.^:,@*]","");
                            User user=new User(sname,semail,"Other");
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(resultemail).setValue(user);

                            currentFirebaseUser.signOut();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }else{
                            //display some message here
                            new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Registration error!")
                                    .show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void SignInText(View v){
        Intent i=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}