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
import com.mywifi.mywifi.Activity.admin.Activity_Detail_Laporan_Gangguan_Admin;
import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Adapter_Gangguan_Admin extends RecyclerView.Adapter<Adapter_Gangguan_Admin.ViewHolder> {

    Context context;
    ArrayList<Model_Gangguan> list;

    public Adapter_Gangguan_Admin(Context context, ArrayList<Model_Gangguan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Gangguan_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_gangguan_admin, parent, false);
        return new Adapter_Gangguan_Admin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Gangguan_Admin.ViewHolder holder, int position) {
        Model_Gangguan model_gangguan = list.get(position);
        holder.status_laporan.setText("Menunggu konfirmasi admin");
        holder.tanggal_laporan.setText(model_gangguan.getTanggal());
        String idPelanggan = model_gangguan.getId_user();
        DatabaseReference dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference("Users");
        dbUser.child(idPelanggan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.nama_pelapor.setText(snapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Laporan_Gangguan_Admin.class).putExtra("detail", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_pelapor, tanggal_laporan, status_laporan;
        Button btn_konfirmasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_pelapor = itemView.findViewById(R.id.nama_pelapor);
            tanggal_laporan = itemView.findViewById(R.id.tanggal_laporan);
            status_laporan = itemView.findViewById(R.id.status_laporan);
            btn_konfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
