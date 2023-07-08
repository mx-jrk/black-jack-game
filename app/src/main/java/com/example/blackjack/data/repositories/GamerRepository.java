package com.example.blackjack.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.blackjack.data.data_sources.room.dao.GamerDAO;
import com.example.blackjack.data.data_sources.room.entites.GamerEntity;
import com.example.blackjack.data.data_sources.room.root.GamerDatabase;

public class GamerRepository {
    private final GamerDAO gamerDAO;
    private final LiveData<GamerEntity> gamer;

    public GamerRepository(Application application){
        GamerDatabase db = GamerDatabase.getDatabase(application);
        gamerDAO = db.gamerDAO();
        gamer = gamerDAO.getGamer();
    }

    public void insert(GamerEntity gamer){
        gamerDAO.insert(gamer);
    }


    public void update(GamerEntity gamer){
            gamerDAO.update(gamer);
    }


    public LiveData<GamerEntity> getGamer(){
        return gamer;
    }
}
