package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Pembayaran;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Activity_Pembayaran_User extends AppCompatActivity {

    private RecyclerView rc_pembayaran;
    private Adapter_Pembayaran myAdapter;
    private ArrayList<Model_Pembayaran> modelPembayaran;
    private DatabaseReference dbPembayaran;
    private ImageView arrow_back;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_user);

        initComponents();

        getData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Pembayaran_User.this, Dashboard_User_Activity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Pembayaran_User.this, Dashboard_User_Activity.class));
        finish();
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        rc_pembayaran = findViewById(R.id.rc_pembayaran);
        rc_pembayaran.setHasFixedSize(true);
        rc_pembayaran.setLayoutManager(new LinearLayoutManager(this));
        modelPembayaran = new ArrayList<>();
        myAdapter = new Adapter_Pembayaran(this, modelPembayaran);
        rc_pembayaran.setAdapter(myAdapter);
        dbPembayaran = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Tagihan");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();
    }
    private void getData() {
        dbPembayaran.orderByChild("idPelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    modelPembayaran.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("status").getValue().toString().equals("belum dibayar")) {
                            Model_Pembayaran model_pembayaran = dataSnapshot.getValue(Model_Pembayaran.class);
                            modelPembayaran.add(model_pembayaran);
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}


