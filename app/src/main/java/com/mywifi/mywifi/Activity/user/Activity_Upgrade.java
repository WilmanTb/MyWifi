package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
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
import com.mywifi.mywifi.R;

import java.text.NumberFormat;
import java.util.Locale;

public class Activity_Upgrade extends AppCompatActivity {

    private Button btn_layanan, btn_upgrade;
    private TextView txt_current, txt_upgrade, txt_namaLayanan, txt_hargaLayanan, txt_mulaiLayanan;
    private CardView cv_currentLayanan;
    private ImageView img_upgrade, arrow_left;
    private DatabaseReference dbUpgrade;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID, MulaiLangganan, Status, HargaLayanan;
    public static String NamaLayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        initComponents();

        getData();

        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Upgrade.this, Dashboard_User_Activity.class));
                finish();
            }
        });

        btn_layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Upgrade.this, Activity_Layanan_Internet.class));
                finish();
            }
        });

        btn_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Upgrade.this, Activity_Detail_Upgrade_User.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Dashboard_User_Activity.class));
        finish();
    }

    private void initComponents() {
        btn_layanan = findViewById(R.id.btn_layanan);
        btn_upgrade = findViewById(R.id.btn_upgrade);
        txt_current = findViewById(R.id.txt_current);
        txt_upgrade = findViewById(R.id.txt_upgrade);
        txt_namaLayanan = findViewById(R.id.txt_namaLayanan);
        txt_hargaLayanan = findViewById(R.id.txt_hargaLayanan);
        txt_mulaiLayanan = findViewById(R.id.txt_mulaiLayanan);
        img_upgrade = findViewById(R.id.img_upgrade);
        arrow_left = findViewById(R.id.btn_back);
        cv_currentLayanan = findViewById(R.id.cv_currentLayanan);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbUpgrade = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void getData() {
        dbUpgrade.child("Pelanggan").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataExist();
                    NamaLayanan = snapshot.child("layanan").getValue().toString();
                    MulaiLangganan = snapshot.child("mulai_langganan").getValue().toString();
                    Status = snapshot.child("status").getValue().toString();
                    if (Status.equals("aktif")) {
                        txt_namaLayanan.setText(NamaLayanan);
                        txt_mulaiLayanan.setText(MulaiLangganan);
                        dbUpgrade.child("Layanan").orderByChild("nama").equalTo(NamaLayanan)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                HargaLayanan = dataSnapshot.child("harga").getValue().toString();
                                            }
                                            txt_hargaLayanan.setText(formatRupiah(Double.parseDouble(HargaLayanan)));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                } else {
                    dataNotExsist();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataExist() {
        txt_upgrade.setVisibility(View.GONE);
        img_upgrade.setVisibility(View.GONE);
        btn_layanan.setVisibility(View.GONE);
        txt_current.setVisibility(View.VISIBLE);
        cv_currentLayanan.setVisibility(View.VISIBLE);
        btn_upgrade.setVisibility(View.VISIBLE);
    }

    private void dataNotExsist() {
        txt_upgrade.setVisibility(View.VISIBLE);
        img_upgrade.setVisibility(View.VISIBLE);
        btn_layanan.setVisibility(View.VISIBLE);
        txt_current.setVisibility(View.GONE);
        cv_currentLayanan.setVisibility(View.GONE);
        btn_upgrade.setVisibility(View.GONE);
    }

    private String formatRupiah(Double number) {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}