package org.location.dao;

import org.location.exception.DAOException;
import java.util.List;

public interface IDao<T> {
    void create(T entity) throws DAOException;
    T read(int id) throws DAOException;
    List<T> list() throws DAOException;
    void update(T entity) throws DAOException;
    void delete(int id) throws DAOException;
}
