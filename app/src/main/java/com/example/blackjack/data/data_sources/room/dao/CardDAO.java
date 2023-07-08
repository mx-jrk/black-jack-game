package com.example.blackjack.data.data_sources.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.blackjack.data.data_sources.room.entites.CardEntity;

import java.util.List;

@Dao
public interface CardDAO {
    @Query("SELECT * FROM cards")
    LiveData<List<CardEntity>> getAllCards();
}
