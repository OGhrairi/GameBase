package com.example.gamebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends GameInfoSuper implements BrowseAdapter.OnBrowseListener {
    @Override
    public void onBrowseClick(int position) {
            Intent intent = new Intent(this, GameInfoActivity.class);
            intent.putExtra("id", position + 1);
            intent.putExtra("tableid",1);
            startActivity(intent);
    }

    public class gameCombo {
        private int id;
        private String name;

        public gameCombo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    public String platformFilter="";

    public void setPlatformFilter(String platformFilter) {
        this.platformFilter = platformFilter;
    }

    public String genreFilter="";

    public void setGenreFilter(String genreFilter) {
        this.genreFilter = genreFilter;
    }

    public int y1Filter=0;

    public void setY1Filter(int y1Filter) {
        this.y1Filter = y1Filter;
    }
    public int y2Filter=0;

    public void setY2Filter(int y2Filter) {
        this.y2Filter = y2Filter;
    }
    public void filterUpdate(){
        TextView t = findViewById(R.id.filterList);
        if(platformFilter != "" | genreFilter!= "" | y1Filter!=0 | y2Filter != 0){
            String filterList="";
            if(platformFilter !=""){
                filterList+= "Platform: "+platformFilter+ " ";
            }
            if(genreFilter !=""){
                filterList+= "Genre: "+genreFilter+" ";
            }
            if(y1Filter !=0){
                filterList+= "From "+y1Filter +" ";
            }
            if (y2Filter != 0) {
                filterList+= "To "+y2Filter +" ";
            }

            t.setText(filterList);
        }else{
            t.setText("No Filters");
        }

    }

    private boolean isFilterLoaded=false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean istab;
    private String[] persist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_search);
        Toolbar childBar = (Toolbar) findViewById(R.id.searchToolbar);
        setSupportActionBar(childBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.searchResultsRecycle);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (findViewById(R.id.tabletSearchResultsFrame) == null) {
            istab = false;
        } else {
            istab = true;

            FilterTabFragment frag = new FilterTabFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.tabletFilterFrame, frag);
            transaction.commit();

        }
        filterGet f = new filterGet();
        f.execute();
    }

    public void filterExpand(View view) {
        filterUpdate();
        FilterFragment frag = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("platform",platformFilter);
        bundle.putString("genre",genreFilter);
        bundle.putInt("y1",y1Filter);
        bundle.putInt("y2",y2Filter);
        frag.setArguments(bundle);
        if(isFilterLoaded) {
            frag.show(getSupportFragmentManager(), "filterFrag");
        }else{
            while(!isFilterLoaded){

            }
            frag.show(getSupportFragmentManager(),"filterFrag");
        }
    }

    public void searchPress(View view) {

        EditText entry = (EditText) findViewById(R.id.searchStringEntry);
        String searchStr = entry.getText().toString();
        if (searchStr == null) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        } else {
            getter g = new getter();
            g.execute(searchStr);
        }
    }
    public void SearchReset(View view){
        platformFilter="";
        genreFilter="";
        y2Filter=0;
        y1Filter=0;
        filterUpdate();
    }

    public void populator(String[] data) {
        adapter = new BrowseAdapter(data, this);
        recyclerView.setAdapter(adapter);
        persist=data;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("resultsChannel","channel",importance);
            channel.setDescription("channel");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public class getter extends AsyncTask<String, Void, List<gameCombo>> {

        @Override
        protected List<gameCombo> doInBackground(String... str) {
            String searchStr = str[0];
            List<gameCombo> gameList = GET(searchStr);
            GameDatabase.getGameDatabase(getApplicationContext()).SearchDao().deleteAllGames();
            int len = gameList.size();
            for(int i=0; i<len; i++){
                SearchResultTable tab = new SearchResultTable();
                tab.setId(i+1);
                tab.setTitle(gameList.get(i).getName());
                tab.setIgdbId(gameList.get(i).getId());
                GameDatabase.getGameDatabase(getApplicationContext()).SearchDao().insertGame(tab);
            }
            return gameList;
        }

        @Override
        protected void onPostExecute(List<gameCombo> gameCombos) {
            super.onPostExecute(gameCombos);
            int length = gameCombos.size();
            String[] myDataset = new String[length];
            for (int i = 0; i < (length); i++) {
                myDataset[i] = gameCombos.get(i).getName();
            }
            populator(myDataset);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"resultsChannel")
                    .setContentTitle("Search Results")
                    .setContentText("Search results are ready to view")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
            managerCompat.notify(0,builder.build());

        }

        private List<gameCombo> GET(String input) {

            String inputStr = input;
            List<gameCombo> results = new ArrayList<>();
            String API_KEY = "54b585e574a2d3fc29465538bc878c87";
            InputStream is = null;
            HttpURLConnection httpURLConnection = null;
            //assemble request, add url, and then headers, and then body text
            try {
                //url
                httpURLConnection = (HttpURLConnection) new URL("https://api-v3.igdb.com/games/").openConnection();
                //header fields
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("user-key", API_KEY);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //body text - string is converted to byte, passed to the outputStream
                String str = "search \"" + inputStr + "\";where version_parent = null & category = 0";
                if(platformFilter!=""){
                    int platformID = GameDatabase.getGameDatabase(getApplicationContext()).PlatformDao().getPlatform(platformFilter);
                    str+=" & release_dates.platform = "+platformID;
                }
                if(genreFilter!="") {
                    int genreID = GameDatabase.getGameDatabase(getApplicationContext()).GenreDao().getGenre(genreFilter);
                    str+=" & genres = "+genreID;
                }
                if(y1Filter!=0){
                    str+= " & release_dates.y>="+y1Filter;
                }
                if(y2Filter!=0){
                    str+=" & release_dates.y<="+y2Filter;
                }

                str += ";fields name;limit 50;";
                byte[] outputBytes = str.getBytes();
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(outputBytes);
                os.close();
                httpURLConnection.connect();
                //Read InputStream
                //responseCode is the HTTP status code sent back from the server
                int responseCode = httpURLConnection.getResponseCode();
                //only try to interpret input stream if response code is ok (200)
                if (responseCode != HttpURLConnection.HTTP_OK) {

                } else {
                    is = httpURLConnection.getInputStream();
                    if (is != null) {
                        //if the input stream returns a valid result, send result to conversion
                        //method below
                        JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            int id = 0;
                            String name = "";
                            while (reader.hasNext()) {

                                switch (reader.nextName()) {
                                    case "id":
                                        id = reader.nextInt();
                                        break;
                                    case "name":
                                        name = reader.nextString();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            gameCombo c = new gameCombo(id, name);
                            results.add(c);

                            reader.endObject();
                        }
                        reader.endArray();
                        is.close();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Disconnect
                httpURLConnection.disconnect();
            }
            return results;
        }
    }


    //this is an asyncTask which is reponsible for retrieving lists of all platforms and genres from the database
    //to be used in the autocompletetext fields in the filter. Note that this retrieval only occurs when
    //the respective local tables are empty, i.e. only happens on first run of the app.
    public class filterGet extends AsyncTask<Void,Void,Void>{
        List<gameCombo> platforms = new ArrayList<>();
        List<gameCombo> genres = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            if(GameDatabase.getGameDatabase(getApplicationContext()).PlatformDao().getAllPlatforms().size()==0){
                getter(0,0);
                getter(50,0);
                getter(100,0);
                getter(150,0);
            }
            if(GameDatabase.getGameDatabase(getApplicationContext()).GenreDao().getAllGenres().size()==0){
                getter(0,1);

            }
            for(int i =0; i< platforms.size(); i++){
                PlatformTable plat = new PlatformTable();
                plat.setName(platforms.get(i).getName());
                plat.setIgdbId(platforms.get(i).getId());
                plat.setId(i+1);
                GameDatabase.getGameDatabase(getApplicationContext()).PlatformDao().insertPlatform(plat);
            }
            for(int j=0; j<genres.size(); j++){
                GenreTable tab = new GenreTable();
                tab.setName(genres.get(j).getName());
                tab.setIgdbId(genres.get(j).getId());
                tab.setId(j+1);
                GameDatabase.getGameDatabase(getApplicationContext()).GenreDao().insertGenre(tab);
            }
            isFilterLoaded=true;
            return null;
        }


        private void getter(int offset, int type) {
            int n = offset;
            int t = type;
            InputStream is = null;
            String API_KEY = "54b585e574a2d3fc29465538bc878c87";
            HttpURLConnection httpURLConnection = null;

            //assemble request, add url, and then headers, and then body text
            try {
                URL u;
                //url
                if (t == 0) {
                    u = new URL("https://api-v3.igdb.com/platforms");
                }else{
                    u = new URL ("https://api-v3.igdb.com/genres");
                }
                httpURLConnection = (HttpURLConnection) u.openConnection();
                //header fields
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("user-key", API_KEY);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //body text - string is converted to byte, passed to the outputStream
                String str = "fields name;limit 50;offset "+n+";";
                byte[] outputBytes = str.getBytes();
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(outputBytes);
                os.close();
                httpURLConnection.connect();
                //Read InputStream
                //responseCode is the HTTP status code sent back from the server
                int responseCode = httpURLConnection.getResponseCode();
                //only try to interpret input stream if response code is ok (200)
                if (responseCode != HttpURLConnection.HTTP_OK) {

                } else {
                    is = httpURLConnection.getInputStream();
                    if (is != null) {
                        //if the input stream returns a valid result, send result to conversion
                        //method below
                        JsonReader reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            int id = 0;
                            String name = "";
                            while (reader.hasNext()) {

                                switch (reader.nextName()) {
                                    case "id":
                                        id = reader.nextInt();
                                        break;
                                    case "name":
                                        name = reader.nextString();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            gameCombo c = new gameCombo(id, name);
                            if(t==0){
                                platforms.add(c);
                            }else {
                                genres.add(c);
                            }


                            reader.endObject();
                        }
                        reader.endArray();
                        is.close();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Disconnect
                httpURLConnection.disconnect();
            }
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        onSaveInstanceState(new Bundle());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (persist == null) {
        } else {
            outState.putStringArray("persist", persist);
        }
        if(platformFilter!="")outState.putString("platformFilter",platformFilter);
        if(genreFilter!="")outState.putString("genreFilter",genreFilter);
        if(y1Filter!=0)outState.putInt("y1",y1Filter);
        if(y2Filter!=0)outState.putInt("y2",y2Filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onRestoreInstanceState(new Bundle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        persist = savedInstanceState.getStringArray("persist");
        if (persist != null) {
            populator(persist);
        }
        if(savedInstanceState.getString("platformFilter")!=null){
            platformFilter=savedInstanceState.getString("platformFilter");
        }
        if(savedInstanceState.getString("genreFilter")!=null) {
            genreFilter = savedInstanceState.getString("genreFilter");
        }
        if(savedInstanceState.getInt("y1")!=0) {
            y1Filter = savedInstanceState.getInt("y1");
        }
        if(savedInstanceState.getInt("y20")!=0) {
            y2Filter = savedInstanceState.getInt("y2");
        }
        filterUpdate();
    }
}
