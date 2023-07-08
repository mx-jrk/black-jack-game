package com.example.blackjack.ui.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.blackjack.data.data_sources.room.entites.GamerEntity;
import com.example.blackjack.data.repositories.GamerRepository;

public class GamerViewModel extends AndroidViewModel {

    public GamerEntity gamer;
    private final GamerRepository gamerRepository;

    public GamerViewModel(@NonNull Application application) {
        super(application);

        this.gamerRepository = new GamerRepository(application);
    }

    public void insertGamer(GamerEntity gamer){
        gamerRepository.insert(gamer);
    }

    public void updateGamer(GamerEntity gamer){
        gamerRepository.update(gamer);
    }

    public LiveData<GamerEntity> getGamer(){
        return gamerRepository.getGamer();
    }
}
