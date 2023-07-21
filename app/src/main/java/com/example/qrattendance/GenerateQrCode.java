package com.example.qrattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

public class GenerateQrCode extends AppCompatActivity {
    Button generateCode,confirm;
    EditText subject;
    ImageView imView;
    String subjectCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = db.getReference();

        generateCode = findViewById(R.id.button);
        subject = findViewById(R.id.subjectcode);
        imView = findViewById(R.id.qrcode);
        confirm = findViewById(R.id.confirm);

        generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectCode = subject.getText().toString().trim();
                String code =  subjectCode + "-" + computeDate() + "-" + computeTime()[0]  + "-" + computeTime()[1];
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(code, BarcodeFormat.QR_CODE,600,600);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    imView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            private String[] computeTime(){
                int hours = java.time.LocalTime.now().getHour();
                int mins = java.time.LocalTime.now().getMinute() + 10;
                if (mins > 60)
                {
                    hours++;
                    mins = mins-60;
                }
                String[] timestamp = new String[2];
                timestamp[0] = Integer.toString(hours);
                timestamp[1] = Integer.toString(mins);
                return timestamp;
            }
            private String computeDate(){
                int dateInt = java.time.LocalDate.now().getDayOfMonth();
                String date = Integer.toString(dateInt);
                return date;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbreference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datasnap : snapshot.getChildren()) {
                            String key = datasnap.getKey();
                            if (key!=null) {
                                String subCode = datasnap.child("Subjects").child(subjectCode).getKey();
                                if (subCode.equals(subjectCode)){
                                    int conducted = datasnap.child("Subjects").child(subjectCode).child("conducted").getValue(Integer.class);
                                    conducted = conducted + 1;
                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("conducted",conducted);
                                    dbreference.child("Students").child(key).child("Subjects").child(subjectCode).updateChildren(map1);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                startActivity(new Intent(GenerateQrCode.this,AdminActivity.class));
                finish();
            }
        });
    }
}