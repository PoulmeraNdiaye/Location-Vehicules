package org.location.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Reservation;
import org.location.models.User;
import org.location.services.ReservationService;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientReservationsController {
    private static final Logger logger = LoggerFactory.getLogger(ClientReservationsController.class);

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Long> idColumn;
    @FXML private TableColumn<Reservation, String> vehicleColumn;
    @FXML private TableColumn<Reservation, String> startDateColumn;
    @FXML private TableColumn<Reservation, String> endDateColumn;
    @FXML private TableColumn<Reservation, String> chauffeurColumn;
    @FXML private TableColumn<Reservation, String> statutColumn;
    @FXML private TableColumn<Reservation, Double> montantFactureColumn;
    @FXML private Label statusLabel;

    private final ReservationService reservationService = new ReservationService();
    private User currentUser;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicleColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            try {
                return new ReadOnlyStringWrapper(reservation.getVehicle() != null
                        ? reservation.getVehicle().getMarque() + " " + reservation.getVehicle().getModele()
                        : "Véhicule inconnu");
            } catch (Exception e) {
                logger.error("Erreur lors de l'accès au véhicule pour la réservation {}: {}", reservation.getId(), e.getMessage());
                return new ReadOnlyStringWrapper("Erreur véhicule");
            }
        });
        startDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getStartDate() != null ? cellData.getValue().getStartDate().toString() : ""));
        endDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getEndDate() != null ? cellData.getValue().getEndDate().toString() : ""));
        chauffeurColumn.setCellValueFactory(cellData -> {
            Boolean avecChauffeur = cellData.getValue().getAvecChauffeur();
            return new ReadOnlyStringWrapper(avecChauffeur != null && avecChauffeur ? "Oui" : "Non");
        });
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        montantFactureColumn.setCellValueFactory(new PropertyValueFactory<>("montantFacture"));
        montantFactureColumn.setCellFactory(column -> new TableCell<Reservation, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", item));
                }
            }
        });

        idColumn.setPrefWidth(80);
        vehicleColumn.setPrefWidth(200);
        startDateColumn.setPrefWidth(120);
        endDateColumn.setPrefWidth(120);
        chauffeurColumn.setPrefWidth(120);
        statutColumn.setPrefWidth(100);
        montantFactureColumn.setPrefWidth(120);

        reservationTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        logger.info("ClientReservationsController initialisé");
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            logger.info("Utilisateur défini pour ClientReservationsController : {} (Client ID: {})",
                    user.getNom(), user.getClient() != null ? user.getClient().getId() : "null");
            refreshTable();
        } else {
            showError("Aucun utilisateur connecté.");
            logger.warn("Aucun utilisateur fourni pour ClientReservationsController");
            reservationTable.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleBack() {
        try {
            String fxmlPath = "/fxml/main-client.fxml";
            java.net.URL fxmlUrl = MainApplication.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IllegalStateException("Fichier FXML non trouvé : " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load(), 1200, 800);
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            ClientController controller = loader.getController();
            controller.setCurrentUser(SessionManager.getCurrentUser());

            Stage stage = (Stage) reservationTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Espace Client");
            stage.setMaximized(false);
            stage.centerOnScreen();

            logger.info("Retour à l'écran client");
        } catch (Exception e) {
            showError("Erreur lors du retour à l'écran client : " + e.getMessage());
            logger.error("Erreur lors du retour à l'écran client : {}", e.getMessage(), e);
        }
    }

    private void refreshTable() {
        try {
            if (currentUser == null || currentUser.getClient() == null) {
                showError("Compte client non configuré. Contactez l'administrateur.");
                logger.warn("Tentative de rafraîchissement du tableau sans utilisateur ou client défini");
                reservationTable.setItems(FXCollections.observableArrayList());
                return;
            }

            logger.debug("Récupération des réservations pour le client ID: {}", currentUser.getClient().getId());
            ObservableList<Reservation> reservations = FXCollections.observableArrayList(
                    reservationService.getUserReservations(currentUser)
            );
            logger.info("Réservations récupérées : {}", reservations.size());
            for (Reservation r : reservations) {
                logger.debug("Réservation : id={}, vehicle={}, statut={}, avecChauffeur={}, montantFacture={}",
                        r.getId(),
                        r.getVehicle() != null ? r.getVehicle().getMarque() + " " + r.getVehicle().getModele() : "null",
                        r.getStatut(),
                        r.getAvecChauffeur(),
                        r.getMontantFacture());
            }
            reservationTable.setItems(reservations);
            reservationTable.refresh();
            statusLabel.setText("Réservations chargées : " + reservations.size() + " trouvées");
            statusLabel.setStyle("-fx-text-fill: black;");
            statusLabel.setVisible(true);
            logger.info("Tableau des réservations rafraîchi avec {} réservations", reservations.size());
        } catch (Exception e) {
            showError("Erreur lors du chargement des réservations : " + e.getMessage());
            logger.error("Erreur lors du chargement des réservations : {}", e.getMessage(), e);
            reservationTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
        logger.warn("Erreur affichée : {}", message);
    }
}