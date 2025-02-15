package blog.javamastery.dsa.stack;

public class SimpleStackTest {
    public static void main(String[] args) {
        var stack = new SimpleStack<String>();

        System.out.printf("Initial: %s%n%n", stack);

        // Push until force a resize
        for (int i = 0; i < 20; i++) {
            stack.push(String.format("Item %d", i));
            System.out.printf("After push %d: %s%n", i, stack);
        }
        System.out.println();

        // Pop until trigger shrinking
        while (stack.size() > 4) {
            var item = stack.pop();
            System.out.printf("After popping %s: %s%n", item, stack);
        }
        System.out.println();

        stack = new SimpleStack<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        stack.push("Fourth");

        System.out.printf("Stack state: %s%n%n", stack);

        // Test iteration (should print in LIFO order)
        System.out.println("Iterating through stack:");
        for (var item : stack) {
            System.out.println(item);
        }

        System.out.println("\nUsing stream:");
        var list = stack.stream().toList();
        System.out.println(list);
        stack.stream()
                .filter(s -> s.length() > 5)
                .forEach(System.out::println);
    }
}
