package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Pembayaran;
import com.mywifi.mywifi.Adapter.Adapter_Transaksi;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.Model.Model_Transaksi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.Data_Value;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Activity_Transaksi_Admin extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_riwayat_transaksi;
    private DatabaseReference dbTransaksi;
    private Adapter_Transaksi adapter_transaksi;
    private ArrayList<Model_Transaksi> model_transaksiArrayList;
    private ArrayList<Model_Pembayaran> model_pembayaranArrayList;
    private Adapter_Pembayaran adapter_pembayaran;
    private FloatingActionButton btn_download;
    private RecyclerView rc_riwayat_transaksi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_admin);

        initComponents();
        getTransaksiData();
//        getTransaksiData1();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData(model_transaksiArrayList);
            }
        });
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        rc_riwayat_transaksi1 = findViewById(R.id.rc_riwayat_transaksi1);
        dbTransaksi = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        rc_riwayat_transaksi = findViewById(R.id.rc_riwayat_transaksi);
        rc_riwayat_transaksi.setHasFixedSize(true);
        rc_riwayat_transaksi.setLayoutManager(new LinearLayoutManager(this));
        model_transaksiArrayList = new ArrayList<>();
        model_pembayaranArrayList = new ArrayList<>();
        adapter_transaksi = new Adapter_Transaksi(this, model_transaksiArrayList);
        adapter_pembayaran = new Adapter_Pembayaran(this, model_pembayaranArrayList);
        rc_riwayat_transaksi.setAdapter(adapter_transaksi);
        rc_riwayat_transaksi1.setAdapter(adapter_pembayaran);
        btn_download = findViewById(R.id.btn_download);
    }

    private void getTransaksiData() {
        dbTransaksi.child("Transaksi").orderByChild("status").equalTo("dibayar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    model_transaksiArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Model_Transaksi listTransaksi = dataSnapshot.getValue(Model_Transaksi.class);
                        model_transaksiArrayList.add(listTransaksi);
                    }
                    adapter_transaksi.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getTransaksiData1() {
//        dbTransaksi.child("Tagihan").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    model_pembayaranArrayList.clear();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Model_Pembayaran listTransaksi = dataSnapshot.getValue(Model_Pembayaran.class);
//                        model_pembayaranArrayList.add(listTransaksi);
//                    }
//                    adapter_pembayaran.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void downloadData(ArrayList<Model_Transaksi> listUser) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Data Transaksi");
            if (!root.exists())
                root.mkdirs();
            File path = new File(root, "/" + strDate + ".xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(path);

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);

            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            headerStyle.setFont(font);

            XSSFSheet sheet = workbook.createSheet("Data Laporan Transaksi");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("ID pelanggan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("ID Pembayaran");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("Layanan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Total bayar");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Tanggal bayar");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5);
            cell.setCellValue("Status");
            cell.setCellStyle(headerStyle);


            for (int i = 0; i < listUser.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(listUser.get(i).getIdPelanggan());
                sheet.setColumnWidth(0, (listUser.get(i).getIdPelanggan().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(listUser.get(i).getIdTransaksi());
                sheet.setColumnWidth(1, listUser.get(i).getIdTransaksi().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(listUser.get(i).getJenis());
                sheet.setColumnWidth(2, listUser.get(i).getJenis().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(listUser.get(i).getHarga());
                sheet.setColumnWidth(3, listUser.get(i).getHarga().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(listUser.get(i).getTanggal());
                sheet.setColumnWidth(4, listUser.get(i).getTanggal().length() * 400);

                cell = row.createCell(5);
                cell.setCellValue(listUser.get(i).getStatus());
                sheet.setColumnWidth(5, listUser.get(i).getStatus().length() * 400);


            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Activity_Transaksi_Admin.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Activity_Transaksi_Admin.this, "com.mywifi.mywifi.provider", path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mimeType = getContentResolver().getType(uri);
            intent.setDataAndType(uri, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Buka dengan...");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(chooser);
//            }
            startActivity(chooser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    private void downloadData(ArrayList<Model_Pembayaran> listUser) {
//        try {
//            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
//            File root = new File(Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Data Transaksi");
//            if (!root.exists())
//                root.mkdirs();
//            File path = new File(root, "/" + strDate + ".xlsx");
//
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            FileOutputStream outputStream = new FileOutputStream(path);
//
//            XSSFCellStyle headerStyle = workbook.createCellStyle();
//            headerStyle.setAlignment(HorizontalAlignment.CENTER);
//            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            headerStyle.setBorderTop(BorderStyle.MEDIUM);
//            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
//            headerStyle.setBorderRight(BorderStyle.MEDIUM);
//            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
//
//            XSSFFont font = workbook.createFont();
//            font.setFontHeightInPoints((short) 12);
//            font.setColor(IndexedColors.WHITE.getIndex());
//            font.setBold(true);
//            headerStyle.setFont(font);
//
//            XSSFSheet sheet = workbook.createSheet("Data Laporan Transaksi");
//            XSSFRow row = sheet.createRow(0);
//
//            XSSFCell cell = row.createCell(0);
//            cell.setCellValue("ID pelanggan");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue("ID Pembayaran");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue("Layanan");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue("Jenis pembayaran");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue("Total bayar");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue("Tanggal bayar");
//            cell.setCellStyle(headerStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue("Status");
//            cell.setCellStyle(headerStyle);
//
//
//            for (int i = 0; i < listUser.size(); i++) {
//                row = sheet.createRow(i + 1);
//
//                cell = row.createCell(0);
//                cell.setCellValue(listUser.get(i).getIdPelanggan());
//                sheet.setColumnWidth(0, (listUser.get(i).getIdPelanggan().length() + 30) * 256);
//
//                cell = row.createCell(1);
//                cell.setCellValue(listUser.get(i).getIdPembayaran());
//                sheet.setColumnWidth(1, listUser.get(i).getIdPembayaran().length() * 400);
//
//                cell = row.createCell(2);
//                cell.setCellValue(listUser.get(i).getNama());
//                sheet.setColumnWidth(2, listUser.get(i).getNama().length() * 400);
//
//                cell = row.createCell(3);
//                cell.setCellValue(listUser.get(i).getJenis());
//                sheet.setColumnWidth(3, listUser.get(i).getJenis().length() * 400);
//
//                cell = row.createCell(4);
//                cell.setCellValue(listUser.get(i).getHarga());
//                sheet.setColumnWidth(4, listUser.get(i).getHarga().length() * 400);
//
//                cell = row.createCell(5);
//                cell.setCellValue(listUser.get(i).getTanggal());
//                sheet.setColumnWidth(5, listUser.get(i).getTanggal().length() * 400);
//
//                cell = row.createCell(6);
//                cell.setCellValue(listUser.get(i).getStatus());
//                sheet.setColumnWidth(6, listUser.get(i).getStatus().length() * 400);
//
//
//            }
//            workbook.write(outputStream);
//            outputStream.close();
//            Toast.makeText(Activity_Transaksi_Admin.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();
//
//            Uri uri = FileProvider.getUriForFile(Activity_Transaksi_Admin.this, "com.mywifi.mywifi.provider", path);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            String mimeType = getContentResolver().getType(uri);
//            intent.setDataAndType(uri, mimeType);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Intent chooser = Intent.createChooser(intent, "Buka dengan...");
////            if (intent.resolveActivity(getPackageManager()) != null) {
////                startActivity(chooser);
////            }
//            startActivity(chooser);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}