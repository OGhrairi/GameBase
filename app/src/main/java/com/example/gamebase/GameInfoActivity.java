package com.example.gamebase;

import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;


public class GameInfoActivity extends GameInfoSuper {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        Toolbar bar = (Toolbar) findViewById(R.id.gameInfoToolbar);
        setSupportActionBar(bar);
        Intent intent = getIntent();
        int message = intent.getIntExtra("id", 0);
        getter g = new getter();
        g.execute(message);
    }
}


