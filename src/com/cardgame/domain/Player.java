package com.cardgame.domain;

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
        return String.format("%s: %s", getName(), hasCard() ? getCard().getDisplay() : "no card");
    }
}
