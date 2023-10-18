package com.mywifi.mywifi.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.user.Activity_Buat_Laporan;
import com.mywifi.mywifi.Adapter.Adapter_Gangguan;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Gangguan_Fragment extends Fragment {

    private View view;
    private Button btn_buat_laporan;
    private ImageView img_info;
    private TextView txt_info;
    private RecyclerView rc_gangguan;
    private FirebaseAuth userAuth;
    private DatabaseReference dbGangguan;
    private Adapter_Gangguan adapter_gangguan;
    private ArrayList<Model_Gangguan> model_gangguan;
    private String UID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_gangguan, container, false);

        initComponents();
        getData();

        btn_buat_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPelangganStatus();
            }
        });

        return view;
    }

    private void initComponents(){
        btn_buat_laporan = view.findViewById(R.id.btn_buat_laporan);
        img_info = view.findViewById(R.id.img_info);
        txt_info = view.findViewById(R.id.txt_info);
        rc_gangguan = view.findViewById(R.id.rc_gangguan);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbGangguan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();

        rc_gangguan.setHasFixedSize(true);
        rc_gangguan.setLayoutManager(new LinearLayoutManager(getActivity()));
        model_gangguan = new ArrayList<>();
        adapter_gangguan = new Adapter_Gangguan(getActivity(), model_gangguan);
        rc_gangguan.setAdapter(adapter_gangguan);

    }

    private void getData(){
        dbGangguan.child("Gangguan").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_gangguan.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("true")) {
                            img_info.setVisibility(View.GONE);
                            txt_info.setVisibility(View.GONE);
                            rc_gangguan.setVisibility(View.VISIBLE);
                            Model_Gangguan list = dataSnapshot.getValue(Model_Gangguan.class);
                            model_gangguan.add(list);
                        } else {
                            img_info.setVisibility(View.VISIBLE);
                            txt_info.setVisibility(View.VISIBLE);
                            rc_gangguan.setVisibility(View.GONE);
                        }
                    }
                    adapter_gangguan.notifyDataSetChanged();
                }else {
                    img_info.setVisibility(View.VISIBLE);
                    txt_info.setVisibility(View.VISIBLE);
                    rc_gangguan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPelangganStatus(){
        dbGangguan.child("Pelanggan").orderByChild("id_pelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    startActivity(new Intent(getActivity(), Activity_Buat_Laporan.class));
                } else {
                    Toast.makeText(getContext(), "Kamu belum jadi pelanggan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
