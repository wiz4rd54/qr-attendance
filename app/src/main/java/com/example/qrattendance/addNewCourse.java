package com.example.qrattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qrattendance.databinding.ActivityAddNewCourseBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addNewCourse extends AppCompatActivity {
    ActivityAddNewCourseBinding binding;
    String program,courseName,year,semester,courseCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar bar = getSupportActionBar();
        bar.hide();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = db.getReference();

        binding.adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                program = binding.program.getText().toString();
                courseName = binding.coursename.getText().toString();
                year = binding.year.getText().toString();
                semester = binding.semester.getText().toString();
                courseCode = binding.coursecode.getText().toString();

                if(TextUtils.isEmpty(program)){
                    Toast.makeText(getApplicationContext(),"Enter program !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(courseName)){
                    Toast.makeText(getApplicationContext(),"Enter course name !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(courseCode)){
                    Toast.makeText(getApplicationContext(),"Enter course code !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(year)){
                    Toast.makeText(getApplicationContext(),"Enter year !",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(semester)){
                    Toast.makeText(getApplicationContext(),"Enter semester !",Toast.LENGTH_SHORT).show();
                    return;
                }

                Course new_course = new Course(courseName, courseCode,0,0,computeDate());
                dbreference.child("Students").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datasnap : snapshot.getChildren()) {
                            String key = datasnap.getKey();
                            Log.i("key",key);
                            if (key!=null) {
                                String spgrm = datasnap.child("course").getValue(String.class);
                                String syear = datasnap.child("year").getValue(String.class);
                                String sem = datasnap.child("semester").getValue(String.class);
                                if (spgrm.equals(program) && syear.equals(year) && sem.equals(semester) ){
                                    dbreference.child("Students").child(key).child("Subjects").child(courseCode).setValue(new_course);
                                }
                            }
                        }
                        Toast.makeText(addNewCourse.this,"Successfully added !",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(addNewCourse.this,AdminActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(addNewCourse.this,"Error :- " + error,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private String computeDate() {
                String date = java.time.LocalDate.now().toString();
                return date;
            }
        });
    }
}