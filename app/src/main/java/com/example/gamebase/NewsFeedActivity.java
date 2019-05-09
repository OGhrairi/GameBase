package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {


    private List<String> mTitles = new ArrayList<>();
    private List<String> mBodies = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar childBar = (Toolbar) findViewById(R.id.feedToolbar);
        setSupportActionBar(childBar);
        //add return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //inflate menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed,menu);
        return true;
    }
    //@Override
    //menu button press handler
   /* public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        BrowseActivity.AsyncGetter a = new BrowseActivity.AsyncGetter(this);
        if(id == R.id.browseMenuRefresh){

            a.execute(0);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }*/
}
