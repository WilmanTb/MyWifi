package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mywifi.mywifi.Adapter.Adapter_Pembayaran;
import com.mywifi.mywifi.Model.Model_Pembayaran;
import com.mywifi.mywifi.R;

import java.util.ArrayList;

public class Activity_Pembayaran_User extends AppCompatActivity {

    private RecyclerView rc_pembayaran;
    private Adapter_Pembayaran myAdapter;
    private ArrayList<Model_Pembayaran> modelPembayaran;
    private DatabaseReference dbPembayaran;
    private ImageView arrow_back;
    private String UID;
    private Button btn_bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_user);

        initComponents();

        getData();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Pembayaran_User.this, Dashboard_User_Activity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Pembayaran_User.this, Dashboard_User_Activity.class));
        finish();
    }

    private void initComponents() {
        arrow_back = findViewById(R.id.btn_back);
        rc_pembayaran = findViewById(R.id.rc_pembayaran);
        rc_pembayaran.setHasFixedSize(true);
        rc_pembayaran.setLayoutManager(new LinearLayoutManager(this));
        modelPembayaran = new ArrayList<>();
        myAdapter = new Adapter_Pembayaran(this, modelPembayaran);
        rc_pembayaran.setAdapter(myAdapter);
        dbPembayaran = FirebaseDatabase.getInstance("https://my-wifi-626bb-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Tagihan");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UID = firebaseUser.getUid();
    }

//    private void makePayment() {
////        SdkUIFlowBuilder.init()
////                .setContext(this)
////                .setMerchantBaseUrl("https://my-wifi-626bb.web.app/store/public/response.php/")
////                .setClientKey("SB-Mid-client-Q-FGg6jCULeq1jXG")
////                .setTransactionFinishedCallback(this)
////                .enableLog(true)
////                .buildSDK();
//        SdkUIFlowBuilder.init()
//                .setClientKey("SB-Mid-client-Q-FGg6jCULeq1jXG") // client_key is mandatory
//                .setContext(this) // context is mandatory
//                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
//                    @Override
//                    public void onTransactionFinished(TransactionResult result) {
//                        if (result.getResponse() != null) {
//                            switch (result.getStatus()) {
//                                case TransactionResult.STATUS_SUCCESS:
//                                    Toast.makeText(Activity_Pembayaran_User.this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
//                                    break;
//                                case TransactionResult.STATUS_PENDING:
//                                    Toast.makeText(Activity_Pembayaran_User.this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
//                                    break;
//                                case TransactionResult.STATUS_FAILED:
//                                    Toast.makeText(Activity_Pembayaran_User.this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
//                                    break;
//                            }
//                            result.getResponse().getStatusMessage();
//                        } else if (result.isTransactionCanceled()) {
//                            Toast.makeText(Activity_Pembayaran_User.this, "Transaction Canceled", Toast.LENGTH_LONG).show();
//                        } else {
//                            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
//                                Toast.makeText(Activity_Pembayaran_User.this, "Transaction Invalid", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(Activity_Pembayaran_User.this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                }) // set transaction finish callback (sdk callback)
//                .setMerchantBaseUrl("https://jenswifi.000webhostapp.com/response.php/") //set merchant url (required)
//                .enableLog(true) // enable sdk log (optional)
//                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
//                .setLanguage("en") //`en` for English and `id` for Bahasa
//                .buildSDK();
//
//        TransactionRequest transactionRequest = new TransactionRequest("144521414", 200000.0);
//        CustomerDetails customerDetails = new CustomerDetails();
//        customerDetails.setCustomerIdentifier("budi-6789");
//        customerDetails.setPhone("08123456789");
//        customerDetails.setFirstName("Budi");
//        customerDetails.setLastName("Utomo");
//        customerDetails.setEmail("budi@utomo.com");
//
//        ShippingAddress shippingAddress = new ShippingAddress();
//        shippingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
//        shippingAddress.setCity("Jakarta");
//        shippingAddress.setPostalCode("10220");
//        customerDetails.setShippingAddress(shippingAddress);
//
//        BillingAddress billingAddress = new BillingAddress();
//        billingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
//        billingAddress.setCity("Jakarta");
//        billingAddress.setPostalCode("10220");
//        customerDetails.setBillingAddress(billingAddress);
//
//        transactionRequest.setCustomerDetails(customerDetails);
//
//        ItemDetails itemDetails1 = new ItemDetails("434355464", 200000.0, 1, "test");
//        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
//        itemDetailsList.add(itemDetails1);
//
//// Set item details into the transaction request.
//        transactionRequest.setItemDetails(itemDetailsList);
//        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
//
//    }

    private void getData() {
        dbPembayaran.orderByChild("idPelanggan").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    modelPembayaran.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("status").getValue().toString().equals("belum dibayar")) {
                            Model_Pembayaran model_pembayaran = dataSnapshot.getValue(Model_Pembayaran.class);
                            modelPembayaran.add(model_pembayaran);
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}


