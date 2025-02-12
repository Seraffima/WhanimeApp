package com.example.whanime.ui.search;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "search_items")
public class SearchItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageUrl;
    private String mainTitle;
    private String subTitle;


    public SearchItem(String imageUrl, String mainTitle, String subTitle) {
        this.imageUrl = imageUrl;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
    }

    public int getId() {
        return id;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setId(int id) {
        this.id = id;
    }
}
