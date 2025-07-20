package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Facture;

public interface FactureFactory {
    IDao<Facture> getFactureDao(Class<? extends IDao<Facture>> daoClass);
}
