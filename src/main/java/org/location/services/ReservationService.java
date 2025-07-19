package org.location.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.location.factory.HibernateFactory;
import org.location.models.Client;
import org.location.models.Reservation;
import org.location.models.User;
import org.location.models.Vehicle;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private static final int POINTS_PER_RESERVATION = 10;
    private static final double CHAUFFEUR_FEE = 1500.0; // Constant for chauffeur fee

    public void createReservation(User user, Vehicle vehicle, LocalDate startDate, LocalDate endDate, boolean avecChauffeur) {
        Transaction transaction = null;
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Client client = user.getClient();
            if (client == null) {
                throw new IllegalStateException("Client non trouvé pour l'utilisateur : " + user.getLogin());
            }

            long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
            if (numberOfDays <= 0) {
                throw new IllegalArgumentException("La durée de la réservation doit être d'au moins un jour.");
            }

            double montantFacture = vehicle.getTarif() * numberOfDays;
            if (avecChauffeur) {
                montantFacture += CHAUFFEUR_FEE; // Add 1500 for chauffeur
            }

            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setVehicle(vehicle);
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setAvecChauffeur(avecChauffeur);
            reservation.setStatut("EN_ATTENTE");
            reservation.setMontantFacture(montantFacture);

            session.save(reservation);

            client.setPointsFidelite(client.getPointsFidelite() + POINTS_PER_RESERVATION);
            session.update(client);
            user.setClient(client);
            SessionManager.setCurrentUser(user);

            transaction.commit();
            logger.info("Réservation créée pour le client {} et le véhicule {} du {} au {}. Montant facturé : {} (avec chauffeur : {}), Points de fidélité : {}",
                    client.getId(), vehicle.getId(), startDate, endDate, montantFacture, avecChauffeur, client.getPointsFidelite());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la création de la réservation : {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Reservation> getUserReservations(User user) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Client client = user.getClient();
            if (client == null) {
                throw new IllegalStateException("Client non trouvé pour l'utilisateur : " + user.getLogin());
            }
            logger.debug("Exécution de la requête pour récupérer les réservations du client ID: {}", client.getId());
            List<Reservation> reservations = session.createQuery(
                            "FROM Reservation r JOIN FETCH r.vehicle WHERE r.client.id = :clientId", Reservation.class)
                    .setParameter("clientId", client.getId())
                    .list();
            logger.debug("Nombre de réservations trouvées : {}", reservations.size());
            return reservations;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des réservations pour l'utilisateur : {}", user.getLogin(), e);
            throw e;
        }
    }

    public Client getClientByEmail(String email) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Client WHERE email = :email", Client.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du client pour l'email : {}", email, e);
            throw e;
        }
    }

    public boolean isVehicleAvailable(Vehicle vehicle, LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.debug("Vérification de la disponibilité du véhicule {} pour {} à {}", vehicle.getId(), startDate, endDate);
            List<Reservation> conflictingReservations = session.createQuery(
                            "FROM Reservation WHERE vehicle.id = :vehicleId " +
                                    "AND (startDate <= :endDate AND endDate >= :startDate)", Reservation.class)
                    .setParameter("vehicleId", vehicle.getId())
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
            logger.debug("Conflits trouvés : {}", conflictingReservations.size());
            return conflictingReservations.isEmpty();
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de la disponibilité du véhicule : {}", vehicle.getId(), e);
            throw e;
        }
    }

    public boolean isVehicleAvailableNow(Vehicle vehicle) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            LocalDate today = LocalDate.now();
            logger.debug("Vérification si le véhicule {} est disponible aujourd'hui ({})", vehicle.getId(), today);
            List<Reservation> activeReservations = session.createQuery(
                            "FROM Reservation WHERE vehicle.id = :vehicleId AND endDate >= :today", Reservation.class)
                    .setParameter("vehicleId", vehicle.getId())
                    .setParameter("today", today)
                    .list();
            logger.debug("Réservations actives pour le véhicule {} : {}", vehicle.getId(), activeReservations.size());
            return activeReservations.isEmpty();
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de la disponibilité actuelle du véhicule : {}", vehicle.getId(), e);
            throw e;
        }
    }
}