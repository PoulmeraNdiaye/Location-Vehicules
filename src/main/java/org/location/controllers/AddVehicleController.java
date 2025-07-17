package org.location.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.location.models.Vehicle;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;
import org.location.services.VehicleService;

public class AddVehicleController {

    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField tarifField;
    @FXML private TextField immatriculationField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private VehicleService vehicleService;
    private Vehicle vehicleToEdit = null;
    private final DataNotifier notifier = NotifierSingleton.getInstance();

    public void initialize() {
        vehicleService = new VehicleService();
    }

    @FXML
    private void handleSave() {
        try {
            String marque = marqueField.getText().trim();
            String modele = modeleField.getText().trim();
            String tarifText = tarifField.getText().trim();
            String immatriculation = immatriculationField.getText().trim();

            if (marque.isEmpty() || modele.isEmpty() || tarifText.isEmpty() || immatriculation.isEmpty()) {
                showError("Tous les champs sont obligatoires.");
                return;
            }

            double tarif;
            try {
                tarif = Double.parseDouble(tarifText);
                if (tarif <= 0) {
                    showError("Le tarif doit être un nombre positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Le tarif doit être un nombre valide.");
                return;
            }

            if (vehicleToEdit != null) {
                vehicleToEdit.setMarque(marque);
                vehicleToEdit.setModele(modele);
                vehicleToEdit.setTarif(tarif);
                vehicleToEdit.setImmatriculation(immatriculation);
                vehicleService.modifierVehicle(vehicleToEdit);
            } else {
                Vehicle vehicle = new Vehicle();
                vehicle.setMarque(marque);
                vehicle.setModele(modele);
                vehicle.setTarif(tarif);
                vehicle.setImmatriculation(immatriculation);
                vehicle.setDisponible(true);

                vehicleService.ajouterVehicle(vehicle);
            }
            if (notifier != null) {
                notifier.notifyObservers();
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showError("Erreur lors de l'enregistrement du véhicule : " + e.getMessage());
        }
    }


    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setVehicleToEdit(Vehicle vehicle) {
        this.vehicleToEdit = vehicle;

        marqueField.setText(vehicle.getMarque());
        modeleField.setText(vehicle.getModele());
        tarifField.setText(String.valueOf(vehicle.getTarif()));
        immatriculationField.setText(vehicle.getImmatriculation());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
