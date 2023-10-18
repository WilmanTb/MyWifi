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
import com.mywifi.mywifi.Activity.admin.Activity_Detail_Verifikasi_Admin;
import com.mywifi.mywifi.Model.Model_Verifikasi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Adapter_VerifikasiAkun_Admin extends RecyclerView.Adapter<Adapter_VerifikasiAkun_Admin.ViewHolder> {

    Context context;
    ArrayList<Model_Verifikasi> list;

    public Adapter_VerifikasiAkun_Admin(Context context, ArrayList<Model_Verifikasi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_VerifikasiAkun_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_verifikasi_akun_admin, parent, false);
        return new Adapter_VerifikasiAkun_Admin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_VerifikasiAkun_Admin.ViewHolder holder, int position) {
        Model_Verifikasi model_verifikasi = list.get(position);
        holder.status_verifikasi.setText("Menunggu konfirmasi");
        String idUser = model_verifikasi.getId_user();
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
        holder.btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Verifikasi_Admin.class).putExtra("detail", list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_pelanggan, status_verifikasi;
        Button btn_konfirmasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_pelanggan = itemView.findViewById(R.id.nama_pelanggan);
            status_verifikasi = itemView.findViewById(R.id.status_verifikasi);
            btn_konfirmasi = itemView.findViewById(R.id.btn_konfirmasi);
        }
    }
}
