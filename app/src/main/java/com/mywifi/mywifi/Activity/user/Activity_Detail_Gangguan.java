package com.mywifi.mywifi.Activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mywifi.mywifi.Model.Model_Gangguan;
import com.mywifi.mywifi.R;

public class Activity_Detail_Gangguan extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView tanggal_gangguan, status_gangguan, keterangan_gangguan;
    private Model_Gangguan model_gangguan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gangguan);

        initComponents();
        getSerializable();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponents(){
        arrow_back = findViewById(R.id.btn_back);
        tanggal_gangguan = findViewById(R.id.tanggal_gangguan);
        status_gangguan = findViewById(R.id.status_gangguan);
        keterangan_gangguan = findViewById(R.id.keterangan_gangguan);
    }

    private void getSerializable(){
        Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_Gangguan){
            model_gangguan = (Model_Gangguan) object;
        }

        if (model_gangguan!=null){
            tanggal_gangguan.setText(model_gangguan.getTanggal());
            keterangan_gangguan.setText(model_gangguan.getKeterangan());

            String status = model_gangguan.getStatus();
            if (!status.equals("false")){
                status_gangguan.setText("Diterima");
            }
        }
    }
}