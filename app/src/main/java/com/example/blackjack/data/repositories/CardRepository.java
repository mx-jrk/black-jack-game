package com.example.blackjack.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.blackjack.data.data_sources.room.dao.CardDAO;
import com.example.blackjack.data.data_sources.room.entites.CardEntity;
import com.example.blackjack.data.data_sources.room.root.CardDatabase;

import java.util.List;

public class CardRepository {
    private final LiveData<List<CardEntity>> cards;

    public CardRepository(Application application){
        CardDatabase db = CardDatabase.getDatabase(application);
        CardDAO cardDAO = db.cardDAO();
        cards = cardDAO.getAllCards();
    }

    public LiveData<List<CardEntity>> getAllCards(){
        return cards;
    }
}
