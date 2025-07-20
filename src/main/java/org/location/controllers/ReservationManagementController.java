package org.location.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.exception.DAOException;
import org.location.models.Reservation;
import org.location.services.ReservationService;

public class ReservationManagementController {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, String> clientCol;
    @FXML private TableColumn<Reservation, String> vehicleCol;
    @FXML private TableColumn<Reservation, String> datesCol;
    @FXML private TableColumn<Reservation, String> chauffeurCol;
    @FXML private TableColumn<Reservation, String> statutCol;
    @FXML private Label adminMessage;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        // Utilisation de lambda au lieu de PropertyValueFactory car les champs sont dérivés
        clientCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getClient().getNom())
        );

        vehicleCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getVehicle().getMarque() + " " +
                                cellData.getValue().getVehicle().getModele()
                )
        );

        datesCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getDateDebut() + " → " +
                                cellData.getValue().getDateFin()
                )
        );

        chauffeurCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getAvecChauffeur() ? "Oui" : "Non")
        );

        statutCol.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatut())
        );

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

        refreshReservations();
    }

    private void afficherFacture(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invoice.fxml"));

            Scene scene = new Scene(loader.load());

            InvoiceController controller = loader.getController();
            controller.setReservation(reservation);

            Stage stage = new Stage();
            stage.setTitle("Facture");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleValider() throws DAOException {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            reservationService.validerReservation(selected);
            adminMessage.setText("Réservation validée !");
            refreshReservations();
        }
    }

    @FXML
    private void handleRejeter() throws DAOException {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            reservationService.refuserReservation(selected);
            adminMessage.setText("Réservation rejetée !");
            refreshReservations();
        }
    }

    private void refreshReservations() {
        reservationTable.getItems().setAll(reservationService.getAllReservations());
    }

}
