package blog.javamastery.cardgame.domain;

import java.util.Objects;

public record Card(Suit suit, Rank rank) {
    public Card {
        Objects.requireNonNull(suit, "Suit cannot be null");
        Objects.requireNonNull(rank, "Rank cannot be null");
    }

    public String getDisplay() {
        return String.format("%s%s", rank.getDisplay(), suit.getDisplay());
    }

    @Override
    public String toString() {
        return String.format("%s of %s", rank, suit);
    }
}
