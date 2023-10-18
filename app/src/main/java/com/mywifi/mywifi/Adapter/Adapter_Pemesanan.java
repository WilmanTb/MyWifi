package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.admin.Actitivy_Detail_Pesanan_Admin;
import com.mywifi.mywifi.Activity.user.Activity_Detail_Maintenance_User;
import com.mywifi.mywifi.Model.Model_Pemesanan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Adapter_Pemesanan extends RecyclerView.Adapter<Adapter_Pemesanan.ViewHolder> {

    Context context;
    ArrayList<Model_Pemesanan> list;

    public Adapter_Pemesanan(Context context, ArrayList<Model_Pemesanan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Pemesanan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pemesanan, parent, false);
        return new Adapter_Pemesanan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Pemesanan.ViewHolder holder, int position) {
        Model_Pemesanan model_pemesanan = list.get(position);
        holder.tanggal_pemesanan.setText(model_pemesanan.getTanggal());
        holder.jenis_layanan.setText(model_pemesanan.getNama_layanan());
        holder.nama_pelanggan.setText(model_pemesanan.getNama_pelanggan());
        holder.btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Actitivy_Detail_Pesanan_Admin.class).putExtra("detail",list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_pelanggan, jenis_layanan, tanggal_pemesanan;
        CardView cv_pemesanan;
        Button btn_konfirmasi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_pelanggan = itemView.findViewById(R.id.nama_pelanggan);
            jenis_layanan = itemView.findViewById(R.id.jenis_layanan);
            tanggal_pemesanan = itemView.findViewById(R.id.tanggal_pesanan);
            cv_pemesanan = itemView.findViewById(R.id.cv_pemesanan);
            btn_konfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
