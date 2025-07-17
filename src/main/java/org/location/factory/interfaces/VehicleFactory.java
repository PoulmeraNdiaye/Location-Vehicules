package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Vehicle;

public interface VehicleFactory {
    IDao<Vehicle> getVehicleDao(Class<? extends IDao<Vehicle>> daoVehicle);
}