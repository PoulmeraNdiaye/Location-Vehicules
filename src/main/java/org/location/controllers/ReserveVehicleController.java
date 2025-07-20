package org.location.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.exception.DAOException;
import org.location.models.Client;
import org.location.models.Reservation;
import org.location.models.Vehicle;
import org.location.services.ReservationService;

import java.time.LocalDate;

public class ReserveVehicleController {

    @FXML private Label vehicleLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox chauffeurCheckBox;
    @FXML private Label errorLabel;
    @FXML private Label estimationLabel;
    private Reservation reservationEstimee;
    private Vehicle selectedVehicle;
    private Client client;
    private boolean estimationAffichee = false;

    private final ReservationService reservationService = new ReservationService();

    public void setVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
        if (vehicleLabel != null && vehicle != null) {
            vehicleLabel.setText(vehicle.getMarque() + " " + vehicle.getModele());
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }



    @FXML
    private void handleEstimate() {
        LocalDate debut = startDatePicker.getValue();
        LocalDate fin = endDatePicker.getValue();
        boolean avecChauffeur = chauffeurCheckBox.isSelected();

        if (selectedVehicle == null || debut == null || fin == null || debut.isAfter(fin)) {
            errorLabel.setText("Veuillez remplir correctement tous les champs.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            reservationEstimee = reservationService.preparerReservation(client, selectedVehicle, debut, fin, avecChauffeur);
            estimationLabel.setText("Estimation : " + reservationEstimee.getCoutTotal() + " €");
            estimationLabel.setVisible(true);
        } catch (Exception e) {
            estimationLabel.setText("Erreur : " + e.getMessage());
        }
    }


    @FXML
    private void handleConfirmReservation() {
        LocalDate debut = startDatePicker.getValue();
        LocalDate fin = endDatePicker.getValue();
        boolean avecChauffeur = chauffeurCheckBox.isSelected();

        if (selectedVehicle == null || debut == null || fin == null || debut.isAfter(fin)) {
            errorLabel.setText("Veuillez remplir correctement tous les champs.");
            errorLabel.setVisible(true);
            return;
        }

        if (!estimationAffichee) {
            double estimation = reservationService.calculerMontant(selectedVehicle, debut, fin, avecChauffeur);
            estimationLabel.setText("Estimation : " + estimation + " €");
            estimationAffichee = true;
            return;
        }

        try {
            Reservation reservation = reservationService.preparerReservation(client, selectedVehicle, debut, fin, avecChauffeur);
            reservation.setCoutTotal(reservationService.calculerMontant(selectedVehicle, debut, fin, avecChauffeur));
            reservationService.enregistrerReservation(reservation);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Réservation enregistrée !");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText("Erreur : " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }


    @FXML
    private void handleCancel() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }


}
