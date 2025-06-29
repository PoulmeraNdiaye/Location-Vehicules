package org.location.factory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateFactory {
    private static final Logger logger = LoggerFactory.getLogger(HibernateFactory.class);
    private static SessionFactory sessionFactory;

    private HibernateFactory() {}

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
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
        return sessionFactory;
    }

    public static synchronized void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("SessionFactory fermé.");
        }
    }
}
