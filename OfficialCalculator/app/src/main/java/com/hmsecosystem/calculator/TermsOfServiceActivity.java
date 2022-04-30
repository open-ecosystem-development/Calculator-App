package com.hmsecosystem.calculator;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsOfServiceActivity extends AppCompatActivity {
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_of_service);

        web = (WebView)findViewById(R.id.webView);
        web.loadUrl("file:///android_asset/terms_of_service.html");

    }

}