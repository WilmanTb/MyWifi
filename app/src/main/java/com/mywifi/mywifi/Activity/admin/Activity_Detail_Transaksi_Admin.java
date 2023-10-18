package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Transaksi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Activity_Detail_Transaksi_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private Model_Transaksi model_transaksi;
    private TextView nama_pelanggan, id_transaksi, jenis_pembayaran, tanggal_pembayaran, status_pembayaran, total_pembayaran;
    private Button btn_dashboard;
    private DatabaseReference dbTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi_admin);

        initComponents();
        getDetailTransaksi();

        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Transaksi_Admin.this, Dashboard_Admin.class));
                finish();
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        id_transaksi = findViewById(R.id.id_transaksi);
        jenis_pembayaran = findViewById(R.id.jenis_pembayaran);
        tanggal_pembayaran = findViewById(R.id.tanggal_pembayaran);
        status_pembayaran = findViewById(R.id.status_pembayaran);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        total_pembayaran = findViewById(R.id.total_pembayaran);
        dbTransaksi = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDetailTransaksi(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Transaksi){
            model_transaksi = (Model_Transaksi) object;
        }

        if (model_transaksi!=null){
            id_transaksi.setText(model_transaksi.getIdTransaksi());
            jenis_pembayaran.setText(model_transaksi.getJenis());
            status_pembayaran.setText(model_transaksi.getStatus());
            tanggal_pembayaran.setText(model_transaksi.getTanggal());
            total_pembayaran.setText(formatRupiah(Double.parseDouble(model_transaksi.getHarga())));

            dbTransaksi.child("Users").child(model_transaksi.getIdPelanggan()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        nama_pelanggan.setText(snapshot.child("nama").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public static String formatRupiah(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("Rp ");
        symbols.setGroupingSeparator('.');

        DecimalFormat rupiahFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        rupiahFormat.setDecimalFormatSymbols(symbols);
        rupiahFormat.setMaximumFractionDigits(0);

        return rupiahFormat.format(amount);
    }
}