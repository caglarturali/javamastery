package com.library.domain;

import com.library.exceptions.AlreadyBorrowedException;
import com.library.exceptions.NotBorrowedException;

import java.util.UUID;

public class BookCopy {
    private final UUID copyId;
    private final BookInfo bookInfo;
    private boolean isAvailable;

    public BookCopy(BookInfo bookInfo) {
        this.copyId = UUID.randomUUID();
        this.bookInfo = bookInfo;
        this.isAvailable = true;
    }

    public UUID getCopyIdAsUUID() {
        return copyId;
    }

    public String getCopyId() {
        return copyId.toString();
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void markAsBorrowed() {
        if (!isAvailable) {
            throw new AlreadyBorrowedException(getCopyId());
        }
        isAvailable = false;
    }

    public void markAsReturned() {
        if (isAvailable) {
            throw new NotBorrowedException(getCopyId());
        }
        isAvailable = true;
    }

    @Override
    public String toString() {
        return String.format(
                "%s by %s [Copy: %s] (%s)",
                bookInfo.title(),
                bookInfo.author(),
                copyId,
                isAvailable ? "Available" : "Borrowed");
    }
}
