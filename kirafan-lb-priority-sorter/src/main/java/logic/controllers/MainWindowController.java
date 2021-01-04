package logic.controllers;

import domain.Mode;
import domain.model.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logic.Database;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private ListView<?> listViewCharactersAll;

    @FXML
    private Button buttonCreateCharacter;

    @FXML
    private ListView<Series> listViewSeriesAll;

    @FXML
    private MenuItem menuItemSeriesEdit;

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
    private ObservableList<Series> seriesAll;

    public Database<Series> getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(Database<Series> seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAllSeriesListView();
    }

    private void initializeAllSeriesListView() {
        seriesAll = FXCollections.observableArrayList(seriesDatabase.findAll());
        listViewSeriesAll.setItems(seriesAll);

        listViewSeriesAll.setCellFactory(param -> new ListCell<Series>() {
            @Override
            protected void updateItem(Series item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNameEN() + ", Crea status: " + item.getCreaStatus().getNameEN());
                }
            }
        });
    }

    @FXML
    public void handleButtonCreateSeriesPressed(ActionEvent event) {
        openSeriesWindow(Mode.CREATE);
    }

    private void openSeriesWindow(Mode mode) {
        System.out.println(seriesAll);
        //        Node node = (Node) event.getSource(); // Grab the node representing the button from the event object
        //        Stage stage = (Stage) node.getScene().getWindow(); // Get the instance of the stage from the node
        //        stage.close(); // close the instance
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/series.fxml"));
            SeriesWindowController controller = new SeriesWindowController();
            controller.setMode(mode);
            controller.setSeriesDatabase(seriesDatabase);
            controller.setSeriesAll(seriesAll);
            String windowTitle = "";

            if (mode == Mode.CREATE) {
               windowTitle = "Add a new series";
            } else if (mode == Mode.EDIT) {
                controller.setSeries(listViewSeriesAll.getSelectionModel().getSelectedItem());
                windowTitle = "Edit a series";
            }

            loader.setController(controller);

            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void handleMenuItemSeriesEditClicked(ActionEvent event) {
        openSeriesWindow(Mode.EDIT);
    }
}
