package com.mywifi.mywifi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.user.Activity_Current_Layanan;
import com.mywifi.mywifi.Activity.user.Activity_Edit_Profil;
import com.mywifi.mywifi.Activity.user.Activity_Tentang_Aplikasi;
import com.mywifi.mywifi.Activity.user.Activity_Verifikasi_Akun;
import com.mywifi.mywifi.Activity.auth.Login_Activity;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends Fragment {

    View view;
    CircleImageView user_picture;
    TextView txt_nama_user, txt_email_user;
    Button btn_logout;
    FirebaseAuth userAuth;
    DatabaseReference dbProfile;
    String UID;
    LoadingDialog loadingDialog;
    CardView cv_edit_profil, cv_verifikasi, cv_layanan_aktif, cv_tentang_aplikasi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initComponents();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        userAuth.signOut();
                        Intent intent = new Intent(getActivity(), Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        loadingDialog.cancel();
                    }
                }; handler.postDelayed(runnable, 2000);
            }
        });

        getUserData();
        clickItem();

        return view;
    }

    private void initComponents(){
        cv_edit_profil = view.findViewById(R.id.cv_edit_profil);
        cv_verifikasi = view.findViewById(R.id.cv_verifikasi_akun);
        cv_layanan_aktif = view.findViewById(R.id.cv_layanan_aktif);
        cv_tentang_aplikasi = view.findViewById(R.id.cv_tentang_aplikasi);
        user_picture = view.findViewById(R.id.img_profilUser);
        txt_email_user = view.findViewById(R.id.txt_email_user);
        txt_nama_user = view.findViewById(R.id.txt_nama_user);
        btn_logout = view.findViewById(R.id.btn_logout);
        loadingDialog = new LoadingDialog(view.getContext());
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbProfile = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getUserData(){
        dbProfile.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    txt_nama_user.setText(snapshot.child("nama").getValue().toString());
                    txt_email_user.setText(snapshot.child("email").getValue().toString());
                    Glide.with(view.getContext()).load(snapshot.child("foto").getValue().toString()).into(user_picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickItem(){
        cv_edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Edit_Profil.class));
            }
        });

        cv_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Verifikasi_Akun.class));
            }
        });

        cv_layanan_aktif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Current_Layanan.class));
            }
        });

        cv_tentang_aplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Tentang_Aplikasi.class));
            }
        });
    }

}
