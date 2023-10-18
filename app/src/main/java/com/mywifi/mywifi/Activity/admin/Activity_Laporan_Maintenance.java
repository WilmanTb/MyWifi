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
import com.mywifi.mywifi.Adapter.Adapter_Laporan_Maintenance;
import com.mywifi.mywifi.Model.Model_Merged_List;
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

public class Activity_Laporan_Maintenance extends AppCompatActivity {

    private RecyclerView rc_laporan_maintenance;
    private ImageView arrow_back;
    private FloatingActionButton btn_download_laporan;
    private DatabaseReference dbMaintenance;
    ArrayList<Model_Merged_List> userMaintenanceModel;
    ArrayList<Model_Merged_List> networkMaintenanceModel;
    ArrayList<Model_Merged_List> pemasanganMaintenanceModel;
    ArrayList<Model_Merged_List> upgradeMaintenanceModel;
    ArrayList<Model_Merged_List> mergedLists;
    private Adapter_Laporan_Maintenance adapter_laporan_maintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_maintenance);

        initComponents();
        getMaintenanceData();

        btn_download_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_laporan(mergedLists);
            }
        });

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        btn_download_laporan = findViewById(R.id.btn_download_laporan);
        dbMaintenance = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        userMaintenanceModel = new ArrayList<>();
        networkMaintenanceModel = new ArrayList<>();
        mergedLists = new ArrayList<>();
        pemasanganMaintenanceModel = new ArrayList<>();
        upgradeMaintenanceModel = new ArrayList<>();
        rc_laporan_maintenance = findViewById(R.id.rc_laporan_maintenance);
        rc_laporan_maintenance.setHasFixedSize(true);
        rc_laporan_maintenance.setLayoutManager(new LinearLayoutManager(this));
        adapter_laporan_maintenance = new Adapter_Laporan_Maintenance(this, mergedLists, userMaintenanceModel, networkMaintenanceModel, pemasanganMaintenanceModel, upgradeMaintenanceModel);
        rc_laporan_maintenance.setAdapter(adapter_laporan_maintenance);
    }

    private void getMaintenanceData() {
        dbMaintenance.child("Maintenance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mergedLists.clear();
                    dbMaintenance.child("Maintenance").child("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                userMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List list = dataSnapshot.getValue(Model_Merged_List.class);
                                    userMaintenanceModel.add(list);
                                    mergedLists.addAll(userMaintenanceModel);
                                }
                                adapter_laporan_maintenance.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Network").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                networkMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List list = dataSnapshot.getValue(Model_Merged_List.class);
                                    networkMaintenanceModel.add(list);
                                    mergedLists.addAll(networkMaintenanceModel);
                                }
                                adapter_laporan_maintenance.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Pemasangan").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                pemasanganMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List list = dataSnapshot.getValue(Model_Merged_List.class);
                                    pemasanganMaintenanceModel.add(list);
                                    mergedLists.addAll(pemasanganMaintenanceModel);
                                }
                                adapter_laporan_maintenance.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dbMaintenance.child("Maintenance").child("Upgrade").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                upgradeMaintenanceModel.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Model_Merged_List list = dataSnapshot.getValue(Model_Merged_List.class);
                                    upgradeMaintenanceModel.add(list);
                                    mergedLists.addAll(upgradeMaintenanceModel);
                                }
                                adapter_laporan_maintenance.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void download_laporan(ArrayList<Model_Merged_List> mergedLists) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Data Maintenance");
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

            XSSFSheet sheet = workbook.createSheet("Data Laporan Maintenance");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("ID maintenance");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("ID user");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("Jenis maintenance");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Tanggal");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Status");
            cell.setCellStyle(headerStyle);


            for (int i = 0; i < mergedLists.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(mergedLists.get(i).getIdMaintenance());
                sheet.setColumnWidth(0, (mergedLists.get(i).getIdMaintenance().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(mergedLists.get(i).getId_user());
                sheet.setColumnWidth(1, mergedLists.get(i).getId_user().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(mergedLists.get(i).getJenis());
                sheet.setColumnWidth(2, mergedLists.get(i).getJenis().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(mergedLists.get(i).getTanggal());
                sheet.setColumnWidth(3, mergedLists.get(i).getTanggal().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(mergedLists.get(i).getStatus());
                sheet.setColumnWidth(4, mergedLists.get(i).getStatus() * 400);


            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Activity_Laporan_Maintenance.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Activity_Laporan_Maintenance.this, "com.mywifi.mywifi.provider", path);
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

}