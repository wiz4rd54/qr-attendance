package com.example.qrattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle studentInfo = getIntent().getExtras();
        String enrollmentNo = studentInfo.getString("enrollment");

        Bundle studentdetails = new Bundle();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbreference= db.getReference("Students").child(enrollmentNo);
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String stu_enrollment = snapshot.child("enrollment").getValue(String.class);
                String stu_name = snapshot.child("name").getValue(String.class);
                String stu_course = snapshot.child("course").getValue(String.class);
                String stu_year = snapshot.child("year").getValue(String.class);
                String stu_semester = snapshot.child("semester").getValue(String.class);

                studentdetails.putString("enrollment",stu_enrollment);
                studentdetails.putString("name",stu_name);
                studentdetails.putString("course",stu_course);
                studentdetails.putString("year",stu_year);
                studentdetails.putString("semester",stu_semester);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,"error"+error,Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ActionBar bar = getSupportActionBar();
        Objects.requireNonNull(bar).setTitle(enrollmentNo);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#04BF8A")));
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dashboard:
                    replaceFragment(new DashboardFragment(), "dashboard",studentdetails);
                    break;
                case R.id.progress:
                    replaceFragment(new ProgressFragment(), "progress",studentdetails);
                    break;
                case R.id.scan:
                    Intent toScanQr = new Intent(Home.this, ScanActivity.class);
                    startActivity(toScanQr, studentdetails);
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment(), "profile",studentdetails);
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle studentInfo = getIntent().getExtras();
        String enrollmentNo = studentInfo.getString("enrollment");
        Bundle studentdetails = new Bundle();
        studentdetails.putString("enrollment",enrollmentNo);
        replaceFragment(new DashboardFragment(), "dashboard",studentdetails);
    }

    private void replaceFragment(Fragment fragment, String tag_name, Bundle studentInformation){
        fragment.setArguments(studentInformation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(tag_name);
        fragmentTransaction.replace(R.id.frameLayout,fragment, tag_name);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}