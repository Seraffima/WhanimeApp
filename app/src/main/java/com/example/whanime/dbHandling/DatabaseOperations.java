package com.example.whanime.dbHandling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.whanime.ui.search.SearchItem;

public class DatabaseOperations {

    private AppDatabase database;
    private SearchItemDAO searchItemDAO;
    private ExecutorService executorService;

    public DatabaseOperations(AppDatabase database) {
        this.database = database;
        this.searchItemDAO = database.searchItemDAO();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void insertSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.insert(item));
    }

    public void updateSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.update(item));
    }

    public void deleteSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.delete(item));
    }

    public LiveData<List<SearchItem>> getAllSearchItems() {
        return searchItemDAO.getAllItems();
    }
}