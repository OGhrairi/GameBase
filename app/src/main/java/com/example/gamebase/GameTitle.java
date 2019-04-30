package com.example.gamebase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "top50_table")
public class GameTitle {

    @PrimaryKey()
    private int id;

    @ColumnInfo (name = "game_title")
    @NonNull
    private String title;


    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
}
