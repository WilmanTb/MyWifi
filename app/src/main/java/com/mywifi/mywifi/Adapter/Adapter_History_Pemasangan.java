package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Pemesanan;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import java.util.ArrayList;

public class Adapter_History_Pemasangan extends RecyclerView.Adapter<Adapter_History_Pemasangan.ViewHolder> {

    Context context;
    ArrayList<Model_Pemesanan> list;

    public Adapter_History_Pemasangan(Context context, ArrayList<Model_Pemesanan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_History_Pemasangan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_history_pemasangan, parent, false);
        return new Adapter_History_Pemasangan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_History_Pemasangan.ViewHolder holder, int position) {
        Model_Pemesanan model_pemesanan = list.get(position);
        holder.txt_tanggal.setText(model_pemesanan.getTanggal());
        holder.nama_pelanggan.setText(model_pemesanan.getNama_pelanggan());
        holder.alamat_pelanggan.setText(model_pemesanan.getNo_rumah() + " " + model_pemesanan.getBlok_rumah());
        holder.jenis_layanan.setText(model_pemesanan.getNama_layanan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_tanggal, nama_pelanggan, alamat_pelanggan, jenis_layanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_tanggal = itemView.findViewById(R.id.txt_tanggal);
            nama_pelanggan = itemView.findViewById(R.id.nama_pelanggan);
            alamat_pelanggan = itemView.findViewById(R.id.alamat_pelanggan);
            jenis_layanan = itemView.findViewById(R.id.jenis_layanan);
        }
    }
}
