package com.mywifi.mywifi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Adapter_Gangguan extends RecyclerView.Adapter<Adapter_Gangguan.ViewHolder> {

    Context context;
    ArrayList<Model_Gangguan> arrayList;

    public Adapter_Gangguan(Context context, ArrayList<Model_Gangguan> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter_Gangguan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_gangguan_user,parent, false);
        return new Adapter_Gangguan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Gangguan.ViewHolder holder, int position) {
        Model_Gangguan model_gangguan = arrayList.get(position);
        holder.tanggal_gangguan.setText(model_gangguan.getTanggal());
        holder.status_gangguan.setText(model_gangguan.getStatus());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tanggal_gangguan, status_gangguan;
        CardView cv_gangguan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal_gangguan = itemView.findViewById(R.id.tanggal_gangguan);
            status_gangguan = itemView.findViewById(R.id.status_gangguan);
            cv_gangguan = itemView.findViewById(R.id.cv_gangguan);
        }
    }
}
