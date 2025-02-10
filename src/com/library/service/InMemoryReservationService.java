package com.library.service;

import com.library.domain.BookCopy;
import com.library.domain.Library;
import com.library.domain.Reservation;
import com.library.exceptions.BookNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryReservationService implements ReservationService {
    // Queue of reservations for each book
    private final Map<String, Queue<Reservation>> reservationsByIsbn;

    // All reservations for O(1) lookup by ID
    private final Map<UUID, Reservation> reservationsById;

    private final Library library;

    public InMemoryReservationService(Library library) {
        this.reservationsByIsbn = new HashMap<>();
        this.reservationsById = new HashMap<>();
        this.library = library;
    }

    @Override
    public void reserve(String patronId, String isbn) {
        var bookCopies = library.findAllByISBN(isbn);
        if (bookCopies.isEmpty()) {
            throw new BookNotFoundException(isbn);
        }

        boolean hasAvailableCopy = bookCopies.stream().anyMatch(BookCopy::isAvailable);

        if (hasAvailableCopy) {
            throw new IllegalStateException(
                    String.format("Book %s has available copies - no reservation needed", isbn)
            );
        }

        boolean hasExistingReservation = reservationsByIsbn
                .getOrDefault(isbn, new LinkedList<>())
                .stream()
                .anyMatch(r -> r.patronId().equals(patronId));

        if (hasExistingReservation) {
            throw new IllegalStateException(
                    String.format("Patron %s already has a reservation for book %s", patronId, isbn)
            );
        }

        var bookInfo = bookCopies.getFirst().getBookInfo();
        var reservation = Reservation.create(patronId, bookInfo);

        reservationsByIsbn.computeIfAbsent(isbn, k -> new LinkedList<>()).offer(reservation);
        reservationsById.put(reservation.id(), reservation);
    }

    @Override
    public void cancelReservation(String patronId, UUID reservationId) {
        var reservation = reservationsById.get(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException(
                    String.format("Reservation not found: %s", reservationId)
            );
        }

        if (!reservation.patronId().equals(patronId)) {
            throw new IllegalStateException(
                    String.format("Reservation does not belong to: %s", patronId)
            );
        }

        var isbn = reservation.bookInfo().isbn();
        var queue = reservationsByIsbn.get(isbn);
        if (queue != null) {
            var newQueue = queue.stream()
                    .filter(r -> !r.id().equals(reservationId))
                    .collect(Collectors.toCollection(LinkedList::new));
            reservationsByIsbn.put(isbn, newQueue);
        }

        reservationsById.remove(reservation.id());
    }

    @Override
    public boolean hasReservations(String isbn) {
        return !reservationsByIsbn.getOrDefault(isbn, new LinkedList<>()).isEmpty();
    }

    @Override
    public Optional<Reservation> getNextReservation(String isbn) {
        var queue = reservationsByIsbn.getOrDefault(isbn, new LinkedList<>());
        if (queue.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(queue.peek());
    }

    @Override
    public void notifyBookAvailable(String isbn) {
        var nextReservation = getNextReservation(isbn);

        nextReservation.ifPresent(reservation ->
                System.out.printf(
                        "Notification: Dear patron %s, the book '%s' is now available. You have until %s to pick it up.%n",
                        reservation.patronId(),
                        reservation.bookInfo().title(),
                        reservation.expiresAt()
                ));
    }
}
