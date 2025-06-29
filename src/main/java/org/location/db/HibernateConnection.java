package org.location.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConnection {
    private static final Logger logger = LoggerFactory.getLogger(HibernateConnection.class);
    private static final HibernateConnection instance = new HibernateConnection();
    private final SessionFactory sessionFactory;

    private HibernateConnection() {
        try {
            StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
            sessionFactory = meta.getSessionFactoryBuilder().build();
            logger.info("SessionFactory initialisé avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du SessionFactory", e);
            throw new RuntimeException("Impossible d'initialiser Hibernate", e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HibernateConnection getInstance() {
        return instance;
    }

    public void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory fermé.");
        }
    }
}