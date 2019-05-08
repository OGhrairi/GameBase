package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT * FROM genre_table")
    List<GenreTable> getAllGenres();

    @Insert
    void insertGenre(GenreTable genre);

    @Insert
    void insertGenres(GenreTable... genres);

    @Delete
    void deleteGenre(GenreTable genre);

    @Query("DELETE FROM genre_table")
    void deleteAllGenres();

}
