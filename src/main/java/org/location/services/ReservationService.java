package org.location.services;

import org.hibernate.Session;
import org.location.dao.FactureDAO;
import org.location.dao.ReservationDAO;
import org.location.dao.VehicleDAO;
import org.location.dao.impl.FactureDAOImpl;
import org.location.dao.impl.ReservationDAOImpl;
import org.location.dao.impl.VehicleDAOImpl;
import org.location.exception.DAOException;
import org.location.factory.ConcreteFactory;
import org.location.factory.HibernateAppFactory;
import org.location.factory.HibernateFactory;
import org.location.factory.interfaces.FactureFactory;
import org.location.factory.interfaces.ReservationFactory;
import org.location.models.*;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final FactureDAO factureDAO;
    private final DataNotifier notifier = NotifierSingleton.getInstance();
    private static final double CHAUFFEUR_TARIF = 1500.0;
    private final ClientService clientService = new ClientService();
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    public ReservationService() {
        this.reservationDAO = (ReservationDAO) ConcreteFactory
                .getFactory(ReservationFactory.class)
                .getReservationDao(ReservationDAOImpl.class);

        this.factureDAO = (FactureDAO) ConcreteFactory
                .getFactory(FactureFactory.class)
                .getFactureDao(FactureDAOImpl.class);
    }

    public double calculerMontant(Vehicle vehicle, LocalDate dateDebut, LocalDate dateFin, boolean avecChauffeur) {
        long jours = ChronoUnit.DAYS.between(dateDebut, dateFin);
        if (jours <= 0) throw new IllegalArgumentException("Durée invalide");
        double montant = jours * vehicle.getTarif();
        if (avecChauffeur) montant += CHAUFFEUR_TARIF;
        return montant;
    }

    public Reservation preparerReservation(Client client, Vehicle vehicle, LocalDate debut, LocalDate fin, boolean avecChauffeur) {
        double montant = calculerMontant(vehicle, debut, fin, avecChauffeur);
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setVehicle(vehicle);
        reservation.setDateDebut(debut);
        reservation.setDateFin(fin);
        reservation.setAvecChauffeur(avecChauffeur);
        reservation.setStatut("EN_ATTENTE");
        return reservation;
    }

    public void enregistrerReservation(Reservation reservation) throws DAOException {
        reservationDAO.create(reservation);
        notifier.notifyObservers();
    }

    public void validerReservation(Reservation reservation) throws DAOException {
        reservation.setStatut("VALIDÉE");
        reservationDAO.update(reservation);

        double montant = calculerMontant(
                reservation.getVehicle(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                Boolean.TRUE.equals(reservation.getAvecChauffeur())
        );
        reservation.setCoutTotal(montant);
        reservationDAO.update(reservation);
        Facture facture = new Facture();
        facture.setMontant(montant);
        facture.setReservation(reservation);

        Client client = reservation.getClient();
        client.setPointsFidelite(client.getPointsFidelite() + 10);
        clientService.updateClient(client);

        Vehicle vehicle = reservation.getVehicle();
        vehicle.setDisponible(false);
        vehicleDAO.update(vehicle);

        factureDAO.create(facture);
        notifier.notifyObservers();
    }

    public void refuserReservation(Reservation reservation) throws DAOException {
        reservation.setStatut("REJETÉE");
        reservationDAO.update(reservation);
        notifier.notifyObservers();
    }

    public List<Reservation> getReservationsByClient(Client client) {
        return reservationDAO.findByClientId(client.getId());
    }

    public List<Reservation> findByClientId(Long clientId) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT r FROM Reservation r " +
                                    "LEFT JOIN FETCH r.client " +
                                    "LEFT JOIN FETCH r.vehicle " +
                                    "WHERE r.client.id = :clientId", Reservation.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }

    public List<Reservation> getReservationsEnAttente() {
        return reservationDAO.findByStatut("EN_ATTENTE");
    }

    public List<Reservation> getReservationsValidees() {
        return reservationDAO.findByStatut("VALIDÉE");
    }

    public List<Reservation> getReservationsRejetees() {
        return reservationDAO.findByStatut("REJETÉE");
    }

    public List<Vehicle> getAllAvailableVehicles() {
        VehicleDAO dao = new VehicleDAOImpl();
        try {
            return dao.list().stream()
                    .filter(v -> Boolean.TRUE.equals(v.getDisponible()))
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void validerReservationEtRejeterLesConflits(Long reservationId) throws DAOException {
        Reservation reservationValidee = reservationDAO.findById(reservationId);
        if (reservationValidee == null) return;

        reservationValidee.setStatut("Validée");
        reservationDAO.update(reservationValidee);

        List<Reservation> enConflit = reservationDAO.findReservationsEnConflit(
                reservationValidee.getVehicle().getId(),
                reservationValidee.getDateDebut(),
                reservationValidee.getDateFin(),
                reservationValidee.getId()
        );

        for (Reservation r : enConflit) {
            r.setStatut("Rejetée");
            reservationDAO.update(r);
        }
    }
    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public long countActiveReservations() {
        return reservationDAO.countByStatus("VALIDEE");
    }

    public double getMonthlyRevenue() {
        return reservationDAO.calculateMonthlyRevenue();
    }
}
