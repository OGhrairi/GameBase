package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {
    String[] myDataset = new String[20];

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        Toolbar childBar = (Toolbar) findViewById(R.id.browseToolbar);
        setSupportActionBar(childBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.browseRecycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        for(int i=0;i<20;i++){
            myDataset[i] = "This is Number " + (i+1);
        }
        adapter = new BrowseAdapter (myDataset);
        recyclerView.setAdapter(adapter);
    }



   /* public void listPopulator(ArrayList<String> res){
        res = globalResult;
        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, R.id.list_textview, res.toArray());
        //maps the listview, weatherlister, from the activity_main xml to a variable listView
        final ListView listView = (ListView) findViewById(R.id.weatherLister);
        //binds the adapter textview items to the listview
        listView.setAdapter(adapter);
        //onclick listener for the items in the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isTab == false) {
                    Intent intent = new Intent(MainActivity.this, WeatherInformationActivity.class);
                    String text = (String) listView.getItemAtPosition(position);
                    //add the selected string data to the intent to send along with it
                    intent.putExtra("weather", text);
                    startActivity(intent);
                }else{
                    String message = (String) listView.getItemAtPosition(position);
                    TextView textView = findViewById(R.id.weatherInfoText);
                    textView.setText(message);
                }
            }
        });
    }*/
}
