package org.location.factory;

import org.location.models.Chauffeur;

public class ChauffeurFactory {
    public Chauffeur createChauffeur(String nom, boolean dispo) {
        return new Chauffeur(nom, dispo);
    }
}
