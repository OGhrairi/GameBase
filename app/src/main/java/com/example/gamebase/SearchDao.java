package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SearchDao {

    @Query("SELECT * FROM search_results_table")
    List<SearchResultTable> getAllGames();

    @Insert
    void insertGame(SearchResultTable game);

    @Insert
    void insertGames(SearchResultTable... games);

    @Delete
    void deleteGame(SearchResultTable game);

    @Query("DELETE FROM search_results_table")
    void deleteAllGames();

    @Query("SELECT igdb_id FROM search_results_table where id = :idIn")
    int getGame(int idIn);
}
