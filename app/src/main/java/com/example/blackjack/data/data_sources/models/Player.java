package com.example.blackjack.data.data_sources.models;

import com.example.blackjack.data.data_sources.room.entites.CardEntity;

import java.util.List;

public class Player {
    private final boolean isDealer;

    private int balance;

    private final List<CardEntity> cards;

    public Player(boolean isDealer, int balance, List<CardEntity> cards) {
        this.isDealer = isDealer;
        this.balance = balance;
        this.cards = cards;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public int getBalance() {
        return balance;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addCard(CardEntity card){
        cards.add(card);
    }

    public int getSum(boolean isFullSum){
        int sum = 0;
        for (CardEntity card : cards){
            if (!isFullSum) {
                isFullSum = true;
                continue;
            }
            if (card.rang.charAt(0) <= '9'){
                sum += Integer.parseInt(card.rang);
            }
            else {
                if (card.rang.charAt(0) == 'a'){
                    if (sum + 11 > 21) sum += 1;
                    else sum += 11;
                }
                else sum += 10;
            }
        }
        return sum;
    }
}
