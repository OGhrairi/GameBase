package com.example.gamebase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "top50_table")
public class GameTitle {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "Title")
    private String title;
    public GameTitle(@NonNull String gameTitle){
        this.title=gameTitle;
    }
    public String getTitle(){
        return this.title;
    }
}
