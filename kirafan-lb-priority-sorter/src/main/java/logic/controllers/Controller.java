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
        }
    }

    public Controller() {
    }

    protected void openWarningWindow(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "abc");
        alert.setTitle("Warning!");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void closeWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close(); // close the instance
    }
}
