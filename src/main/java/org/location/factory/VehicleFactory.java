package org.location.factory;

import org.location.models.Vehicle;

public class VehicleFactory {
    public Vehicle createVehicle(String marque, String modele, double tarif, String immatriculation) {
        return new Vehicle(marque, modele, tarif, immatriculation);
    }
}
