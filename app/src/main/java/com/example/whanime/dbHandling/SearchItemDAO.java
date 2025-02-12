package com.example.whanime.dbHandling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whanime.ui.search.SearchItem;

import java.util.List;

@Dao
public interface SearchItemDAO {
    @Insert
    void insert(SearchItem item);

    @Update
    void update(SearchItem item);

    @Delete
    void delete(SearchItem item);

    @Query("SELECT * FROM search_items")
    List<SearchItem> getAllItems();
}
