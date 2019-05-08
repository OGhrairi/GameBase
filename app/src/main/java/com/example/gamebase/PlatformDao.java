package com.example.gamebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlatformDao {

    @Query("SELECT * FROM platform_table")
    List<PlatformTable> getAllPlatforms();

    @Insert
    void insertPlatform(PlatformTable platform);

    @Insert
    void insertPlatforms(PlatformTable... platforms);

    @Delete
    void deletePlatform(PlatformTable platform);

    @Query("DELETE FROM platform_table")
    void deleteAllPlatforms();

    @Query("SELECT igdb_id FROM platform_table where platform_name = :platformName")
    int getPlatform(String platformName);
}
