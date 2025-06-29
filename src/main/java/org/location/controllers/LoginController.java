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

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerClientButton;
    @FXML private Label errorLabel;

    private UserService userService;

    public void initialize() {
        userService = new UserService();

        // Ajouter des listeners pour masquer l'erreur
        loginField.textProperty().addListener((obs, oldText, newText) -> {
            errorLabel.setVisible(false);
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            errorLabel.setVisible(false);
        });

        // Permettre la connexion avec Enter
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();

        if (login.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        try {
            User user = userService.authenticate(login, password);

            if (user != null) {
                // Sauvegarder l'utilisateur connecté
                SessionManager.setCurrentUser(user);

                // Rediriger selon le rôle
                switch (user.getRole()) {
                    case ADMIN:
                    case EMPLOYEE:
                        switchToAdminInterface();
                        break;
                    case CLIENT:
                        switchToClientInterface();
                        break;
                }
            } else {
                showError("Login ou mot de passe incorrect");
            }
        } catch (Exception e) {
            showError("Erreur de connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterClient() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/register-client.fxml")
            );
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = (Stage) registerClientButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inscription Client");
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de l'inscription");
            e.printStackTrace();
        }
    }

    private void switchToAdminInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/main-admin.fxml")
            );
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Administration - Location de Voitures");
            stage.setMaximized(true);
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de l'interface admin");
            e.printStackTrace();
        }
    }

    private void switchToClientInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/main-client.fxml")
            );
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Location de Voitures - " + SessionManager.getCurrentUser().getNom());
            stage.setMaximized(true);
        } catch (Exception e) {
            showError("Erreur lors de l'ouverture de l'interface client");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}