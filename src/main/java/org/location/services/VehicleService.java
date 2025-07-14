package org.location.services;

import org.location.dao.VehicleDAO;
import org.location.dao.impl.VehicleDAOImpl;
import org.location.exception.DAOException;
import org.location.models.Vehicle;

import java.util.List;

public class VehicleService {
    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    public void ajouterVehicle(Vehicle vehicle) throws DAOException {
        vehicleDAO.create(vehicle);
    }

    public List<Vehicle> getAllVehicles() throws DAOException {
        return vehicleDAO.list();
    }

    public Vehicle chercherParId(int id) throws DAOException {
        return vehicleDAO.read(id);
    }

    public void modifierVehicle(Vehicle vehicle) throws DAOException {
        vehicleDAO.update(vehicle);
    }

    public void supprimerVehicle(int id) throws DAOException {
        vehicleDAO.delete(id);
    }

    public List<Vehicle> getAvailableVehicles() throws DAOException {
        List<Vehicle> all = vehicleDAO.list();
        return all.stream().filter(Vehicle::getDisponible).toList();
    }
    public long countAvailableVehicles() throws DAOException {
        return getAvailableVehicles().size();
    }

}
