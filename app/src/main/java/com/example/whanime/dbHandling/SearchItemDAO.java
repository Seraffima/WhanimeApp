// SearchItemDAO.java
package com.example.whanime.dbHandling;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.whanime.ui.search.SearchItem;
import java.util.List;

@Dao
public interface SearchItemDAO {
    @Insert
    void insert(SearchItem searchItem);

    @Update
    void update(SearchItem searchItem);

    @Delete
    void delete(SearchItem searchItem);

    @Query("SELECT * FROM search_items")
    LiveData<List<SearchItem>> getAllItems();
}