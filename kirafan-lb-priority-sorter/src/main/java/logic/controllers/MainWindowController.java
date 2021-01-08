package logic.controllers;

import domain.Mode;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import logic.DatabaseHandler;
import logic.GlobalListHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends Controller implements Initializable {

    @FXML
    private ListView<GameCharacter> listViewCharactersAll;

    @FXML
    private MenuItem menuItemCharacterEdit;

    @FXML
    private MenuItem menuItemCharacterDelete;

    @FXML
    private Button buttonCreateCharacter;

    @FXML
    private ListView<Series> listViewSeriesAll;

    @FXML
    private MenuItem menuItemSeriesEdit;

    @FXML
    private MenuItem menuItemSeriesDelete;

    @FXML
    private Button buttonCreateSeries;

    @FXML
    private ListView<Weapon> listViewWeaponsAll;

    @FXML
    private MenuItem menuItemEditWeapon;

    @FXML
    private MenuItem menuItemWeaponDelete;

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

    private DatabaseHandler databaseHandler;
    private GlobalListHandler listHandler;

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ObservableLists
        ObservableList<GameCharacter> charactersAll = FXCollections.observableArrayList(databaseHandler.getAllCharacters());
        ObservableList<Series> seriesAll = FXCollections.observableArrayList(databaseHandler.getAllSeries());
        ObservableList<Weapon> weaponsAll = FXCollections.observableArrayList(databaseHandler.getAllWeapons());

        // Initialize ListHandler
        listHandler = new GlobalListHandler();
        listHandler.setCharactersAll(charactersAll);
        listHandler.setSeriesAll(seriesAll);
        listHandler.setWeaponsAll(weaponsAll);

        // Initialize ListViews
        listViewSeriesAll.setItems(seriesAll);
        listViewCharactersAll.setItems(charactersAll);
        listViewWeaponsAll.setItems(weaponsAll);
// If you ever want to display objects in an another way than the toString() method, this is how you do it:
//
//        listViewSeriesAll.setCellFactory(param -> new ListCell<Series>() {
//            @Override
//            protected void updateItem(Series item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    String name = item.getName() != null ? item.getName() : "NULL";
//                    String creaStatus = item.getCreaStatus() != null ? item.getCreaStatus().getNameEN() : "NULL";
//
//                    setText(name+ ", Crea status: " + creaStatus);
//                }
//            }
//        });
    }

    @FXML
    public void handleCreateCharacterButtonPressed(ActionEvent event) {
        if (listHandler.getSeriesAll().isEmpty()) {
            openWarningWindow("No series added yet", "Create at least one series before creating a character");
        } else {
            openCharacterWindow(Mode.CREATE);
        }
    }

    private void openCharacterWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/character.fxml");
        CharacterWindowController controller = new CharacterWindowController();
        controller.setMode(mode);
        controller.setDatabaseHandler(databaseHandler);
        controller.setGlobalHandler(listHandler);
        String windowTitle = "";

        if (mode == Mode.CREATE) {
            windowTitle = "Create a new character";
        } else if (mode == Mode.EDIT) {
            windowTitle = "Edit a character";
            controller.setCharacter(listViewCharactersAll.getSelectionModel().getSelectedItem());
        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleCharacterEditMenuItemClicked(ActionEvent event) {
        openCharacterWindow(Mode.EDIT);
    }

    @FXML
    public void handleCharacterDeleteMenuItemClicked(ActionEvent event) {
        GameCharacter character = listViewCharactersAll.getSelectionModel().getSelectedItem();

        if (databaseHandler.deleteCharacter(character)) {
            listHandler.deleteCharacter(character);
        } else {
            openErrorWindow("Updating characters.json failed", "Character was not deleted from characters.json");
        }
    }

    @FXML
    public void handleCreateSeriesButtonPressed(ActionEvent event) {
        openSeriesWindow(Mode.CREATE);
    }

    private void openSeriesWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/series.fxml");
        SeriesWindowController controller = new SeriesWindowController();
        controller.setMode(mode);
        controller.setDatabaseHandler(databaseHandler);
        controller.setGlobalListHandler(listHandler);
        String windowTitle = "";

        if (mode == Mode.CREATE) {
            windowTitle = "Create a new series";
        } else if (mode == Mode.EDIT) {
            windowTitle = "Edit a series";
            controller.setSeries(listViewSeriesAll.getSelectionModel().getSelectedItem());
        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleSeriesEditMenuItemClicked(ActionEvent event) {
        openSeriesWindow(Mode.EDIT);
    }

    @FXML
    public void handleSeriesDeleteMenuItemClicked(ActionEvent event) {
        Series series = listViewSeriesAll.getSelectionModel().getSelectedItem();

        if (databaseHandler.deleteSeries(series)) {
            listHandler.deleteSeries(series);
        } else {
            openErrorWindow("Updating series.json failed", "Series was not deleted from series.json");
        }
    }

    @FXML
    public void handleCreateWeaponButtonPressed(ActionEvent event) {
        openWeaponWindow(Mode.CREATE);
    }

    private void openWeaponWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/weapon.fxml");
        WeaponWindowController controller = new WeaponWindowController();
        controller.setMode(mode);
        controller.setDatabaseHandler(databaseHandler);
        controller.setGlobalListHandler(listHandler);

        String windowTitle = "";

        if (mode == Mode.CREATE) {
            windowTitle = "Create a new weapon";
        } else if (mode == Mode.EDIT) {
            windowTitle = "Edit a weapon";
            controller.setWeapon(listViewWeaponsAll.getSelectionModel().getSelectedItem());
        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleWeaponEditMenuItemClicked(ActionEvent event) {
        openWeaponWindow(Mode.EDIT);
    }

    @FXML
    public void handleWeaponDeleteMenuItemClicked(ActionEvent event) {

    }


}
