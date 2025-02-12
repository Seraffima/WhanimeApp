package com.example.whanime.dbHandling;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.whanime.ui.search.SearchItem;
import com.example.whanime.dbHandling.SearchItemDAO;

@Database(entities = {SearchItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SearchItemDAO searchItemDao();
}