package blog.javamastery.library.exception;

public class AlreadyBorrowedException extends CopyStateException {
    public AlreadyBorrowedException(String copyId) {
        super(String.format("Book is already borrowed: %s", copyId));
    }
}
