package com.example.blackjack.data.data_sources.room.root;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.blackjack.data.data_sources.room.dao.GamerDAO;
import com.example.blackjack.data.data_sources.room.entites.GamerEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GamerEntity.class}, version = 5)
public abstract class GamerDatabase extends RoomDatabase {
    public abstract GamerDAO gamerDAO();

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile GamerDatabase INSTANCE;

    public static GamerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GamerDatabase.class, "user_database").fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}