package com.mywifi.mywifi.Activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mywifi.mywifi.R;

public class Activity_Detail_Upgrade_User extends AppCompatActivity {

    private CardView cv_layanan1, cv_layanan2;
    private String Layanan = "", cvLayanan;
    private ImageView arrow_back;
    public static int IdLayanan;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_upgrade_user);

        Layanan = Activity_Upgrade.NamaLayanan;
        initComponents();

        cv_layanan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvLayanan = "5 Mbps";
                ID = 1;
                checkLayanan(cvLayanan, ID);
            }
        });

        cv_layanan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvLayanan = "10 Mbps";
                ID = 2;
                checkLayanan(cvLayanan, ID);
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Upgrade_User.this, Activity_Upgrade.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Detail_Upgrade_User.this, Activity_Upgrade.class));
        finish();
    }

    private void initComponents(){
        cv_layanan1 = findViewById(R.id.cv_layanan1);
        cv_layanan2 = findViewById(R.id.cv_layanan2);
        arrow_back = findViewById(R.id.btn_back);
    }

    private void checkLayanan(String layanan, int id){
        if (layanan.equals(Layanan)){
            Toast.makeText(this, "Anda sedang menggunakan layanan ini", Toast.LENGTH_SHORT).show();
        } else {
            IdLayanan = id;
            startActivity(new Intent(this, Activity_Kirim_Permintaan_Upgrade.class));
            finish();
        }
    }
}