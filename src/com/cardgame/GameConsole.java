package com.cardgame;

import com.cardgame.domain.Game;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GameConsole {
    private final PrintStream output;
    private final Scanner scanner;
    private final Game game;

    public GameConsole(InputStream input, PrintStream output) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.game = new Game();
    }

    public GameConsole() {
        this(System.in, System.out);
    }

    public void start() {
        output.println("Welcome to High Card Game!");
        output.println("Enter player names (empty line to finish):");

        while (true) {
            output.print("Enter player name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                if (game.getPlayerCount() < 2) {
                    output.println("Need at least 2 players to start!");
                    continue;
                }
                break;
            }

            try {
                game.addPlayer(name);
                output.println("Player added!");
            } catch (IllegalArgumentException e) {
                output.printf("Error: %s%n", e.getMessage());
            }
        }

        playGame();
    }

    private void playGame() {
        output.println("\nGame ready!");
        output.println(game);

        output.println("\nPress Enter to play a round (q to quit)");

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("q")) {
                break;
            }

            try {
                var result = game.playRound();
                output.println("Round result:");
                output.println(result);
                if (game.isPlayable()) {
                    output.println("\nReady for another round (q to quit)");
                } else {
                    output.println("\nEnd of cards. Play again!");
                    break;
                }
            } catch (IllegalStateException e) {
                output.printf("Game over: %s%n", e.getMessage());
                break;
            }
        }

        output.println("Thanks for playing!");
    }

    public static void main(String[] args) {
        new GameConsole().start();
    }
}
