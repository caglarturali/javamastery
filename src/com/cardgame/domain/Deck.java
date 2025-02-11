package com.cardgame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in deck");
        }
        return cards.removeLast();
    }

    public List<Card> draw(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot draw negative number of cards");
        }
        if (n > cards.size()) {
            throw new IllegalStateException(
                    String.format("Not enough cards left in deck. Requested: %d, Available: %d", n, cards.size())
            );
        }
        var drawn = new ArrayList<Card>();
        for (int i = 0; i < n; i++) {
            drawn.add(cards.removeLast());
        }
        return drawn;
    }

    public int remaining() {
        return cards.size();
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Empty deck";
        }

        return String.format("Deck: %d cards%n%s",
                cards.size(),
                cards.stream()
                        .map(Card::getDisplay)
                        .collect(Collectors.joining(", "))
        );
    }
}
