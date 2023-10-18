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

import com.mywifi.mywifi.Activity.admin.Activity_Detail_Laporan_User;
import com.mywifi.mywifi.Model.Model_User;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_List_User extends RecyclerView.Adapter<Adapter_List_User.ViewHolder> {

    Context context;
    ArrayList<Model_User> listUser;

    public Adapter_List_User(Context context, ArrayList<Model_User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public Adapter_List_User.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pelanggan, parent, false);
        return new Adapter_List_User.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_List_User.ViewHolder holder, int position) {
        Model_User model_user = listUser.get(position);
        holder.nama_pelanggan.setText(model_user.getNama());
        holder.no_hp.setText(model_user.getHp());
        holder.alamat_pelanggan.setText(model_user.getNoRumah() + " " + model_user.getBlok());
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Laporan_User.class).putExtra("detail", listUser.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_pelanggan, no_hp, alamat_pelanggan;
        Button btn_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_pelanggan = itemView.findViewById(R.id.nama_pelanggan);
            no_hp = itemView.findViewById(R.id.no_hp);
            alamat_pelanggan = itemView.findViewById(R.id.alamat_pelanggan);
            btn_detail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
