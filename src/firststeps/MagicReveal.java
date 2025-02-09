package firststeps;

public class MagicReveal {
    public static void main(String[] args) {
        // Java version - JRE information
        String version = System.getProperty("java.version");

        // JVM name and details
        String jvmName = System.getProperty("java.vm.name");

        // Classpath - set by JDK tools
        String classpath = System.getProperty("java.class.path");

        System.out.println("Your magic runs on:");
        System.out.printf("Java version: %s%n", version);
        System.out.printf("JVM: %s%n", jvmName);
        System.out.printf("Classpath: %s%n", classpath);
    }
}
