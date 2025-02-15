package blog.javamastery.cardgame.domain;

import blog.javamastery.cardgame.util.StringUtils;

public enum Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES;

    public String getDisplay() {
        return StringUtils.charAt(name(), 0);
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(name());
    }
}
