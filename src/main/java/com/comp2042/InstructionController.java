package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class InstructionController {

    @FXML
    private void closeWindow(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
