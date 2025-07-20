package org.location.dao.impl;

import org.hibernate.Session;
import org.location.dao.ReservationDAO;
import org.location.factory.HibernateFactory;
import org.location.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.location.factory.HibernateFactory.getSessionFactory;


public class ReservationDAOImpl extends ObjectDAOImpl<Reservation> implements ReservationDAO {

/*    @Override
    public List<Reservation> findByClientId(Long clientId) {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Reservation r JOIN FETCH r.vehicle WHERE r.client.id = :clientId", Reservation.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }

 */

    @Override
    public List<Reservation> findByStatut(String statut) {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Reservation r JOIN FETCH r.vehicle WHERE r.statut = :statut", Reservation.class)
                    .setParameter("statut", statut)
                    .list();
        }
    }

    public List<Reservation> findReservationsEnConflit(Long vehicleId, LocalDate debut, LocalDate fin, Long exclureId) {
        try (Session session = getSessionFactory().openSession()) {
            String hql = "FROM Reservation r WHERE r.vehicle.id = :vehicleId " +
                    "AND r.id <> :exclureId " +
                    "AND r.statut = 'En attente' " +
                    "AND r.dateDebut <= :fin AND r.dateFin >= :debut";
            return session.createQuery(hql, Reservation.class)
                    .setParameter("vehicleId", vehicleId)
                    .setParameter("exclureId", exclureId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }
    @Override
    public Reservation findById(Long id) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reservation> findReservationsEnConflit(int vehicleId, LocalDate dateDebut, LocalDate dateFin, Long exclureId) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            String hql = "FROM Reservation r WHERE r.vehicle.id = :vehicleId " +
                    "AND r.id != :exclureId " +
                    "AND r.dateDebut <= :dateFin AND r.dateFin >= :dateDebut " +
                    "AND r.statut = 'EN_ATTENTE'";

            return session.createQuery(hql, Reservation.class)
                    .setParameter("vehicleId", vehicleId)
                    .setParameter("exclureId", exclureId)
                    .setParameter("dateDebut", dateDebut)
                    .setParameter("dateFin", dateFin)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Reservation> findAll() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT r FROM Reservation r " +
                            "JOIN FETCH r.client " +
                            "JOIN FETCH r.vehicle", Reservation.class
            ).getResultList();
        }
    }
    @Override
    public List<Reservation> findByClientId(Long clientId) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT r FROM Reservation r " +
                                    "LEFT JOIN FETCH r.vehicle " +
                                    "LEFT JOIN FETCH r.client " +
                                    "WHERE r.client.id = :clientId", Reservation.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }

}
