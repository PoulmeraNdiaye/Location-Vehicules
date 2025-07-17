package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Admin;

public interface AdminFactory {
    IDao<Admin> getAdminDao(Class<? extends IDao<Admin>> daoAdmin);
}