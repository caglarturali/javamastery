package com.library.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record BorrowingRecord(
        UUID borrowId,
        String copyId,
        String patronId,
        LocalDateTime borrowedAt,
        LocalDateTime dueDate,
        Optional<LocalDateTime> returnedAt
) {
    private static final Duration DEFAULT_LENDING_PERIOD = Duration.ofDays(14);

    public BorrowingRecord {
        Objects.requireNonNull(borrowId, "Borrow ID cannot be null");
        Objects.requireNonNull(copyId, "Copy ID cannot be null");
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        Objects.requireNonNull(borrowedAt, "Borrow date cannot be null");
        Objects.requireNonNull(dueDate, "Due date cannot be null");
        Objects.requireNonNull(returnedAt, "Return date cannot be null (use Optional.empty())");

        if (dueDate.isBefore(borrowedAt)) {
            throw new IllegalArgumentException("Due date cannot be before borrow date");
        }

        if (returnedAt.isPresent() && returnedAt.get().isBefore(borrowedAt)) {
            throw new IllegalArgumentException("Return date cannot be before borrow date");
        }
    }

    public static BorrowingRecord create(String copyId, String patronId) {
        var now = LocalDateTime.now();
        return new BorrowingRecord(
                UUID.randomUUID(),
                copyId,
                patronId,
                now,
                now.plus(DEFAULT_LENDING_PERIOD),
                Optional.empty()
        );
    }

    public BorrowingRecord withReturn() {
        return new BorrowingRecord(
                borrowId,
                copyId,
                patronId,
                borrowedAt,
                dueDate,
                Optional.of(LocalDateTime.now())
        );
    }

    @Override
    public String toString() {
        var dateFmt = DateTimeFormatter.ISO_LOCAL_DATE;
        var baseInfo = String.format(
                "Borrowing %s by patron %s on %s (due %s)",
                copyId,
                patronId,
                borrowedAt.format(dateFmt),
                dueDate.format(dateFmt)
        );

        return returnedAt
                .map(returned -> String.format("%s - returned on %s", baseInfo, returned.format(dateFmt)))
                .orElseGet(() -> String.format("%s - not returned yet", baseInfo));
    }
}
