package org.location.services;

import org.location.dao.VehicleDAO;
import org.location.dao.impl.VehicleDAOImpl;
import org.location.exception.DAOException;
import org.location.factory.ConcreteFactory;
import org.location.factory.interfaces.VehicleFactory;
import org.location.models.Vehicle;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;

import java.util.List;

public class VehicleService {
   // private final VehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final VehicleDAOImpl vehicleDAO = (VehicleDAOImpl) ConcreteFactory
            .getFactory(VehicleFactory.class)
            .getVehicleDao(VehicleDAOImpl.class);
    private final DataNotifier notifier = NotifierSingleton.getInstance();



    public void ajouterVehicle(Vehicle vehicle) throws DAOException {
        vehicleDAO.create(vehicle);
        if (notifier != null) notifier.notifyObservers();

    }

    public List<Vehicle> getAllVehicles() throws DAOException {
        return vehicleDAO.list();
    }

    public Vehicle chercherParId(int id) throws DAOException {
        return vehicleDAO.read(id);
    }

    public void modifierVehicle(Vehicle vehicle) throws DAOException {
        vehicleDAO.update(vehicle);
        if (notifier != null) notifier.notifyObservers();

    }

    public void supprimerVehicle(int id) throws DAOException {
        vehicleDAO.delete(id);
        if (notifier != null) notifier.notifyObservers();

    }

    public List<Vehicle> getAvailableVehicles() throws DAOException {
        List<Vehicle> all = vehicleDAO.list();
        return all.stream().filter(Vehicle::getDisponible).toList();
    }
    public long countAvailableVehicles() throws DAOException {
        return getAvailableVehicles().size();
    }

}
