package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Pemesanan;
import com.mywifi.mywifi.Model.Model_Pemesanan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Activity_Pemasangan_Admin extends AppCompatActivity {

    private RecyclerView rc_pemesanan;
    private ImageView arrow_back, img_info;
    private TextView txt_info;
    private Adapter_Pemesanan adapterPemesanan;
    private ArrayList<Model_Pemesanan> modelPemesanan;
    private DatabaseReference dbPemesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasangan_admin);

        initComponents();
        getDataPemesanan();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        img_info = findViewById(R.id.img_info);
        txt_info = findViewById(R.id.txt_info);
        rc_pemesanan = findViewById(R.id.rc_pemesanan);
        rc_pemesanan.setHasFixedSize(true);
        rc_pemesanan.setLayoutManager(new LinearLayoutManager(this));
        modelPemesanan = new ArrayList<>();
        adapterPemesanan = new Adapter_Pemesanan(this, modelPemesanan);
        rc_pemesanan.setAdapter(adapterPemesanan);
        dbPemesanan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getDataPemesanan(){
        dbPemesanan.child("Pemesanan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    modelPemesanan.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.child("status").getValue().equals("Menunggu konfirmasi") || dataSnapshot.child("status").getValue().equals("Dikonfirmasi")) {
                            Model_Pemesanan model_pemesanan = dataSnapshot.getValue(Model_Pemesanan.class);
                            modelPemesanan.add(model_pemesanan);
                            img_info.setVisibility(View.GONE);
                            txt_info.setVisibility(View.GONE);
                        }
                    }
                    adapterPemesanan.notifyDataSetChanged();
                    if (modelPemesanan == null){
                        img_info.setVisibility(View.VISIBLE);
                        txt_info.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}