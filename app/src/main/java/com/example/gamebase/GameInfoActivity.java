package com.example.gamebase;

import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
    MenuItem shareButton;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gameinfo,menu);
        shareButton = menu.findItem(R.id.infoShareButton);


        return true;
    }

    @Override
    public void setIntent() {
        super.setIntent();
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareButton);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
        shareActionProvider.setShareIntent(shareIntent);

    }
}


