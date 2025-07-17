package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Chauffeur;

public interface ChauffeurFactory {
    IDao<Chauffeur> getChauffeurDao(Class<? extends IDao<Chauffeur>> daoChauffeur);
}