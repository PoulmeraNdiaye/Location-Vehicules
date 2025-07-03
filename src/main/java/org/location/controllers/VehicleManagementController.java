package org.location.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Vehicle;
import org.location.services.VehicleService;
import org.location.utils.SceneManager;

import java.util.List;

public class VehicleManagementController {

    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Long> idColumn;
    @FXML private TableColumn<Vehicle, String> marqueColumn;
    @FXML private TableColumn<Vehicle, String> modeleColumn;
    @FXML private TableColumn<Vehicle, Double> tarifColumn;
    @FXML private TableColumn<Vehicle, String> immatriculationColumn;
    //@FXML private TableColumn<Vehicle, Boolean> disponibleColumn;
    @FXML private TableColumn<Vehicle, String> disponibleColumn;

    private final VehicleService vehicleService = new VehicleService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        tarifColumn.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        immatriculationColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        //disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        disponibleColumn.setCellValueFactory(cellData -> {
            boolean dispo = Boolean.TRUE.equals(cellData.getValue().getDisponible());
            return new ReadOnlyStringWrapper(dispo ? "Disponible" : "Indisponible");
        });

        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();
    }

    @FXML
    private void refreshTable() {
        try {
            List<Vehicle> vehiclesList = vehicleService.getAvailableVehicles();
            System.out.println("Véhicules récupérés : ");
            vehiclesList.forEach(System.out::println);
            ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehiclesList);
            vehicleTable.setItems(vehicles);
        } catch (Exception e) {
            showError("Erreur lors du chargement des véhicules : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddVehicle() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/add-vehicle.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Véhicule");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(vehicleTable.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            refreshTable();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onEditVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SceneManager.openEditVehicleWindow(selected);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun véhicule sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un véhicule à modifier.");
            alert.showAndWait();
        }
    }
    @FXML
    private void onDeleteVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Suppression d'un véhicule");
            confirm.setContentText("Voulez-vous vraiment supprimer ce véhicule ?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        vehicleService.deleteVehicle(selected);
                        refreshTable();
                    } catch (Exception e) {
                        showError("Erreur lors de la suppression : " + e.getMessage());
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun véhicule sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un véhicule à supprimer.");
            alert.showAndWait();
        }
    }

}