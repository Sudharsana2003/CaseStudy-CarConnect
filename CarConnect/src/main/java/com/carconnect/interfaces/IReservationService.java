package com.carconnect.interfaces;

import com.carconnect.models.Reservation;
import com.carconnect.exceptions.CustomerNotFoundException;
import com.carconnect.exceptions.ReservationException;

import java.util.List;

public interface IReservationService {
    Reservation getReservationById(int reservationId) throws CustomerNotFoundException;

    List<Reservation> getReservationsByCustomerId(int customerId) throws CustomerNotFoundException;

    void createReservation(Reservation reservation) throws ReservationException; // Fix: Added exception

    void updateReservation(Reservation reservation) throws CustomerNotFoundException;

    void cancelReservation(int reservationId) throws CustomerNotFoundException;
}
