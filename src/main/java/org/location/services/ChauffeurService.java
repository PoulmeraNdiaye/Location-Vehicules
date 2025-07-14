package org.location.services;

import org.location.dao.ChauffeurDAO;
import org.location.dao.impl.ChauffeurDAOImpl;
import org.location.exception.DAOException;
import org.location.models.Chauffeur;

import java.util.List;

public class ChauffeurService {
    private final ChauffeurDAO chauffeurDAO = new ChauffeurDAOImpl();

    public void ajouterChauffeur(Chauffeur chauffeur) throws DAOException {
        chauffeurDAO.create(chauffeur);
    }

    public List<Chauffeur> getAllChauffeurs() throws DAOException {
        return chauffeurDAO.list();
    }

    public Chauffeur chercherParId(int id) throws DAOException {
        return chauffeurDAO.read(id);
    }

    public void modifierChauffeur(Chauffeur chauffeur) throws DAOException {
        chauffeurDAO.update(chauffeur);
    }

    public void supprimerChauffeur(int id) throws DAOException {
        chauffeurDAO.delete(id);
    }
}
