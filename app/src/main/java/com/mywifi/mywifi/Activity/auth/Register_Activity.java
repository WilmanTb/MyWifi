package com.mywifi.mywifi.Activity.auth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mywifi.mywifi.Activity.user.Dashboard_User_Activity;
import com.mywifi.mywifi.jClass.LoadingDialog;
import com.mywifi.mywifi.Model.Model_User;
import com.mywifi.mywifi.R;

public class Register_Activity extends AppCompatActivity {

    private EditText et_nama, et_email, et_hp, et_noRumah, et_blokRumah, et_password;
    private Button btn_register;
    private TextView txt_login;
    private DatabaseReference dbUsers;
    private FirebaseAuth mAuth;
    private BeautifulProgressDialog progressDialog;
    private String UID, Nama, Email, Handphone, NoRumah, BlokRumah, Password, Ktp = "empty", Status = "unverified", Foto = "https://firebasestorage.googleapis.com/v0/b/my-wifi-626bb.appspot.com/o/foto_profil%2Fman.png?alt=media&token=5ca022b9-1594-457a-b7ab-b2ff31c464b4";
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        checkEmpty();
                        loadingDialog.cancel();
                    }
                };
                handler.postDelayed(runnable,3000);
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register_Activity.this, Login_Activity.class));
        finish();
    }

    private void initComponents(){
        et_nama = findViewById(R.id.et_nama);
        et_email = findViewById(R.id.et_email);
        et_hp = findViewById(R.id.et_hp);
        et_noRumah = findViewById(R.id.et_noRumah);
        et_blokRumah = findViewById(R.id.et_blokRumah);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);
        dbUsers = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(this);
    }

    private void getText(){
        Nama = et_nama.getText().toString();
        Email = et_email.getText().toString();
        Handphone = et_hp.getText().toString();
        NoRumah = et_noRumah.getText().toString();
        BlokRumah = et_blokRumah.getText().toString();
        Password = et_password.getText().toString();
    }

    private void checkEmpty(){
        getText();
        if (Nama.isEmpty() || Email.isEmpty() || Handphone.isEmpty() || NoRumah.isEmpty() || BlokRumah.isEmpty() || Password.isEmpty()){
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            checkPasswordLength();
        }
    }

    private void checkPasswordLength() {
        if (Password.length() < 8){
            Toast.makeText(this, "Panjang password minimal 8 karakter", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(Nama, Email, Handphone, NoRumah, BlokRumah, Password, Ktp, Status, Foto);
        }
    }

    private void registerUser(String nama, String email, String handphone, String noRumah, String blokRumah, String password, String ktp, String status,String foto) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UID = firebaseUser.getUid();
                    Model_User modelUser = new Model_User(nama, email, handphone, blokRumah, noRumah, password, ktp, status, foto);
                    dbUsers.child("Users").child(UID).setValue(modelUser);
                    Toast.makeText(Register_Activity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register_Activity.this, Dashboard_User_Activity.class));
                    finish();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Register_Activity.this, "Registrasi gagal\nMohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}