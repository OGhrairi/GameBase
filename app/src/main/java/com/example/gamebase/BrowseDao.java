package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface BrowseDao {

    @Query("SELECT * FROM top50_table")
    List<BrowseResultsTable> getAllGames();

    @Insert
    void insertGame(BrowseResultsTable game);

    @Insert
    void insertGames(BrowseResultsTable... games);

    @Delete
    void deleteGame(BrowseResultsTable game);

    @Query("DELETE FROM top50_table")
    void deleteAllGames();

    @Query("SELECT igdb_id FROM top50_table where id = :idIn")
    int getGame(int idIn);
}
