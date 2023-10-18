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
import com.mywifi.mywifi.Adapter.Adapter_List_User;
import com.mywifi.mywifi.Model.Model_User;
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

public class Activity_Laporan_Data_Pelanggan extends AppCompatActivity {

    private ImageView arrow_back;
    private RecyclerView rc_list_user;
    private Adapter_List_User adapter_list_user;
    private ArrayList<Model_User> model_user_list;
    private DatabaseReference dbUser;
    private FloatingActionButton btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_data_pelanggan);

        initComponents();
        getUserList();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData(model_user_list);
            }
        });
    }

    private void initComponents(){
        btn_download = findViewById(R.id.btn_download);
        arrow_back = findViewById(R.id.btn_back);
        rc_list_user = findViewById(R.id.rc_list_user);
        rc_list_user.setHasFixedSize(true);
        rc_list_user.setLayoutManager(new LinearLayoutManager(this));
        model_user_list = new ArrayList<>();
        adapter_list_user = new Adapter_List_User(this, model_user_list);
        rc_list_user.setAdapter(adapter_list_user);
        dbUser = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void getUserList(){
        dbUser.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_user_list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_User listUser = dataSnapshot.getValue(Model_User.class);
                        model_user_list.add(listUser);
                    }
                    adapter_list_user.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadData(ArrayList<Model_User> listUser) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Data User");
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

            XSSFSheet sheet = workbook.createSheet("Data User");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("Nama pelanggan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("Email");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("No. Rumah");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Blok");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Hp");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5);
            cell.setCellValue("Status");
            cell.setCellStyle(headerStyle);


            for (int i = 0; i < listUser.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(listUser.get(i).getNama());
                sheet.setColumnWidth(0, (listUser.get(i).getNama().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(listUser.get(i).getEmail());
                sheet.setColumnWidth(1, listUser.get(i).getEmail().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(listUser.get(i).getNoRumah());
                sheet.setColumnWidth(2, listUser.get(i).getNoRumah().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(listUser.get(i).getBlok());
                sheet.setColumnWidth(3, listUser.get(i).getBlok().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(listUser.get(i).getHp());
                sheet.setColumnWidth(4, listUser.get(i).getHp().length() * 400);

                cell = row.createCell(5);
                cell.setCellValue(listUser.get(i).getStatus());
                sheet.setColumnWidth(5, listUser.get(i).getStatus().length() * 400);


            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Activity_Laporan_Data_Pelanggan.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Activity_Laporan_Data_Pelanggan.this, "com.mywifi.mywifi.provider", path);
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