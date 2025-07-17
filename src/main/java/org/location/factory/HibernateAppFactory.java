package org.location.factory;

import org.location.dao.IDao;
import org.location.dao.impl.*;
import org.location.factory.interfaces.*;
import org.location.models.*;

public class HibernateAppFactory extends AbstractAppFactory {

    @Override
    public ClientFactory getClientFactory() {
        return new ClientFactory() {
            @Override
            public IDao<Client> getClientDao(Class<? extends IDao<Client>> daoClass) {
                if (daoClass == ClientDAOImpl.class) {
                    return new ClientDAOImpl();
                }
                return null;
            }
        };
    }

    @Override
    public AdminFactory getAdminFactory() {
        /*
        return new AdminFactory() {
            @Override
            public IDao<Admin> getAdminDao(Class<? extends IDao<Admin>> daoClass) {
                if (daoClass == AdminDaoImpl.class) {
                    return new AdminDaoImpl();
                }
                return null;
            }
        };

         */
        return null;
    }



    @Override
    public VehicleFactory getVehicleFactory() {
        return new VehicleFactory() {
            @Override
            public IDao<Vehicle> getVehicleDao(Class<? extends IDao<Vehicle>> daoClass) {
                if (daoClass == VehicleDAOImpl.class) {
                    return new VehicleDAOImpl();
                }
                return null;
            }
        };
    }

    @Override
    public ReservationFactory getReservationFactory() {
        /*
        return new ReservationFactory() {
            @Override
            public IDao<Reservation> getReservationDao(Class<? extends IDao<Reservation>> daoClass) {
                if (daoClass == ReservationDaoImpl.class) {
                    return new ReservationDaoImpl();
                }
                return null;
            }
        };

         */
        return null;
    }



    @Override
    public ChauffeurFactory getChauffeurFactory() {
        return new ChauffeurFactory() {
            @Override
            public IDao<Chauffeur> getChauffeurDao(Class<? extends IDao<Chauffeur>> daoClass) {
                if (daoClass == ChauffeurDAOImpl.class) {
                    return new ChauffeurDAOImpl();
                }
                return null;
            }
        };
    }

}
