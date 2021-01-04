package logic.controllers;

import domain.model.Series;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Database;

import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController implements Initializable {
    @FXML
    private ListView<?> listViewCharactersAll;

    @FXML
    private Button buttonCreateCharacter;

    @FXML
    private ListView<?> listViewSeriesAll;

    @FXML
    private Button buttonCreateSeries;

    @FXML
    private AnchorPane allWeaponsListView;

    @FXML
    private ListView<?> listViewWeaponsAll;

    @FXML
    private Button buttonCreateWeapon;

    @FXML
    private ListView<?> listViewCharactersEvent;

    @FXML
    private Button buttonAddEventCharacter;

    @FXML
    private Button buttonClearEventCharacters;

    @FXML
    private ListView<?> listViewSeriesEvent;

    @FXML
    private Button buttonAddEventSeries;

    @FXML
    private Button buttonClearEventSeries;

    @FXML
    private ListView<?> listViewCharactersNoLB;

    @FXML
    private CheckBox checkBoxFilter;


    private Database<Series> seriesDatabase;

    public Database<Series> getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(Database<Series> seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleButtonCreateSeriesPressed(ActionEvent event) {
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
