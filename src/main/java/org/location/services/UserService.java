package org.location.services;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.location.factory.HibernateFactory;
import org.location.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    /*
    private final UserDAOImpl userDAO = (UserDAOImpl) ConcreteFactory
            .getFactory(UserFactory.class)
            .getUserDao(UserDAOImpl.class);

     */


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


}