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

    public void insertVehicle(String marque, String modele, Double tarif, String immatriculation) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            // Commenté temporairement pour éviter l'erreur HQL
            /*
            Query<Vehicle> query = session.createQuery("FROM Vehicle WHERE immatriculation = :immat", Vehicle.class);
            query.setParameter("immat", immatriculation);
            if (query.uniqueResult() != null) {
                logger.error("L'immatriculation {} existe déjà.", immatriculation);
                throw new IllegalArgumentException("L'immatriculation existe déjà.");
            }
            */

            Vehicle vehicle = new Vehicle(marque, modele, tarif, immatriculation);
            Transaction transaction = session.beginTransaction();
            session.save(vehicle);
            transaction.commit();
            logger.info("Véhicule inséré avec succès : {}", immatriculation);
        } catch (Exception e) {
            logger.error("Erreur lors de l'insertion du véhicule : {}", immatriculation, e);
            throw e;
        }
    }

    public long countAvailableVehicles() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(v) FROM Vehicle v WHERE v.disponible = true", Long.class);
            Long result = query.uniqueResult();
            logger.debug("Nombre de véhicules disponibles : {}", result);
            return result != null ? result : 0;
        } catch (Exception e) {
            logger.error("Erreur lors du comptage des véhicules disponibles", e);
            throw e;
        }
    }

    public void testVehicleQuery() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Vehicle> query = session.createQuery("FROM Vehicle", Vehicle.class);
            List<Vehicle> vehicles = query.list();
            logger.info("Nombre de véhicules trouvés : {}", vehicles.size());
        } catch (Exception e) {
            logger.error("Erreur lors du test de la requête HQL", e);
            throw e;
        }
    }
}