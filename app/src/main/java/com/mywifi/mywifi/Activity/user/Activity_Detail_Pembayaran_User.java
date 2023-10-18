package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.Model.Model_Transaksi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_Detail_Pembayaran_User extends AppCompatActivity implements TransactionFinishedCallback {

    private TextView nama_layanan, jenis_pembayaran, status_pembayaran, harga_layanan;
    private Button btn_bayar;
    private ImageView arrow_back;
    private DatabaseReference dbPembayaran;
    private FirebaseAuth userAuth;
    private LoadingDialog loadingDialog;
    public static String UID, NamaLayanan, JenisPembayaran, HargaLayanan, transactionId, NamaUser, EmailUser, NoHp;
    private Model_Pembayaran model_pembayaran;
    private String Tanggal;
    private static long randomInteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran_user);

        initComponents();
        getData();
        getCurrentDate();
        getUserData();
        getTagihan();
        transactionID();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Detail_Pembayaran_User.this, Activity_Pembayaran_User.class));
                finish();
            }
        });

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.cancel();
                        MidtransSDK.getInstance().setTransactionRequest(transactionRequest(transactionId,Double.parseDouble(HargaLayanan), 1, NamaLayanan));
                        MidtransSDK.getInstance().startPaymentUiFlow(Activity_Detail_Pembayaran_User.this );
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        });

        createPayment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Pembayaran_User.class));
        finish();
    }

    private void initComponents() {
        nama_layanan = findViewById(R.id.nama_layanan);
        jenis_pembayaran = findViewById(R.id.jenis_pembayaran);
        status_pembayaran = findViewById(R.id.status_pembayaran);
        harga_layanan = findViewById(R.id.harga_layanan);
        btn_bayar = findViewById(R.id.btn_bayar);
        arrow_back = findViewById(R.id.btn_back);
        userAuth = FirebaseAuth.getInstance();
        dbPembayaran = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        loadingDialog = new LoadingDialog(this);

        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
    }

    private void getData(){
        final Object obj = getIntent().getSerializableExtra("detail");
        if (obj instanceof Model_Pembayaran){
            model_pembayaran = (Model_Pembayaran) obj;
        }

        if (model_pembayaran != null){
            nama_layanan.setText(model_pembayaran.getNama());
            jenis_pembayaran.setText(model_pembayaran.getJenis());
            status_pembayaran.setText(model_pembayaran.getStatus());
            harga_layanan.setText(formatRupiah(Double.parseDouble(model_pembayaran.getHarga())));
//            NamaLayanan = model_pembayaran.getNama();
//            JenisPembayaran = model_pembayaran.getJenis();
//            HargaLayanan = model_pembayaran.getHarga();
        }
    }
    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private void createPayment(){
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl("https://jenswifi.000webhostapp.com/response.php/")
                .setClientKey("SB-Mid-client-Q-FGg6jCULeq1jXG")
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .buildSDK();
    }

    public static CustomerDetails customerDetails(){
        Activity_Detail_Pembayaran_User activity_detail_pembayaran_user = new Activity_Detail_Pembayaran_User();
        String nama = activity_detail_pembayaran_user.NamaUser;
        String email = activity_detail_pembayaran_user.EmailUser;
        String hp = activity_detail_pembayaran_user.NoHp;
        CustomerDetails cd = new CustomerDetails();
        cd.setCustomerIdentifier(nama);
        cd.setFirstName(nama);
        cd.setEmail(email);
        cd.setPhone(hp);
        return cd;
    }

    public static TransactionRequest transactionRequest(String id, double price, int qty, String name){
        Activity_Detail_Pembayaran_User activity_detail_pembayaran_user = new Activity_Detail_Pembayaran_User();
        double amount = Double.parseDouble(activity_detail_pembayaran_user.HargaLayanan);
        String orderID = String.valueOf(activity_detail_pembayaran_user.randomInteger);
        TransactionRequest request =  new TransactionRequest("MyWifi-"+orderID + " " ,amount);
        request.setCustomerDetails(customerDetails());
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        return request;
    }

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        if (transactionResult.getStatus().equals("settlement")) {
           String basurl = "https://api.sandbox.midtrans.com/v2/";
           String transId = transactionResult.getResponse().getTransactionId();
           String status = "/status";
           String Url = basurl + transId + status;
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(Url)
                    .addHeader("accept", "application/json")
                    .addHeader("authorization", "Basic U0ItTWlkLXNlcnZlci1tRTVJZU9LZnh4RjNmLW5zOWpaMjYtbjQ6")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            final String statusCode = jsonObject.getString("status_code");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String statusTransaksi = statusCode;
                                    String idTransaksi = dbPembayaran.push().getKey();
                                    Model_Transaksi model_transaksi = new Model_Transaksi(UID, model_pembayaran.getNama(), Tanggal, model_pembayaran.getHarga(), "dibayar", idTransaksi);
                                    if (statusTransaksi.equals("200")){
                                        dbPembayaran.child("Tagihan").child(model_pembayaran.getIdPembayaran()).child("status").setValue("dibayar");
                                        dbPembayaran.child("Transaksi").child(idTransaksi).setValue(model_transaksi);
                                        Toast.makeText(Activity_Detail_Pembayaran_User.this, "Tagihan berhasil dibayar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Activity_Detail_Pembayaran_User.this, "Tagihan pembayaran menunggu dibayarkan", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Handle the error response
                    }

                    // Close the response body
                    response.body().close();
                }
            });

        } else {
            // Payment failed
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }


    private void transactionID(){
        Random random = new Random();
        randomInteger = (random.nextLong() % 9000000000L) + 1000000000L;
        transactionId = String.valueOf(randomInteger);
        Log.d("ID1", transactionId);
    }

    private void getUserData(){
        dbPembayaran.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NamaUser = snapshot.child("nama").getValue().toString();
                EmailUser = snapshot.child("email").getValue().toString();
                NoHp = snapshot.child("hp").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTagihan(){
        dbPembayaran.child("Tagihan").orderByChild("idPelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        NamaLayanan = dataSnapshot.child("nama").getValue().toString();
                        JenisPembayaran = dataSnapshot.child("jenis").getValue().toString();
                        HargaLayanan = dataSnapshot.child("harga").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Tanggal = simpleDateFormat.format(calendar.getTime());
    }
}