package com.mywifi.mywifi.Activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mywifi.mywifi.R;

public class Activity_Layanan_Internet extends AppCompatActivity {

    CardView cv_layanan1, cv_layanan2, cv_layanan3;
    ImageView arrow_left;
    public static int ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_internet);

        cv_layanan1 = findViewById(R.id.cv_layanan1);
        cv_layanan2 = findViewById(R.id.cv_layanan2);
        cv_layanan3 = findViewById(R.id.cv_layanan3);
        arrow_left = findViewById(R.id.btn_back);

        cv_layanan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = 1;
                startActivity(new Intent(Activity_Layanan_Internet.this, Activity_Detail_Layanan.class));
            }
        });

        cv_layanan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = 2;
                startActivity(new Intent(Activity_Layanan_Internet.this, Activity_Detail_Layanan.class));
            }
        });

        cv_layanan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = 3;
                startActivity(new Intent(Activity_Layanan_Internet.this, Activity_Detail_Layanan.class));
            }
        });

        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Layanan_Internet.this, Dashboard_User_Activity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Dashboard_User_Activity.class));
        finish();
    }
}