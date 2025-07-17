package org.location.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.exception.DAOException;
import org.location.models.Chauffeur;
import org.location.observer.DataNotifier;
import org.location.observer.NotifierSingleton;
import org.location.services.ChauffeurService;

public class AddDriverController {

    @FXML private TextField nomField;
    @FXML private CheckBox dispoCheckBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final ChauffeurService chauffeurService = new ChauffeurService();
    private Chauffeur chauffeurToEdit = null;
    private final DataNotifier notifier = NotifierSingleton.getInstance();

    public void setChauffeurToEdit(Chauffeur chauffeur) {
        this.chauffeurToEdit = chauffeur;
        nomField.setText(chauffeur.getNom());
        dispoCheckBox.setSelected(Boolean.TRUE.equals(chauffeur.getDispo()));
    }

    @FXML
    private void handleSave() {
        String nom = nomField.getText();
        boolean dispo = dispoCheckBox.isSelected();

        if (nom == null || nom.isBlank()) {
            showError("Le nom ne peut pas être vide.");
            return;
        }

        try {
            if (chauffeurToEdit == null) {
                Chauffeur chauffeur = new Chauffeur();
                chauffeur.setNom(nom);
                chauffeur.setDispo(dispo);
                chauffeurService.ajouterChauffeur(chauffeur);
            } else {
                chauffeurToEdit.setNom(nom);
                chauffeurToEdit.setDispo(dispo);
                chauffeurService.modifierChauffeur(chauffeurToEdit);
            }

            if (notifier != null) {
                notifier.notifyObservers();
            }

            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (DAOException e) {
            e.printStackTrace();
            showError("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }


    @FXML
    private void handleCancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
