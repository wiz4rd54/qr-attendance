package com.example.qrattendance;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.Map;

public class ScanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanCode();
    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
       if (result.getContents() != null)
       {
           AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
           builder.setTitle("Result");
           String code = result.getContents();
           String[] code_Split = code.split("-");
           builder.setMessage(code);
           builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
                   FirebaseAuth fAuth = FirebaseAuth.getInstance();
                   String currentUser = fAuth.getCurrentUser().getEmail().toString();
                   String enrollmentNo = currentUser.substring(0,8);

                   FirebaseDatabase database = FirebaseDatabase.getInstance();
                   DatabaseReference dbreference = database.getReference();

                   dbreference.child("Students").child(enrollmentNo).child("Subjects").child(code_Split[0]).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               int present = snapshot.child("present").getValue(Integer.class);
                               String lastTime = snapshot.child("lastTime").getValue(String.class);
                               int recent = Integer.parseInt(lastTime);
                               Map<String, Object> map = new HashMap<>();
                               int date = Integer.parseInt(code_Split[1]);
                               int hours = Integer.parseInt(code_Split[2]);
                               int mins = Integer.parseInt(code_Split[3]);
                               int current_date = Integer.parseInt(computeDate());
                               int current_hours = Integer.parseInt(computeTime()[0]);
                               int current_mins = Integer.parseInt(computeTime()[1]);
                               if(date==current_date){
                                   if (current_hours<=hours && current_mins<mins){
                                       if(current_hours!=recent){
                                           present++;
                                           map.put("present",present);
                                           map.put("lastDate",fullDate());
                                           map.put("lastTime",Integer.toString(hours));
                                           dbreference.child("Students").child(enrollmentNo).child("Subjects").child(code_Split[0]).updateChildren(map);
                                       }
                                   }
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(ScanActivity.this, "Error "+error,Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }).show();
       }
    });

    private String[] computeTime(){
        int hours = java.time.LocalTime.now().getHour();
        int mins = java.time.LocalTime.now().getMinute();
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
    private String fullDate(){
        String date = java.time.LocalDate.now().toString();
        return date;
    }
}