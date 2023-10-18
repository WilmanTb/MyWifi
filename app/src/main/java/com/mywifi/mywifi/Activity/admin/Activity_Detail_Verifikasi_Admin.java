package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Verifikasi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

public class Activity_Detail_Verifikasi_Admin extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    private TextView nama_pelanggan, status_verifikasi;
    private ImageView arrow_back, img_ktp_pelanggan;
    private Model_Verifikasi model_verifikasi;
    private Button btn_konfirmasi;
    private AppCompatButton btn_tolak;
    private DatabaseReference dbVerifikasi;
    private String idVerifikasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_verifikasi_admin);

        initComponents();
        getDetailVerifikasi();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        dbVerifikasi.child("Users").child(model_verifikasi.getId_user()).child("status").setValue("verified");
                        dbVerifikasi.child("Verifikasi").child(idVerifikasi).child("status").setValue("false");
                        dbVerifikasi.child("Verifikasi").child(idVerifikasi).child("show").setValue("true");
                        startActivity(new Intent(Activity_Detail_Verifikasi_Admin.this, Dashboard_Admin.class));
                        finish();
                        Toast.makeText(Activity_Detail_Verifikasi_Admin.this, "Akun pelanggan berhasil di verifikasi", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        btn_tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        dbVerifikasi.child("Verifikasi").child(idVerifikasi).child("status").setValue("ditolak");
                        dbVerifikasi.child("Verifikasi").child(idVerifikasi).child("show").setValue("true");
                        startActivity(new Intent(Activity_Detail_Verifikasi_Admin.this, Dashboard_Admin.class));
                        finish();
                        Toast.makeText(Activity_Detail_Verifikasi_Admin.this, "Verifikasi akun pelanggan berhasil di tolak", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void initComponents(){
        loadingDialog = new LoadingDialog(this);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        status_verifikasi = findViewById(R.id.status_verifikasi);
        arrow_back = findViewById(R.id.btn_back);
        img_ktp_pelanggan = findViewById(R.id.img_ktp_pelanggan);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        btn_tolak = findViewById(R.id.btn_tolak);
        dbVerifikasi = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDetailVerifikasi(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Verifikasi){
            model_verifikasi = (Model_Verifikasi) object;
        }

        if (model_verifikasi!=null){
            status_verifikasi.setText("Menunggu konfirmasi");
            Glide.with(getApplicationContext()).load(model_verifikasi.getKtp()).into(img_ktp_pelanggan);
            String idPelanggan = model_verifikasi.getId_user();
            dbVerifikasi.child("Users").child(idPelanggan).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nama_pelanggan.setText(snapshot.child("nama").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            dbVerifikasi.child("Verifikasi").orderByChild("id_user").equalTo(idPelanggan).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        idVerifikasi = dataSnapshot.getKey();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}