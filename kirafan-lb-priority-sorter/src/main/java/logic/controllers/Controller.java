package logic.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public abstract class Controller {
    public static void openWindow(URL url, Controller controller, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();

            // this makes it so that after opening a new window (e.g. when creatinga new series) the rest of the GUI
            // will be disabled
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public Controller() {
    }

    protected void openErrorWindow(String header, String content) {
        openAlertWindow(Alert.AlertType.ERROR, "Error", header, content);
    }

    protected void openWarningWindow(String header, String content) {
        openAlertWindow(Alert.AlertType.WARNING, "Warning", header, content);
    }

    protected boolean openConfirmationWindow(String header, String content) {
        return openAlertWindow(Alert.AlertType.CONFIRMATION, "Warning", header, content) == ButtonType.OK;
    }

    private ButtonType openAlertWindow(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert.showAndWait().get();
    }

    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close(); // close the instance
    }
}
