package org.location.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.location.factory.HibernateFactory;
import org.location.models.Client;
import org.location.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User authenticate(String login, String motDePasse) {
        logger.debug("Tentative d'authentification pour le login : {}", login);
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);
            User user = query.uniqueResult();
            if (user != null && motDePasse.equals(user.getMotDePasse())) {
                logger.info("Authentification réussie pour : {}", login);
                return user;
            } else {
                logger.warn("Échec de l'authentification pour : {}", login);
                return null;
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification pour le login : {}", login, e);
            throw e;
        }
    }

    public void insertUser(String nom, String login, String motDePasse, User.Role role) {
        User user = new User(nom, login, motDePasse, role);
        Transaction transaction = null;
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info("Utilisateur inséré avec succès : {}", login);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Erreur lors de l'insertion de l'utilisateur : {}", login, e);
            throw e;
        }
    }

    public void insertClient(String nom, String email) {
        Client client = new Client(nom, email);
        Transaction transaction = null;
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(client);
            transaction.commit();
            logger.info("Client inséré avec succès : {}", email);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Erreur lors de l'insertion du client : {}", email, e);
            throw e;
        }
    }


}