package com.example.whanime.dbHandling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.whanime.ui.search.SearchItem;

// Clase para realizar operaciones en la base de datos
public class DatabaseOperations {

    private AppDatabase database; // Instancia de la base de datos
    private SearchItemDAO searchItemDAO; // DAO para SearchItem
    private ExecutorService executorService; // Servicio de ejecutores para operaciones en segundo plano

    // Constructor que inicializa la base de datos, el DAO y el servicio de ejecutores
    public DatabaseOperations(AppDatabase database) {
        this.database = database;
        this.searchItemDAO = database.searchItemDAO();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    // Metodo para insertar un SearchItem en la base de datos
    public void insertSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.insert(item));
    }

    // Metodo para actualizar un SearchItem en la base de datos
    public void updateSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.update(item));
    }

    // Metodo para eliminar un SearchItem de la base de datos
    public void deleteSearchItem(SearchItem item) {
        executorService.execute(() -> searchItemDAO.delete(item));
    }

    // Metodo para obtener todos los SearchItems de la base de datos
    public LiveData<List<SearchItem>> getAllSearchItems() {
        return searchItemDAO.getAllItems();
    }
}