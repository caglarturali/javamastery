package blog.javamastery.cardgame.util;

public class StringUtils {
    public static String capitalize(String str) {
        return str.toUpperCase().charAt(0) + str.toLowerCase().substring(1);
    }

    public static String charAt(String str, int idx) {
        return String.valueOf(str.charAt(idx));
    }
}
