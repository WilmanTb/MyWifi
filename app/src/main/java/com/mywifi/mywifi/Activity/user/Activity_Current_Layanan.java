package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Current_Layanan;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.Model.Model_Layanan;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Activity_Current_Layanan extends AppCompatActivity {

    private ImageView img_info, arrow_back;
    private TextView txt_info;
    private RecyclerView rc_current_layanan;
    private Adapter_Current_Layanan adapterCurrentLayanan;
    private ArrayList<Model_Layanan> modelLayanan;
    private FirebaseAuth userAuth;
    private DatabaseReference dbCurrentLayanan;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_layanan);

        initComponents();
        getCurrentLayanan();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents() {
        img_info = findViewById(R.id.img_not_found);
        arrow_back = findViewById(R.id.btn_back);
        txt_info = findViewById(R.id.txt_info);
        rc_current_layanan = findViewById(R.id.rc_current_layanan);
        rc_current_layanan.setHasFixedSize(true);
        rc_current_layanan.setLayoutManager(new LinearLayoutManager(this));
        modelLayanan = new ArrayList<>();
        adapterCurrentLayanan = new Adapter_Current_Layanan(this, modelLayanan);
        rc_current_layanan.setAdapter(adapterCurrentLayanan);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbCurrentLayanan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getCurrentLayanan() {
        dbCurrentLayanan.child("Pelanggan").orderByChild("id_pelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    img_info.setVisibility(View.GONE);
                    txt_info.setVisibility(View.GONE);
                    rc_current_layanan.setVisibility(View.VISIBLE);
                    modelLayanan.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("status").getValue().toString().equals("aktif")) {
                            Model_Layanan list = dataSnapshot.getValue(Model_Layanan.class);
                            modelLayanan.add(list);
                        }
                    }
                    adapterCurrentLayanan.notifyDataSetChanged();
                } else {
                    img_info.setVisibility(View.VISIBLE);
                    txt_info.setVisibility(View.VISIBLE);
                    rc_current_layanan.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}