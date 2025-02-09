package com.library.exceptions;

public class NotBorrowedException extends CopyStateException {
    public NotBorrowedException(String isbn) {
        super(String.format("Book is not borrowed: %s", isbn));
    }
}
