package com.example.gamebase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GameTitle.class}, version = 1)
public abstract class TitleRoomDatabase extends RoomDatabase {
    public abstract TitleDao titleDao();

    private static volatile TitleRoomDatabase INSTANCE;

    static TitleRoomDatabase getDatabase(final Context context){
        if( INSTANCE == null){
            synchronized (TitleRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TitleRoomDatabase.class, "gameinfo_database").build();
                }
            }
        }
        return INSTANCE;
    }

}


