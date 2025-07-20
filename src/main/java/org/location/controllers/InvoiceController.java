package org.location.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.location.models.Reservation;

public class InvoiceController {

    @FXML private Label clientLabel;
    @FXML private Label vehiculeLabel;
    @FXML private Label datesLabel;
    @FXML private Label chauffeurLabel;
    @FXML private Label coutLabel;

    public void setReservation(Reservation reservation) {
        clientLabel.setText("Client : " + reservation.getClient().getNom());
        vehiculeLabel.setText("Véhicule : " + reservation.getVehicle().getMarque() + " " + reservation.getVehicle().getModele());
        datesLabel.setText("Période : " + reservation.getDateDebut() + " → " + reservation.getDateFin());
        chauffeurLabel.setText("Chauffeur : " + (reservation.getAvecChauffeur() ? "Oui" : "Non"));
        coutLabel.setText("Total : " + reservation.getCoutTotal() + " €");
    }
}
