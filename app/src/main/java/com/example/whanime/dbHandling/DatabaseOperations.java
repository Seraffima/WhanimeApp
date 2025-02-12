package com.example.whanime.dbHandling;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.whanime.dbHandling.AppDatabase;
import com.example.whanime.dbHandling.SearchItemDAO;
import com.example.whanime.ui.search.SearchItem;

public class DatabaseOperations {

    private AppDatabase database;
    private SearchItemDAO searchItemDAO;
    private ExecutorService executorService;

    public DatabaseOperations(AppDatabase database) {
        this.database = database;
        this.searchItemDAO = database.searchItemDao();
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

    public void getAllSearchItems() {
        executorService.execute(() -> {
            List<SearchItem> items = searchItemDAO.getAllItems();
            // Handle the result here (e.g., update UI)
        });
    }
}
