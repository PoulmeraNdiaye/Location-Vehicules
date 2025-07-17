package org.location.factory;
import org.location.factory.interfaces.*;

public class ConcreteFactory {
    private static final AbstractAppFactory appFactory = new HibernateAppFactory();

    public static <T> T getFactory(Class<T> factoryClass) {

        if (factoryClass == ClientFactory.class) {
            return factoryClass.cast(appFactory.getClientFactory());
        } else if (factoryClass == AdminFactory.class) {
            return factoryClass.cast(appFactory.getAdminFactory());
        } else if (factoryClass == ChauffeurFactory.class) {
            return factoryClass.cast(appFactory.getChauffeurFactory());
        } else if (factoryClass == VehicleFactory.class) {
            return factoryClass.cast(appFactory.getVehicleFactory());
        } else if (factoryClass == ReservationFactory.class) {
            return factoryClass.cast(appFactory.getReservationFactory());
        }

        return null;
    }
}
