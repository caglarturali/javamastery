package firststeps;

public class TypesDemo {
    public static void main(String[] args) {
        // Primitive types
        int number = 42;
        double decimal = 3.14;
        boolean flag = true;
        char letter = 'A';

        // Reference type
        String text = "Hello";

        System.out.println(number + " is of type " + ((Object) number).getClass().getName());
        System.out.println(text + " is of type " + text.getClass().getName());
    }
}
