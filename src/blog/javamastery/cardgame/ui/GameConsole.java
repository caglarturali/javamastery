package blog.javamastery.cardgame.ui;

import blog.javamastery.cardgame.config.DisplayConfig;
import blog.javamastery.cardgame.domain.Game;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GameConsole implements AutoCloseable {
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

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
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
        var commands = "Commands: [Enter] play, [d] debug, [q] quit";
        boolean running = true;

        output.printf("%n%s%n%s%n%n%s%n", "Game ready!", game, commands);

        while (running) {
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "q" -> {
                    running = false;
                }
                case "d" -> {
                    output.printf("%n%s%n%s%n%n%s%n",
                            String.format("Switched to %s mode", DisplayConfig.toggleMode()),
                            game,
                            commands
                    );
                }
                default -> {
                    try {
                        var result = game.playRound();
                        output.printf("%s%n%s%n%n", "Round result:", result);
                        if (game.isPlayable()) {
                            output.printf("%s%n%s%n", "Ready for another round.", commands);
                        } else {
                            output.println("End of cards. Play again!");
                            running = false;
                        }
                    } catch (IllegalStateException e) {
                        output.printf("Game over: %s%n", e.getMessage());
                        running = false;
                    }
                }
            }
        }

        output.println("Thanks for playing!");
    }

    public static void main(String[] args) {
        try (var console = new GameConsole()) {
            console.start();
        }
    }
}
