package com.example.whanime.dbHandling;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.whanime.ui.search.SearchItem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Definición de la base de datos Room
@Database(entities = {SearchItem.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Metodo abstracto para obtener el DAO de SearchItem
    public abstract SearchItemDAO searchItemDAO();

    // Instancia única de la base de datos
    private static volatile AppDatabase INSTANCE;
    // Número de hilos para el pool de ejecutores
    private static final int NUMBER_OF_THREADS = 4;
    // Pool de ejecutores para operaciones de escritura en la base de datos
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Metodo para obtener la instancia de la base de datos
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Construcción de la base de datos con fallback a migración destructiva
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "whanime_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}