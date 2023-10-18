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
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Upgrade;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_Detail_Upgrade_Admin extends AppCompatActivity {

    private Model_Upgrade model_upgrade;
    private TextView nama_pelanggan, current_layanan, tanggal_upgrade, status_upgrade, upgrade_to, harga_layanan;
    private DatabaseReference dbUpgrade;
    private LoadingDialog loadingDialog;
    private Button btn_konfirmasi;
    private ImageView arrow_back;
    private String idUpgrade, currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_upgrade_admin);

        initComponents();
        getUpgradeDetail();
        getCurrentDate();


        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMaintenance = dbUpgrade.push().getKey();
                Model_Merged_List model_merged_list = new Model_Merged_List(idMaintenance, currentDate, Data_Value.maintainUpgradePicture,model_upgrade.getId_pelanggan(), "Upgrade internet anda", 1, true);
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        dbUpgrade.child("Maintenance").child("Upgrade").child(idMaintenance).setValue(model_merged_list);
                        dbUpgrade.child("Upgrade").child(model_upgrade.getIdUpgrade()).child("status").setValue("Dikonfirmasi");
                        startActivity(new Intent(Activity_Detail_Upgrade_Admin.this, Dashboard_Admin.class));
                        finish();
                        Toast.makeText(Activity_Detail_Upgrade_Admin.this, "Permintaan upgrade layanan berhasil di konfirmasi", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        current_layanan = findViewById(R.id.current_layanan);
        tanggal_upgrade = findViewById(R.id.tanggal_upgrade);
        status_upgrade = findViewById(R.id.status_upgrade);
        upgrade_to = findViewById(R.id.upgrade_to);
        harga_layanan = findViewById(R.id.harga_layanan);
        loadingDialog = new LoadingDialog(this);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        dbUpgrade = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getUpgradeDetail(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Upgrade){
            model_upgrade = (Model_Upgrade) object;
        }

        if (model_upgrade!=null){
            current_layanan.setText(model_upgrade.getUpgrade_layanan());
            upgrade_to.setText(model_upgrade.getCurrent_layanan());
            tanggal_upgrade.setText(model_upgrade.getTanggal());
            status_upgrade.setText(model_upgrade.getStatus());
            harga_layanan.setText("Rp " + model_upgrade.getHarga());
            idUpgrade = model_upgrade.getIdUpgrade();
            String idUser = model_upgrade.getId_pelanggan();
            dbUpgrade.child("Users").child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nama_pelanggan.setText(snapshot.child("nama").getValue().toString());
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