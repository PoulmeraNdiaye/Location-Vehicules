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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ClientManagementController.class);

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Long> idColumn;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, Integer> pointsFideliteColumn;

    private ClientService clientService;

    @FXML
    public void initialize() {
        clientService = new ClientService();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        pointsFideliteColumn.setCellValueFactory(new PropertyValueFactory<>("pointsFidelite"));

        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
        logger.info("ClientManagementController initialisé");
    }

    @FXML
    private void refreshTable() {
        try {
            ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
            clientTable.setItems(clients);
            logger.info("Tableau des clients rafraîchi avec {} clients", clients.size());
        } catch (Exception e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
            logger.error("Erreur lors du chargement des clients : {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleAddClient() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/register-client.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Client");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(clientTable.getScene().getWindow());
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
            refreshTable();
            logger.info("Fenêtre d'ajout de client ouverte et tableau rafraîchi");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire d'ajout de client : " + e.getMessage());
            logger.error("Erreur lors de l'ouverture du formulaire d'ajout de client : {}", e.getMessage(), e);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
        logger.warn("Erreur affichée : {}", message);
    }
}