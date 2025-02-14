package blog.javamastery.library.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record Reservation(
        UUID id,
        String patronId,
        BookInfo bookInfo,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
    private static final Duration DEFAULT_RESERVATION_DURATION = Duration.ofDays(2);

    public Reservation {
        Objects.requireNonNull(id, "Reservation ID cannot be null");
        Objects.requireNonNull(patronId, "Patron ID cannot be null");
        Objects.requireNonNull(bookInfo, "Book info cannot be null");
        Objects.requireNonNull(createdAt, "Created date cannot be null");
        Objects.requireNonNull(expiresAt, "Expiration date cannot be null");

        if (expiresAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Expiration date cannot be before creation date");
        }
    }

    /**
     * Creates a new reservation with default duration of 2 days
     *
     * @param patronId Patron info
     * @param bookInfo Book to reserve
     * @return Reservation object
     */
    public static Reservation create(String patronId, BookInfo bookInfo) {
        return create(patronId, bookInfo, DEFAULT_RESERVATION_DURATION);
    }

    public static Reservation create(String patronId, BookInfo bookInfo, Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return new Reservation(
                UUID.randomUUID(),
                patronId,
                bookInfo,
                now,
                now.plus(duration)
        );
    }
}
