package org.location.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.location.MainApplication;
import org.location.controllers.AddVehicleController;
import org.location.models.Vehicle;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApplication.class.getResource("/fxml/" + fxmlFile)
            );
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    MainApplication.class.getResource("/css/styles.css").toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void openEditVehicleWindow(Vehicle vehicle) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fxml/add-vehicle.fxml"));
            Parent root = loader.load();

            AddVehicleController controller = loader.getController();
            controller.setVehicleToEdit(vehicle); // méthode à créer

            Stage stage = new Stage();
            stage.setTitle("Modifier Véhicule");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}