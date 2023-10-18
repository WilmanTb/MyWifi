package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Upgrade;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Activity_Kirim_Permintaan_Upgrade extends AppCompatActivity {

    private TextView current_layanan, upgrade_layanan, harga_layanan, deskripsi_layanan;
    private Button btn_submit;
    private ImageView arrow_back;
    private DatabaseReference dbUpgrade;
    private FirebaseAuth userAuth;
    private LoadingDialog loadingDialog;
    private String NamaUser, Tanggal, NoRumah, BlokRumah, Status = "Menunggu konfirmasi", UID, CurrentLayanan, UpgradeLayanan, HargaLayanan, DeskripsiLayanan;
    private int IdLayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_permintaan_upgrade);

        IdLayanan = Activity_Detail_Upgrade_User.IdLayanan;
        CurrentLayanan = Activity_Upgrade.NamaLayanan;

        initComponents();
        userData();
        currentDate();
        setData(IdLayanan);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Activity_Kirim_Permintaan_Upgrade.this)
                        .setMessage("Yakin melakukan upgrade ?")
                        .setPositiveButton("Ya", null)
                        .setNegativeButton("Tidak", null)
                        .show();
                Button positiveButton = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                createPesanan();
                                Toast.makeText(Activity_Kirim_Permintaan_Upgrade.this, "Upgrade layanan berhasil diajukan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Activity_Kirim_Permintaan_Upgrade.this, Dashboard_User_Activity.class));
                                finish();
                                loadingDialog.cancel();
                            }
                        };
                        handler.postDelayed(runnable, 3000);
                    }
                });
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Kirim_Permintaan_Upgrade.this, Activity_Detail_Upgrade_User.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Kirim_Permintaan_Upgrade.this, Activity_Detail_Upgrade_User.class));
        finish();
    }

    private void initComponents() {
        current_layanan = findViewById(R.id.current_layanan);
        upgrade_layanan = findViewById(R.id.upgrade_layanan);
        harga_layanan = findViewById(R.id.harga_layanan);
        deskripsi_layanan = findViewById(R.id.deskripsi_layanan);
        btn_submit = findViewById(R.id.btn_submit);
        arrow_back = findViewById(R.id.btn_back);
        loadingDialog = new LoadingDialog(this);
        userAuth = FirebaseAuth.getInstance();
        dbUpgrade = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void userData() {
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();

        dbUpgrade.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NamaUser = snapshot.child("nama").getValue().toString();
                NoRumah = snapshot.child("noRumah").getValue().toString();
                BlokRumah = snapshot.child("blok").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void currentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Tanggal = simpleDateFormat.format(calendar.getTime());
    }

    private void setData(int ID) {
        if (ID == 1) {
            dbUpgrade.child("Layanan").child("id1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UpgradeLayanan = snapshot.child("nama").getValue().toString();
                    HargaLayanan = snapshot.child("harga").getValue().toString();
                    DeskripsiLayanan = snapshot.child("deskripsi").getValue().toString();

                    setText();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (ID == 2) {
            dbUpgrade.child("Layanan").child("id2").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UpgradeLayanan = snapshot.child("nama").getValue().toString();
                    HargaLayanan = snapshot.child("harga").getValue().toString();
                    DeskripsiLayanan = snapshot.child("deskripsi").getValue().toString();

                    setText();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setText() {
        current_layanan.setText(CurrentLayanan);
        upgrade_layanan.setText(UpgradeLayanan);
        harga_layanan.setText(formatRupiah(Double.parseDouble(HargaLayanan)));
        deskripsi_layanan.setText(DeskripsiLayanan);
    }

    private void createPesanan() {
        DatabaseReference dbOrder = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Upgrade");
        String idUpgrade = dbOrder.push().getKey();
        Model_Upgrade model_upgrade = new Model_Upgrade(CurrentLayanan, UpgradeLayanan, HargaLayanan, Tanggal, NamaUser, NoRumah, BlokRumah, Status, UID,idUpgrade);
        dbOrder.child(idUpgrade).setValue(model_upgrade);
    }

    private String formatRupiah(Double number) {
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}