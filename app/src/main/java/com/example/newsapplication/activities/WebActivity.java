package com.example.newsapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.newsapplication.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        final String url = getIntent().getStringExtra("url");
        WebView webView = findViewById(R.id.activity_web_view);
        webView.loadUrl(url);
    }
}