package blog.javamastery.library.domain;

public record BookInfo(
        String isbn,
        String title,
        String author,
        int yearPublished
) {
    @Override
    public String toString() {
        return String.format(
                "%s by %s [%s]",
                title,
                author,
                isbn);
    }
}
