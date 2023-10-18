package com.mywifi.mywifi.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Activity.auth.Login_Activity;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.jClass.LoadingDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Profil_Admin_Fragment extends Fragment {

    View view;
    TextView txt_jlh_saldo, txt_jlh_user, txt_jlh_pemasangan;
    Button btn_logout;
    DatabaseReference dbReference;
    LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profil__admin_, container, false);

        initComponents();
        getAllInfoData();
        getTotalIncome();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        startActivity(new Intent(getActivity(), Login_Activity.class));
                    }
                };
                handler.postDelayed(runnable, 2000);
            }
        });

        return view;
    }

    private void initComponents(){
        txt_jlh_pemasangan = view.findViewById(R.id.txt_jlh_pemasangan);
        txt_jlh_user = view.findViewById(R.id.txt_jlh_user);
        txt_jlh_saldo = view.findViewById(R.id.txt_jlh_saldo);
        btn_logout = view.findViewById(R.id.btn_logout);
        dbReference = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        loadingDialog = new LoadingDialog(getActivity());

    }

    private void getAllInfoData(){
        dbReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_jlh_user.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbReference.child("Maintenance").child("Pemasangan").orderByChild("status").equalTo(4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_jlh_pemasangan.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalIncome(){
        dbReference.child("Transaksi").orderByChild("status").equalTo("dibayar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalIncome = 0;
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String hargaString = dataSnapshot.child("harga").getValue().toString();
                        int harga = Integer.parseInt(hargaString);
                        totalIncome += harga;
                    }
                    txt_jlh_saldo.setText(formatRupiah(totalIncome));
                } else{
                    txt_jlh_saldo.setText(formatRupiah(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String formatRupiah(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("Rp ");
        symbols.setGroupingSeparator('.');

        DecimalFormat rupiahFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        rupiahFormat.setDecimalFormatSymbols(symbols);
        rupiahFormat.setMaximumFractionDigits(0);

        return rupiahFormat.format(amount);
    }
}