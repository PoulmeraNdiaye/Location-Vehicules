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
import org.location.models.Client;
import org.location.models.User;
import org.location.models.Vehicle;
import org.location.services.ReservationService;
import org.location.services.VehicleService;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Long> idColumn;
    @FXML private TableColumn<Vehicle, String> marqueColumn;
    @FXML private TableColumn<Vehicle, String> modeleColumn;
    @FXML private TableColumn<Vehicle, Double> tarifColumn;
    @FXML private TableColumn<Vehicle, String> immatriculationColumn;
    @FXML private TableColumn<Vehicle, String> dispoColumn;
    @FXML private TextField marqueField;
    @FXML private TextField modeleField;
    @FXML private TextField prixMaxField;
    @FXML private Label clientNameLabel;
    @FXML private Label pointsLabel;
    @FXML private Label statusLabel;

    private final VehicleService vehicleService = new VehicleService();
    private final ReservationService reservationService = new ReservationService();
    private ObservableList<Vehicle> vehicleList;
    private User currentUser;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        tarifColumn.setCellValueFactory(new PropertyValueFactory<>("tarif"));
        immatriculationColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        dispoColumn.setCellValueFactory(cellData -> {
            boolean dispo = Boolean.TRUE.equals(cellData.getValue().getDisponible());
            return new ReadOnlyStringWrapper(dispo ? "Disponible" : "Indisponible");
        });

        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();

        marqueField.textProperty().addListener((obs, oldText, newText) -> handleSearchVehicles());
        modeleField.textProperty().addListener((obs, oldText, newText) -> handleSearchVehicles());
        prixMaxField.textProperty().addListener((obs, oldText, newText) -> handleSearchVehicles());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;

        if (user != null) {
            clientNameLabel.setText(user.getNom());
            statusLabel.setText("Connecté : " + user.getNom());
            logger.info("Utilisateur connecté : {}", user.getNom());

            if (user instanceof Client client) {
                pointsLabel.setText(client.getPointsFidelite() + " pts");
                logger.info("Points fidélité : {}", client.getPointsFidelite());
            } else {
                pointsLabel.setText("0 pts");
                logger.warn("L'utilisateur n'est pas un client : {}", user.getLogin());
                showError("L'utilisateur connecté n'est pas un client.");
            }

        } else {
            clientNameLabel.setText("Client");
            pointsLabel.setText("0 pts");
            statusLabel.setText("Connecté");
            logger.warn("Aucun utilisateur fourni pour l'initialisation");
            showError("Utilisateur non connecté.");
        }
    }

    @FXML
    private void handleSearchVehicles() {
        String marque = marqueField.getText().trim().toLowerCase();
        String modele = modeleField.getText().trim().toLowerCase();
        String prixMax = prixMaxField.getText().trim();

        try {
            List<Vehicle> filteredVehicles = vehicleList != null ? vehicleList : vehicleService.getAvailableVehicles();

            if (!marque.isEmpty()) {
                filteredVehicles = filteredVehicles.stream()
                        .filter(v -> v.getMarque().toLowerCase().contains(marque))
                        .collect(Collectors.toList());
            }

            if (!modele.isEmpty()) {
                filteredVehicles = filteredVehicles.stream()
                        .filter(v -> v.getModele().toLowerCase().contains(modele))
                        .collect(Collectors.toList());
            }

            if (!prixMax.isEmpty()) {
                try {
                    double maxPrice = Double.parseDouble(prixMax);
                    filteredVehicles = filteredVehicles.stream()
                            .filter(v -> v.getTarif() <= maxPrice)
                            .collect(Collectors.toList());
                } catch (NumberFormatException e) {
                    showError("Le prix maximum doit être un nombre valide.");
                    logger.warn("Format de prix invalide : {}", prixMax);
                    return;
                }
            }

            vehicleTable.setItems(FXCollections.observableArrayList(filteredVehicles));
            statusLabel.setText("Recherche : " + filteredVehicles.size() + " véhicules trouvés");
            logger.info("Recherche effectuée : marque='{}', modele='{}', prixMax='{}', résultats={}",
                    marque, modele, prixMax, filteredVehicles.size());
        } catch (Exception e) {
            showError("Erreur lors de la recherche : " + e.getMessage());
            logger.error("Erreur recherche véhicules : {}", e.getMessage(), e);
        }
    }

    @FXML
    private void handleNewReservation() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showError("Veuillez sélectionner un véhicule à réserver.");
            return;
        }

        if (!selectedVehicle.getDisponible()) {
            showError("Ce véhicule n'est pas disponible.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/reserve-vehicle.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            ReserveVehicleController controller = loader.getController();
            controller.setClient((Client) currentUser);
            controller.setVehicle(selectedVehicle);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Réserver un Véhicule");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(vehicleTable.getScene().getWindow());
            dialogStage.setScene(scene);

            dialogStage.showAndWait();
            refreshTable();
            statusLabel.setText("Réservation effectuée");

        } catch (Exception e) {
            showError("Erreur ouverture fenêtre réservation : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleViewReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/client-reservations.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) vehicleTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réservations");
            stage.setMaximized(false);
            stage.centerOnScreen();

            statusLabel.setText("Affichage des réservations");

        } catch (Exception e) {
            showError("Erreur affichage réservations : " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            SessionManager.setCurrentUser(null);

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) clientNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.setMaximized(false);
            stage.centerOnScreen();

        } catch (Exception e) {
            showError("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void refreshTable() {
        try {
            List<Vehicle> vehiclesList = vehicleService.getAvailableVehicles();
            vehicleList = FXCollections.observableArrayList(vehiclesList);
            vehicleTable.setItems(vehicleList);
            statusLabel.setText("Véhicules disponibles : " + vehicleList.size());
        } catch (Exception e) {
            showError("Erreur chargement véhicules : " + e.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }
}
