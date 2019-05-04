package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar childBar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(childBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void filterExpand(View view){
        FilterFragment frag = new FilterFragment();
        frag.show(getSupportFragmentManager(),"missiles");


    }
}
