package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_User;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

public class Activity_Detail_Laporan_User extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView nama_pelanggan, email_pelanggan, hp_pelanggan, no_rumah, blok_rumah, status_pelanggan, nama_layanan;
    private Model_User model_user;
    private DatabaseReference dbUser;
    private String idPelanggan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_user);

        initComponents();
        getDetailUser();
        getPelangganKey(new CallBackId() {
            @Override
            public void onCallBack(String ID) {
                dbUser.child("Pelanggan").child(ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            nama_layanan.setText(snapshot.child("layanan").getValue().toString());
                        } else {
                            nama_layanan.setText("Belum berlangganan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        email_pelanggan = findViewById(R.id.email_pelanggan);
        hp_pelanggan = findViewById(R.id.hp_pelanggan);
        no_rumah = findViewById(R.id.no_rumah);
        blok_rumah = findViewById(R.id.blok_rumah);
        status_pelanggan = findViewById(R.id.status_pelanggan);
        nama_layanan = findViewById(R.id.nama_layanan);
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDetailUser() {
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_User) {
            model_user = (Model_User) object;
        }

        if (model_user != null) {
            nama_pelanggan.setText(model_user.getNama());
            email_pelanggan.setText(model_user.getEmail());
            hp_pelanggan.setText(model_user.getHp());
            no_rumah.setText(model_user.getNoRumah());
            blok_rumah.setText(model_user.getBlok());
            status_pelanggan.setText(model_user.getStatus());
        }
    }

    private void getPelangganKey(CallBackId callBackId) {
        dbUser.child("Users").orderByChild("nama").equalTo(model_user.getNama()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        idPelanggan = dataSnapshot.getKey();
                    }
                    callBackId.onCallBack(idPelanggan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private interface CallBackId {
        void onCallBack(String ID);
    }
}