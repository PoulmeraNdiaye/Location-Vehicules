package org.location.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.location.dao.ClientDAO;
import org.location.exception.DAOException;
import org.location.factory.HibernateFactory;
import org.location.models.Client;

public class ClientDAOImpl extends ObjectDAOImpl<Client> implements ClientDAO {

    @Override
    public Client findByEmail(String email) throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<Client> query = session.createQuery("from Client where email = :email", Client.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la recherche par email", e);
        }
    }
}
