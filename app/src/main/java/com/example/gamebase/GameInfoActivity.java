package com.example.gamebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameInfoActivity extends AppCompatActivity {
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

    public class getter extends AsyncTask<Integer,Void,String[]>{

        @Override
        protected String[] doInBackground(Integer... ints) {
            String[] out;
            int id = ints[0];
            int igdbId = GameDatabase.getGameDatabase(getApplicationContext()).gameDao().getGame(id);
            out = GET(igdbId);
            return out;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            String title = strings[0];
            String desc = strings[1];
            GameInfoFragment frag = new GameInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            bundle.putString("desc",desc);
            frag.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.gameInfoFrame, frag);
            transaction.commit();
        }

        private String[] GET(int id) {
            int igdbId = id;
            String[] results = new String[2];
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
                String str = "fields name, summary; where id = " + igdbId + ";";
                System.out.println(str);
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
                        reader.beginObject();
                        while(reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "id":
                                    reader.skipValue();
                                    break;
                                case "name":
                                    results[0] = reader.nextString();
                                    break;
                                case "summary":
                                    results[1] = reader.nextString();
                                default:
                                    break;
                            }
                        }
                        reader.endObject();
                        reader.endArray();
                        is.close();

                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Disconnect
                httpURLConnection.disconnect();
            }
            return results;
        }

    }
}


