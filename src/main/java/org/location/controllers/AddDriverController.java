package org.location.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.location.models.Chauffeur;
import org.location.services.ChauffeurService;

public class AddDriverController {

    @FXML private TextField nomField;
    @FXML private CheckBox dispoCheckBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final ChauffeurService chauffeurService = new ChauffeurService();
    private Chauffeur chauffeurToEdit = null;

    public void setChauffeurToEdit(Chauffeur chauffeur) {
        this.chauffeurToEdit = chauffeur;
        nomField.setText(chauffeur.getNom());
        dispoCheckBox.setSelected(Boolean.TRUE.equals(chauffeur.getDispo()));
    }

    @FXML
    private void handleSave() {
        try {
            String nom = nomField.getText().trim();
            boolean dispo = dispoCheckBox.isSelected();

            if (nom.isEmpty()) {
                showError("Le nom est obligatoire.");
                return;
            }

            if (chauffeurToEdit != null) {
                chauffeurToEdit.setNom(nom);
                chauffeurToEdit.setDispo(dispo);
                chauffeurService.updateChauffeur(chauffeurToEdit);
            } else {
                Chauffeur newChauffeur = new Chauffeur(nom, dispo);
                chauffeurService.insertChauffeur(nom, dispo);
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
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
