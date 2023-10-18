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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_History_Pemasangan;
import com.mywifi.mywifi.Model.Model_Merged_List;
import com.mywifi.mywifi.Model.Model_Pemesanan;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity_Laporan_Pemasangan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_filter;
    RecyclerView rc_history_pemasangan;
    Adapter_History_Pemasangan adapter_history_pemasangan;
    ArrayList<Model_Merged_List> model_merged_lists;
    ArrayList<Model_Pemesanan> model_pemesanan_list;
    TextView hari_txt, tanggal_txt, bulan_txt;
    String currentHari, currentBulan, currentDate, newCurrentDate, filterPeriode, startDate;
    ArrayAdapter<CharSequence> adapterFilterTanggal;
    Spinner spin_filter;
    int currentTanggal;
    DatabaseReference dbPemasangan;
    FloatingActionButton btn_download_laporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pemasangan);

        initComponents();
        getHistoryPemasanganData();
        getCurrenDate();
        spinnerPeriodePasang();

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredTGL();
            }
        });

        btn_download_laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData(model_pemesanan_list);
            }
        });
    }

    private void initComponents(){
        btn_filter = findViewById(R.id.btn_filter);
        spin_filter = findViewById(R.id.spin_filter);
        hari_txt = findViewById(R.id.hari_txt);
        tanggal_txt = findViewById(R.id.tanggal_txt);
        bulan_txt = findViewById(R.id.bulan_txt);
        rc_history_pemasangan = findViewById(R.id.rc_history_pemasangan);
        rc_history_pemasangan.setHasFixedSize(true);
        rc_history_pemasangan.setLayoutManager(new LinearLayoutManager(this));
        model_pemesanan_list = new ArrayList<>();
        adapter_history_pemasangan = new Adapter_History_Pemasangan(this, model_pemesanan_list);
        rc_history_pemasangan.setAdapter(adapter_history_pemasangan);
        dbPemasangan = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
        btn_download_laporan = findViewById(R.id.btn_download_laporan);
    }

    private void getHistoryPemasanganData(){
        dbPemasangan.child("Pemesanan").orderByChild("status").equalTo("selesai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    model_pemesanan_list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_Pemesanan listPemasangan = dataSnapshot.getValue(Model_Pemesanan.class);
                        model_pemesanan_list.add(listPemasangan);
                    }
                    adapter_history_pemasangan.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrenDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("id", "ID"));
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE, MMMM, yyyy", new Locale("id", "ID"));
        currentDate = simpleDateFormat.format(calendar.getTime());
        newCurrentDate = simpleDateFormat1.format(calendar.getTime());
        currentTanggal = extractDayFromString(currentDate);
        currentHari = extractAbbreviatedDayOfWeek(newCurrentDate);
        currentBulan = extractMonthAndYear(currentDate); // Use simpleDateFormat here instead of simpleDateFormat1
        tanggal_txt.setText(String.valueOf(currentTanggal));
        hari_txt.setText(currentHari);
        bulan_txt.setText(currentBulan);
    }

    private int extractDayFromString (String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error if the parsing fails.
        }
    }

    private String extractAbbreviatedDayOfWeek(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMMM, yyyy", new Locale("id", "ID"));
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return dateFormat.format(calendar.getTime()).split(",")[0].trim();
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error"; // Return "Error" to indicate an error if the parsing fails.
        }
    }

    private String extractMonthAndYear(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("id", "ID"));
        try {
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM, yyyy", new Locale("id", "ID"));
            return outputDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error"; // Return "Error" to indicate an error if the parsing fails.
        }
    }

    private void spinnerPeriodePasang() {
        adapterFilterTanggal = ArrayAdapter.createFromResource(this, R.array.WaktuPemasangan, android.R.layout.simple_spinner_item);
        adapterFilterTanggal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_filter.setAdapter(adapterFilterTanggal);
        spin_filter.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spin_filter = (Spinner) parent;
        if (!parent.getItemAtPosition(position).equals("--Periode Pasang--")) {
            filterPeriode = parent.getItemAtPosition(position).toString();
        } else {
            getHistoryPemasanganData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void filteredTGL() {
        if (filterPeriode != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            if (filterPeriode.equals("1 Minggu")) {
                calendar.add(Calendar.DAY_OF_WEEK, -7);
            } else if (filterPeriode.equals("1 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -30);
            } else if (filterPeriode.equals("3 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -90);
            } else if (filterPeriode.equals("6 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -180);
            }
            startDate = dateFormat.format(calendar.getTime());
            dbPemasangan.child("Pemesanan").orderByChild("tanggal").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        model_pemesanan_list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String tanggal = dataSnapshot.child("tanggal").getValue().toString();
                            try {
                                Date date = dateFormat.parse(startDate);
                                Date date2 = dateFormat.parse(tanggal);
                                if (date2.getTime() >= date.getTime()) {
                                    Model_Pemesanan listPemasangan = dataSnapshot.getValue(Model_Pemesanan.class);
                                    model_pemesanan_list.add(listPemasangan);
                                }
                                adapter_history_pemasangan.notifyDataSetChanged();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        Toast.makeText(Activity_Laporan_Pemasangan.this, "Data tidak ditemukan2", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void importData(ArrayList<Model_Pemesanan> listPemasangan) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Laporan Pemasangan");
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

            XSSFSheet sheet = workbook.createSheet("Data Laporan Pemasangan Internet");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("Nama pelanggan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("ID");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("No. Rumah");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Blok");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Tanggal");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5);
            cell.setCellValue("Jenis layanan");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(6);
            cell.setCellValue("Harga");
            cell.setCellStyle(headerStyle);

            for (int i = 0; i < listPemasangan.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(listPemasangan.get(i).getNama_pelanggan());
                sheet.setColumnWidth(0, (listPemasangan.get(i).getNama_pelanggan().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(listPemasangan.get(i).getId_pelanggan());
                sheet.setColumnWidth(1, listPemasangan.get(i).getId_pelanggan().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(listPemasangan.get(i).getNo_rumah());
                sheet.setColumnWidth(2, listPemasangan.get(i).getNo_rumah().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(listPemasangan.get(i).getBlok_rumah());
                sheet.setColumnWidth(3, listPemasangan.get(i).getBlok_rumah().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(listPemasangan.get(i).getTanggal());
                sheet.setColumnWidth(4, listPemasangan.get(i).getTanggal().length() * 400);

                cell = row.createCell(5);
                cell.setCellValue(listPemasangan.get(i).getNama_layanan());
                sheet.setColumnWidth(5, listPemasangan.get(i).getNama_layanan().length() * 400);

                cell = row.createCell(6);
                cell.setCellValue(listPemasangan.get(i).getHarga());
                sheet.setColumnWidth(6, listPemasangan.get(i).getHarga().length() * 400);

            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Activity_Laporan_Pemasangan.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Activity_Laporan_Pemasangan.this, "com.mywifi.mywifi.provider", path);
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