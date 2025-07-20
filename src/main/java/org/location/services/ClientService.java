package org.location.services;

import org.location.dao.ClientDAO;
import org.location.dao.IDao;
import org.location.dao.impl.ClientDAOImpl;
import org.location.exception.DAOException;
import org.location.factory.ConcreteFactory;
import org.location.factory.interfaces.ClientFactory;
import org.location.models.Client;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;

import java.util.List;

public class ClientService {
    //private final ClientDAO clientDAO = new ClientDAOImpl();
    private final ClientDAO clientDAO = (ClientDAO) ConcreteFactory
            .getFactory(ClientFactory.class)
            .getClientDao(ClientDAOImpl.class);
    private final DataNotifier notifier = NotifierSingleton.getInstance();



    public void ajouterClient(Client client) throws DAOException {
        clientDAO.create(client);
        if (notifier != null) notifier.notifyObservers();

    }

    public List<Client> getAllClients() throws DAOException {
        return clientDAO.list();
    }

    public Client chercherClientParId(int id) throws DAOException {
        return clientDAO.read(id);
    }

    public Client chercherParEmail(String email) throws DAOException {
        return clientDAO.findByEmail(email);
    }

    public void modifierClient(Client client) throws DAOException {
        clientDAO.update(client);
        if (notifier != null) notifier.notifyObservers();

    }

    public void supprimerClient(int id) throws DAOException {
        clientDAO.delete(id);
        if (notifier != null) notifier.notifyObservers();

    }

    public long countTotalClients() throws DAOException {
        return clientDAO.list().size();
    }

    public void updateClient(Client client) throws DAOException {
        clientDAO.update(client);

    }
}
