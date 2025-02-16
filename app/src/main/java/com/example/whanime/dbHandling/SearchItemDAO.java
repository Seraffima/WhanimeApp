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

// Interfaz DAO para acceder a los datos de SearchItem
@Dao
public interface SearchItemDAO {
    // Metodo para insertar un SearchItem en la base de datos
    @Insert
    void insert(SearchItem searchItem);

    // Metodo para actualizar un SearchItem en la base de datos
    @Update
    void update(SearchItem searchItem);

    // Metodo para eliminar un SearchItem de la base de datos
    @Delete
    void delete(SearchItem searchItem);

    // Metodo para obtener todos los SearchItems de la base de datos
    @Query("SELECT * FROM search_items")
    LiveData<List<SearchItem>> getAllItems();
}