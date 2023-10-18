package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;

public class Activity_Detail_Maintenance_User extends AppCompatActivity {

    private TextView txt_jenisMaintenance, txt_first_status_progress, txt_second_status_progress, txt_third_status_progress, txt_fourth_status_progress, txt_first_status, txt_second_status, txt_third_status, txt_fourth_status, txt_deskripsi_maintenance, first_tanggal, second_tanggal, third_tanggal, fourth_tanggal;
    private Button btn_dashboard;
    private ImageView arrow_back;
    private View first_circle_progress, second_circle_progress, third_circle_progress, fourth_circle_progress, first_line_progress, second_line_progress, third_line_progress;
    private Model_Merged_List modelMaintenance;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbMaintenance;
    private String UID, firstStatus, secondStatus, thirdStatus, fourthStatus, deskripsiMaintenance, StatusMaintenance;
    private ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_maintenance_user);

        initComponents();
        getData();
        setStatusProgress(firstStatus, secondStatus, thirdStatus, fourthStatus, deskripsiMaintenance);
        setTextStatus();
        setTrackingProgres(StatusMaintenance);
        getMaintenanceTanggalUpdate();

        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Maintenance_User.this, Dashboard_User_Activity.class));
                finish();
            }
        });
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Maintenance_User.this, Activity_Maintenance_User.class));
                finish();
            }
        });
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        txt_deskripsi_maintenance = findViewById(R.id.txt_deskripsi_maintenance);
        txt_jenisMaintenance = findViewById(R.id.txt_jenisMaintenance);
        txt_first_status_progress = findViewById(R.id.txt_first_status_progress);
        txt_second_status_progress = findViewById(R.id.txt_second_status_progress);
        txt_third_status_progress = findViewById(R.id.txt_third_status_progress);
        txt_fourth_status_progress = findViewById(R.id.txt_fourth_status_progress);
        txt_first_status = findViewById(R.id.txt_first_status);
        txt_second_status = findViewById(R.id.txt_second_status);
        txt_third_status = findViewById(R.id.txt_third_status);
        txt_fourth_status = findViewById(R.id.txt_fourth_status);
        first_circle_progress = findViewById(R.id.first_circle_progress);
        second_circle_progress = findViewById(R.id.second_circle_progress);
        third_circle_progress = findViewById(R.id.third_circle_progress);
        fourth_circle_progress = findViewById(R.id.fourth_circle_progress);
        first_line_progress = findViewById(R.id.first_line_progress);
        second_line_progress = findViewById(R.id.second_line_progress);
        third_line_progress = findViewById(R.id.third_line_progress);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        first_tanggal = findViewById(R.id.first_tanggal);
        second_tanggal = findViewById(R.id.second_tanggal);
        third_tanggal = findViewById(R.id.third_tanggal);
        fourth_tanggal = findViewById(R.id.fourth_tanggal);

    }

    private void getData() {
        Object obj = getIntent().getSerializableExtra("detail");
        if (obj instanceof Model_Merged_List) {
            modelMaintenance = (Model_Merged_List) obj;
        }

        if (modelMaintenance != null) {
            txt_jenisMaintenance.setText(modelMaintenance.getJenis());
            StatusMaintenance = String.valueOf(modelMaintenance.getStatus());
        }
    }

    private void setStatusProgress(String first, String second, String third, String fourth, String deskripsi) {
        if (modelMaintenance.getJenis().equals("Maintenance internet anda")) {
            first = "Konfirmasi";
            second = "Checking";
            third = "Perbaikan";
            fourth = "Selesai";
            deskripsi = Data_Value.maintainUser;

        } else if (modelMaintenance.getJenis().equals("Maintenance server")) {

            first = "Checking";
            second = "Perbaikan";
            third = "Finishing";
            fourth = "Selesai";
            deskripsi = Data_Value.maintainNetwork;

        } else if (modelMaintenance.getJenis().equals("Pemasangan internet anda")) {
            first = "Konfirmasi";
            second = "Survei";
            third = "Pemasangan";
            fourth = "Selesai";
            deskripsi = Data_Value.maintainPemasangan;
        }

        firstStatus = first;
        secondStatus = second;
        thirdStatus = third;
        fourthStatus = fourth;
        deskripsiMaintenance = deskripsi;
    }

    private void setTrackingProgres(String trackingProgres){
        colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.main));
        if (trackingProgres.equals("1")){
            first_circle_progress.setBackgroundTintList(colorStateList);
        } else if (trackingProgres.equals("2")) {
            first_circle_progress.setBackgroundTintList(colorStateList);
            first_line_progress.setBackgroundTintList(colorStateList);
            second_circle_progress.setBackgroundTintList(colorStateList);
        } else if (trackingProgres.equals("3")) {
            first_circle_progress.setBackgroundTintList(colorStateList);
            first_line_progress.setBackgroundTintList(colorStateList);
            second_circle_progress.setBackgroundTintList(colorStateList);
            second_line_progress.setBackgroundTintList(colorStateList);
            third_circle_progress.setBackgroundTintList(colorStateList);

        } else if (trackingProgres.equals("4")){
            first_circle_progress.setBackgroundTintList(colorStateList);
            first_line_progress.setBackgroundTintList(colorStateList);
            second_circle_progress.setBackgroundTintList(colorStateList);
            second_line_progress.setBackgroundTintList(colorStateList);
            third_circle_progress.setBackgroundTintList(colorStateList);
            third_line_progress.setBackgroundTintList(colorStateList);
            fourth_circle_progress.setBackgroundTintList(colorStateList);
        }
    }

    private void setTextStatus(){
        txt_first_status.setText(firstStatus);
        txt_second_status.setText(secondStatus);
        txt_third_status.setText(thirdStatus);
        txt_fourth_status.setText(fourthStatus);
        txt_deskripsi_maintenance.setText(deskripsiMaintenance);
    }

    private void getMaintenanceTanggalUpdate(){
        dbMaintenance.child("tglMaintenance").child(modelMaintenance.getIdMaintenance()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    first_tanggal.setText(snapshot.child("tgl1").getValue().toString());
                    second_tanggal.setText(snapshot.child("tgl2").getValue().toString());
                    third_tanggal.setText(snapshot.child("tgl3").getValue().toString());
                    fourth_tanggal.setText(snapshot.child("tgl4").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}