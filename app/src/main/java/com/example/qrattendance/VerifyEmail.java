package com.example.qrattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmail extends AppCompatActivity {
    Button verifyMail;
    EditText getemail;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        fAuth = FirebaseAuth.getInstance();
        getemail = findViewById(R.id.enterEmail);
        verifyMail = findViewById(R.id.verifyEmail);


        verifyMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getemail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter username !",Toast.LENGTH_LONG).show();
                    return;
                }

                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Mail sent to registered Email ID",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(VerifyEmail.this, MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Mail sent to registered Email ID",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}