package com.mywifi.mywifi.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.mywifi.mywifi.Activity.admin.Activity_Laporan_Gangguan_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Maintenance_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Pemasangan_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Transaksi_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Upgrade_Admin;
import com.mywifi.mywifi.Activity.admin.Activity_Verifikasi_Akun_Admin;
import com.mywifi.mywifi.R;

public class Home_Admin_Fragment extends Fragment {

    private View view;
    private ImageView img_profilAdmin;
    private ImageView menu_pemasangan, menu_transaksi, menu_maintenance, menu_laporan_gangguan, menu_verifikasi_user, menu_upgrade;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment__home__admin, container, false);

        initComponents();

        menu_pemasangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Pemasangan_Admin.class));
            }
        });

        menu_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Transaksi_Admin.class));
            }
        });

        menu_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Maintenance_Admin.class));
            }
        });

        menu_laporan_gangguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Laporan_Gangguan_Admin.class));
            }
        });

        menu_verifikasi_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Verifikasi_Akun_Admin.class));
            }
        });

        menu_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Upgrade_Admin.class));
            }
        });

        img_profilAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDestinationFragment();
            }
        });

        return view;
    }
    private void initComponents(){
        img_profilAdmin = view.findViewById(R.id.img_profilAdmin);
        menu_pemasangan = view.findViewById(R.id.menu_permintaan_pemasangan);
        menu_transaksi = view.findViewById(R.id.menu_pembayaran);
        menu_maintenance = view.findViewById(R.id.menu_maintenance);
        menu_laporan_gangguan = view.findViewById(R.id.menu_laporan_gangguan);
        menu_verifikasi_user = view.findViewById(R.id.menu_verifikasi_akun);
        menu_upgrade = view.findViewById(R.id.menu_upgrade);
    }

    private void switchToDestinationFragment() {
        Profil_Admin_Fragment profil_admin_fragment = new Profil_Admin_Fragment();

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, profil_admin_fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack so user can navigate back
        fragmentTransaction.commit();
    }
}