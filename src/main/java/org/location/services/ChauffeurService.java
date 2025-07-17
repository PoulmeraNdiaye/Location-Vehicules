package org.location.services;

import org.location.dao.ChauffeurDAO;
import org.location.dao.impl.ChauffeurDAOImpl;
import org.location.exception.DAOException;
import org.location.factory.ConcreteFactory;
import org.location.factory.interfaces.ChauffeurFactory;
import org.location.models.Chauffeur;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;

import java.util.List;

public class ChauffeurService {
    //private final ChauffeurDAO chauffeurDAO = new ChauffeurDAOImpl();
    private final ChauffeurDAOImpl chauffeurDAO = (ChauffeurDAOImpl) ConcreteFactory
            .getFactory(ChauffeurFactory.class)
            .getChauffeurDao(ChauffeurDAOImpl.class);

    private final DataNotifier notifier = NotifierSingleton.getInstance();



    public void ajouterChauffeur(Chauffeur chauffeur) throws DAOException {
        chauffeurDAO.create(chauffeur);
        if (notifier != null) notifier.notifyObservers();

    }

    public List<Chauffeur> getAllChauffeurs() throws DAOException {
        return chauffeurDAO.list();
    }

    public Chauffeur chercherParId(int id) throws DAOException {
        return chauffeurDAO.read(id);
    }

    public void modifierChauffeur(Chauffeur chauffeur) throws DAOException {
        chauffeurDAO.update(chauffeur);
        if (notifier != null) notifier.notifyObservers();

    }

    public void supprimerChauffeur(int id) throws DAOException {
        chauffeurDAO.delete(id);
        if (notifier != null) notifier.notifyObservers();

    }
}
