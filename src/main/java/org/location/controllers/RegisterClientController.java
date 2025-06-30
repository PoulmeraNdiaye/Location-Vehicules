package org.location.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
        import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.Client;
import org.location.utils.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterClientController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterClientController.class);

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label errorLabel;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();

        // Masquer l'erreur lors de la modification des champs
        nameField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
        emailField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
    }

    @FXML
    private void handleRegister() {
        String nom = nameField.getText().trim();
        String email = emailField.getText().trim();

        // Validation des champs
        if (nom.isEmpty() || email.isEmpty()) {
            logger.warn("Tentative d'inscription avec des champs vides");
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Validation simple de l'email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Email invalide : {}", email);
            showError("Veuillez entrer un email valide");
            return;
        }

        try {
            // Enregistrer le client
            userService.insertClient(nom, email);
            logger.info("Inscription réussie pour le client : {}", email);

            // Rediriger vers l'interface de connexion
            handleBack();
        } catch (Exception e) {
            logger.error("Erreur lors de l'inscription du client : {}", email, e);
            showError("Erreur lors de l'inscription : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            logger.debug("Retour à l'interface de connexion");
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/login.fxml")
            );
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Le fichier /fxml/login.fxml n'a pas été trouvé");
            }
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion - Système de Location");
            stage.setMaximized(false);
            stage.centerOnScreen();
            logger.info("Retour à l'interface de connexion réussi");
        } catch (Exception e) {
            logger.error("Erreur lors du retour à l'interface de connexion", e);
            showError("Erreur lors du retour : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}