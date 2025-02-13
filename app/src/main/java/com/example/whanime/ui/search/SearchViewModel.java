package com.example.whanime.ui.search;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.whanime.dbHandling.AppDatabase;
import com.example.whanime.dbHandling.SearchItemDAO;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final SearchItemDAO searchItemDAO;
    private final LiveData<List<SearchItem>> allItems;

    public SearchViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        searchItemDAO = db.searchItemDAO();
        allItems = searchItemDAO.getAllItems();
    }

    public LiveData<List<SearchItem>> getAllItems() {
        return allItems;
    }

    public void delete(SearchItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> searchItemDAO.delete(item));
    }

    // Add insert method
    public void insert(SearchItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> searchItemDAO.insert(item));
    }
}