package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity_Buat_Laporan extends AppCompatActivity {

    private AppCompatEditText et_keterangan_gangguan;
    private Button btn_submit;
    private ImageView arrow_back;
    private DatabaseReference dbGangguan;
    private FirebaseAuth userAuth;
    private String UID, KeteranganGangguan, Tanggal, currentTime, hpUser;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_laporan);

        initComponents();
        getCurrentDate();
        getUserPhoneNumber();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeteranganGangguan = et_keterangan_gangguan.getText().toString();
                if (KeteranganGangguan.isEmpty()){
                    Toast.makeText(Activity_Buat_Laporan.this, "Keterangan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                   checkVerifiedStatus();
                }
            }
        });
    }

    private void initComponents(){
        et_keterangan_gangguan = findViewById(R.id.et_keterangan_gangguan);
        btn_submit = findViewById(R.id.btn_submit);
        arrow_back = findViewById(R.id.btn_back);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbGangguan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void checkLaporan(){
        dbGangguan.child("Gangguan").orderByChild("id_user").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        if (dataSnapshot.child("status").equals("false")) {
                            createLaporanGangguan();
                        }
                        else {
                            Toast.makeText(Activity_Buat_Laporan.this, "Kamu sudah memiliki laporan gangguan. Mohon tunggu sampai laporan gangguan kamu selesai diproses", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } else {
                    createLaporanGangguan();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createLaporanGangguan(){
        String idGangguan = dbGangguan.push().getKey();
        Model_Gangguan model_gangguan = new Model_Gangguan(UID,Tanggal, KeteranganGangguan,"true", currentTime, hpUser, idGangguan);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dbGangguan.child("Gangguan").child(idGangguan).setValue(model_gangguan);
                loadingDialog.cancel();
                Toast.makeText(Activity_Buat_Laporan.this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_Buat_Laporan.this, Dashboard_User_Activity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Tanggal = simpleDateFormat.format(calendar.getTime());

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf.format(date);
    }

    private void verificationDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up_verifikasi);
        dialog.show();

        AppCompatButton btn_verifikasi = dialog.findViewById(R.id.btn_verifikasi);
        AppCompatButton btn_dismiss = dialog.findViewById(R.id.btn_nanti_aja);

        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Buat_Laporan.this, Activity_Upload_Ktp.class));
                finish();
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void checkVerifiedStatus(){
        dbGangguan.child("Users").child(UID).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue().toString();
                if (status.equals("unverified")){
                    verificationDialog();
                } else {
                    checkLaporan();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserPhoneNumber(){
        dbGangguan.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hpUser = snapshot.child("hp").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}