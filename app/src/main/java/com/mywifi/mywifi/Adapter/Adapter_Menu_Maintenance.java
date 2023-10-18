package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mywifi.mywifi.Activity.user.Activity_Detail_Maintenance_User;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_Menu_Maintenance extends RecyclerView.Adapter<Adapter_Menu_Maintenance.ViewHolder> {

    Context context;
    ArrayList<Model_Merged_List> mergedLists;
    ArrayList<Model_Merged_List> userLists;
    ArrayList<Model_Merged_List> networkLists;
    ArrayList<Model_Merged_List> pemasanganLists;
    ArrayList<Model_Merged_List> upgradeLists;

    public Adapter_Menu_Maintenance(Context context, ArrayList<Model_Merged_List> mergedLists,ArrayList<Model_Merged_List> userLists, ArrayList<Model_Merged_List> networkLists,  ArrayList<Model_Merged_List> pemasanganLists, ArrayList<Model_Merged_List> upgradeLists) {
        this.context = context;
        this.userLists = userLists;
        this.networkLists = networkLists;
        this.mergedLists = mergedLists;
        this.pemasanganLists = pemasanganLists;
        this.upgradeLists = upgradeLists;
    }

    @NonNull
    @Override
    public Adapter_Menu_Maintenance.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_maintenance, parent, false);
        return new Adapter_Menu_Maintenance.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Menu_Maintenance.ViewHolder holder, int position) {
        Model_Merged_List model_merged_list = mergedLists.get(position);
        holder.txt_maintenance.setText(model_merged_list.getJenis());
        holder.txt_tanggal.setText(model_merged_list.getTanggal());
        Glide.with(context).load(mergedLists.get(position).getGambar()).into(holder.img_maintenance);
        holder.cv_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Maintenance_User.class).putExtra("detail",mergedLists.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mergedLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv_maintenance;
        TextView txt_maintenance, txt_tanggal;
        ImageView img_maintenance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_maintenance = itemView.findViewById(R.id.cv_maintenance);
            txt_maintenance = itemView.findViewById(R.id.txt_maintenance);
            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
            img_maintenance = itemView.findViewById(R.id.img_maintenance);
        }
    }
}
