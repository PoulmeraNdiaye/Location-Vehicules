package org.location.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.models.Vehicle;
import org.location.services.ReservationService;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReserveVehicleController {
    private static final Logger logger = LoggerFactory.getLogger(ReserveVehicleController.class);
    private static final double CHAUFFEUR_FEE = 1500.0;

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private CheckBox chauffeurCheckBox;
    @FXML private Label errorLabel;
    @FXML private Label vehicleLabel;

    private final ReservationService reservationService = new ReservationService();
    private Vehicle selectedVehicle;

    public void setVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
        if (vehicle != null) {
            vehicleLabel.setText(vehicle.getMarque() + " " + vehicle.getModele());
            logger.info("Véhicule sélectionné pour la réservation : {} {}", vehicle.getMarque(), vehicle.getModele());
            errorLabel.setVisible(false);
        } else {
            vehicleLabel.setText("Aucun véhicule sélectionné");
            logger.warn("Aucun véhicule fourni pour la réservation");
            showError("Veuillez sélectionner un véhicule avant de réserver.");
        }
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        if (selectedVehicle == null) {
            vehicleLabel.setText("Aucun véhicule sélectionné");
            logger.debug("Initialisation sans véhicule sélectionné");
            showError("Veuillez sélectionner un véhicule avant de réserver.");
        } else {
            vehicleLabel.setText(selectedVehicle.getMarque() + " " + selectedVehicle.getModele());
        }
    }

    @FXML
    private void handleConfirmReservation() {
        if (selectedVehicle == null) {
            showError("Aucun véhicule sélectionné pour la réservation.");
            logger.error("Tentative de réservation sans véhicule sélectionné");
            return;
        }

        if (SessionManager.getCurrentUser() == null || SessionManager.getCurrentUser().getClient() == null) {
            showError("Compte client non configuré. Contactez l'administrateur.");
            logger.warn("Tentative de réservation sans client associé");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        boolean avecChauffeur = chauffeurCheckBox.isSelected();

        if (startDate == null || endDate == null) {
            showError("Veuillez sélectionner une date de début et une date de fin.");
            logger.warn("Tentative de réservation avec dates non sélectionnées");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
            showError("La date de début ne peut pas être antérieure à aujourd'hui.");
            logger.warn("Tentative de réservation avec date de début dans le passé : {}", startDate);
            return;
        }
        if (startDate.isAfter(endDate) || startDate.equals(endDate)) {
            showError("La date de début doit être antérieure à la date de fin et la réservation doit durer au moins une journée.");
            logger.warn("Dates invalides : startDate={}, endDate={}", startDate, endDate);
            return;
        }

        try {
            if (!reservationService.isVehicleAvailable(selectedVehicle, startDate, endDate)) {
                showError("Ce véhicule n'est pas disponible pour les dates sélectionnées.");
                logger.warn("Véhicule {} indisponible pour les dates {} à {}",
                        selectedVehicle.getId(), startDate, endDate);
                return;
            }

            long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
            double montantFacture = selectedVehicle.getTarif() * numberOfDays;
            if (avecChauffeur) {
                montantFacture += CHAUFFEUR_FEE;
            }

            reservationService.createReservation(SessionManager.getCurrentUser(),
                    selectedVehicle, startDate, endDate, avecChauffeur);

            String vehicleName = selectedVehicle.getMarque() + " " + selectedVehicle.getModele();
            String userName = SessionManager.getCurrentUser().getNom();
            String message = String.format(
                    "La réservation du véhicule %s par %s pour une durée de %d jour(s) s'élève à %.2f €%s et a bien été enregistrée avec succès.",
                    vehicleName, userName, numberOfDays, montantFacture,
                    avecChauffeur ? " (inclut 1500 € pour le chauffeur)" : "");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation de Réservation");
            alert.setHeaderText("Réservation Enregistrée");
            alert.setContentText(message);
            alert.showAndWait();

            logger.info("Réservation confirmée pour le véhicule {} du {} au {} (avec chauffeur : {}, montant : {})",
                    selectedVehicle.getId(), startDate, endDate, avecChauffeur, montantFacture);

            Stage stage = (Stage) startDatePicker.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showError("Erreur lors de la réservation : " + e.getMessage());
            logger.error("Erreur lors de la confirmation de la réservation : {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleCancel() {
        logger.info("Annulation de la réservation, fermeture de la fenêtre");
        Stage stage = (Stage) startDatePicker.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
        logger.warn("Erreur affichée : {}", message);
    }
}