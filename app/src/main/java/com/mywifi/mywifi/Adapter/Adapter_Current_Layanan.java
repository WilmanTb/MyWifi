package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mywifi.mywifi.Model.Model_Layanan;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_Current_Layanan extends RecyclerView.Adapter<Adapter_Current_Layanan.ViewHolder> {

    Context context;
    ArrayList<Model_Layanan> arrayList;

    public Adapter_Current_Layanan(Context context, ArrayList<Model_Layanan> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_Current_Layanan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_current_layanan, parent, false);
        return new Adapter_Current_Layanan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Current_Layanan.ViewHolder holder, int position) {
        Model_Layanan model_layanan = arrayList.get(position);
        holder.nama_layanan.setText(model_layanan.getLayanan());
        holder.tanggal_layanan.setText(model_layanan.getMulai_langganan());
        holder.status_layanan.setText(model_layanan.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_layanan, tanggal_layanan, status_layanan;
        CardView cv_current_layanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_layanan = itemView.findViewById(R.id.nama_layanan);
            tanggal_layanan = itemView.findViewById(R.id.tanggal_layanan);
            status_layanan = itemView.findViewById(R.id.status_layanan);
            cv_current_layanan = itemView.findViewById(R.id.cv_current_layanan);
        }
    }
}
