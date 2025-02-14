package blog.javamastery.library.service;

import blog.javamastery.library.domain.BookCopy;
import blog.javamastery.library.domain.BorrowingRecord;
import blog.javamastery.library.domain.Library;
import blog.javamastery.library.exception.BookNotFoundException;
import blog.javamastery.library.exception.NotBorrowedException;

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
