package com.example.qrattendance;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DashboardFragment extends Fragment {
    View view;
    Button applyLeave,academic_calendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard,container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = database.getReference();

        Bundle studentInfo = getArguments();
        String enrollment = Objects.requireNonNull(studentInfo).getString("enrollment");
        TableLayout layout = view.findViewById(R.id.table);

        dbreference.child("Students").child(enrollment).child("Subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnap: snapshot.getChildren()){
                        TableRow new_row = new TableRow(getActivity());
                        TextView new_text = new TextView(getActivity());
                        TextView new_text2 = new TextView(getActivity());
                        String subjects = datasnap.child("name").getValue().toString();
                        int present = datasnap.child("present").getValue(Integer.class);
                        int conducted = datasnap.child("conducted").getValue(Integer.class);
                        String percentage = "0";
                        if(conducted!=0){
                            float attendance = ((float)present/(float)conducted)*100;
                            percentage = Float.toString(attendance);
                        }
                        new_text.setText(subjects);
                        new_text.setTextSize(20);
                        new_text.setTextColor(Color.BLACK);
                        new_text.setPadding(20,20,20,20);
                        new_text.setBackgroundResource(R.drawable.curved_borders);
                        new_text.setWidth(700);
                        new_text2.setText(percentage);
                        new_text2.setTextSize(20);
                        new_text2.setTextColor(Color.BLACK);
                        new_text2.setPadding(20,20,20,20);
                        new_text2.setBackgroundResource(R.drawable.curved_borders);
                        new_text2.setWidth(200);
                        new_text2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(10,10,10,10);
                        new_row.setLayoutParams(lp);
                        new_row.addView(new_text);
                        new_row.addView(new_text2);
                        layout.addView(new_row);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"error "+error,Toast.LENGTH_SHORT).show();
            }
        });

        applyLeave = view.findViewById(R.id.apply_leave);
        applyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction().addToBackStack(null);
                Fragment leaveApplication = new Leave_Application();
                leaveApplication.setArguments(studentInfo);
                fr.replace(R.id.frameLayout,leaveApplication);
                fr.commit();
            }
        });

        academic_calendar = view.findViewById(R.id.calendar);
        academic_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction().addToBackStack(null);
                Fragment academicCalendar= new AcademicCalendarFragment();
                fr.replace(R.id.frameLayout,academicCalendar);
                fr.commit();
            }
        });
        return view;
    }
}