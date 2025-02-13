package com.example.whanime.dbHandling;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.whanime.ui.search.SearchItem;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SearchItem.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SearchItemDAO searchItemDAO();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
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