package org.location.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Client;
import org.location.models.Reservation;
import org.location.models.User;
import org.location.services.ReservationService;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientReservationsController {
    private static final Logger logger = LoggerFactory.getLogger(ClientReservationsController.class);

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Long> idColumn;
    @FXML private TableColumn<Reservation, String> vehicleColumn;
    @FXML private TableColumn<Reservation, String> dateDebutColumn;
    @FXML private TableColumn<Reservation, String> dateFinColumn;
    @FXML private TableColumn<Reservation, String> statutColumn;
    @FXML private Label errorLabel;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicleColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            return new ReadOnlyStringWrapper(
                    reservation.getVehicle() != null
                            ? reservation.getVehicle().getMarque() + " " + reservation.getVehicle().getModele()
                            : "Inconnu"
            );
        });
        dateDebutColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDateDebut() != null
                        ? cellData.getValue().getDateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : ""
        ));
        dateFinColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                cellData.getValue().getDateFin() != null
                        ? cellData.getValue().getDateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : ""
        ));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        reservationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser instanceof Client client) {
                List<Reservation> reservations = reservationService.getReservationsByClient(client);
                reservationTable.setItems(FXCollections.observableArrayList(reservations));
                logger.info("Réservations chargées pour le client {} : {} réservations trouvées",
                        client.getId(), reservations.size());
            } else {
                showError("L'utilisateur connecté n'est pas un client.");
                logger.warn("Utilisateur connecté non client : {}", currentUser.getLogin());
            }
        } catch (Exception e) {
            showError("Erreur lors du chargement des réservations : " + e.getMessage());
            logger.error("Erreur lors du chargement des réservations : {}", e.getMessage(), e);
        }


        reservationTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Reservation selected = row.getItem();
                    afficherFacture(selected);
                }
            });
            return row;
        });
    }


    private void afficherFacture(Reservation reservation) {
        if (reservation == null) {
            showError("Aucune réservation sélectionnée.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/invoice.fxml"));
            Parent root = loader.load(); // <-- possible erreur ici

            InvoiceController controller = loader.getController();
            controller.setReservation(reservation); // <-- possible erreur ici

            Stage stage = new Stage();
            stage.setTitle("Facture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            logger.error("Erreur IO lors du chargement de la facture : {}", e.getMessage(), e);
            showError("Erreur IO : " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'affichage de la facture : {}", e.getMessage(), e);
            showError("Erreur : " + e.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/main-client.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) reservationTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Espace Client - Location de Voitures");
            stage.setMaximized(false);
            stage.centerOnScreen();

            ClientController controller = loader.getController();
            controller.setCurrentUser((Client) SessionManager.getCurrentUser());

            logger.info("Retour à l'écran principal du client");
        } catch (Exception e) {
            showError("Erreur lors du retour à l'espace client : " + e.getMessage());
            logger.error("Erreur lors du retour à l'espace client : {}", e.getMessage(), e);
        }
    }
    private void showInvoice(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invoice.fxml"));
            Parent root = loader.load();

            InvoiceController controller = loader.getController();
            controller.setReservation(reservation);

            Stage stage = new Stage();
            stage.setTitle("Facture");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
        logger.warn("Erreur affichée : {}", message);
    }
}
