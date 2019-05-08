package com.example.gamebase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
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
        //check if using tablet, if so, fragment is inflated on this page, otherwise navigate to
        //game info dedicated page
        //position starts at 0
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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean istab;
    private String[] persist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        FilterFragment frag = new FilterFragment();
        frag.show(getSupportFragmentManager(), "missiles");
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

    public void populator(String[] data) {
        adapter = new BrowseAdapter(data, this);
        recyclerView.setAdapter(adapter);
        persist=data;
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            System.out.println("network");
        }

        @Override
        protected void onPostExecute(List<gameCombo> gameCombos) {
            super.onPostExecute(gameCombos);
            int length = gameCombos.size();
            System.out.println(length);
            String[] myDataset = new String[length];
            for (int i = 0; i < (length); i++) {
                myDataset[i] = gameCombos.get(i).getName() + " id: " + gameCombos.get(i).getId();
                System.out.println(myDataset[i]);
            }
            populator(myDataset);

        }

        private List<gameCombo> GET(String input) {
            publishProgress();
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
                String str = "search \"" + inputStr + "\";where version_parent = null & category = 0;fields name;limit 50;";
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


    public class filterGet extends AsyncTask<Void,List<gameCombo>,Void>{
        List<gameCombo> platforms = new ArrayList<>();
        List<gameCombo> genres = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            GameDatabase.getGameDatabase(getApplicationContext()).PlatformDao().deleteAllPlatforms();
            GameDatabase.getGameDatabase(getApplicationContext()).GenreDao().deleteAllGenres();
            getter(0,0);
            getter(50,0);
            getter(100,0);
            getter(150,0);
            getter(0,1);

            publishProgress(platforms,genres);
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
            return null;
        }

        @Override
        protected void onProgressUpdate(List<gameCombo>... games) {
            super.onProgressUpdate(games);
            List<gameCombo> game = games[0];
            for(int i=0;i<game.size();i++){
                System.out.println(game.get(i).getName());
            }
            game=games[1];
            for(int j=0;j<game.size();j++){
                System.out.println(game.get(j).getName());
            }
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
    }
}
