package com.hmsecosystem.calculator;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        web = (WebView)findViewById(R.id.webView);
        web.loadUrl("file:///android_asset/privacy_policy.html");

    }

}