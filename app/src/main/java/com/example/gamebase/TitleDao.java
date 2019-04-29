package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TitleDao {
    @Insert
    void insert(GameTitle title);

    @Query("DELETE FROM top50_table")
    void deleteAll();

    @Query("SELECT * from top50_table ORDER BY id ASC")
    List<GameTitle> getAllTitles();

}
