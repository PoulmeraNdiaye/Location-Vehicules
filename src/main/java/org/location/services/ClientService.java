package org.location.services;

import org.location.dao.ClientDAO;
import org.location.dao.impl.ClientDAOImpl;
import org.location.exception.DAOException;
import org.location.models.Client;

import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAOImpl();

    public void ajouterClient(Client client) throws DAOException {
        clientDAO.create(client);
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
    }

    public void supprimerClient(int id) throws DAOException {
        clientDAO.delete(id);
    }

    public long countTotalClients() throws DAOException {
        return clientDAO.list().size();
    }
}
