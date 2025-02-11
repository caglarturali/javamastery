package com.cardgame.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private final Deck deck;
    private final List<Player> players;

    public Game() {
        this.deck = new Deck();
        this.deck.shuffle();
        this.players = new ArrayList<>();
    }

    public void addPlayer(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }

        boolean nameExists = players.stream()
                .map(Player::getName)
                .anyMatch(name -> name.equalsIgnoreCase(playerName.trim()));

        if (nameExists) {
            throw new IllegalArgumentException(
                    String.format("Player name %s already exists", playerName)
            );
        }

        this.players.add(new Player(playerName));
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isPlayable() {
        return deck.remaining() >= players.size();
    }

    public GameResult playRound() {
        if (players.size() < 2) {
            throw new IllegalStateException("Game requires at least 2 players");
        }
        if (deck.remaining() < players.size()) {
            throw new IllegalStateException("Not enough cards to play a round");
        }

        // Deal cards to all players
        players.forEach(player -> player.setCard(deck.draw()));

        int maxValue = players.stream()
                .mapToInt(p -> p.getCard().rank().getValue())
                .max()
                .orElseThrow(() -> new IllegalStateException("No cards found to compare"));

        var winners = players.stream()
                .filter(p -> p.getCard().rank().getValue() == maxValue)
                .toList();

        return new GameResult(winners, maxValue);
    }

    @Override
    public String toString() {
        return String.format("Cards remaining: %d%nPlayer count: %d%n%s",
                deck.remaining(),
                players.size(),
                players.stream()
                        .map(p -> String.format("- %s", p.getName()))
                        .collect(Collectors.joining("\n"))
        );
    }
}
