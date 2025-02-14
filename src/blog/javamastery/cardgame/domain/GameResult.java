package blog.javamastery.cardgame.domain;

import blog.javamastery.cardgame.config.DisplayConfig;

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
        var baseOutput = String.format("Max value: %d", maxValue);
        var playersStr = String.format("Winners: %s",
                winners.stream()
                        .map(Player::toString)
                        .collect(Collectors.joining(", ")));
        var winnerStr = String.format("Winner: %s",
                getWinner().map(Player::toString).orElse("TIE!"));

        return switch (DisplayConfig.getMode()) {
            case DEBUG -> String.format("%s%n\t%s%n\t%s", baseOutput, playersStr, winnerStr);
            case PLAYER -> String.format("%s%n\t%s", baseOutput, winnerStr);
        };
    }
}
