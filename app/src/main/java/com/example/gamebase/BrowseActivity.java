package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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


public class BrowseActivity extends GameInfoSuper implements BrowseAdapter.OnBrowseListener{

    @Override
    public void onBrowseClick(int position) {
        //check if using tablet, if so, fragment is inflated on this page, otherwise navigate to
        //game info dedicated page
        //position starts at 0
        if(isTab == false) {
            Intent intent = new Intent(this, GameInfoActivity.class);
            intent.putExtra("id", position + 1);
            startActivity(intent);
        }else{
            getter g = new getter();
            g.execute(position+1);
        }
    }
    public void pageSwitcher(String[] data){
        adapter = new BrowseAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }

    //custom datatype used in storing data from the api to the local database
    public class gameCombo{
        private int id;
        private String name;
        public gameCombo(int id, String name){
            this.id=id;
            this.name=name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    ArrayList<String> list = new ArrayList<>();
    //define the variables to create the recyclerview
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        //generate custom toolbar
        Toolbar childBar = (Toolbar) findViewById(R.id.browseToolbar);
        setSupportActionBar(childBar);
        //add return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if( findViewById(R.id.gameInfoFrame) == null){
            isTab=false;
        }else{
            /*However, if the fragment does exist, that means the app is open on a tablet, and so
            we want to fill the fragment placeholder with the gameInfoFrame.
            1:create an instance of the fragment
            2:begin a fragment 'transaction' to replace the placeholder with the instantiated fragment
            */
            isTab=true;
            GameInfoFragment frag = new GameInfoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.gameInfoFrame,frag);
            transaction.commit();
        }
        //bind layout manager to recycler in xml
        recyclerView = (RecyclerView) findViewById(R.id.browseRecycleView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //run async task with arg 1, which just retrieves the list from the room db and displays on
        //recycler view
        AsyncGetter a = new AsyncGetter(this);
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
        AsyncGetter a = new AsyncGetter(this);
        if(id == R.id.browseMenuRefresh){

            a.execute(0);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }


    //Async class which handles all interaction with the database and the api server
    public class AsyncGetter extends AsyncTask<Integer,Integer,List<GameTitle>>{
        private Context mContext;
        public AsyncGetter(Context context){
            mContext=context;
        }
        @Override
        protected List<GameTitle> doInBackground(Integer... inputArg) {
            //if input 0, clears table and repopulates with fresh data from server
            if (inputArg[0].equals(0)) {
                //let user know it is refreshing data
                publishProgress(0);
                //retrieve fresh list by calling the GET() function, which handles http stuff
                List<gameCombo> titleList = GET();
                int length = titleList.size();
                //clear table
                GameDatabase.getGameDatabase(getApplicationContext()).gameDao().deleteAllGames();
                //repopulate table
                for (int i = 0; i < length; i++) {
                    GameTitle title = new GameTitle();
                    title.setTitle(titleList.get(i).getName());
                    title.setId(i + 1);
                    title.setIgdbId(titleList.get(i).getId());
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
                myDataset[i] = titles.get(i).getTitle() + " id: " + titles.get(i).getIgdbId();
            }
            pageSwitcher(myDataset);

        }
        //method which handles all web stuff on this activity
        //no input args, and returns an array of top 50 game titles
        private List<gameCombo> GET() {
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
                String str = "fields name, id; sort popularity desc; limit 50;";
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
        //interprets the objects within the json array, returns gameCombo, custom datatype which
        //houses a string and an int, in this case for each game's name and IGDB id number
        public gameCombo readMessage (JsonReader reader) throws IOException{
            String name="";
            int id=0;
            reader.beginObject();
            //if it's the game name, save it. if it's anything else, ignore
            while(reader.hasNext()){
                switch(reader.nextName()){
                    case "id":
                       id=reader.nextInt();
                        break;
                    case "name":
                        name=reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            gameCombo combo = new gameCombo(id,name);
            return combo;
        }

    }

    //onStop/onStart handlers for maintaining the listview results on screen rotation
    @Override
    protected void onStop() {
        super.onStop();
        onSaveInstanceState(new Bundle());
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (persist == null) {
        }else{
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
        persist=savedInstanceState.getStringArray("persist");
        if(persist != null) {
            TextView titleView = findViewById(R.id.gameInfoTitle);
            titleView.setText(persist[0]);
            TextView descView = findViewById(R.id.gameInfoDesc);
            descView.setText(persist[1]);
        }
    }
}
