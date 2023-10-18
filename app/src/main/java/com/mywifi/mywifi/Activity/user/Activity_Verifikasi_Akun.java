package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.R;

public class Activity_Verifikasi_Akun extends AppCompatActivity {

    private TextView txt_user_name, txt_email, txt_status_verifikasi;
    private ImageView arrow_back;
    private Button btn_verifikasi;
    private CardView cv_status_verifikasi;
    private FirebaseAuth userAuth;
    private DatabaseReference dbUser;
    private String UID, verificationRequestStatus = "empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_akun);

        initComponents();
        checkVerifyStatus();

        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificationRequestStatus.equals("true")){
                    Toast.makeText(Activity_Verifikasi_Akun.this, "Kamu sudah melakukan verifikasi. Admin akan segera memverifikasi akun kamu", Toast.LENGTH_SHORT).show();
                }else if (verificationRequestStatus.equals("false")){
                    Toast.makeText(Activity_Verifikasi_Akun.this, "Akun kamu sudah di verifikasi", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Activity_Verifikasi_Akun.this, Activity_Upload_Ktp.class));
                    finish();
                }
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Verifikasi_Akun.this, Dashboard_User_Activity.class));
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

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        btn_verifikasi = findViewById(R.id.btn_verifikasi);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_email = findViewById(R.id.txt_email);
        txt_status_verifikasi = findViewById(R.id.txt_status_verifikasi);
        cv_status_verifikasi = findViewById(R.id.cv_status_verifikasi);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void checkVerifyStatus() {
        dbUser.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("status").getValue().toString();
                int color = ContextCompat.getColor(Activity_Verifikasi_Akun.this, R.color.main);
                if (status.equals("unverified")) {
                    dbUser.child("Verifikasi").child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                cv_status_verifikasi.setCardBackgroundColor(Color.RED);
                                Dialog dialog = new Dialog(Activity_Verifikasi_Akun.this);
                                dialog.setContentView(R.layout.pop_up_verifikasi);
                                dialog.show();

                                AppCompatButton btn_verifikasi = dialog.findViewById(R.id.btn_verifikasi);
                                AppCompatButton btn_dismiss = dialog.findViewById(R.id.btn_nanti_aja);

                                btn_verifikasi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Activity_Verifikasi_Akun.this, Activity_Upload_Ktp.class));
                                        finish();
                                    }
                                });

                                btn_dismiss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                verificationRequestStatus = snapshot.child("status").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    cv_status_verifikasi.setCardBackgroundColor(color);
                }
                txt_status_verifikasi.setText(status);
                txt_email.setText(snapshot.child("email").getValue().toString());
                txt_user_name.setText(snapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}