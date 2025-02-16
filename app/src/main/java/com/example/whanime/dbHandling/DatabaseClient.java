package com.example.whanime.dbHandling;

import android.content.Context;
import androidx.room.Room;

// Clase para gestionar la instancia de la base de datos
public class DatabaseClient {
    private Context context; // Contexto de la aplicación
    private static DatabaseClient instance; // Instancia única de DatabaseClient

    private AppDatabase appDatabase; // Instancia de la base de datos

    // Constructor privado para evitar la creación de múltiples instancias
    private DatabaseClient(Context context) {
        this.context = context;
        // Construcción de la base de datos Room
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "WhanimeDB").build();
    }

    // Metodo para obtener la instancia única de DatabaseClient
    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    // Metodo para obtener la instancia de la base de datos
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}