package com.cardgame.domain;

import com.cardgame.utils.StringUtils;

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
