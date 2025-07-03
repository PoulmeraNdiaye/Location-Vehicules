package org.location.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.location.factory.HibernateFactory;
import org.location.models.Client;
import org.location.models.Reservation;
import org.location.models.User;
import org.location.models.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private static final int POINTS_PER_RESERVATION = 10; // Exemple : 10 points par réservation

    public void createReservation(User user, Vehicle vehicle, LocalDate dateDebut, LocalDate dateFin, boolean avecChauffeur) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Client client = session.createQuery("FROM Client WHERE email = :email", Client.class)
                    .setParameter("email", user.getLogin())
                    .uniqueResult();
            if (client == null) {
                throw new IllegalStateException("Client non trouvé pour l'utilisateur : " + user.getLogin());
            }

            if (!isVehicleAvailable(vehicle, dateDebut, dateFin)) {
                throw new IllegalStateException("Le véhicule n'est pas disponible pour les dates sélectionnées.");
            }

            Reservation reservation = new Reservation(client, vehicle, dateDebut, dateFin, "EN_ATTENTE", avecChauffeur);
            vehicle.setDisponible(false); 
            client.setPointsFidelite(client.getPointsFidelite() + POINTS_PER_RESERVATION);
            session.save(reservation);
            session.update(vehicle);
            session.update(client);
            tx.commit();
            logger.info("Réservation créée pour le client {} et le véhicule {} du {} au {}. Points de fidélité mis à jour : {}",
                    client.getId(), vehicle.getId(), dateDebut, dateFin, client.getPointsFidelite());
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la réservation : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la création de la réservation", e);
        }
    }

    public List<Reservation> getReservationsByClient(Client client) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.info("Récupération des réservations pour le client : {}", client.getId());
            Query<Reservation> query = session.createQuery("FROM Reservation WHERE client = :client", Reservation.class);
            query.setParameter("client", client);
            List<Reservation> reservations = query.getResultList();
            logger.info("Nombre de réservations récupérées : {}", reservations.size());
            return reservations;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des réservations : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des réservations", e);
        }
    }

    public boolean isVehicleAvailable(Vehicle vehicle, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE vehicle = :vehicle AND " +
                            "(dateDebut <= :endDate AND dateFin >= :startDate)", Reservation.class);
            query.setParameter("vehicle", vehicle);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Reservation> conflictingReservations = query.getResultList();
            boolean isAvailable = conflictingReservations.isEmpty();
            logger.info("Vérification de disponibilité pour le véhicule {} du {} au {} : {}",
                    vehicle.getId(), startDate, endDate, isAvailable ? "disponible" : "indisponible");
            return isAvailable;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de disponibilité : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la vérification de disponibilité", e);
        }
    }
}