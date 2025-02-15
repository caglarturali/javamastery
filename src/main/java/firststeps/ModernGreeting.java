package firststeps;

import java.util.List;
import java.util.stream.Collectors;

public class ModernGreeting {
    public static void main(String[] args) {
        String name = "Java Journey";
        var currentTime = java.time.LocalTime.now();
        String greeting = """
                Welcome to %s!
                The time is %s
                Ready to begin your adventure?
                """.formatted(name, currentTime);
        // System.out.println(greeting);

        var languages = List.of("JavaScript", "TypeScript", "Python", "C#", "Java");
        var message = """
                Your programming journey includes:
                %s
                
                And now you're diving deeper into Java!
                """.formatted(
                languages
                        .stream().map(lang -> "- " + lang)
                        .collect(Collectors.joining("\n"))
        );
        System.out.println(message);
    }
}
