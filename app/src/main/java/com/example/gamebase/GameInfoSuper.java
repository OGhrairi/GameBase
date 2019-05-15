package com.example.gamebase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class GameInfoSuper extends AppCompatActivity {
    //abstract class which encompasses common functionality when displaying game info either on
    //its own page, or alongside browse page
    public InfoCombo persist;
    public String shareText;
    //responsible for retrieving game information when a game is pressed on the recyclerview


    public class InfoCombo{
        private double rating;
        private int ratingCount;
        private String name;
        private String summary;
        private ArrayList<String> artworkUrls;

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public int getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(int ratingCount) {
            this.ratingCount = ratingCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public ArrayList<String> getArtworkUrls() {
            return artworkUrls;
        }

        public void setArtworkUrls(ArrayList<String> artworkUrls) {
            this.artworkUrls = artworkUrls;
        }
    }
    public class getter extends AsyncTask<Integer,Void,InfoCombo> {

        @Override
        protected InfoCombo doInBackground(Integer... ints) {

            int id = ints[0];
            int tableNum = ints[1];
            int igdbId=0;
            if(tableNum == 0){
                igdbId = GameDatabase.getGameDatabase(getApplicationContext()).BrowseDao().getGame(id);
            }else if(tableNum == 1){
                igdbId = GameDatabase.getGameDatabase(getApplicationContext()).SearchDao().getGame(id);
            }

            return GET(igdbId);
        }

        @Override
        protected void onPostExecute(InfoCombo infoCombos) {
            super.onPostExecute(infoCombos);
            InfoCombo results = infoCombos;
            System.out.println(results.getArtworkUrls().size());
            GameInfoFragment frag = new GameInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putDouble("rating",results.getRating());
            bundle.putInt("ratingCount",results.getRatingCount());
            bundle.putString("name",results.getName());
            bundle.putString("summary",results.getSummary());
            bundle.putStringArrayList("artworks",results.getArtworkUrls());
            frag.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.gameInfoFrame, frag);
            transaction.commit();
            persist = results;
            shareText=results.getName()+": "+results.getSummary();
            setIntent();
        }

        private InfoCombo GET(int id) {
            int igdbId = id;
            String API_KEY = "54b585e574a2d3fc29465538bc878c87";
            InputStream is = null;
            HttpURLConnection httpURLConnection = null;
            //assemble request, add url, and then headers, and then body text
            double rating=0;
            int ratingCount=0;
            String name="";
            String summary="";
            ArrayList<String> artworkUrls = new ArrayList<>();
            try {
                //url
                httpURLConnection = (HttpURLConnection) new URL("https://api-v3.igdb.com/games/").openConnection();
                //header fields
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("user-key", API_KEY);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //body text - string is converted to byte, passed to the outputStream
                // fields name, summary, aggregated_rating, aggregated_rating_count, release_dates.platform, release_dates.m, release_dates.y ,websites, artworks;where id=1020;
                String str = "fields name, summary, aggregated_rating, aggregated_rating_count; where id = " + igdbId + ";";
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
                        while (reader.hasNext()) {
                            switch (reader.nextName()) {
                                case "id":
                                    reader.skipValue();
                                    break;
                                case "aggregated_rating":
                                    rating = reader.nextDouble();
                                    break;
                                case "aggregated_rating_count":
                                    ratingCount = reader.nextInt();
                                    break;
                                case "name":
                                    name = reader.nextString();
                                    break;
                                case "summary":
                                    summary = reader.nextString();
                                default:
                                    break;
                            }
                        }
                        reader.endObject();
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
            try {
                //url
                httpURLConnection = (HttpURLConnection) new URL("https://api-v3.igdb.com/artworks/").openConnection();
                //header fields
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("user-key", API_KEY);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                //body text - string is converted to byte, passed to the outputStream
                // fields name, summary, aggregated_rating, aggregated_rating_count, release_dates.platform, release_dates.m, release_dates.y ,websites, artworks;where id=1020;
                String str = "fields url; where game = " + igdbId + ";";
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
                            reader.beginObject();
                            while (reader.hasNext()) {
                                switch (reader.nextName()) {
                                    case "id":
                                        reader.skipValue();
                                        break;
                                    case "url":
                                        String initialUrl = reader.nextString();
                                        String[] parts = initialUrl.split("/");
                                        String hash = parts[parts.length-1];
                                        String finalStr = "https://images.igdb.com/igdb/image/upload/t_screenshot_med/"+hash;
                                        artworkUrls.add(finalStr);
                                        break;
                                }
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
            InfoCombo infoCombo = new InfoCombo();
            infoCombo.setArtworkUrls(artworkUrls);
            infoCombo.setName(name);
            infoCombo.setRating(rating);
            infoCombo.setRatingCount(ratingCount);
            infoCombo.setSummary(summary);
            return infoCombo;
        }

    }
    public void setIntent(){

    }
}
