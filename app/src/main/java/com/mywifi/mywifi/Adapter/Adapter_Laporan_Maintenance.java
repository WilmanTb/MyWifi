package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_Laporan_Maintenance extends RecyclerView.Adapter<Adapter_Laporan_Maintenance.ViewHolder> {

    Context context;
    ArrayList<Model_Merged_List> mergedLists;
    ArrayList<Model_Merged_List> userLists;
    ArrayList<Model_Merged_List> networkLists;
    ArrayList<Model_Merged_List> pemasanganLists;
    ArrayList<Model_Merged_List> upgradeLists;
    String firstStatus, secondStatus, thirdStatus, fourthStatus;

    public Adapter_Laporan_Maintenance(Context context, ArrayList<Model_Merged_List> mergedLists, ArrayList<Model_Merged_List> userLists, ArrayList<Model_Merged_List> networkLists, ArrayList<Model_Merged_List> pemasanganLists, ArrayList<Model_Merged_List> upgradeLists) {
        this.context = context;
        this.mergedLists = mergedLists;
        this.userLists = userLists;
        this.networkLists = networkLists;
        this.pemasanganLists = pemasanganLists;
        this.upgradeLists = upgradeLists;
    }

    @NonNull
    @Override
    public Adapter_Laporan_Maintenance.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_laporan_maintenance, parent, false);
        return new Adapter_Laporan_Maintenance.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Laporan_Maintenance.ViewHolder holder, int position) {
        Model_Merged_List model_merged_list = mergedLists.get(position);
        String jenisMaintenance = model_merged_list.getJenis();
        String status = String.valueOf(model_merged_list.getStatus());
        if (jenisMaintenance.equals("Maintenance internet anda")){
            holder.jenis_maintenance.setText("Maintenance internet user");
            firstStatus = "Konfirmasi";
            secondStatus = "Checking";
            thirdStatus = "Perbaikan";
            fourthStatus = "Selesai";
            if (status.equals("1")){
                holder.status_maintenance.setText(firstStatus);
            } else if (status.equals("2")) {
                holder.status_maintenance.setText(secondStatus);
            } else if (status.equals("3")) {
                holder.status_maintenance.setText(thirdStatus);
            }else if (status.equals("4")) {
                holder.status_maintenance.setText(fourthStatus);
            }
        }else if (jenisMaintenance.equals("Maintenance server")){
            holder.jenis_maintenance.setText(jenisMaintenance);
            firstStatus = "Checking";
            secondStatus = "Perbaikan";
            thirdStatus = "Finishing";
            fourthStatus = "Selesai";
            if (status.equals("1")){
                holder.status_maintenance.setText(firstStatus);
            } else if (status.equals("2")) {
                holder.status_maintenance.setText(secondStatus);
            } else if (status.equals("3")) {
                holder.status_maintenance.setText(thirdStatus);
            }else if (status.equals("4")) {
                holder.status_maintenance.setText(fourthStatus);
            }
        } else if (jenisMaintenance.equals("Pemasangan internet anda")){
            holder.jenis_maintenance.setText("Pemasangan internet user");
            firstStatus = "Konfirmasi";
            secondStatus = "Survei";
            thirdStatus = "Pemasangan";
            fourthStatus = "Selesai";
            if (status.equals("1")){
                holder.status_maintenance.setText(firstStatus);
            } else if (status.equals("2")) {
                holder.status_maintenance.setText(secondStatus);
            } else if (status.equals("3")) {
                holder.status_maintenance.setText(thirdStatus);
            }else if (status.equals("4")) {
                holder.status_maintenance.setText(fourthStatus);
            }
        } else if (jenisMaintenance.equals("Upgrade internet anda")) {
            holder.jenis_maintenance.setText("Upgrade internet user");
            firstStatus = "Konfirmasi";
            secondStatus = "Managing";
            thirdStatus = "Updating";
            fourthStatus = "Selesai";
            if (status.equals("1")){
                holder.status_maintenance.setText(firstStatus);
            } else if (status.equals("2")) {
                holder.status_maintenance.setText(secondStatus);
            } else if (status.equals("3")) {
                holder.status_maintenance.setText(thirdStatus);
            }else if (status.equals("4")) {
                holder.status_maintenance.setText(fourthStatus);
            }
        }
        holder.tanggal_maintenance.setText(model_merged_list.getTanggal());
    }

    @Override
    public int getItemCount() {
        return mergedLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jenis_maintenance, tanggal_maintenance, status_maintenance;
        CardView cv_laporan_maintenance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jenis_maintenance = itemView.findViewById(R.id.jenis_maintenance);
            tanggal_maintenance = itemView.findViewById(R.id.tanggal_maintenance);
            status_maintenance = itemView.findViewById(R.id.status_maintenance);
            cv_laporan_maintenance = itemView.findViewById(R.id.cv_maintenance);
        }
    }
}
