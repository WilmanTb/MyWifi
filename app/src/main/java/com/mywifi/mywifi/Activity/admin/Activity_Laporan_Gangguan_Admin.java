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
import com.mywifi.mywifi.Adapter.Adapter_Gangguan_Admin;
import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Activity_Laporan_Gangguan_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_laporan_gangguan;
    private Adapter_Gangguan_Admin adapter_gangguan_admin;
    private ArrayList<Model_Gangguan> model_gangguanArrayList;
    private DatabaseReference dbGangguan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_gangguan_admin);

        initComponents();
        getLaporanGangguanData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        rc_laporan_gangguan = findViewById(R.id.rc_laporan_gangguan);
        rc_laporan_gangguan.setHasFixedSize(true);
        rc_laporan_gangguan.setLayoutManager(new LinearLayoutManager(this));
        model_gangguanArrayList = new ArrayList<>();
        adapter_gangguan_admin = new Adapter_Gangguan_Admin(this, model_gangguanArrayList);
        rc_laporan_gangguan.setAdapter(adapter_gangguan_admin);
        dbGangguan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getLaporanGangguanData(){
        dbGangguan.child("Gangguan").orderByChild("status").equalTo("true").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_gangguanArrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_Gangguan listGangguan = dataSnapshot.getValue(Model_Gangguan.class);
                        model_gangguanArrayList.add(listGangguan);
                    }
                    adapter_gangguan_admin.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}