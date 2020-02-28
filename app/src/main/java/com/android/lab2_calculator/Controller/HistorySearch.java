package com.android.lab2_calculator.Controller;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lab2_calculator.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistorySearch extends AppCompatActivity {

    private ArrayList<String> operations;
    private WebView webView;
    private LinearLayout messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_search);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://google.com");
        messageList = findViewById(R.id.message_List);

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.operations = getIntent().getStringArrayListExtra("operationsList");
        for (String op: this.operations) {
            addOperation(op);
        }
    }

    public TextView textView(String operation){
        if(null==operation || operation.trim().isEmpty()){
            operation = "empty operation";
        }
        TextView tv = new TextView(this);
        tv.setTextColor(255);
        tv.setPadding(2,5, 2, 2);
        return tv;
    }

    public void addOperation(final String operationToAdd){
        messageList.addView(textView(operationToAdd));
    }

}
