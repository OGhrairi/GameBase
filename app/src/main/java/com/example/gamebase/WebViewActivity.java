package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {
    String url;
    WebView webView;
    boolean isHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        isHelp = intent.getBooleanExtra("helpbool",false);
        Toolbar childBar = (Toolbar) findViewById(R.id.webViewToolbar);
        setSupportActionBar(childBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        if(savedInstanceState==null){
            webView.loadUrl(url);
        }else{
            webView.restoreState(savedInstanceState);
        }
    }
    //inflate menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isHelp) {
            getMenuInflater().inflate(R.menu.menu_helpview, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_webview,menu);
        }
        return true;
    }
    @Override
    //menu button press handler
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.webViewRefresh:
                webView.loadUrl(url);
                break;
            case R.id.webViewExternalButton:
                externalBrowser();
                break;
        }
        return true;
    }

    public void externalBrowser(){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }
}
