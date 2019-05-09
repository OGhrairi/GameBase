package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FeedDao {

    @Query("SELECT * FROM feed_table")
    List<FeedTable> getAllFeeds();

    @Insert
    void insertFeed(FeedTable feed);

    @Query("DELETE FROM feed_table")
    void deleteAllFeeds();

    @Query("SELECT * FROM feed_table WHERE id=:articleId")
    FeedTable getFeed(int articleId);
}
