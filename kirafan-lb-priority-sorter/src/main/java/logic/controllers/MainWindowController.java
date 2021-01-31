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
    private MenuItem menuItemAddEventCharacter;

    @FXML
    private Button buttonCreateCharacter;

    @FXML
    private ListView<Series> listViewSeriesAll;

    @FXML
    private MenuItem menuItemSeriesEdit;

    @FXML
    private MenuItem menuItemSeriesDelete;

    @FXML
    private MenuItem menuItemAddEventSeries;

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
    private ListView<GameCharacter> listViewCharactersEvent;

    @FXML
    private MenuItem menuItemRemoveEventCharacter;

    @FXML
    private Button buttonClearEventCharacters;

    @FXML
    private ListView<Series> listViewSeriesEvent;

    @FXML
    private MenuItem menuItemRemoveEventSeries;

    @FXML
    private Button buttonClearEventSeries;

    @FXML
    private ListView<GameCharacter> listViewCharactersNoLB;

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
        ObservableList<GameCharacter> charactersEvent = FXCollections.observableArrayList(databaseHandler.getEventCharacters());
        ObservableList<GameCharacter> charactersNoLB = FXCollections.observableArrayList(databaseHandler.getNonLimitBrokenCharacters());
        ObservableList<Series> seriesAll = FXCollections.observableArrayList(databaseHandler.getAllSeries());
        ObservableList<Series> seriesEvent = FXCollections.observableArrayList(databaseHandler.getEventSeries());
        ObservableList<Weapon> weaponsAll = FXCollections.observableArrayList(databaseHandler.getAllWeapons());

        // Initialize ListHandler
        listHandler = new GlobalListHandler(databaseHandler.getEvent());
        listHandler.setAllCharacters(charactersAll);
        listHandler.setEventCharacters(charactersEvent);
        listHandler.setNonLimitBrokenCharacters(charactersNoLB);
        listHandler.setAllSeries(seriesAll);
        listHandler.setEventSeries(seriesEvent);
        listHandler.setAllWeapons(weaponsAll);
        listHandler.assignExclusiveCharactersToWeapons();
        listHandler.sortAllLists();

        // Initialize ListViews
        listViewCharactersAll.setItems(charactersAll);
        listViewCharactersEvent.setItems(charactersEvent);
        listViewCharactersNoLB.setItems(charactersNoLB);
        listViewSeriesAll.setItems(seriesAll);
        listViewSeriesEvent.setItems(seriesEvent);
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
        if (listHandler.getAllSeries().isEmpty()) {
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

        if (openConfirmationWindow("Deleting character", "Are you sure you want to delete character " + character + "?")) {
            if (databaseHandler.deleteCharacter(character)) {
                listHandler.deleteCharacter(character);
                listHandler.sortNonLimitBrokenCharacters();
            } else {
                openErrorWindow("Updating characters.json failed", "Character was not deleted from characters.json");
            }
        }
    }

    @FXML
    public void handleAddEventCharacterMenuItemClicked(ActionEvent event) {
        GameCharacter character = listViewCharactersAll.getSelectionModel().getSelectedItem();

        if (databaseHandler.addEventCharacter(character)) {
            listHandler.addEventCharacter(character);
            listHandler.sortEventCharacters();
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
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

        if (openConfirmationWindow("Deleting series", "Are you sure you want to delete series " + series + "?")) {
            if (databaseHandler.deleteSeries(series)) {
                listHandler.deleteSeries(series);
                listHandler.sortNonLimitBrokenCharacters();
            } else {
                openErrorWindow("Updating series.json failed", "Series was not deleted from series.json");
            }
        }
    }

    @FXML
    public void handleAddEventSeriesMenuItemClicked(ActionEvent event) {
        Series series = listViewSeriesAll.getSelectionModel().getSelectedItem();

        if (databaseHandler.addEventSeries(series)) {
            listHandler.addEventSeries(series);
            listHandler.sortEventSeries();
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
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
        Weapon weapon = listViewWeaponsAll.getSelectionModel().getSelectedItem();

        if (openConfirmationWindow("Deleting weapon", "Are you sure you want to delete weapon " + weapon + "?")) {
            if (databaseHandler.deleteWeapon(weapon)) {
                listHandler.deleteWeapon(weapon);
                listHandler.sortNonLimitBrokenCharacters();
            } else {
                openErrorWindow("Updating weapons.json failed", "Weapon was not deleted from series.json");
            }
        }
    }

    @FXML
    public void handleClearEventCharactersButtonPressed(ActionEvent event) {
        if (databaseHandler.clearEventCharacters()) {
            listHandler.clearEventCharacters();
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
        }
    }

    @FXML
    public void handleRemoveEventCharacterMenuItemClicked(ActionEvent event) {
        GameCharacter character = listViewCharactersEvent.getSelectionModel().getSelectedItem();

        if (databaseHandler.removeEventCharacter(character)) {
            listHandler.removeEventCharacter(character);
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
        }
    }

    @FXML
    public void handleClearEventSeriesButtonPressed(ActionEvent event) {
        if (databaseHandler.clearEventSeries()) {
            listHandler.clearEventSeries();
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
        }
    }

    @FXML
    public void handleRemoveEventSeriesMenuItemClicked(ActionEvent event) {
        Series series = listViewSeriesEvent.getSelectionModel().getSelectedItem();

        if (databaseHandler.removeEventSeries(series)) {
            listHandler.removeEventSeries(series);
        } else {
            openErrorWindow("Updating event.json failed", "Changes were not saved to event.json");
        }
    }

    @FXML
    public void handleFilterCheckBoxTicked(ActionEvent event) {
        listHandler.filterNonLimitBrokenCharacters(checkBoxFilter.isSelected(), databaseHandler);
        listHandler.sortNonLimitBrokenCharacters();
    }
}
