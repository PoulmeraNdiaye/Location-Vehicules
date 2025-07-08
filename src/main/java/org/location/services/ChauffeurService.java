package org.location.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.location.factory.HibernateFactory;
import org.location.models.Chauffeur;
import org.location.factory.ChauffeurFactory;

import java.util.List;

public class ChauffeurService {

    public void insertChauffeur(String nom, boolean dispo) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            ChauffeurFactory factory = new ChauffeurFactory();
            Chauffeur chauffeur = factory.createChauffeur(nom, dispo);
            session.save(chauffeur);
            tx.commit();
        }
    }

    public void updateChauffeur(Chauffeur chauffeur) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(chauffeur);
            tx.commit();
        }
    }

    public void deleteChauffeur(Chauffeur chauffeur) {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(chauffeur);
            tx.commit();
        }
    }

    public List<Chauffeur> getAllChauffeurs() {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Chauffeur", Chauffeur.class).getResultList();
        }
    }
}
