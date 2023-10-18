package com.mywifi.mywifi.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mywifi.mywifi.Activity.admin.Activity_Laporan_Data_Pelanggan;
import com.mywifi.mywifi.Activity.admin.Activity_Laporan_Maintenance;
import com.mywifi.mywifi.Activity.admin.Activity_Laporan_Pemasangan;
import com.mywifi.mywifi.R;

public class Riwayat_Admin_Fragment extends Fragment {

    View view;
    CardView cv_laporan_pemasangan, cv_laporan_maintenance, cv_data_pelanggan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_riwayat__admin_, container, false);

        initComponents();

        cv_laporan_pemasangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Laporan_Pemasangan.class));
            }
        });


        cv_data_pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Laporan_Data_Pelanggan.class));
            }
        });

        cv_laporan_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Activity_Laporan_Maintenance.class));
            }
        });

        return view;
    }

    private void initComponents(){
       cv_data_pelanggan = view.findViewById(R.id.cv_data_pelanggan);
       cv_laporan_maintenance = view.findViewById(R.id.cv_laporan_maintenance);
       cv_laporan_pemasangan = view.findViewById(R.id.cv_laporan_pemasangan);
    }

}