package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_Update_Maintenance extends AppCompatActivity {

    private ImageView arrow_back;
    private Button btn_update_maintenance;
    private Model_Merged_List model_merged_list;
    private DatabaseReference dbMaintenance;
    private LoadingDialog loadingDialog;
    private int setStatus = 0;

    private String firstStatus, secondStatus, thirdStatus, fourthStatus, idMaintenance, parentMaintenanceId, nama_layanan, currentDate, harga_layanan, tglProgressMaintenance;
    private TextView lbl_nama_pelanggan, jenis_maintenance, nama_pelanggan, tanggal_maintenance, status_maintenance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_maintenance);

        initComponents();
        getMaintenanceData();
        getMaintenanceParentId();
        getCurrentDate();
        getJenisLayanan();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_update_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpadateDialog();
            }
        });
    }

    private void initComponents() {
        loadingDialog = new LoadingDialog(this);
        arrow_back = findViewById(R.id.btn_back);
        btn_update_maintenance = findViewById(R.id.btn_update_maintenance);
        lbl_nama_pelanggan = findViewById(R.id.lbl_nama_pelanggan);
        jenis_maintenance = findViewById(R.id.jenis_maintenance);
        nama_pelanggan = findViewById(R.id.nama_pelanggan);
        tanggal_maintenance = findViewById(R.id.tanggal_maintenance);
        status_maintenance = findViewById(R.id.status_maintenance);
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getMaintenanceData() {
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Merged_List) {
            model_merged_list = (Model_Merged_List) object;
        }

        if (model_merged_list != null) {
            String jenisMaintenance = model_merged_list.getJenis();
            String statusMaintenance = String.valueOf(model_merged_list.getStatus());
            idMaintenance = model_merged_list.getIdMaintenance();
            if (jenisMaintenance.equals("Maintenance internet anda")) {
                lbl_nama_pelanggan.setVisibility(View.VISIBLE);
                nama_pelanggan.setVisibility(View.VISIBLE);
                jenis_maintenance.setText("Maintenance user");
                dbMaintenance.child("Users").child(model_merged_list.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String namaUser = snapshot.child("nama").getValue().toString();
                        nama_pelanggan.setText(namaUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                firstStatus = "Konfirmasi";
                secondStatus = "Checking";
                thirdStatus = "Perbaikan";
                fourthStatus = "Selesai";
                if (statusMaintenance.equals("1")) {
                    status_maintenance.setText(firstStatus);
                } else if (statusMaintenance.equals("2")) {
                    status_maintenance.setText(secondStatus);
                } else if (statusMaintenance.equals("3")) {
                    status_maintenance.setText(thirdStatus);
                } else if (statusMaintenance.equals("4")) {
                    status_maintenance.setText(fourthStatus);
                }

            } else if (jenisMaintenance.equals("Pemasangan internet anda")) {
                lbl_nama_pelanggan.setVisibility(View.VISIBLE);
                nama_pelanggan.setVisibility(View.VISIBLE);
                jenis_maintenance.setText("Pemasangan internet user");
                dbMaintenance.child("Users").child(model_merged_list.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String namaUser = snapshot.child("nama").getValue().toString();
                        nama_pelanggan.setText(namaUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firstStatus = "Konfirmasi";
                secondStatus = "Survei";
                thirdStatus = "Pemasangan";
                fourthStatus = "Selesai";
                if (statusMaintenance.equals("1")) {
                    status_maintenance.setText(firstStatus);
                } else if (statusMaintenance.equals("2")) {
                    status_maintenance.setText(secondStatus);
                } else if (statusMaintenance.equals("3")) {
                    status_maintenance.setText(thirdStatus);
                } else if (statusMaintenance.equals("4")) {
                    status_maintenance.setText(fourthStatus);
                }
            } else if (jenisMaintenance.equals("Upgrade internet anda")) {
                lbl_nama_pelanggan.setVisibility(View.VISIBLE);
                nama_pelanggan.setVisibility(View.VISIBLE);
                jenis_maintenance.setText("Upgrade internet user");
                dbMaintenance.child("Users").child(model_merged_list.getId_user()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String namaUser = snapshot.child("nama").getValue().toString();
                        nama_pelanggan.setText(namaUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firstStatus = "Konfirmasi";
                secondStatus = "Managing";
                thirdStatus = "Updating";
                fourthStatus = "Selesai";
                if (statusMaintenance.equals("1")) {
                    status_maintenance.setText(firstStatus);
                } else if (statusMaintenance.equals("2")) {
                    status_maintenance.setText(secondStatus);
                } else if (statusMaintenance.equals("3")) {
                    status_maintenance.setText(thirdStatus);
                } else if (statusMaintenance.equals("4")) {
                    status_maintenance.setText(fourthStatus);
                }
            } else {
                jenis_maintenance.setText(jenisMaintenance);
                firstStatus = "Checking";
                secondStatus = "Perbaikan";
                thirdStatus = "Finishing";
                fourthStatus = "Selesai";
                if (statusMaintenance.equals("1")) {
                    status_maintenance.setText(firstStatus);
                } else if (statusMaintenance.equals("2")) {
                    status_maintenance.setText(secondStatus);
                } else if (statusMaintenance.equals("3")) {
                    status_maintenance.setText(thirdStatus);
                } else if (statusMaintenance.equals("4")) {
                    status_maintenance.setText(fourthStatus);
                }
            }

            tanggal_maintenance.setText(model_merged_list.getTanggal());
        }
    }

//    private void getMaintenanceParentId() {
//        if (model_merged_list.getJenis().equals("Maintenance internet anda")){
//            dbMaintenance.child("Maintenance").child("User").orderByChild(idMaintenance).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            parentMaintenanceId = dataSnapshot.getKey();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        } else if (model_merged_list.getJenis().equals("Maintenance server")) {
//            dbMaintenance.child("Maintenance").child("Network").orderByChild(idMaintenance).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            parentMaintenanceId = dataSnapshot.getKey();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        } else if (model_merged_list.getJenis().equals("Pemasangan internet anda")) {
//            dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild(idMaintenance).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            parentMaintenanceId = dataSnapshot.getKey();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//    }

    private void getMaintenanceParentId() {
        if (model_merged_list.getJenis().equals("Maintenance internet anda")) {
            dbMaintenance.child("Maintenance").child("User").orderByChild("idMaintenance").equalTo(model_merged_list.getIdMaintenance()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        parentMaintenanceId = "User";

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (model_merged_list.getJenis().equals("Maintenance server")) {
            dbMaintenance.child("Maintenance").child("Network").orderByChild("idMaintenance").equalTo(model_merged_list.getIdMaintenance()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        parentMaintenanceId = "Network";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (model_merged_list.getJenis().equals("Pemasangan internet anda")) {
            dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild("idMaintenance").equalTo(model_merged_list.getIdMaintenance()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        parentMaintenanceId = "Pemasangan";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (model_merged_list.getJenis().equals("Upgrade internet anda")) {
            dbMaintenance.child("Maintenance").child("Upgrade").orderByChild("idMaintenance").equalTo(model_merged_list.getIdMaintenance()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        parentMaintenanceId = "Upgrade";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void openUpadateDialog() {
        Dialog updateMaintenanceDialog = new Dialog(this);
        updateMaintenanceDialog.setContentView(R.layout.update_maintenance_progress);
        updateMaintenanceDialog.show();

        Button btn_update = updateMaintenanceDialog.findViewById(R.id.btn_update_maintenance);
        RadioGroup radioGroup = updateMaintenanceDialog.findViewById(R.id.radio_group);
        RadioButton firstStatus = updateMaintenanceDialog.findViewById(R.id.radio_status_satu);
        RadioButton secondStatus = updateMaintenanceDialog.findViewById(R.id.radio_status_dua);
        RadioButton thirdStatus = updateMaintenanceDialog.findViewById(R.id.radio_status_tiga);
        RadioButton fourthStatus = updateMaintenanceDialog.findViewById(R.id.radio_status_empat);

        String status = String.valueOf(model_merged_list.getStatus());
        if (status.equals("1")) {
            firstStatus.setChecked(true);
        } else if (status.equals("2")) {
            secondStatus.setChecked(true);
        } else if (status.equals("3")) {
            thirdStatus.setChecked(true);
        } else if (status.equals("4")) {
            fourthStatus.setChecked(true);
        }

        if (firstStatus.isChecked()) {
            setStatus = 1;
        } else if (secondStatus.isChecked()) {
            setStatus = 2;
        } else if (thirdStatus.isChecked()) {
            setStatus = 3;
        } else if (fourthStatus.isChecked()) {
            setStatus = 4;
        }

        if (model_merged_list.getJenis().equals("Maintenance server")) {
            firstStatus.setText("Checking");
            secondStatus.setText("Perbaikan");
            thirdStatus.setText("Finishing");
            fourthStatus.setText("Selesai");
        } else if (model_merged_list.getJenis().equals("Maintenance internet anda")) {
            firstStatus.setText("Konfirmasi");
            secondStatus.setText("Checking");
            thirdStatus.setText("Perbaikan");
            fourthStatus.setText("Selesai");
        } else if (model_merged_list.getJenis().equals("Pemasangan internet anda")) {
            firstStatus.setText("Konfirmasi");
            secondStatus.setText("Survei");
            thirdStatus.setText("Pemasangan");
            fourthStatus.setText("Selesai");
        } else if (model_merged_list.getJenis().equals("Upgrade internet anda")) {
            firstStatus.setText("Konfirmasi");
            secondStatus.setText("Managing");
            thirdStatus.setText("Updating");
            fourthStatus.setText("Selesai");
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if (firstStatus.isChecked()) {
                    setStatus = 1;
                    tglProgressMaintenance = "tgl1";
                } else if (secondStatus.isChecked()) {
                    setStatus = 2;
                    tglProgressMaintenance = "tgl2";
                } else if (thirdStatus.isChecked()) {
                    setStatus = 3;
                    tglProgressMaintenance = "tgl3";
                } else if (fourthStatus.isChecked()) {
                    setStatus = 4;
                    tglProgressMaintenance = "tgl4";
                }
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        if (setStatus == 4 && model_merged_list.getJenis().equals("Pemasangan internet anda")) {
                            String idPembayaranPSB = dbMaintenance.push().getKey();
                            String idPembayaranBulanan = dbMaintenance.push().getKey();
                            Model_Pembayaran model_pembayaran_psb = new Model_Pembayaran(nama_layanan, "belum dibayar", "300000", idPembayaranPSB, model_merged_list.getId_user(), "PSB", currentDate);
                            Model_Pembayaran model_pembayaran_bulanan = new Model_Pembayaran(nama_layanan, "belum dibayar", harga_layanan, idPembayaranBulanan, model_merged_list.getId_user(), "bulanan", currentDate);
                            dbMaintenance.child("Tagihan").child(idPembayaranPSB).setValue(model_pembayaran_psb);
                            dbMaintenance.child("Tagihan").child(idPembayaranBulanan).setValue(model_pembayaran_bulanan);
                        }
                        dbMaintenance.child("Maintenance").child(parentMaintenanceId).child(idMaintenance).child("status").setValue(setStatus);
                        dbMaintenance.child("tglMaintenance").child(idMaintenance).child(tglProgressMaintenance).setValue(currentDate);
                        startActivity(new Intent(Activity_Update_Maintenance.this, Activity_Maintenance_Admin.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }

    private void getJenisLayanan() {
        dbMaintenance.child("Pemesanan").orderByChild("id_pelanggan").equalTo(model_merged_list.getId_user()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String statusPesanan = dataSnapshot.child("status").getValue().toString();
                        if (statusPesanan.equals("Dikonfirmasi")) {
                            nama_layanan = dataSnapshot.child("nama_layanan").getValue().toString();
                            harga_layanan = dataSnapshot.child("harga").getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}