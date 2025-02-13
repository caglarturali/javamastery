package com.library.domain;

import java.util.UUID;

public class BookCopy {
    private final UUID copyId;
    private final BookInfo bookInfo;

    public BookCopy(BookInfo bookInfo) {
        this.copyId = UUID.randomUUID();
        this.bookInfo = bookInfo;
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

    @Override
    public String toString() {
        return String.format(
                "%s by %s [Copy: %s]",
                bookInfo.title(),
                bookInfo.author(),
                copyId);
    }
}
