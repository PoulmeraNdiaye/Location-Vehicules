package org.location.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Chauffeur;
import org.location.services.ChauffeurService;

import java.util.List;

public class DriverManagementController {

    @FXML private TableView<Chauffeur> driverTable;
    @FXML private TableColumn<Chauffeur, Long> idColumn;
    @FXML private TableColumn<Chauffeur, String> nomColumn;
    @FXML private TableColumn<Chauffeur, String> dispoColumn;

    private final ChauffeurService chauffeurService = new ChauffeurService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        dispoColumn.setCellValueFactory(cellData -> {
            Boolean dispo = cellData.getValue().getDispo();
            return new ReadOnlyStringWrapper((dispo != null && dispo) ? "Disponible" : "Indisponible");
        });

        driverTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    @FXML
    private void refreshTable() {
        List<Chauffeur> chauffeurs = chauffeurService.getAllChauffeurs();
        ObservableList<Chauffeur> data = FXCollections.observableArrayList(chauffeurs);
        driverTable.setItems(data);
    }

    @FXML
    private void handleAddDriver() {
        openDriverForm(null);
    }

    @FXML
    private void handleEditDriver() {
        Chauffeur selected = driverTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDriverForm(selected);
        } else {
            showAlert("Veuillez sélectionner un chauffeur à modifier.");
        }
    }

    @FXML
    private void handleDeleteDriver() {
        Chauffeur selected = driverTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer le chauffeur sélectionné ?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    chauffeurService.deleteChauffeur(selected);
                    refreshTable();
                }
            });
        } else {
            showAlert("Veuillez sélectionner un chauffeur à supprimer.");
        }
    }

    private void openDriverForm(Chauffeur chauffeur) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/add-chauffeur.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle(chauffeur == null ? "Ajouter un Chauffeur" : "Modifier le Chauffeur");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            AddDriverController controller = loader.getController();
            if (chauffeur != null) {
                controller.setChauffeurToEdit(chauffeur);
            }

            dialogStage.showAndWait();
            refreshTable();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur : " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
