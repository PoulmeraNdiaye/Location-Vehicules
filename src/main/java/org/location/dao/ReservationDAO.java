package org.location.dao;

import org.location.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO extends IDao<Reservation> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByStatut(String statut);

    Reservation findById(Long id);

    List<Reservation> findReservationsEnConflit(
            int vehicleId,
            LocalDate dateDebut,
            LocalDate dateFin,
            Long exclureId
    );

    List<Reservation> findAll();
}
