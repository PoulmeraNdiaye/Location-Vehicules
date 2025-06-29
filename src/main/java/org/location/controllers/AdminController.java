package org.location.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.location.MainApplication;
//import org.mnjaay.services.VehicleService;
//import org.mnjaay.services.ClientService;
//import org.mnjaay.services.ReservationService;
import org.location.utils.SessionManager;

public class AdminController {

    @FXML private TabPane mainTabPane;
    @FXML private Label userLabel;
    @FXML private Label statusLabel;

    // Labels pour les statistiques
    @FXML private Label availableVehiclesLabel;
    @FXML private Label activeReservationsLabel;
    @FXML private Label totalClientsLabel;
    @FXML private Label monthlyRevenueLabel;

     // private VehicleService vehicleService;
   // private ClientService clientService;
   // private ReservationService reservationService;

    public void initialize() {
        // Initialiser les services
           //vehicleService = new VehicleService();
        //clientService = new ClientService();
        //reservationService = new ReservationService();

        // Afficher l'utilisateur connecté
        if (SessionManager.getCurrentUser() != null) {
            userLabel.setText("Utilisateur: " + SessionManager.getCurrentUser().getNom());
        }

        // Charger les statistiques
        loadDashboardStatistics();
    }

    private void loadDashboardStatistics() {
        try {
            // Charger les statistiques depuis la base de données
          // long availableVehicles = vehicleService.countAvailableVehicles();
           /* long activeReservations = reservationService.countActiveReservations();
            long totalClients = clientService.countTotalClients();
            double monthlyRevenue = reservationService.getMonthlyRevenue();*/

            // Mettre à jour les labels
            //availableVehiclesLabel.setText(String.valueOf(availableVehicles));
            /*activeReservationsLabel.setText(String.valueOf(activeReservations));
            totalClientsLabel.setText(String.valueOf(totalClients));
            monthlyRevenueLabel.setText(String.format("%.2f €", monthlyRevenue));*/

            statusLabel.setText("Statistiques mises à jour");
        } catch (Exception e) {
            statusLabel.setText("Erreur lors du chargement des statistiques");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageVehicles() {
        try {
            // Vérifier si l'onglet existe déjà
            Tab existingTab = findTabByText("Gestion Véhicules");
            if (existingTab != null) {
                mainTabPane.getSelectionModel().select(existingTab);
                return;
            }

            // Créer nouvel onglet pour la gestion des véhicules
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/vehicle-management.fxml")
            );
            Tab vehicleTab = new Tab("Gestion Véhicules");
            vehicleTab.setContent(loader.load());

            mainTabPane.getTabs().add(vehicleTab);
            mainTabPane.getSelectionModel().select(vehicleTab);

            statusLabel.setText("Gestion des véhicules ouverte");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de la gestion des véhicules: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageClients() {
        try {
            Tab existingTab = findTabByText("Gestion Clients");
            if (existingTab != null) {
                mainTabPane.getSelectionModel().select(existingTab);
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/client-management.fxml")
            );
            Tab clientTab = new Tab("Gestion Clients");
            clientTab.setContent(loader.load());

            mainTabPane.getTabs().add(clientTab);
            mainTabPane.getSelectionModel().select(clientTab);

            statusLabel.setText("Gestion des clients ouverte");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de la gestion des clients: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageDrivers() {
        try {
            Tab existingTab = findTabByText("Gestion Chauffeurs");
            if (existingTab != null) {
                mainTabPane.getSelectionModel().select(existingTab);
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/driver-management.fxml")
            );
            Tab driverTab = new Tab("Gestion Chauffeurs");
            driverTab.setContent(loader.load());

            mainTabPane.getTabs().add(driverTab);
            mainTabPane.getSelectionModel().select(driverTab);

            statusLabel.setText("Gestion des chauffeurs ouverte");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de la gestion des chauffeurs: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewReservations() {
        try {
            Tab existingTab = findTabByText("Réservations");
            if (existingTab != null) {
                mainTabPane.getSelectionModel().select(existingTab);
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/reservation-management.fxml")
            );
            Tab reservationTab = new Tab("Réservations");
            reservationTab.setContent(loader.load());

            mainTabPane.getTabs().add(reservationTab);
            mainTabPane.getSelectionModel().select(reservationTab);

            statusLabel.setText("Liste des réservations ouverte");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture des réservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleNewReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/new-reservation.fxml")
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nouvelle Réservation");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainTabPane.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            // Actualiser les statistiques après une nouvelle réservation
            loadDashboardStatistics();

        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire de réservation: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuickAddVehicle() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/add-vehicle.fxml")
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Véhicule");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainTabPane.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            loadDashboardStatistics();

        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire d'ajout de véhicule: " + e.getMessage());
        }
    }

    @FXML
    private void handleQuickAddClient() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/add-client.fxml")
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter un Client");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainTabPane.getScene().getWindow());

            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            loadDashboardStatistics();

        } catch (Exception e) {
            showError("Erreur lors de l'ouverture du formulaire d'ajout de client: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Confirmer la déconnexion");
        alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                SessionManager.logout();

                try {
                    FXMLLoader loader = new FXMLLoader(
                            MainApplication.class.getResource("/fxml/login.fxml")
                    );
                    Scene scene = new Scene(loader.load());
                    scene.getStylesheets().add(
                            MainApplication.class.getResource("/css/styles.css").toExternalForm()
                    );

                    Stage stage = (Stage) mainTabPane.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Connexion - Système de Location");
                    stage.setMaximized(false);
                    stage.centerOnScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Confirmer la fermeture");
        alert.setContentText("Êtes-vous sûr de vouloir quitter l'application ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0);
            }
        });
    }

    private Tab findTabByText(String text) {
        return mainTabPane.getTabs().stream()
                .filter(tab -> text.equals(tab.getText()))
                .findFirst()
                .orElse(null);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();

        statusLabel.setText("Erreur: " + message);
    }
}