package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_VerifikasiAkun_Admin;
import com.mywifi.mywifi.Model.Model_Verifikasi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.util.ArrayList;

public class Activity_Verifikasi_Akun_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_verifikasi_akun;
    private DatabaseReference dbVerifikasi;
    private Adapter_VerifikasiAkun_Admin adapter_verifikasiAkun_admin;
    private ArrayList<Model_Verifikasi> model_verifikasiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_akun_admin);

        initComponents();
        getVerifikasiData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        rc_verifikasi_akun = findViewById(R.id.rc_verifikasi_akun_pelanggan);
        rc_verifikasi_akun.setHasFixedSize(true);
        rc_verifikasi_akun.setLayoutManager(new LinearLayoutManager(this));
        model_verifikasiArrayList = new ArrayList<>();
        adapter_verifikasiAkun_admin = new Adapter_VerifikasiAkun_Admin(this, model_verifikasiArrayList);
        rc_verifikasi_akun.setAdapter(adapter_verifikasiAkun_admin);
        dbVerifikasi = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getVerifikasiData(){
        dbVerifikasi.child("Verifikasi").orderByChild("status").equalTo("true").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_verifikasiArrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_Verifikasi model_verifikasi = dataSnapshot.getValue(Model_Verifikasi.class);
                        model_verifikasiArrayList.add(model_verifikasi);
                    }
                    adapter_verifikasiAkun_admin.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}