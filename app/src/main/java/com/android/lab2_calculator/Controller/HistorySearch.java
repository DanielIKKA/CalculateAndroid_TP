package com.android.lab2_calculator.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lab2_calculator.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class HistorySearch extends AppCompatActivity {

    private ArrayList<String> operations;
    private WebView webView;
    private LinearLayout messageList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_search);

        webView = findViewById(R.id.webView);
        messageList = findViewById(R.id.message_List);
        searchBar = findViewById(R.id.search_bar);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://google.com");

        Button btn = findViewById(R.id.returnBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(prepareForSegue());
            }
        });

        Button goBtn = findViewById(R.id.search_go_btn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(searchBar.getText().toString());
            }
        });
    }

    private Intent prepareForSegue() {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("operationsList", this.operations);

        return intent;
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
        tv.setText(operation);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(0,10, 0, 10);
        return tv;
    }

    public void addOperation(final String operationToAdd){
        messageList.addView(textView(operationToAdd));
    }

}
