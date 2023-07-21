package com.example.qrattendance;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    EditText username,password;
    Button login;
    Intent mainPage;
    TextView forgot_pwd;
    Intent verifyEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        login = findViewById(R.id.login);
        forgot_pwd = findViewById(R.id.forgotPWD);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        Bundle studentInfo = new Bundle();

        if(fAuth.getCurrentUser() != null){
            if (fAuth.getCurrentUser().getEmail().toString().equals("admin@nuv.ac.in")){
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
                finish();
            }
            else{
                mainPage = new Intent(MainActivity.this, Home.class);
                String email = fAuth.getCurrentUser().getEmail().toString();
                studentInfo.putString("enrollment", email.substring(0,8));
                mainPage.putExtras(studentInfo);
                startActivity(mainPage);
                finish();
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString().trim();
                String passWord = password.getText().toString().trim();
                mainPage = new Intent(MainActivity.this, Home.class);

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(getApplicationContext(),"Enter E-mail !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(passWord)){
                    Toast.makeText(getApplicationContext(),"Enter password !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(passWord.length() < 8){
                    Toast.makeText(getApplicationContext(),"Password length less then 8 characters !",Toast.LENGTH_LONG).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(userName,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Logged In Successfully !",Toast.LENGTH_LONG).show();
                            if (userName.equals("admin@nuv.ac.in") && passWord.equals("helloworld")){
                                startActivity(new Intent(MainActivity.this,AdminActivity.class));
                                finish();
                            }
                            else {
                                String student_enrollment = userName.substring(0,8);
                                studentInfo.putString("enrollment",student_enrollment);
                                Log.i("Student Enrollment",student_enrollment);
                                mainPage.putExtras(studentInfo);
                                startActivity(mainPage);
                            }
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyEmail = new Intent(MainActivity.this, VerifyEmail.class);
                startActivity(verifyEmail);
            }
        });
    }
}