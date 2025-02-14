package blog.javamastery.library.service;

import blog.javamastery.library.domain.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface ReservationService {
    void reserve(String patronId, String isbn);

    void cancelReservation(String patronId, UUID reservationId);

    boolean hasReservations(String isbn);

    Optional<Reservation> getNextReservation(String isbn);

    void notifyBookAvailable(String isbn);
}
