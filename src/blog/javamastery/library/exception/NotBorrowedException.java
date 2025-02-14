package blog.javamastery.library.exception;

public class NotBorrowedException extends CopyStateException {
    public NotBorrowedException(String isbn) {
        super(String.format("Book is not borrowed: %s", isbn));
    }
}
