package com.library.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super(String.format("Book not found: %s", id));
    }
}
