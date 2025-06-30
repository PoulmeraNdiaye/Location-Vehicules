package org.location.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.location.factory.HibernateFactory;
import org.location.models.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);


    public void insertClient(String nom, String email) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide ou nul.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide ou nul.");
        }

        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.info("Tentative d'insertion d'un client : nom={}, email={}", nom, email);
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Client client = new Client(nom.trim(), email.trim());
                session.save(client);
                tx.commit();
                logger.info("Client inséré avec succès : id={}", client.getId());
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                    logger.error("Erreur lors de l'insertion du client : {}", e.getMessage(), e);
                }
                throw new RuntimeException("Erreur lors de l'insertion du client", e);
            }
        }
    }


    public long countTotalClients() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.info("Exécution de la requête HQL pour compter les clients");
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Client", Long.class);
            Long result = query.getSingleResult();
            logger.debug("Nombre total de clients : {}", result);
            return result != null ? result : 0;
        } catch (Exception e) {
            logger.error("Erreur lors du comptage des clients : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors du comptage des clients", e);
        }
    }


    public List<Client> getAllClients() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            logger.info("Exécution de la requête HQL pour récupérer tous les clients");
            Query<Client> query = session.createQuery("FROM Client", Client.class);
            List<Client> clients = query.getResultList();
            logger.info("Nombre de clients récupérés : {}", clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des clients : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des clients", e);
        }
    }


    public void testClientQuery() {
        try {
            List<Client> clients = getAllClients();
            logger.info("Test de requête HQL réussi, {} clients trouvés.", clients.size());
        } catch (Exception e) {
            logger.error("Erreur lors du test de la requête HQL : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors du test de la requête HQL", e);
        }
    }
}