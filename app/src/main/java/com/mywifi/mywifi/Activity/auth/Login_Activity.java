package com.mywifi.mywifi.Activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.user.Dashboard_User_Activity;
import com.mywifi.mywifi.Activity.admin.Dashboard_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Teknisi;
import com.mywifi.mywifi.jClass.LoadingDialog;
import com.mywifi.mywifi.R;

public class Login_Activity extends AppCompatActivity {

    private TextView txt_register;
    private EditText et_email, et_password;
    private DatabaseReference dbUser;
    private FirebaseAuth mAuth;
    private String Email, Password, UID, passUser;
    private Button btn_login;
    private LoadingDialog loadingDialog;
    private int backPressCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Register_Activity.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getText();
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        if (Email.equals("admin") && Password.equals("admin")){
                            startActivity(new Intent(Login_Activity.this, Dashboard_Admin.class));
                            finish();
                        } else if (Email.equals("") && Password.equals("")) {
                            Toast.makeText(Login_Activity.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else if (Email.equals("teknisi1@gmail.com") && Password.equals("teknisi1")) {
                            loginTeknisi();
                        } else {
                            checkEmail(new EmailCallBack() {
                                @Override
                                public void onCallBack(String UID) {
                                    checkPassword(UID);
                                }
                            });
                        }

                    }
                };
                handler.postDelayed(runnable, 3000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount == 1) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            // Reset the back press count after a certain duration (e.g., 2 seconds)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 2000);
        } else if (backPressCount == 2) {
            // If the back button is pressed twice within the specified duration, exit the app
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    private void initComponents() {
        txt_register = findViewById(R.id.txt_register);
        btn_login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        loadingDialog = new LoadingDialog(this);
        mAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    }

    private void getText() {
        Email = et_email.getText().toString();
        Password = et_password.getText().toString();
    }

    private void checkEmail(EmailCallBack emailCallBack) {
        getText();
        dbUser.orderByChild("email").equalTo(Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            UID = dataSnapshot.getKey();
                        }
                        break;
                    }
                    emailCallBack.onCallBack(UID);
                }else{
                    Toast.makeText(Login_Activity.this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPassword(String UID){
        getText();
        dbUser.child(UID).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    passUser = snapshot.getValue().toString();
                    if (passUser.equals(Password)){
                        loginUser();
                    }else {
                        Toast.makeText(Login_Activity.this, "Password salah", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login_Activity.this, Dashboard_User_Activity.class));
                    finish();
                } else {
                    Toast.makeText(Login_Activity.this, "Login gagal\nMohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginTeknisi() {
        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login_Activity.this, Activity_Teknisi.class));
                    finish();
                } else {
                    Toast.makeText(Login_Activity.this, "Login gagal\nMohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private interface EmailCallBack {
        void onCallBack(String UID);
    }
}