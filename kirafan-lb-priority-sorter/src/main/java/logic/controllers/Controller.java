package logic.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public abstract class Controller {
    public static void openWindow(URL url, Controller controller, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
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

    private void openAlertWindow(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type, content);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close(); // close the instance
    }
}
