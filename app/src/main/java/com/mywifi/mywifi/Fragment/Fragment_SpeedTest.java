package com.mywifi.mywifi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mywifi.mywifi.R;

public class Fragment_SpeedTest extends Fragment {

    View view;
    WebView speedTest_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.speed_test, container, false);

        speedTest_view = view.findViewById(R.id.speedTest_view);
        speedTest_view.setWebViewClient(new WebViewClient());
        speedTest_view.loadUrl("https://www.speedtest.net/");

        WebSettings webSettings = speedTest_view.getSettings();
        webSettings.setJavaScriptEnabled(true);


        return view;
    }
}
