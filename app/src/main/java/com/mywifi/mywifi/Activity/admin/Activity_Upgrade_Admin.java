package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Upgrade_Admin;
import com.mywifi.mywifi.Model.Model_Upgrade;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Activity_Upgrade_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_upgrade_admin;
    private Adapter_Upgrade_Admin adapter_upgrade_admin;
    private ArrayList<Model_Upgrade> model_upgradeArrayList;
    private DatabaseReference dbUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_admin);

        initComponents();
        getPermintaanUpgrade();
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        rc_upgrade_admin = findViewById(R.id.rc_upgrade_admin);
        rc_upgrade_admin.setHasFixedSize(true);
        rc_upgrade_admin.setLayoutManager(new LinearLayoutManager(this));
        model_upgradeArrayList = new ArrayList<>();
        adapter_upgrade_admin = new Adapter_Upgrade_Admin(this, model_upgradeArrayList);
        rc_upgrade_admin.setAdapter(adapter_upgrade_admin);
        dbUpgrade = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getPermintaanUpgrade(){
        dbUpgrade.child("Upgrade").orderByChild("status").equalTo("Menunggu konfirmasi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_upgradeArrayList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Model_Upgrade listUpgrade = dataSnapshot.getValue(Model_Upgrade.class);
                        model_upgradeArrayList.add(listUpgrade);
                    }
                    adapter_upgrade_admin.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}