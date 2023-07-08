package com.example.blackjack.ui.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.blackjack.data.data_sources.room.entites.CardEntity;
import com.example.blackjack.data.repositories.CardRepository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {
    public List<CardEntity> cards;
    private final CardRepository cardRepository;

    public CardViewModel(@NonNull Application application) {
        super(application);
        cardRepository = new CardRepository(application);
    }

    public LiveData<List<CardEntity>> getAllCards(){
        return cardRepository.getAllCards();
    }
}
