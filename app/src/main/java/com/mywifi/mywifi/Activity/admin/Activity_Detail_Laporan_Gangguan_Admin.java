package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Tgl_Maintenance;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_Detail_Laporan_Gangguan_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView nama_pelapor, id_gangguan, tanggal_gangguan, status_gangguan, keterangan_gangguan, hp_user, noRumah_user, blokRumah_user;
    private Button btn_konfirmasi;
    private DatabaseReference dbGangguan;
    private LoadingDialog loadingDialog;
    private String idGangguan, namaPelapor, currentDate;
    private Model_Gangguan model_gangguan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_gangguan_admin);


        initComponents();
        getCurrentDate();
        getDetailLaporanGangguan();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMaintenance = dbGangguan.push().getKey();
                Model_Merged_List model_merged_list_user = new Model_Merged_List(idMaintenance, currentDate, Data_Value.maintainNetworkPicture, model_gangguan.getId_user(), "Maintenance internet anda", 1, true );
                Model_Tgl_Maintenance model_tgl_maintenance = new Model_Tgl_Maintenance(currentDate, "", "", "");
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        dbGangguan.child("Gangguan").child(model_gangguan.getIdGangguan()).child("status").setValue("Dikonfirmasi");
                        dbGangguan.child("Maintenance").child("User").child(idMaintenance).setValue(model_merged_list_user);
                        dbGangguan.child("tglMaintenance").child(idMaintenance).setValue(model_tgl_maintenance);
                        startActivity(new Intent(Activity_Detail_Laporan_Gangguan_Admin.this, Dashboard_Admin.class));
                        finish();
                        Toast.makeText(Activity_Detail_Laporan_Gangguan_Admin.this, "Laporan gangguan berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        nama_pelapor = findViewById(R.id.nama_pelapor);
        id_gangguan = findViewById(R.id.id_gangguan);
        tanggal_gangguan = findViewById(R.id.tanggal_gangguan);
        status_gangguan = findViewById(R.id.status_gangguan);
        keterangan_gangguan = findViewById(R.id.keterangan_gangguan);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        loadingDialog = new LoadingDialog(this);
        dbGangguan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        hp_user = findViewById(R.id.hp_user);
        noRumah_user = findViewById(R.id.noRumah_user);
        blokRumah_user = findViewById(R.id.blokRumah_user);
    }

    private void getDetailLaporanGangguan(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Gangguan){
            model_gangguan = (Model_Gangguan) object;
        }

        if (model_gangguan!=null){
            tanggal_gangguan.setText(model_gangguan.getTanggal());
            keterangan_gangguan.setText(model_gangguan.getKeterangan());
            status_gangguan.setText("Menunggu konfirmasi");
            id_gangguan.setText(model_gangguan.getIdGangguan());
            dbGangguan.child("Users").child(model_gangguan.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    namaPelapor = snapshot.child("nama").getValue().toString();
                    nama_pelapor.setText(namaPelapor);
                    blokRumah_user.setText(snapshot.child("noRumah").getValue().toString());
                    noRumah_user.setText(snapshot.child("blok").getValue().toString());
                    hp_user.setText(snapshot.child("hp").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }
}