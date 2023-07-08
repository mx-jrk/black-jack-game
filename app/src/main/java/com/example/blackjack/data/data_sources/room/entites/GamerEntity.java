package com.example.blackjack.data.data_sources.room.entites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "gamer_table")
public class GamerEntity {
    @PrimaryKey(autoGenerate = true)
    public int balance;

    public GamerEntity(int balance) {
        this.balance = balance;
    }

    public GamerEntity() {
    }
}
