package com.example.qrattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.qrattendance.databinding.ActivityAddstudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class addNewStudent extends AppCompatActivity {
    private FirebaseAuth fAuth;
    ActivityAddstudentBinding binding;
    String enrollment,name,email,password,course,year,semester;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddstudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar bar = getSupportActionBar();
        bar.hide();

        fAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbrefernce = db.getReference();

        binding.adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enrollment = binding.enrollment.getText().toString();
                name = binding.name.getText().toString();
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();
                course = binding.course.getText().toString();
                year = binding.year.getText().toString();
                semester = binding.semester.getText().toString();
                List<Object> courses_list = new ArrayList<>();
                if(TextUtils.isEmpty(enrollment)){
                    Toast.makeText(getApplicationContext(),"Enter enrollment number !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Enter student name !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(course)){
                    Toast.makeText(getApplicationContext(),"Enter course student is enrolled in !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(year)){
                    Toast.makeText(getApplicationContext(),"Enter year in which student studies !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(semester)){
                    Toast.makeText(getApplicationContext(),"Enter semester !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter email !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Enter password !",Toast.LENGTH_LONG).show();
                    return;
                }
                if(password.length() < 8){
                    Toast.makeText(getApplicationContext(),"Password length less then 8 characters !",Toast.LENGTH_LONG).show();
                    return;
                }

                Student student = new Student(enrollment,name,email,course,year,semester);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            dbrefernce.child("Students").child(enrollment).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(addNewStudent.this, "Student registered and added succesfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(addNewStudent.this,AdminActivity.class));
                                }
                            });
                        }
                        else {
                            Toast.makeText(addNewStudent.this,"Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}