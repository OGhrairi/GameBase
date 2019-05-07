package com.example.gamebase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BrowseResultsTable.class, SearchResultTable.class, PlatformTable.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase {

    private static GameDatabase INSTANCE;

    public abstract BrowseDao BrowseDao();

    public static GameDatabase getGameDatabase (Context context){
        if(INSTANCE == null){
            INSTANCE =

                    Room.databaseBuilder(context.getApplicationContext(),
                            GameDatabase.class,
                            "game_db")
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
