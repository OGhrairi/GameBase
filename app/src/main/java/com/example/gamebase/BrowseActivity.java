package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class BrowseActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    //define the variables to create the recyclerview
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        //generate custom toolbar
        Toolbar childBar = (Toolbar) findViewById(R.id.browseToolbar);
        setSupportActionBar(childBar);
        //add return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //bind layout manager to recycler in xml
        recyclerView = (RecyclerView) findViewById(R.id.browseRecycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //run async task with arg 1, which just retrieves the list from the room db and displays on
        //recycler view
        AsyncGetter a = new AsyncGetter();
        a.execute(1);

    }
    //inflate menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse,menu);
        return true;
    }
    @Override
    //menu button press handler
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        AsyncGetter a = new AsyncGetter();
        if(id == R.id.browseMenuRefresh){

            a.execute(0);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }


    //Async class which handles all interaction with the database and the api server
    public class AsyncGetter extends AsyncTask<Integer,Integer,List<GameTitle>>{

        @Override
        protected List<GameTitle> doInBackground(Integer... inputArg) {
            //if input 0, clears table and repopulates with fresh data from server
            if (inputArg[0].equals(0)) {
                //let user know it is refreshing data
                publishProgress(0);
                //retrieve fresh list by calling the GET() function, which handles http stuff
                List<String> titleList = GET();
                int length = titleList.size();
                //clear table
                GameDatabase.getGameDatabase(getApplicationContext()).gameDao().deleteAllGames();
                //repopulate table
                for (int i = 0; i < length; i++) {
                    GameTitle title = new GameTitle();
                    title.setTitle(titleList.get(i));
                    title.setId(i + 1);
                    GameDatabase.getGameDatabase(getApplicationContext()).gameDao().insertGame(title);
                }
                //publishProgress(1);
            }
            return GameDatabase.getGameDatabase(getApplicationContext()).gameDao().getAllGames();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0].equals(0)) {
                Toast.makeText(BrowseActivity.this, "Fetching Data", Toast.LENGTH_SHORT).show();
            }
            if(values[0].equals(1)){
                Toast.makeText(BrowseActivity.this, "Data Retrieved", Toast.LENGTH_SHORT).show();
            }
        }

        //onPostExecute is responsible for taking table data and binding it to the recyclerview
        @Override
        protected void onPostExecute(List<GameTitle> titles) {
            super.onPostExecute(titles);
            int length = titles.size();
            String[] myDataset = new String[length];
            for (int i = 0; i < (length); i++) {
                myDataset[i] = titles.get(i).getTitle() + " id: " + titles.get(i).getId();
            }
            adapter = new BrowseAdapter(myDataset);
            recyclerView.setAdapter(adapter);
        }
        //method which handles all web stuff on this activity
        //no input args, and returns an array of top 50 game titles
        private List<String> GET() {
            List<String> results = new ArrayList<>();
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
                String str = "fields name; sort total_rating desc; where total_rating != null; limit 50;";
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
                        while(reader.hasNext()) {
                            //iterate through top level of the object, which is an array
                            //in each entry, sent the object inside to the readMessage method to interpret
                            results.add(readMessage(reader));
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
        //interprets the objects within the json array
        public String readMessage (JsonReader reader) throws IOException{
            String name="";
            reader.beginObject();
            //if it's the game name, save it. if it's anything else, ignore
            while(reader.hasNext()){
                String n = reader.nextName();
                if(n.equals("name")){
                    name = reader.nextString();
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return name;
        }


    }

}
