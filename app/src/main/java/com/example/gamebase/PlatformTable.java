package com.example.gamebase;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "platform_table")
public class PlatformTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "platform_name")
    @NonNull
    private String name;

    @ColumnInfo (name = "igdb_id")
    @NonNull
    private int igdbId;

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getIgdbId(){return this.igdbId;}
    public void setIgdbId(int id){this.igdbId = id;}
}
