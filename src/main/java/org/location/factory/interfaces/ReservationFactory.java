package org.location.factory.interfaces;

import org.location.dao.IDao;
import org.location.models.Reservation;

public interface ReservationFactory {
    IDao<Reservation> getReservationDao(Class<? extends IDao<Reservation>> daoReservation);
}