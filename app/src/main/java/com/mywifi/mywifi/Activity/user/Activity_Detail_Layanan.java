package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Pemesanan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Activity_Detail_Layanan extends AppCompatActivity {

    private int ID;
    private TextView nama_layanan, harga_layanan, deskripsi_layanan;
    private Button btn_pesan;
    private DatabaseReference dbPemesanan;
    private ImageView arrow_back;
    private FirebaseAuth userAuth;
    private LoadingDialog loadingDialog;
    private String NamaUser, Tanggal, NoRumah, BlokRumah, Status = "Menunggu konfirmasi", UID, NamaLayanan, HargaLayanan, DeskripsiLayanan, userStatus, tanggal_survei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layanan);

        ID = Activity_Layanan_Internet.ID;

        initComponents();
        userData();
        currentDate();
        setData(ID);

        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("verified")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Activity_Detail_Layanan.this)
                            .setMessage("Yakin melakukan pesanan ?")
                            .setPositiveButton("Ya", null)
                            .setNegativeButton("Tidak", null)
                            .show();
                    Button positiveButton = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTanggalSurveiDialog();
                        }
                    });
                } else {
                    Dialog dialog = new Dialog(Activity_Detail_Layanan.this);
                    dialog.setContentView(R.layout.pop_up_verifikasi);
                    dialog.show();

                    AppCompatButton btn_verifikasi = dialog.findViewById(R.id.btn_verifikasi);
                    AppCompatButton btn_dismiss = dialog.findViewById(R.id.btn_nanti_aja);

                    btn_verifikasi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Activity_Detail_Layanan.this, Activity_Upload_Ktp.class));
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
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Layanan.this, Activity_Layanan_Internet.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Detail_Layanan.this, Activity_Layanan_Internet.class));
        finish();
    }

    private void initComponents() {
        nama_layanan = findViewById(R.id.nama_layanan);
        harga_layanan = findViewById(R.id.harga_layanan);
        deskripsi_layanan = findViewById(R.id.deskripsi_layanan);
        btn_pesan = findViewById(R.id.btn_pesan);
        nama_layanan = findViewById(R.id.nama_layanan);
        loadingDialog = new LoadingDialog(this);
        arrow_back = findViewById(R.id.btn_back);
        userAuth = FirebaseAuth.getInstance();
        dbPemesanan = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void userData() {
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();

        dbPemesanan.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NamaUser = snapshot.child("nama").getValue().toString();
                NoRumah = snapshot.child("noRumah").getValue().toString();
                BlokRumah = snapshot.child("blok").getValue().toString();
                userStatus = snapshot.child("status").getValue().toString();
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
            dbPemesanan.child("Layanan").child("id1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NamaLayanan = snapshot.child("nama").getValue().toString();
                    HargaLayanan = snapshot.child("harga").getValue().toString();
                    DeskripsiLayanan = snapshot.child("deskripsi").getValue().toString();

                    setText();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (ID == 2) {
            dbPemesanan.child("Layanan").child("id2").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NamaLayanan = snapshot.child("nama").getValue().toString();
                    HargaLayanan = snapshot.child("harga").getValue().toString();
                    DeskripsiLayanan = snapshot.child("deskripsi").getValue().toString();

                    setText();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (ID == 3) {
            dbPemesanan.child("Layanan").child("id3").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NamaLayanan = snapshot.child("nama").getValue().toString();
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
        nama_layanan.setText(NamaLayanan);
        harga_layanan.setText(formatRupiah(Double.parseDouble(HargaLayanan)));
        deskripsi_layanan.setText(DeskripsiLayanan);
    }

    private void createPesanan(){
        DatabaseReference dbOrder = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Pemesanan");
        Model_Pemesanan model_pemesanan = new Model_Pemesanan(NamaLayanan, HargaLayanan, Tanggal, NamaUser, NoRumah, BlokRumah, Status, UID, tanggal_survei);
        dbOrder.child(dbOrder.push().getKey()).setValue(model_pemesanan);
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private void showTanggalSurveiDialog(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < 3; i++) {
            String formattedDate = sdf.format(calendar.getTime());
            dateList.add(formattedDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Increment the date by 1 day
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_tanggal_survei);
        dialog.show();

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        RadioButton tgl_satu = dialog.findViewById(R.id.radio_tgl_satu);
        RadioButton tgl_dua = dialog.findViewById(R.id.radio_tgl_dua);
        RadioButton tgl_tiga = dialog.findViewById(R.id.radio_tgl_tiga);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);

        tgl_satu.setText(dateList.get(0));
        tgl_dua.setText(dateList.get(1));
        tgl_tiga.setText(dateList.get(2));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = dialog.findViewById(checkedId);
            tanggal_survei = selectedRadioButton.getText().toString();
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        createPesanan();
                        Toast.makeText(Activity_Detail_Layanan.this, "Pemesanan layanan berhasil diajukan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Activity_Detail_Layanan.this, Dashboard_User_Activity.class));
                        finish();
                        loadingDialog.cancel();
                    }
                };

                handler.postDelayed(runnable, 2000);
            }
        });


    }
}