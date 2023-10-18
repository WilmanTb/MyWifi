package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Maintenance_Admin;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Activity_Maintenance_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private Button btn_create_maintenance;
    private RecyclerView rc_maintenance;
    private Adapter_Maintenance_Admin adapterMaintenanceAdmin;
    private ArrayList<Model_Merged_List> maintenanceList;
    private ArrayList<Model_Merged_List> userMaintenanceModel;
    private ArrayList<Model_Merged_List> networkMaintenanceModel;
    private ArrayList<Model_Merged_List> pemasanganMaintenanceModel;
    private ArrayList<Model_Merged_List> upgradedMaintenanceModel;
    private DatabaseReference dbMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_admin);

        initComponents();
        getMaintenanceData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Maintenance_Admin.this, Activity_Create_Maintenance.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Dashboard_Admin.class));
        finish();
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        btn_create_maintenance = findViewById(R.id.btn_create_maintenance);
        rc_maintenance = findViewById(R.id.rc_maintenance);
        rc_maintenance.setHasFixedSize(true);
        rc_maintenance.setLayoutManager(new LinearLayoutManager(this));
        maintenanceList = new ArrayList<>();
        userMaintenanceModel = new ArrayList<>();
        networkMaintenanceModel = new ArrayList<>();
        upgradedMaintenanceModel = new ArrayList<>();
        pemasanganMaintenanceModel = new ArrayList<>();
        adapterMaintenanceAdmin = new Adapter_Maintenance_Admin(this, maintenanceList, userMaintenanceModel, networkMaintenanceModel, pemasanganMaintenanceModel, upgradedMaintenanceModel);
        rc_maintenance.setAdapter(adapterMaintenanceAdmin);
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getMaintenanceData() {
        dbMaintenance.child("Maintenance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maintenanceList.clear();
                    dbMaintenance.child("Maintenance").child("Network").orderByChild("show").equalTo(true).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                networkMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List model_merged_list = dataSnapshot.getValue(Model_Merged_List.class);
                                    networkMaintenanceModel.add(model_merged_list);
                                    maintenanceList.addAll(networkMaintenanceModel);
                                }
                                adapterMaintenanceAdmin.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild("show").equalTo(true).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                pemasanganMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List model_merged_list = dataSnapshot.getValue(Model_Merged_List.class);
                                    pemasanganMaintenanceModel.add(model_merged_list);
                                    maintenanceList.addAll(pemasanganMaintenanceModel);
                                }
                                adapterMaintenanceAdmin.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("User").orderByChild("show").equalTo(true).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List model_merged_list = dataSnapshot.getValue(Model_Merged_List.class);
                                    userMaintenanceModel.add(model_merged_list);
                                    maintenanceList.addAll(userMaintenanceModel);
                                }
                                adapterMaintenanceAdmin.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Upgrade").orderByChild("show").equalTo(true).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                upgradedMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List model_merged_list = dataSnapshot.getValue(Model_Merged_List.class);
                                    upgradedMaintenanceModel.add(model_merged_list);
                                    maintenanceList.addAll(upgradedMaintenanceModel);
                                }
                                adapterMaintenanceAdmin.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}