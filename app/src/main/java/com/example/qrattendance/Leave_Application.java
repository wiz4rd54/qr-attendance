package com.example.qrattendance;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class Leave_Application extends Fragment {
    EditText dateFrom,dateTo, reasons;
    Button apply;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave__application, container, false);
        Bundle studentInfo = getArguments();
        String enrollmentNo = Objects.requireNonNull(studentInfo).getString("enrollment");
        apply = view.findViewById(R.id.button);
        dateFrom = view.findViewById(R.id.dateFrom);
        dateTo = view.findViewById(R.id.dateTo);
        reasons = view.findViewById(R.id.reason);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = db.getReference();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromDate = dateFrom.getText().toString();
                String toDate = dateTo.getText().toString();
                String leaveReason = reasons.getText().toString();
                String key = fromDate+"-"+toDate;
                Context context = getContext();

                if (fromDate.isEmpty() ){
                    Toast.makeText(context,"Please enter From date",Toast.LENGTH_SHORT ).show();
                }
                if (toDate.isEmpty()){
                    Toast.makeText(context,"Please enter To date",Toast.LENGTH_SHORT ).show();
                }
                if (leaveReason.isEmpty()){
                    Toast.makeText(context,"Give reason for leave.",Toast.LENGTH_SHORT ).show();
                }

                Boolean isValidFrom = validateDate(fromDate);
                Boolean isValidTo = validateDate(toDate);
                if (isValidFrom && isValidTo){
                    Leave newLeave = new Leave(fromDate,toDate,leaveReason);
                    dbreference.child("Students").child(enrollmentNo).child("Leave").child(key).setValue(newLeave);
                    Toast.makeText(context,"Successfully Applied !!",Toast.LENGTH_SHORT ).show();
                    dateFrom.setText("");
                    dateTo.setText("");
                    reasons.setText("");
                }
                else{
                    Toast.makeText(context,"Enter proper date format",Toast.LENGTH_SHORT ).show();
                }
            }
        });
        return view;
    }

    public boolean validateDate(String date){
        String format = "MM-dd-yyyy";
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e){
            return false;
        }
        return true;
    }
}