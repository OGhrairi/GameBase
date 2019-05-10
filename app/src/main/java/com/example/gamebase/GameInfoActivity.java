package com.example.gamebase;

import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


public class GameInfoActivity extends GameInfoSuper {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        Toolbar bar = (Toolbar) findViewById(R.id.gameInfoToolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);
        int tableid = intent.getIntExtra("tableid",0);
        getter g = new getter();
        g.execute(message,tableid);
    }
    @Override
    //menu button press handler
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}


