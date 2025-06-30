package org.location.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.location.factory.HibernateFactory;
import org.location.models.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VehicleService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    public void insertVehicle(String marque, String modele, double tarif, String immatriculation) {
        if (marque == null || marque.trim().isEmpty()) {
            throw new IllegalArgumentException("La marque est obligatoire.");
        }
        if (modele == null || modele.trim().isEmpty()) {
            throw new IllegalArgumentException("Le modèle est obligatoire.");
        }
        if (tarif <= 0) {
            throw new IllegalArgumentException("Le tarif doit être supérieur à 0.");
        }
        if (immatriculation == null || immatriculation.trim().isEmpty()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire.");
        }

        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.info("Insertion du véhicule: {}, {}, {}, {}", marque, modele, tarif, immatriculation);
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = new Vehicle(marque.trim(), modele.trim(), tarif, immatriculation.trim());
            session.save(vehicle);
            tx.commit();
            logger.info("Véhicule inséré avec succès. ID = {}", vehicle.getId());
        } catch (Exception e) {
            logger.error("Erreur lors de l'insertion du véhicule", e);
            throw new RuntimeException("Échec d'insertion du véhicule", e);
        }
    }

    public long countAvailableVehicles() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(v) FROM Vehicle v", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Erreur lors du comptage", e);
            throw new RuntimeException("Échec du comptage", e);
        }
    }

    public List<Vehicle> getAllVehicles() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Vehicle> query = session.createQuery("FROM Vehicle", Vehicle.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des véhicules", e);
            throw new RuntimeException("Échec de récupération", e);
        }
    }

    public List<Vehicle> getAvailableVehicles() {
        return getAllVehicles();
    }
}
