package com.example.kartik.foodmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Welcome extends AppCompatActivity {

    EditText editText;
    private FirebaseAuth firebaseAuth;
    String emailId;
    String resultemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent in=getIntent();
        emailId=in.getExtras().getString("emailId");
        resultemail = emailId.replaceAll("[-+.^:,@]","");
        Toast.makeText(getApplicationContext(),emailId,Toast.LENGTH_LONG).show();
        editText=findViewById(R.id.editText);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    





    public void createAndShare (View v){
        String eventName=editText.getText().toString();
        String id=String.format("%04d",new Random().nextInt(10000));

        DatabaseReference mDatabase;
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(resultemail).child("events").setValue(id);

        Event event=new Event(eventName,"mehul hutiya",0);
        mDatabase.child("events").child(id).setValue(event);
    }
}
