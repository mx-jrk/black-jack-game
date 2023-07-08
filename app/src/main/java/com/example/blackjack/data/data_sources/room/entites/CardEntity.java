package com.example.blackjack.data.data_sources.room.entites;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cards")
public class CardEntity {

    @PrimaryKey
    public int id;

    public String rang;

    public String suit;

    public String imgSrc;
}
