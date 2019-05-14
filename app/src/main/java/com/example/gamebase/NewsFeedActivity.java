package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Toolbar childBar = (Toolbar) findViewById(R.id.feedToolbar);
        setSupportActionBar(childBar);
        //add return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.feedRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loader l = new loader();
        l.execute(false);
    }
    //inflate menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed,menu);
        return true;
    }
    @Override
    //menu button press handler
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.feedMenuRefresh) {
            loader l = new loader();
            if(findViewById(R.id.feedRecycler)!=null){
                RecyclerView v = findViewById(R.id.feedRecycler);
                v.setVisibility(View.INVISIBLE);
            }
            l.execute(true);
            return true;
        }else return super.onOptionsItemSelected(item);

    }
    public void webOpener(List<String> titles, List<String> imageUrls, List<String> bodies, List<String> Urls){
        adapter = new FeedAdapter(titles,imageUrls,bodies,Urls,this);
        ProgressBar bar = findViewById(R.id.feedProgressBar);
        bar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        RecyclerView v = findViewById(R.id.feedRecycler);
        v.setVisibility(View.VISIBLE);
    }
    public class loader extends AsyncTask<Boolean,Void,List<FeedTable>>{
        List<String> title = new ArrayList<>();
        List<String> articleUrl = new ArrayList<>();
        List<Integer> siteUrlId = new ArrayList<>();
        List<Integer> sourceId = new ArrayList<>();
        List<Long> date = new ArrayList<>();
        List<String> source = new ArrayList<>();
        List<String> imgUrl = new ArrayList<>();
        @Override
        protected List<FeedTable> doInBackground(Boolean... bools) {
            if(bools[0]) {
                GameDatabase.getGameDatabase(getApplicationContext()).FeedDao().deleteAllFeeds();
                publishProgress();
                GET();
            }
            return GameDatabase.getGameDatabase(getApplicationContext()).FeedDao().getAllFeeds();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            ProgressBar bar = findViewById(R.id.feedProgressBar);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<FeedTable> feedTables) {
            super.onPostExecute(feedTables);

            List<String> titles = new ArrayList<>();
            List<String> imageUrls = new ArrayList<>();
            List<String> bodies = new ArrayList<>();
            List<String> Urls = new ArrayList<>();
            List<FeedTable> articles = feedTables;
            for(int i=0; i<articles.size(); i++){
                titles.add(articles.get(i).getTitle());
                imageUrls.add(articles.get(i).getImageUrl());
                Urls.add(articles.get(i).getArticleUrl());
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd");
                    Date date = new Date(articles.get(i).getDate()*1000L);
                    Long millis = articles.get(i).getDate()*1000L;
                    String d = simpleDateFormat.format(date);
                    String rel = DateUtils.getRelativeTimeSpanString(millis,System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS).toString();
                    String bod = "From "+articles.get(i).getSourceName()+" - "+rel;
                    bodies.add(bod);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            webOpener(titles,imageUrls,bodies,Urls);

        }

        private void queryResponse(String url, String body, int type) {
            int conType = type;
            InputStream is = null;
            String API_KEY = "54b585e574a2d3fc29465538bc878c87";
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("user-key", API_KEY);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                byte[] outputBytes = body.getBytes();
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(outputBytes);
                os.close();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != httpURLConnection.HTTP_OK) {
                } else {
                    is = httpURLConnection.getInputStream();
                    if (is != null) {
                        JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        switch (conType){
                            case 0:
                                {reader.beginArray();
                                while (reader.hasNext()) {
                                    reader.beginObject();
                                    String img = "";
                                    Long publishDate = 0L;
                                    int srcId = 0;
                                    String aTitle = "";
                                    int siteId = 0;
                                    while (reader.hasNext()) {

                                        switch (reader.nextName()) {
                                            case "id":
                                                reader.skipValue();
                                                break;
                                            case "image":
                                                img = reader.nextString();
                                                break;
                                            case "published_at":
                                                publishDate = reader.nextLong();
                                                break;
                                            case "pulse_source":
                                                srcId = reader.nextInt();
                                                break;
                                            case "title":
                                                aTitle = reader.nextString();
                                                break;
                                            case "website":
                                                siteId = reader.nextInt();
                                                break;
                                            default:
                                                break;
                                        }

                                    }
                                    imgUrl.add(img);
                                    date.add(publishDate);
                                    sourceId.add(srcId);
                                    title.add(aTitle);
                                    siteUrlId.add(siteId);
                                    reader.endObject();
                                }
                                reader.endArray();}
                                break;
                            case 1:
                             {reader.beginArray();
                                reader.beginObject();
                                String artUrl="";
                                while(reader.hasNext()){
                                    switch (reader.nextName()){
                                        case "id":
                                            reader.skipValue();
                                            break;
                                        case "url":
                                            artUrl = reader.nextString();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                articleUrl.add(artUrl);
                                reader.endObject();
                                reader.endArray();
                             }
                                break;
                            case 2:
                                {reader.beginArray();
                                reader.beginObject();
                                String name="";
                                while(reader.hasNext()){

                                    switch (reader.nextName()){
                                        case "id":
                                            reader.skipValue();
                                            break;
                                        case "name":
                                            name = reader.nextString();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                source.add(name);
                                reader.endObject();
                                reader.endArray();
                                }
                                break;
                            default:
                            break;
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            } finally {
                //Disconnect
                httpURLConnection.disconnect();
            }
        }
        private void GET(){

            String search1 = "fields title, website, published_at, pulse_source, image;sort published_at desc;limit 20;";
            queryResponse("https://api-v3.igdb.com/pulses", search1,0);
            for(int i=0; i< title.size();i++) {
                String search2 = "fields url; where id=" + siteUrlId.get(i).toString()+";";
                String search3 = "fields name; where id=" + sourceId.get(i).toString()+";";
                queryResponse("https://api-v3.igdb.com/pulse_urls",search2,1);
                queryResponse("https://api-v3.igdb.com/pulse_sources",search3,2);
                FeedTable feed = new FeedTable();
                feed.setId(i+1);
                feed.setTitle(title.get(i));
                feed.setArticleUrl(articleUrl.get(i));
                feed.setDate(date.get(i));
                feed.setSourceName(source.get(i));
                feed.setImageUrl(imgUrl.get(i));
                GameDatabase.getGameDatabase(getApplicationContext()).FeedDao().insertFeed(feed);
            }
        }
    }

}
