package com.mywifi.mywifi.Activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mywifi.mywifi.Fragment.Home_Admin_Fragment;
import com.mywifi.mywifi.Fragment.Profil_Admin_Fragment;
import com.mywifi.mywifi.Fragment.Riwayat_Admin_Fragment;
import com.mywifi.mywifi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard_Admin extends AppCompatActivity {

    private int backPressCount;
    private BottomNavigationView bottom_navigation;
    private Home_Admin_Fragment home_adminFragment = new Home_Admin_Fragment();
    private Riwayat_Admin_Fragment riwayat_admin_fragment = new Riwayat_Admin_Fragment();
    private Profil_Admin_Fragment profil_admin_fragment = new Profil_Admin_Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        }


        bottom_navigation = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, home_adminFragment).commit();
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, home_adminFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profil_admin_fragment).commit();
                        return true;
                    case R.id.riwayat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, riwayat_admin_fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCount++;

        if (backPressCount == 1){
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 2000);
        } else if (backPressCount == 2){
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    private void check(){
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Create a list to store the formatted dates
        List<String> dateList = new ArrayList<>();

        // Format the date as "dd/MM/yyyy" and add to the list
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < 3; i++) {
            String formattedDate = sdf.format(calendar.getTime());
            dateList.add(formattedDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Increment the date by 1 day
        }

        // Print or use the date list as needed
        for (String date : dateList) {
            Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        }
    }
}
