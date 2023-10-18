package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Fragment.Gangguan_Fragment;
import com.mywifi.mywifi.Fragment.Home_Fragment;
import com.mywifi.mywifi.Fragment.Profile_Fragment;
import com.mywifi.mywifi.Fragment.Fragment_SpeedTest;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

public class Dashboard_User_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbMaintenance;
    private BottomNavigationView bottom_navigation;
    private Home_Fragment homeFragment = new Home_Fragment();
    private Profile_Fragment profileFragment = new Profile_Fragment();
    private Gangguan_Fragment gangguanFragment = new Gangguan_Fragment();
    private Fragment_SpeedTest speedTestFragment = new Fragment_SpeedTest();
    private int backPressCount;
    private String maintenanceUserKey, maintenanceNetworkKey, maintenancePemasanganKey, maintenanceUpgradeKey, maintenanceType, UID, pemesananKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        }

        mAuth = FirebaseAuth.getInstance();
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        bottom_navigation = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();

        getDataPemesanan();

        finishedMaintenanceNotification();
        showPopUpVerifiedAccount();

        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                        return true;
                    case R.id.gangguan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, gangguanFragment).commit();
                        return true;
                    case R.id.speed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, speedTestFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount == 1){
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 2000);
        } else if (backPressCount == 2){
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    private void finishedMaintenanceNotification() {
        checkChildNode("User", 4, new OnKeyFoundListener() {
            @Override
            public void onKeyFound(String key) {
                maintenanceUserKey = key;
                maintenanceType = "user";
                // Continue with the logic here based on the "User" node result
                showPopUpFinishedMaintenance(maintenanceType);
            }

            @Override
            public void onKeyNotFound() {
                checkChildNode("Network", 4, new OnKeyFoundListener() {
                    @Override
                    public void onKeyFound(String key) {
                        maintenanceNetworkKey = key;
                        maintenanceType = "network";
                        // Continue with the logic here based on the "User" node result
                        showPopUpFinishedMaintenance(maintenanceType);
                    }

                    @Override
                    public void onKeyNotFound() {
                        checkChildNode("Pemasangan", 4, new OnKeyFoundListener() {
                            @Override
                            public void onKeyFound(String key) {
                                maintenancePemasanganKey = key;
                                maintenanceType = "pemasangan";
                                // Continue with the logic here based on the "User" node result
                                showPopUpFinishedMaintenance(maintenanceType);
                            }

                            @Override
                            public void onKeyNotFound() {
                                checkChildNode("Upgrade", 4, new OnKeyFoundListener() {
                                    @Override
                                    public void onKeyFound(String key) {
                                        maintenanceUpgradeKey = key;
                                        maintenanceType = "upgrade";
                                        showPopUpFinishedMaintenance(maintenanceType);
                                    }

                                    @Override
                                    public void onKeyNotFound() {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void checkChildNode(String childNode, int status, OnKeyFoundListener listener) {
        dbMaintenance.child("Maintenance").child(childNode).orderByChild("status").equalTo(status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String show = dataSnapshot.child("show").getValue().toString();
                        if ("true".equals(show)) {
                            String key = dataSnapshot.getKey();
                            listener.onKeyFound(key);
                            return; // Return immediately after finding the first valid key
                        }
                    }
                }
                listener.onKeyNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the onCancelled event
            }
        });
    }

    // Interface to handle the key found or not found scenarios
    interface OnKeyFoundListener {
        void onKeyFound(String key);
        void onKeyNotFound();
    }

    private void showPopUpFinishedMaintenance(String maintenanceType){
        Dialog maintenanceDialog = new Dialog(this);
        maintenanceDialog.setContentView(R.layout.pop_up_finished_maintenance_notification);
        maintenanceDialog.show();

        TextView maintenanceInfo = maintenanceDialog.findViewById(R.id.txt_informasi);
        Button btn_ok = maintenanceDialog.findViewById(R.id.btn_ok);

        if (maintenanceType.equals("user")){
            maintenanceInfo.setText("Perbaikan internet kamu sudah selesai... Terimakasih sudah menunggu");
        } else if (maintenanceType.equals("network")) {
            maintenanceInfo.setText("Perbaikan server sudah selesai... Terimakasih sudah menunggu");
        } else if (maintenanceType.equals("pemasangan")) {
            maintenanceInfo.setText("Pemasangan internet kamu sudah selesai... Terimakasih sudah menunggu");
        } else if (maintenanceType.equals("upgrade")) {
            maintenanceInfo.setText("Upgrade internet kamu sudah selesai...Terimakasih sudah menunggu");
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maintenanceUserKey!=null){
                    dbMaintenance.child("Maintenance").child("User").child(maintenanceUserKey).child("show").setValue(false);
                }
                if (maintenanceNetworkKey!=null){
                    dbMaintenance.child("Maintenance").child("Network").child(maintenanceNetworkKey).child("show").setValue(false);
                }
                if (maintenancePemasanganKey!=null){
                    dbMaintenance.child("Maintenance").child("Pemasangan").child(maintenancePemasanganKey).child("show").setValue(false);
                    dbMaintenance.child("Pemesanan").child(pemesananKey).child("status").setValue("selesai");
                }
                if (maintenanceUpgradeKey!=null){
                    dbMaintenance.child("Maintenance").child("Upgrade").child(maintenanceUpgradeKey).child("show").setValue(false);
                    dbMaintenance.child("Pelanggan").child(UID).child("layanan").setValue("10 Mbps");
                }
                maintenanceDialog.dismiss();
            }
        });
    }

    private void showPopUpVerifiedAccount(){
        dbMaintenance.child("Verifikasi").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String type;
                    String status = snapshot.child("status").getValue().toString();
                    String show = snapshot.child("show").getValue().toString();
                    if (status.equals("false") && show.equals("true")){
                        type = "diterima";
                        verifiedDialog(type);
                    } else if (status.equals("ditolak") && show.equals("true")) {
                        type = "ditolak";
                        verifiedDialog(type);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifiedDialog(String verifiedType){
        Dialog verifiedNotification = new Dialog(this);
        verifiedNotification.setContentView(R.layout.popup_verified_notification);
        verifiedNotification.show();

        TextView verificationStatus = verifiedNotification.findViewById(R.id.txt_informasi);
        Button btn_ok = verifiedNotification.findViewById(R.id.btn_ok);
        ImageView img_verification = verifiedNotification.findViewById(R.id.img_maintenance);
        TextView greetTxt = verifiedNotification.findViewById(R.id.yay);

        if (verifiedType.equals("diterima")){
            verificationStatus.setText("Akun kamu sudah di verifikasi\nKamu sudah bisa menikmati layanan MyWifi oleh Jens Wifi");
        } else {
            verificationStatus.setText("Verifikasi akun kamu ditolak admin\nSilahkan verifikasi ulang");
            img_verification.setImageResource(R.drawable.sad);
            greetTxt.setText("Yah...");
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMaintenance.child("Verifikasi").child(UID).child("show").setValue(false);
                verifiedNotification.dismiss();
            }
        });

    }

    private void getDataPemesanan(){
        dbMaintenance.child("Pemesanan").orderByChild("id_pelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        pemesananKey = dataSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}