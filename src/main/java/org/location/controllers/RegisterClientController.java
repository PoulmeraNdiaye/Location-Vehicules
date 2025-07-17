package org.location.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Client;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;
import org.location.services.ClientService;
import org.location.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterClientController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterClientController.class);

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label errorLabel;
    @FXML private PasswordField passwordField;

    private final ClientService clientService = new ClientService();
    private final DataNotifier notifier = NotifierSingleton.getInstance();

    @FXML
    public void initialize() {
        nameField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
        emailField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
        passwordField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));

    }

    @FXML
    private void handleRegister() {
        String nom = nameField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = passwordField.getText().trim();

        if (nom.isEmpty() || email.isEmpty()|| motDePasse.isEmpty()) {
            logger.warn("Tentative d'inscription avec des champs vides");
            showError("Veuillez remplir tous les champs");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Email invalide : {}", email);
            showError("Veuillez entrer un email valide");
            return;
        }

        try {
            //Client client = new Client(nom, email);
            //Client client = new Client(email, "1234", nom, email);
            Client client = new Client(nom, email, email, motDePasse);


            clientService.ajouterClient(client);
            logger.info("Inscription réussie pour le client : {}", email);

            if (notifier != null) {
                notifier.notifyObservers();
            }
            handleBack();
        } catch (DAOException e) {
            logger.error("Erreur DAO lors de l'inscription du client : {}", email, e);
            showError("Erreur d'accès aux données : " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'inscription du client : {}", email, e);
            showError("Erreur inattendue : " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion - Système de Location");
            stage.setMaximized(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            logger.error("Erreur lors du retour à l'interface de connexion", e);
            showError("Erreur lors du retour : " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

}
