package com.mywifi.mywifi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test extends AppCompatActivity {

    private Button btn_test;
    private TextView txt_response;
    private String Hasil ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn_test = findViewById(R.id.btn_test);
        txt_response = findViewById(R.id.txt_response);


        String url = "https://api.sandbox.midtrans.com/v2/2971522c-4c8f-4e09-8caa-e29defac102b/status";



        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performGetRequest(url);
                Toast.makeText(Test.this, Hasil, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void performGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Basic U0ItTWlkLXNlcnZlci1tRTVJZU9LZnh4RjNmLW5zOWpaMjYtbjQ6")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        final String statusCode = jsonObject.getString("status_code");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI with the response data
                                // For example, you can set the response text in a TextView
                                txt_response.setText(statusCode);
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    // Handle the successful response

                } else {
                    // Handle the error response
                    // Note: Make sure to close the response body by calling response.body().close()

                }
                response.body().close();
            }
        });
    }


}