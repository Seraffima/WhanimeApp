package com.example.whanime.ui.search;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.whanime.dbHandling.AppDatabase;
import com.example.whanime.dbHandling.SearchItemDAO;
import java.util.List;

// ViewModel para manejar los elementos de búsqueda
public class SearchViewModel extends AndroidViewModel {
    private final SearchItemDAO searchItemDAO; // DAO para los elementos de búsqueda
    private final LiveData<List<SearchItem>> allItems; // LiveData para observar todos los elementos

    // Constructor que inicializa el DAO y obtiene todos los elementos
    public SearchViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        searchItemDAO = db.searchItemDAO();
        allItems = searchItemDAO.getAllItems();
    }

    // Metodo para obtener todos los elementos
    public LiveData<List<SearchItem>> getAllItems() {
        return allItems;
    }

    // Metodo para eliminar un elemento
    public void delete(SearchItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> searchItemDAO.delete(item));
    }

    // Metodo para insertar un elemento
    public void insert(SearchItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> searchItemDAO.insert(item));
    }
}