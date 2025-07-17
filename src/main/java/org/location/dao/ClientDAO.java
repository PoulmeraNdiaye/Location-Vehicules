package org.location.dao;

import org.location.models.Client;
import org.location.exception.DAOException;
import java.util.List;

public interface ClientDAO extends IDao<Client> {
    Client findByEmail(String email) throws DAOException;
}
