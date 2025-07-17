package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Client;

public interface ClientFactory {
    IDao<Client> getClientDao(Class<? extends IDao<Client>> daoClass);
}
