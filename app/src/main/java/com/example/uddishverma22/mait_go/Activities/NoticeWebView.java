package com.example.uddishverma22.mait_go.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.uddishverma22.mait_go.R;

public class NoticeWebView extends AppCompatActivity {

    WebView webView;
    Intent i;
    String pdf = null;

    public static final String TAG = "NoticeWebView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_web_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        i = getIntent();
        Log.d(TAG, "onCreate: " + i.getStringExtra("url").substring(0, 1));

        if(i.getStringExtra("url").substring(0, 1).equals("h"))    {
            pdf = i.getStringExtra("url");
            Log.d(TAG, i.getStringExtra("url"));
        }
        else {
            pdf = "http://www.ipu.ac.in" + i.getStringExtra("url");
            Log.d(TAG, "http://www.ipu.ac.in" + i.getStringExtra("url"));
        }
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
    }
}
