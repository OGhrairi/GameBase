package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar childBar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(childBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GameInfoFragment frag = new GameInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //TODO: create a new fragment/use existing fragment for showing results in the recyclerview used before
        //transaction.replace(R.id.searchResultsFrame,frag);
        transaction.commit();
    }
    public void filterExpand(View view){
        FilterFragment frag = new FilterFragment();
        frag.show(getSupportFragmentManager(),"missiles");


    }
}
