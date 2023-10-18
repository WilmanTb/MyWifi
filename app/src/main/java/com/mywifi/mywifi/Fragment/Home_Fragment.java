package com.mywifi.mywifi.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.user.Activity_Edit_Profil;
import com.mywifi.mywifi.Activity.user.Activity_Layanan_Internet;
import com.mywifi.mywifi.Activity.user.Activity_Pembayaran_User;
import com.mywifi.mywifi.Activity.user.Activity_Upgrade;
import com.mywifi.mywifi.Activity.user.Activity_Maintenance_User;
import com.mywifi.mywifi.Activity.user.Activity_Upload_Ktp;
import com.mywifi.mywifi.Adapter.Adapter_Maintenance;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Fragment extends Fragment {

    View view;
    TextView txt_pemberitahuan, txt_nama;
    ImageView img_info;
    CircleImageView foto_profil;
    CardView menu_layanan, menu_pembayaran, menu_upgrade, menu_maintenance;
    Adapter_Maintenance myAdapter;
    RecyclerView rc_info;
    ArrayList<Model_Merged_List> userMaintenanceModel;
    ArrayList<Model_Merged_List> networkMaintenanceModel;
    ArrayList<Model_Merged_List> pemasanganMaintenanceModel;
    ArrayList<Model_Merged_List> upgradeMaintenanceModel;
    ArrayList<Model_Merged_List> mergedLists;
    DatabaseReference dbMaintenance;
    FirebaseAuth userAuth;
    String UID, Status, Foto;
    LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();
        getData();
        userData();

        menu_layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        startActivity(new Intent(view.getContext(), Activity_Layanan_Internet.class));
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        menu_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (Status.equals("unverified")) {
                            verificationDialog();
                        } else {
                            startActivity(new Intent(view.getContext(), Activity_Pembayaran_User.class));
//                            Toast.makeText(view.getContext(), "Menu pembayaran belum dibuat", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.cancel();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        menu_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (Status.equals("unverified")) {
                            verificationDialog();
                        } else {
                            checkPelanggan();
                        }
                        loadingDialog.cancel();
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        menu_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (Status.equals("unverified")) {
                            verificationDialog();
                        } else {
                            startActivity(new Intent(view.getContext(), Activity_Maintenance_User.class));
                        }
                        loadingDialog.cancel();
                    }
                };
                handler.postDelayed(runnable, 2000);
//                startActivity(new Intent(view.getContext(), Test.class));
            }
        });

        foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Edit_Profil.class));
            }
        });


        return view;
    }

    private void initComponents() {
        txt_pemberitahuan = view.findViewById(R.id.txt_pemberitahuan);
        txt_nama = view.findViewById(R.id.txt_nama);
        menu_layanan = view.findViewById(R.id.menu_layanan);
        menu_pembayaran = view.findViewById(R.id.menu_pembayaran);
        menu_upgrade = view.findViewById(R.id.menu_upgrade);
        menu_maintenance = view.findViewById(R.id.menu_maintenance);
        img_info = view.findViewById(R.id.img_info);
        foto_profil = view.findViewById(R.id.img_profilUser);
        loadingDialog = new LoadingDialog(view.getContext());
        rc_info = view.findViewById(R.id.rc_info);
        rc_info.setHasFixedSize(true);
        rc_info.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        userMaintenanceModel = new ArrayList<>();
        networkMaintenanceModel = new ArrayList<>();
        mergedLists = new ArrayList<>();
        pemasanganMaintenanceModel = new ArrayList<>();
        upgradeMaintenanceModel = new ArrayList<>();
        myAdapter = new Adapter_Maintenance(view.getContext(), mergedLists, userMaintenanceModel, networkMaintenanceModel, pemasanganMaintenanceModel, upgradeMaintenanceModel);
        rc_info.setAdapter(myAdapter);
        userAuth = FirebaseAuth.getInstance();
        dbMaintenance = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void getData() {
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();

        dbMaintenance.child("Users").child(UID).child("nama").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Nama = snapshot.getValue().toString();
                txt_nama.setText(Nama);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbMaintenance.child("Maintenance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rc_info.setVisibility(View.VISIBLE);
                    mergedLists.clear();
                    dbMaintenance.child("Maintenance").child("User").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String show = dataSnapshot.child("show").getValue().toString();
                                    if (show.equals("true")) {
                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                        userMaintenanceModel.add(modelMergedList);
                                        mergedLists.addAll(userMaintenanceModel);
                                        checkMaintenance();
                                    } else {
                                        checkMaintenance();
                                    }
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Network").orderByChild("id_user").equalTo("empty")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        networkMaintenanceModel.clear();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String show = dataSnapshot.child("show").getValue().toString();
                                            if (show.equals("true")) {
                                                Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                                networkMaintenanceModel.add(modelMergedList);
                                                mergedLists.addAll(networkMaintenanceModel);
                                                checkMaintenance();
                                            } else {
                                                checkMaintenance();
                                            }
                                        }
                                        myAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                pemasanganMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String show = dataSnapshot.child("show").getValue().toString();
                                    if (show.equals("true")) {
                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
                                        pemasanganMaintenanceModel.add(modelMergedList);
                                        mergedLists.addAll(pemasanganMaintenanceModel);
                                        checkMaintenance();
                                    } else {
                                        checkMaintenance();
                                    }
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                    dbMaintenance.child("Maintenance").child("Upgrade").orderByChild("id_user").equalTo(UID).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                upgradeMaintenanceModel.clear();
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    String show = dataSnapshot.child("show").getValue().toString();
//                                    if (show.equals("true")) {
//                                        Model_Merged_List modelMergedList = dataSnapshot.getValue(Model_Merged_List.class);
//                                        upgradeMaintenanceModel.add(modelMergedList);
//                                        mergedLists.addAll(upgradeMaintenanceModel);
//                                        checkMaintenance();
//                                    } else {
//                                        checkMaintenance();
//                                    }
//                                }
//                                myAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkMaintenance() {
        if (mergedLists.isEmpty()) {
            txt_pemberitahuan.setVisibility(View.VISIBLE);
            img_info.setVisibility(View.VISIBLE);
            rc_info.setVisibility(View.GONE);
        } else {
            txt_pemberitahuan.setVisibility(View.GONE);
            img_info.setVisibility(View.GONE);
            rc_info.setVisibility(View.VISIBLE);
        }
    }

    private void userData() {
        dbMaintenance.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Status = snapshot.child("status").getValue().toString();
                Foto = snapshot.child("foto").getValue().toString();
                if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !Foto.equals("empty")) {
                    Glide.with(requireContext()).load(Foto).into(foto_profil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPelanggan(){
        dbMaintenance.child("Pelanggan").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String layanan = snapshot.child("layanan").getValue().toString();
                    if (layanan.equals("10 Mbps")){
                        Toast.makeText(view.getContext(), "Anda sudah menggunakan layanan tipe tinggi", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(view.getContext(), Activity_Upgrade.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificationDialog(){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pop_up_verifikasi);
        dialog.show();

        AppCompatButton btn_verifikasi = dialog.findViewById(R.id.btn_verifikasi);
        AppCompatButton btn_dismiss = dialog.findViewById(R.id.btn_nanti_aja);

        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Upload_Ktp.class));
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    private void finishedMaintenanceNotification(){
//        dbMaintenance.child("Maintenance").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    dbMaintenance.child("Maintenance").child("User").orderByChild("status").equalTo(4).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                                    String show = dataSnapshot.child("show").getValue().toString();
//                                    if (show.equals("true")){
//                                        maintenanceUserKey = dataSnapshot.getKey();
//                                    }
//                                }
//                            } else {
//                                dbMaintenance.child("Maintenance").child("Network").orderByChild("status").equalTo(4).addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if (snapshot.exists()){
//                                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                                                String show = dataSnapshot.child("show").getValue().toString();
//                                                if (show.equals("true")){
//                                                    maintenanceNetworkKey = dataSnapshot.getKey();
//                                                }
//                                            }
//                                        } else {
//                                            dbMaintenance.child("Maintenance").child("Pemasangan").orderByChild("status").equalTo(4).addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    if (snapshot.exists()){
//                                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                                                            String show = dataSnapshot.child("show").getValue().toString();
//                                                            if (show.equals("true")){
//                                                                maintenancePemasanganKey = dataSnapshot.getKey();
//                                                            }
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}
