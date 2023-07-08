package com.example.blackjack.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.blackjack.data.data_sources.room.entites.GamerEntity;

@Dao
public interface GamerDAO {
    @Insert
    void insert(GamerEntity gamer);

    @Update
    void update(GamerEntity gamer);

    @Query("SELECT * FROM gamer_table")
    LiveData<GamerEntity> getGamer();

}
