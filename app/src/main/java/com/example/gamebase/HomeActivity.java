package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

}
