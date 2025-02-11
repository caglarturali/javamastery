package com.cardgame.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record GameResult(List<Player> winners, int maxValue) {
    public Optional<Player> getWinner() {
        if (winners.size() > 1) {
            return Optional.empty();
        }
        return Optional.ofNullable(winners.getFirst());
    }

    @Override
    public String toString() {
        return String.format("Max value: %d%n\tPlayers: %s%n\tWinner: %s",
                maxValue,
                winners.stream()
                        .map(p -> String.format("%s (%s)", p.getName(), p.getCard()))
                        .collect(Collectors.joining(", ")),
                getWinner().map(Player::getName).orElse("TIE!")
        );
    }
}
