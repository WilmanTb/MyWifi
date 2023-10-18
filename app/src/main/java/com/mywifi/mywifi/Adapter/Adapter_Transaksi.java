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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.admin.Activity_Detail_Transaksi_Admin;
import com.mywifi.mywifi.Model.Model_Transaksi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Adapter_Transaksi extends RecyclerView.Adapter<Adapter_Transaksi.ViewHolder> {

    Context context;
    ArrayList<Model_Transaksi> list;

    public Adapter_Transaksi(Context context, ArrayList<Model_Transaksi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Transaksi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_riwayat_transaksi, parent, false);
        return new Adapter_Transaksi.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Transaksi.ViewHolder holder, int position) {
        Model_Transaksi model_transaksi = list.get(position);
        holder.jenis_pembayaran.setText(model_transaksi.getJenis());
        holder.tanggal_pembayaran.setText(model_transaksi.getTanggal());

        String idUser = model_transaksi.getIdPelanggan();
        DatabaseReference dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference("Users");
        dbUser.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.nama_pelanggan.setText(snapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Transaksi_Admin.class).putExtra("detail", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_pelanggan, jenis_pembayaran, tanggal_pembayaran;
        Button btn_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_pelanggan = itemView.findViewById(R.id.nama_pelanggan);
            jenis_pembayaran = itemView.findViewById(R.id.jenis_pembayaran);
            tanggal_pembayaran = itemView.findViewById(R.id.tanggal_pembayaran);
            btn_detail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
