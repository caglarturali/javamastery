package com.library.service;

import com.library.domain.Library;
import com.library.exceptions.BookNotFoundException;

public class BorrowingService {
    private final Library library;

    public BorrowingService(Library library) {
        this.library = library;
    }

    public String borrowBook(String isbn) {
        var copy = library.findOneByISBN(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
        copy.markAsBorrowed();
        return copy.getCopyId();
    }

    public String returnBook(String copyId) {
        var copy = library.findOneById(copyId).orElseThrow(() -> new BookNotFoundException(copyId));
        copy.markAsReturned();
        return copy.getCopyId();
    }
}
