package org.location.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Client;
import org.location.services.ClientService;

public class ClientManagementController {

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Long> idColumn;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, Integer> pointsFideliteColumn;

    private ClientService clientService;

    public void initialize() {
        clientService = new ClientService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        pointsFideliteColumn.setCellValueFactory(new PropertyValueFactory<>("pointsFidelite"));

        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    @FXML
    private void refreshTable() {
        try {
            ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
            clientTable.setItems(clients);
        } catch (Exception e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddClient() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/register-client.fxml")
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Client");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(clientTable.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            refreshTable();

        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire d'ajout de client : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
    }
}