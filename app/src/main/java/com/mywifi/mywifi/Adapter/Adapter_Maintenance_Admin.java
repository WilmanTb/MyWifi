package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mywifi.mywifi.Activity.admin.Activity_Update_Maintenance;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_Maintenance_Admin extends RecyclerView.Adapter<Adapter_Maintenance_Admin.ViewHolder> {

    Context context;
    ArrayList<Model_Merged_List> mergedLists;
    ArrayList<Model_Merged_List> userLists;
    ArrayList<Model_Merged_List> networkLists;
    ArrayList<Model_Merged_List> pemasanganLists;
    ArrayList<Model_Merged_List> upgradeLists;
    String firstStatus, secondStatus, thirdStatus, fourthStatus;

    public Adapter_Maintenance_Admin(Context context, ArrayList<Model_Merged_List> mergedLists,ArrayList<Model_Merged_List> userLists, ArrayList<Model_Merged_List> networkLists,  ArrayList<Model_Merged_List> pemasanganLists,ArrayList<Model_Merged_List> upgradeLists) {
        this.context = context;
        this.userLists = userLists;
        this.networkLists = networkLists;
        this.mergedLists = mergedLists;
        this.pemasanganLists = pemasanganLists;
        this.upgradeLists = upgradeLists;
    }

    @NonNull
    @Override
    public Adapter_Maintenance_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_maintenance_admin, parent, false);
        return new Adapter_Maintenance_Admin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Maintenance_Admin.ViewHolder holder, int position) {
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
        holder.btn_update_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Update_Maintenance.class).putExtra("detail", mergedLists.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mergedLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jenis_maintenance, tanggal_maintenance, status_maintenance;
        Button btn_update_maintenance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jenis_maintenance = itemView.findViewById(R.id.jenis_maintenance);
            tanggal_maintenance = itemView.findViewById(R.id.tanggal_maintenance);
            status_maintenance = itemView.findViewById(R.id.status_maintenance);
            btn_update_maintenance = itemView.findViewById(R.id.btn_update_maintenance);
        }
    }
}
