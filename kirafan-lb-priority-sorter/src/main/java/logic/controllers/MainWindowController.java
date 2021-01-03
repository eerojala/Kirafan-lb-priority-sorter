package logic.controllers;

import domain.model.Series;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Database;

public class MainWindowController {
    public void pressAddSeriesButton(ActionEvent event) {
        Database<Series> seriesDatabase = new Database<>("kirafan-lb-priority-sorter/src/main/resources/json",
                "domain.model", "series");

//        Node node = (Node) event.getSource(); // Grab the node representing the button from the event object
//        Stage stage = (Stage) node.getScene().getWindow(); // Get the instance of the stage from the node
//        stage.close(); // close the instance


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/series.fxml"));
            SeriesWindowController controller = new SeriesWindowController();
            controller.setSeriesDatabase(seriesDatabase);
            loader.setController(controller);

            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add a new series");
            stage.setScene(new Scene(root));
            stage.show();
            //  ((Node)(event.getSource())).getScene().getWindow().hide(); // hides the current window
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
