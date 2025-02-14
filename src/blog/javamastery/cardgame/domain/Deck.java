package blog.javamastery.cardgame.domain;

import blog.javamastery.cardgame.config.DisplayConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private final List<Card> cards;
    private final List<Card> discardPile;

    public Deck() {
        cards = new ArrayList<>();
        discardPile = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        if (!discardPile.isEmpty()) {
            cards.addAll(discardPile);
            discardPile.clear();
        }
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            if (discardPile.isEmpty()) {
                throw new IllegalStateException("Not enough cards left.");
            }
            shuffle();
        }
        return cards.removeLast();
    }

    public List<Card> draw(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot draw negative number of cards");
        }
        if (n > remaining()) {
            if (n > totalCards()) {
                throw new IllegalStateException(
                        String.format("Not enough cards left. Requested: %d, Available: %d", n, totalCards())
                );
            }
            shuffle();
        }
        var drawn = new ArrayList<Card>();
        for (int i = 0; i < n; i++) {
            drawn.add(draw());
        }
        return drawn;
    }

    public void discard(List<Card> playedCards) {
        discardPile.addAll(playedCards);
    }

    public int remaining() {
        return cards.size();
    }

    public int totalCards() {
        return cards.size() + discardPile.size();
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Empty deck";
        }

        var baseOutput = String.format("Deck: %d cards, Discard: %d cards",
                cards.size(),
                discardPile.size());

        return switch (DisplayConfig.getMode()) {
            case DEBUG -> String.format("%s%n%s",
                    baseOutput,
                    cards.stream()
                            .map(Card::getDisplay)
                            .collect(Collectors.joining(", ")
                            ));
            case PLAYER -> baseOutput;
        };
    }
}
