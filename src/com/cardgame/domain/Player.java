package com.cardgame.domain;

import com.cardgame.config.DisplayConfig;

public class Player {
    private final String name;
    private Card card;

    public Player(String name) {
        this.name = name;
        this.card = null;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public boolean hasCard() {
        return card != null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return switch (DisplayConfig.getMode()) {
            case DEBUG -> String.format("%s (%s)", getName(), hasCard() ? getCard() : "no card");
            case PLAYER -> getName();
        };
    }
}
