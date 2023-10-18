package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.mywifi.mywifi.Adapter.Adapter_Menu_Maintenance;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Activity_Maintenance_User extends AppCompatActivity {

    private ImageView arrow_back, img_info;
    private RecyclerView rc_maintenance;
    private TextView txt_pemberitahuan;
    private DatabaseReference dbMaintenance;
    private FirebaseAuth userAuth;
    private Adapter_Menu_Maintenance myAdapter;
    private ArrayList<Model_Merged_List> userMaintenanceModel;
    private ArrayList<Model_Merged_List> networkMaintenanceModel;
    private ArrayList<Model_Merged_List> pemasanganMaintenanceModel;
    private ArrayList<Model_Merged_List> upgradedMaintenanceModel;
    private ArrayList<Model_Merged_List> mergedLists;
    private String UID, stsUser, stsNetwork, stsUpgrade,stsPemasangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_user);

        initComponents();
        getData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Maintenance_User.this, Dashboard_User_Activity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Maintenance_User.this, Dashboard_User_Activity.class));
        finish();
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        img_info = findViewById(R.id.img_info);
        txt_pemberitahuan = findViewById(R.id.txt_pemberitahuan);
        rc_maintenance = findViewById(R.id.rc_maintenance);
        rc_maintenance.setHasFixedSize(true);
        rc_maintenance.setLayoutManager(new LinearLayoutManager(this));
        userMaintenanceModel = new ArrayList<>();
        networkMaintenanceModel = new ArrayList<>();
        pemasanganMaintenanceModel = new ArrayList<>();
        upgradedMaintenanceModel = new ArrayList<>();
        mergedLists = new ArrayList<>();
        myAdapter = new Adapter_Menu_Maintenance(this, mergedLists, userMaintenanceModel, networkMaintenanceModel, pemasanganMaintenanceModel, upgradedMaintenanceModel);
        rc_maintenance.setAdapter(myAdapter);
        userAuth = FirebaseAuth.getInstance();
        dbMaintenance = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    }

    private void getData() {
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();

        dbMaintenance.child("Maintenance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rc_maintenance.setVisibility(View.VISIBLE);
                    mergedLists.clear();
                    dbMaintenance.child("Maintenance").child("User").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String show = dataSnapshot.child("show").getValue().toString();
                                    stsUser = dataSnapshot.child("status").getValue().toString();
                                    if (show.equals("true")) {
                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                        userMaintenanceModel.add(modelMergedList);
                                        mergedLists.addAll(userMaintenanceModel);
                                        checkMaintenance();
                                    } else {
                                        checkMaintenance();
                                    }
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Network").orderByChild("id_user").equalTo("empty")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        networkMaintenanceModel.clear();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String show = dataSnapshot.child("show").getValue().toString();
                                            if (show.equals("true")) {
                                                Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                                networkMaintenanceModel.add(modelMergedList);
                                                mergedLists.addAll(networkMaintenanceModel);
                                                checkMaintenance();
                                            } else {
                                                checkMaintenance();
                                            }
                                        }
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                pemasanganMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String show = dataSnapshot.child("show").getValue().toString();
                                    if (show.equals("true")) {
                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                        pemasanganMaintenanceModel.add(modelMergedList);
                                        mergedLists.addAll(pemasanganMaintenanceModel);
                                        checkMaintenance();
                                    } else {
                                        checkMaintenance();
                                    }
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    dbMaintenance.child("Maintenance").child("Upgrade").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                upgradedMaintenanceModel.clear();
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    String show = dataSnapshot.child("show").getValue().toString();
//                                    if (show.equals("true")) {
//                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
//                                        upgradedMaintenanceModel.add(modelMergedList);
//                                        mergedLists.addAll(upgradedMaintenanceModel);
//                                        checkMaintenance();
//                                    } else {
//                                        checkMaintenance();
//                                    }
//                                }
//                                myAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkMaintenance() {
        if (mergedLists.isEmpty()) {
            txt_pemberitahuan.setVisibility(View.VISIBLE);
            img_info.setVisibility(View.VISIBLE);
            rc_maintenance.setVisibility(View.GONE);
        } else {
            txt_pemberitahuan.setVisibility(View.GONE);
            img_info.setVisibility(View.GONE);
            rc_maintenance.setVisibility(View.VISIBLE);
        }
    }
}