package org.location.factory;
import org.location.factory.interfaces.*;

public abstract class AbstractAppFactory {
    public abstract ClientFactory getClientFactory();
    public abstract AdminFactory getAdminFactory();
    public abstract ChauffeurFactory getChauffeurFactory();
    public abstract VehicleFactory getVehicleFactory();
    public abstract ReservationFactory getReservationFactory();

    public abstract FactureFactory getFactureFactory();
}
