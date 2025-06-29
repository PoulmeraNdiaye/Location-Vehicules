package org.location;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.location.factory.HibernateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/login.fxml"));
            if (fxmlLoader.getLocation() == null) {
                throw new IOException("Fichier FXML '/fxml/login.fxml' introuvable dans les ressources.");
            }
            Scene scene = new Scene(fxmlLoader.load());
            URL cssUrl = getClass().getResource("/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                logger.warn("Fichier CSS '/css/styles.css' introuvable, continuation sans CSS.");
            }
            stage.setScene(scene);
            stage.setTitle("Connexion - Location de Voitures");
            stage.show();
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de l'interface de connexion", e);
            throw new RuntimeException("Impossible de démarrer l'application", e);
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Fermeture de l'application : libération des ressources Hibernate.");
            HibernateFactory.closeSessionFactory();
        }));
        launch(args);
    }
}