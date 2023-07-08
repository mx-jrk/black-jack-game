package com.example.blackjack.data.data_sources.room.root;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.blackjack.data.data_sources.room.dao.CardDAO;
import com.example.blackjack.data.data_sources.room.entites.CardEntity;

@Database(entities = {CardEntity.class}, version = 1, exportSchema = false)
public abstract class CardDatabase extends RoomDatabase {
    public abstract CardDAO cardDAO();

    private static volatile CardDatabase INSTANCE;

    public static CardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CardDatabase.class, "cards.db")
                            .createFromAsset("cards.db")
                            .fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
