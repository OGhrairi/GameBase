package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar bar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(bar);
    }

    public void goToSearch(View view){
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);

    }
    public void goToNewsFeed(View view){
        Intent intent = new Intent(HomeActivity.this, NewsFeedActivity.class);
        startActivity(intent);
    }
    public void goToBrowse(View view){
        Intent intent = new Intent(HomeActivity.this, BrowseActivity.class);
        startActivity(intent);
    }
    public void goToSettings(View view){
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    public void goToHelp(View view){
        Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
        intent.putExtra("url","file:///android_asset/helpPage.html");
        intent.putExtra("helpbool",true);
        startActivity(intent);
    }

}

