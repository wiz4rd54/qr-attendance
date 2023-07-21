package com.example.qrattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    View view;
    TextView name,enrollment,course,year,semester;
    Button logOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle studentInfo = getArguments();
        String stu_name = studentInfo.getString("name");
        String stu_enrollment = studentInfo.getString("enrollment");
        String stu_course = studentInfo.getString("course");
        String stu_year = studentInfo.getString("year");
        String stu_semester = studentInfo.getString("semester");

        name = view.findViewById(R.id.name);
        name.setText(stu_name);
        enrollment = view.findViewById(R.id.enrollment);
        enrollment.setText(stu_enrollment);
        course = view.findViewById(R.id.course);
        course.setText(stu_course);
        year = view.findViewById(R.id.year);
        year.setText(stu_year);
        semester = view.findViewById(R.id.semester);
        semester.setText(stu_semester);

        logOut = view.findViewById(R.id.button);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Context context = view.getContext();
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}