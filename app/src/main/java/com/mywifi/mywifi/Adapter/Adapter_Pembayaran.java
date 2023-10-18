package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mywifi.mywifi.Activity.user.Activity_Detail_Pembayaran_User;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_Pembayaran extends RecyclerView.Adapter<Adapter_Pembayaran.ViewHolder> {

    Context context;
    ArrayList<Model_Pembayaran> list;

    public Adapter_Pembayaran(Context context, ArrayList<Model_Pembayaran> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Pembayaran.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pembayaran, parent, false);
        return new Adapter_Pembayaran.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Pembayaran.ViewHolder holder, int position) {
        Model_Pembayaran model_pembayaran = list.get(position);
        holder.nama_layanan.setText(model_pembayaran.getNama());
        holder.harga_layanan.setText(formatRupiah(Double.parseDouble(model_pembayaran.getHarga())));
        holder.jenis_pembayaran.setText(model_pembayaran.getJenis());
        holder.cv_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Activity_Detail_Pembayaran_User.class).putExtra("detail",list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_layanan, harga_layanan, jenis_pembayaran;
        CardView cv_pembayaran;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_layanan = itemView.findViewById(R.id.nama_layanan);
            harga_layanan = itemView.findViewById(R.id.harga_layanan);
            jenis_pembayaran = itemView.findViewById(R.id.jenis_pembayaran);
            cv_pembayaran = itemView.findViewById(R.id.cv_pembayaran);
        }
    }
    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}
