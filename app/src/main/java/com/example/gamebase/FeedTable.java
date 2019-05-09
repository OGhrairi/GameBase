package com.example.gamebase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "feed_table")
public class FeedTable {

    @PrimaryKey
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @ColumnInfo(name = "article_title")
    @NonNull
    private String title;
    @NonNull
    public String getTitle() { return title; }
    public void setTitle(@NonNull String title) { this.title = title; }

    @ColumnInfo(name = "article_url")
    @NonNull
    private String articleUrl;

    @NonNull
    public String getArticleUrl() { return articleUrl; }
    public void setArticleUrl(@NonNull String articleUrl) { this.articleUrl = articleUrl; }

    @ColumnInfo(name = "article_date")
    @NonNull
    private Date date;

    @NonNull
    public Date getDate() { return date; }
    public void setDate(@NonNull Date date) { this.date = date; }

    @ColumnInfo(name = "article_source")
    @NonNull
    private String sourceName;

    @NonNull
    public String getSourceName() { return sourceName; }
    public void setSourceName(@NonNull String sourceName) { this.sourceName = sourceName; }

    @ColumnInfo(name = "image_url")
    @NonNull
    private String imageUrl;

    @NonNull
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(@NonNull String imageUrl) { this.imageUrl = imageUrl; }
}
