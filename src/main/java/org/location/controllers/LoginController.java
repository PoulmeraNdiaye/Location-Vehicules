package org.location.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.models.User;
import org.location.utils.UserService;
import org.location.utils.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private Button registerButton;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService();

        loginField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
        passwordField.textProperty().addListener((obs, oldText, newText) -> errorLabel.setVisible(false));
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            logger.warn("Tentative de connexion avec des champs vides");
            showError("Veuillez remplir tous les champs");
            return;
        }

        try {
            User user = userService.authenticate(login, password);
            if (user != null) {
                SessionManager.setCurrentUser(user);
                logger.info("Connexion réussie pour l'utilisateur : {} (Rôle : {})", login, user.getRole());

                switch (user.getRole()) {
                    case ADMIN:
                        switchToAdminInterface();
                        break;
                    case EMPLOYEE:
                        logger.warn("Interface employé non implémentée pour : {}", login);
                        showError("Interface employé non disponible pour le moment");
                        break;
                    case CLIENT:
                        switchToClientInterface(user);
                        break;
                }
            } else {
                logger.warn("Échec de la connexion pour : {}", login);
                showError("Login ou mot de passe incorrect");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la tentative de connexion pour : {}", login, e);
            showError("Erreur de connexion : " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        try {
            logger.debug("Tentative de chargement de /fxml/register-client.fxml");
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/register-client.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Le fichier /fxml/register-client.fxml n'a pas été trouvé");
            }
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inscription Client");
            stage.setMaximized(false);
            stage.centerOnScreen();
            logger.info("Ouverture de l'interface d'inscription client");
        } catch (Exception e) {
            logger.error("Erreur lors de l'ouverture de l'inscription client", e);
            showError("Erreur lors de l'ouverture de l'inscription : " + e.getMessage());
        }
    }

    private void switchToAdminInterface() {
        try {
            logger.debug("Tentative de chargement de /fxml/main-admin.fxml");
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/main-admin.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Le fichier /fxml/main-admin.fxml n'a pas été trouvé");
            }
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administration - Location de Voitures");
            stage.setMaximized(false);
            stage.centerOnScreen();
            logger.info("Redirection vers l'interface admin réussie");
        } catch (Exception e) {
            logger.error("Erreur lors de l'ouverture de l'interface admin", e);
            showError("Erreur lors de l'ouverture de l'interface admin : " + e.getMessage());
        }
    }

    private void switchToClientInterface(User user) {
        try {
            logger.debug("Tentative de chargement de /fxml/main-client.fxml");
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/main-client.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("Le fichier /fxml/main-client.fxml n'a pas été trouvé");
            }
            Scene scene = new Scene(loader.load(), 800, 600);
            scene.getStylesheets().add(MainApplication.class.getResource("/css/styles.css").toExternalForm());

            ClientController clientController = loader.getController();
            clientController.setCurrentUser(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Espace Client - Location de Voitures");
            stage.setMaximized(false);
            stage.centerOnScreen();
            logger.info("Redirection vers l'interface client réussie pour : {}", user.getLogin());
        } catch (Exception e) {
            logger.error("Erreur lors de l'ouverture de l'interface client", e);
            showError("Erreur lors de l'ouverture de l'interface client : " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}