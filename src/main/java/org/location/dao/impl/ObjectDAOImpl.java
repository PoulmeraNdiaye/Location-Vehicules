package org.location.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.location.dao.IDao;
import org.location.exception.DAOException;
import org.location.factory.HibernateFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class ObjectDAOImpl<T> implements IDao<T> {

    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public ObjectDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void create(T entity) throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            //session.save(entity);
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la création de l'entité", e);
        }
    }

    @Override
    public T read(int id) throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la lecture de l'entité", e);
        }
    }

    @Override
    public List<T> list() throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("from " + entityClass.getSimpleName(), entityClass);
            return query.list();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la récupération de la liste", e);
        }
    }

    @Override
    public void update(T entity) throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la mise à jour de l'entité", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        try (Session session = HibernateFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
            }
            tx.commit();
        } catch (Exception e) {
            throw new DAOException("Erreur lors de la suppression de l'entité", e);
        }
    }
}
