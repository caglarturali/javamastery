package com.library.service;

import com.library.domain.BookCopy;
import com.library.domain.BorrowingRecord;
import com.library.domain.Library;
import com.library.exceptions.AlreadyBorrowedException;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.NotBorrowedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BorrowingService {
    private final Library library;
    private final List<BorrowingRecord> borrowingHistory;

    public BorrowingService(Library library) {
        this.library = library;
        this.borrowingHistory = new ArrayList<>();
    }

    public BorrowingRecord borrowBook(String isbn, String patronId) {
        var copy = findAvailableCopy(isbn).orElseThrow(() -> new BookNotFoundException(isbn));

        var record = BorrowingRecord.create(copy.getCopyId(), patronId);
        borrowingHistory.add(record);
        return record;
    }

    public BorrowingRecord returnBook(String copyId) {
        var currentBorrowing = getCurrentBorrowing(copyId)
                .orElseThrow(() -> new NotBorrowedException(copyId));

        var returnRecord = currentBorrowing.withReturn();
        borrowingHistory.add(returnRecord);
        return returnRecord;
    }

    public List<BorrowingRecord> getBorrowingHistory(String copyId) {
        return borrowingHistory.stream()
                .filter(record -> record.copyId().equals(copyId))
                .toList();
    }

    public boolean isCurrentlyBorrowed(String copyId) {
        return getCurrentBorrowing(copyId).isPresent();
    }

    private Optional<BookCopy> findAvailableCopy(String isbn) {
        return library.findAllByISBN(isbn).stream()
                .filter(copy -> !isCurrentlyBorrowed(copy.getCopyId()))
                .findFirst();
    }

    private Optional<BorrowingRecord> getCurrentBorrowing(String copyId) {
        return borrowingHistory.stream()
                .filter(record -> record.copyId().equals(copyId))
                .max(Comparator.comparing(BorrowingRecord::borrowedAt))
                .filter(record -> record.returnedAt().isEmpty());
    }
}
