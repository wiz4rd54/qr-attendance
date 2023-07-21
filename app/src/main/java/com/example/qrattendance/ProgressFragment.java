package com.example.qrattendance;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgressFragment extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_progress,container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = database.getReference();

        Bundle studentInfo = getArguments();
        String enrollment = Objects.requireNonNull(studentInfo).getString("enrollment");
        List<Course> courses = new ArrayList<Course>();
        LinearLayout linearLayout = view.findViewById(R.id.mainView);

        dbreference.child("Students").child(enrollment).child("Subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){
                if (snapshot.exists()) {
                    for (DataSnapshot datasnap : snapshot.getChildren()) {

                        CardView new_card = new CardView(getActivity());
                        TableLayout new_table = new TableLayout(getActivity());
                        TableRow new_row1 = new TableRow(getActivity());
                        TableRow new_row2 = new TableRow(getActivity());
                        TableRow new_row3 = new TableRow(getActivity());
                        TextView subject_name = new TextView(getActivity());
                        TextView presentText = new TextView(getActivity());
                        TextView conductedText = new TextView(getActivity());
                        TextView percentagecount = new TextView(getActivity());

                        String subjects = datasnap.getKey();
                        String subjectName = datasnap.child("name").getValue(String.class);
                        int present = datasnap.child("present").getValue(Integer.class);
                        int conducted = datasnap.child("conducted").getValue(Integer.class);
                        String percentage = "0";
                        if (conducted != 0) {
                            float attendance = (float) present / (float) conducted * 100;
                            percentage = Float.toString(attendance);
                        }

                        subject_name.setText(subjectName);
                        subject_name.setTextSize(20);
                        subject_name.setTextColor(Color.WHITE);
                        subject_name.setPadding(20, 20, 20, 20);
                        subject_name.setWidth(800);
                        int color = 0xff024059;
                        subject_name.setBackgroundColor(color);
                        subject_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        presentText.setText("Present In : " + Integer.toString(present));
                        presentText.setTextSize(18);
                        presentText.setTextColor(Color.BLACK);
                        presentText.setPadding(30, 20, 20, 20);
                        presentText.setWidth(800);

                        conductedText.setText("Total conducted : " + Integer.toString(conducted));
                        conductedText.setTextSize(18);
                        conductedText.setTextColor(Color.BLACK);
                        conductedText.setPadding(30, 20, 20, 20);
                        conductedText.setWidth(800);

                        percentagecount.setText("Percentage : " + percentage);
                        percentagecount.setTextSize(18);
                        percentagecount.setTextColor(Color.BLACK);
                        percentagecount.setPadding(30, 20, 20, 20);
                        percentagecount.setWidth(800);

                        TableRow.LayoutParams tr = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                        presentText.setLayoutParams(tr);
                        conductedText.setLayoutParams(tr);
                        percentagecount.setLayoutParams(tr);
                        new_row1.addView(presentText);
                        new_row2.addView(conductedText);
                        new_row3.addView(percentagecount);

                        new_table.addView(subject_name);
                        new_table.addView(new_row1);
                        new_table.addView(new_row2);
                        new_table.addView(new_row3);

                        CardView.LayoutParams cp = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT);
                        new_table.setLayoutParams(cp);
                        new_card.addView(new_table);
                        new_card.setBackgroundResource(R.drawable.curved_borders);

                        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        fp.setMargins(10,10,10,10);
                        new_card.setLayoutParams(fp);
                        linearLayout.addView(new_card);
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
    });
        return view;
    }
}