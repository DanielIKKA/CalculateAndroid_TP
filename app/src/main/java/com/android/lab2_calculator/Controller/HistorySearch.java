package com.android.lab2_calculator.Controller;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.lab2_calculator.R;

import androidx.appcompat.app.AppCompatActivity;

public class HistorySearch extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_search);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://google.com");

    }

}
