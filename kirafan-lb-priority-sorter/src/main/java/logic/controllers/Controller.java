package logic.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public interface Controller {
    public static void openWindow(URL url, Controller controller, String windowTitle) {
        //        Node node = (Node) event.getSource(); // Grab the node representing the button from the event object
        //        Stage stage = (Stage) node.getScene().getWindow(); // Get the instance of the stage from the node
        //        stage.close(); // close the instance
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
}
