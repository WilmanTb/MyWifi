package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Tgl_Maintenance;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_Create_Maintenance extends AppCompatActivity {

    private TextInputLayout input_nama_pelanggan;
    private ImageView arrow_back;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btn_create_maintenance;
    private RadioGroup radioGroup;
    private RadioButton server_maintenance, user_maintenance;
    private DatabaseReference dbMaintenance;
    private LoadingDialog loadingDialog;
    private String jenisMaintenance = "", namaPelanggan="", currentDate, idUser;
    private ArrayList<String> arrayListPelanggan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);

        initComponents();
        cariNamaPelanggan();
        getCurrentDate();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.user_maintenance){
                    input_nama_pelanggan.setVisibility(View.VISIBLE);
                    jenisMaintenance = "user";
                } else {
                    input_nama_pelanggan.setVisibility(View.GONE);
                    jenisMaintenance = "server";
                }
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMaintenance = dbMaintenance.push().getKey();
                Model_Merged_List model_merged_list_server = new Model_Merged_List(idMaintenance, currentDate, Data_Value.maintainNetworkPicture, "empty", "Maintenance server", 1, true );
                Model_Tgl_Maintenance model_tgl_maintenance = new Model_Tgl_Maintenance(currentDate,"", "", "");
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (jenisMaintenance.equals("server")){
                            dbMaintenance.child("Maintenance").child("Network").child(idMaintenance).setValue(model_merged_list_server);
                            dbMaintenance.child("tglMaintenance").child(idMaintenance).setValue(model_tgl_maintenance);
                            loadingDialog.cancel();
                            startActivity(new Intent(Activity_Create_Maintenance.this, Activity_Maintenance_Admin.class));
                            finish();
                            Toast.makeText(Activity_Create_Maintenance.this, "Maintenance server berhasil dibuat", Toast.LENGTH_SHORT).show();
                        } else if (jenisMaintenance.equals("user")) {
                            namaPelanggan = autoCompleteTextView.getText().toString();
                            if (namaPelanggan.isEmpty()){
                                Toast.makeText(Activity_Create_Maintenance.this, "Nama pelanggan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!arrayListPelanggan.contains(namaPelanggan)){
                                    Toast.makeText(Activity_Create_Maintenance.this, "Nama pelanggan tidak ditemukan", Toast.LENGTH_SHORT).show();
                                } else {
                                    getIDUser(new IdUserCallBack() {
                                        @Override
                                        public void onCallBack(String ID) {
                                            if (ID.equals("")) {
                                                Toast.makeText(Activity_Create_Maintenance.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Model_Merged_List model_merged_list_user = new Model_Merged_List(idMaintenance, currentDate, Data_Value.maintainNetworkPicture, ID, "Maintenance internet anda", 1, true );
                                                dbMaintenance.child("Maintenance").child("User").child(idMaintenance).setValue(model_merged_list_user);
                                                dbMaintenance.child("tglMaintenance").child(idMaintenance).setValue(model_tgl_maintenance);
                                                loadingDialog.cancel();
                                                startActivity(new Intent(Activity_Create_Maintenance.this, Activity_Maintenance_Admin.class));
                                                finish();
                                                Toast.makeText(Activity_Create_Maintenance.this, "Maintenance user berhasil dibuat", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                        } else {
                            Toast.makeText(Activity_Create_Maintenance.this, "Pilih jenis maintenance terlebih dahulu", Toast.LENGTH_SHORT).show();
                            loadingDialog.cancel();
                        }
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        input_nama_pelanggan = findViewById(R.id.input_nama_pelanggan);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        btn_create_maintenance = findViewById(R.id.btn_create_maintenance);
        radioGroup = findViewById(R.id.radioGroup);
        server_maintenance = findViewById(R.id.server_maintenance);
        user_maintenance = findViewById(R.id.user_maintenance);
        loadingDialog = new LoadingDialog(this);
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void cariNamaPelanggan(){
        dbMaintenance.child("Users").orderByChild("status").equalTo("verified").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListPelanggan.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    arrayListPelanggan.add(dataSnapshot.child("nama").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Activity_Create_Maintenance.this, android.R.layout.simple_list_item_1, arrayListPelanggan);
                autoCompleteTextView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIDUser(IdUserCallBack idUserCallBack){
        namaPelanggan = autoCompleteTextView.getText().toString();
        dbMaintenance.child("Users").orderByChild("nama").equalTo(namaPelanggan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        idUser = dataSnapshot.getKey();
                        idUserCallBack.onCallBack(idUser);
                    }
                } else {
                    idUser = "";
                    idUserCallBack.onCallBack(idUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDateFormat.format(calendar.getTime());
    }

    private interface IdUserCallBack{
        void onCallBack(String ID);
    }
}