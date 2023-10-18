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
import com.mywifi.mywifi.Model.Model_Pelanggan;
import com.mywifi.mywifi.Model.Model_Pemasangan;
import com.mywifi.mywifi.Model.Model_Pemesanan;
import com.mywifi.mywifi.Model.Model_Tgl_Maintenance;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Actitivy_Detail_Pesanan_Admin extends AppCompatActivity {

    private TextView nama_pelanggan, no_rumah, blok_rumah, jenis_layanan, harga_layanan, tanggal_pesanan, status_pesanan, tanggal_survei;
    private Button btn_konfirmasi;
    private ImageView arrow_back;
    private DatabaseReference dbPesanan;
    private Model_Pemesanan model_pemesanan;
    private LoadingDialog loadingDialog;
    private String Gambar = "https://firebasestorage.googleapis.com/v0/b/my-wifi-626bb.appspot.com/o/img_maintenance%2Fundraw_coffee_break_h3uu.png?alt=media&token=73337f9f-6322-4a9d-9086-a9c609b5fd41",
            IdUser, JenisLayanan = "Pemasangan internet anda", Tanggal, IdPesanan = "", StatusKonfirmasi;
    private int StatusPemasangan = 1;
    private boolean ShowPemasangan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actitivy_detail_pesanan_admin);

        initComponents();
        getDataPesanan();
        getCurrentDate();
        getIdPesanan();

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMaintenance = dbPesanan.push().getKey();
                Model_Pemasangan model_pemasangan = new Model_Pemasangan(Gambar, IdUser, JenisLayanan, Tanggal, ShowPemasangan, StatusPemasangan, idMaintenance);
                Model_Pelanggan model_pelanggan = new Model_Pelanggan(model_pemesanan.getId_pelanggan(), model_pemesanan.getNama_layanan(), Tanggal, "aktif");
                Model_Tgl_Maintenance model_tgl_maintenance = new Model_Tgl_Maintenance(Tanggal, "", "", "");
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        if (StatusKonfirmasi == null) {
                            dbPesanan.child("Maintenance").child("Pemasangan").child(idMaintenance).setValue(model_pemasangan);
                            dbPesanan.child("Pemesanan").child(IdPesanan).child("status").setValue("Dikonfirmasi");
                            dbPesanan.child("Pelanggan").child(model_pemesanan.getId_pelanggan()).setValue(model_pelanggan);
                            dbPesanan.child("tglMaintenance").child(idMaintenance).setValue(model_tgl_maintenance);

                            startActivity(new Intent(Actitivy_Detail_Pesanan_Admin.this, Dashboard_Admin.class));
                            finish();
                            Toast.makeText(Actitivy_Detail_Pesanan_Admin.this, "Pesanan berhasil di konfirmasi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Actitivy_Detail_Pesanan_Admin.this, "Pesanan ini sudah dikonfirmasi", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initComponents() {
        tanggal_survei = findViewById(R.id.tanggal_survei);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        no_rumah = findViewById(R.id.no_rumah);
        blok_rumah = findViewById(R.id.blok_rumah);
        jenis_layanan = findViewById(R.id.jenis_layanan);
        harga_layanan = findViewById(R.id.harga_layanan);
        tanggal_pesanan = findViewById(R.id.tanggal_pesanan);
        status_pesanan = findViewById(R.id.status_pesanan);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        arrow_back = findViewById(R.id.btn_back);
        loadingDialog = new LoadingDialog(this);
        dbPesanan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDataPesanan() {
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Pemesanan) {
            model_pemesanan = (Model_Pemesanan) object;
        }

        if (model_pemesanan != null) {
            nama_pelanggan.setText(model_pemesanan.getNama_pelanggan());
            no_rumah.setText(model_pemesanan.getNo_rumah());
            blok_rumah.setText(model_pemesanan.getBlok_rumah());
            jenis_layanan.setText(model_pemesanan.getNama_layanan());
            harga_layanan.setText(formatRupiah(Double.parseDouble(model_pemesanan.getHarga())));
            tanggal_pesanan.setText(model_pemesanan.getTanggal());
            status_pesanan.setText(model_pemesanan.getStatus());
            IdUser = model_pemesanan.getId_pelanggan();
            tanggal_survei.setText(model_pemesanan.getTanggal_survei());
        }
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Tanggal = simpleDateFormat.format(calendar.getTime());
    }

    private void getIdPesanan() {
        dbPesanan.child("Pemesanan").orderByChild("id_pelanggan").equalTo(model_pemesanan.getId_pelanggan()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("status").getValue().equals("Menunggu konfirmasi")) {
                            IdPesanan = dataSnapshot.getKey();
                        } else {
                            StatusKonfirmasi = dataSnapshot.child("status").getValue().toString();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}