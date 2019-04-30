package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {

    @Query("SELECT * FROM top50_table")
    List<GameTitle> getAllGames();

    @Insert
    void insertGame(GameTitle game);

    @Insert
    void insertGames(GameTitle... games);

    @Delete
    void deleteGame(GameTitle game);

    @Query("DELETE FROM top50_table")
    void deleteAllGames();

}
