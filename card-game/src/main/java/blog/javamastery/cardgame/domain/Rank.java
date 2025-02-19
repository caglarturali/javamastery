package blog.javamastery.cardgame.domain;

import blog.javamastery.cardgame.util.StringUtils;

public enum Rank {
    TWO, THREE, FOUR, FIVE, SIX,
    SEVEN, EIGHT, NINE, TEN,
    JACK, QUEEN, KING, ACE;

    public int getValue() {
        return switch (this) {
            case ACE -> 11;
            case KING, QUEEN, JACK -> 10;
            default -> ordinal() + 2; // TWO is ordinal 0
        };
    }

    public String getDisplay() {
        return switch (this) {
            case ACE, KING, QUEEN, JACK -> StringUtils.charAt(name(), 0);
            default -> String.valueOf(getValue());
        };
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
