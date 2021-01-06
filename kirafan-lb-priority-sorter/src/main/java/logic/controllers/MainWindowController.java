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
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Database;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Controller, Initializable {

    @FXML
    private ListView<GameCharacter> listViewCharactersAll;

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

    private Database<GameCharacter> characterDatabase;
    private Database<Series> seriesDatabase;
    private Database<Weapon> weaponDatabase;
    private ObservableList<GameCharacter> charactersAll;
    private ObservableList<Series> seriesAll;
    private ObservableList<Weapon> weaponsAll;

    public Database<GameCharacter> getCharacterDatabase() {
        return characterDatabase;
    }

    public void setCharacterDatabase(Database<GameCharacter> characterDatabase) {
        this.characterDatabase = characterDatabase;
    }

    public Database<Series> getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(Database<Series> seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    public Database<Weapon> getWeaponDatabase() {
        return weaponDatabase;
    }

    public void setWeaponDatabase(Database<Weapon> weaponDatabase) {
        this.weaponDatabase = weaponDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeObservableLists();
        initializeAllSeriesListView();
    }

    private void initializeObservableLists() {
        charactersAll = FXCollections.observableArrayList(characterDatabase.findAll());
        seriesAll = FXCollections.observableArrayList(seriesDatabase.findAll());
        weaponsAll = FXCollections.observableArrayList(weaponDatabase.findAll());
    }

    private void initializeAllSeriesListView() {
        listViewSeriesAll.setItems(seriesAll);

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
        if (seriesAll.isEmpty()) {
            openWarningWindow("No series added yet", "Create at least one series before creating a character");
        } else {
            openCharacterWindow(Mode.CREATE);
        }
    }

    private void openWarningWindow(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "abc");
        alert.setTitle("Warning!");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openCharacterWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/character.fxml");
        CharacterWindowController controller = new CharacterWindowController();
        controller.setMode(mode);
        controller.setCharacterDatabase(characterDatabase);
        controller.setCharactersAll(charactersAll);
        controller.setSeriesAll(seriesAll);
        controller.setWeaponsAll(weaponsAll);
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
    public void handleButtonCreateSeriesPressed(ActionEvent event) {
        openSeriesWindow(Mode.CREATE);
    }

    private void openSeriesWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/series.fxml");
        SeriesWindowController controller = new SeriesWindowController();
        controller.setMode(mode);
        controller.setSeriesDatabase(seriesDatabase);
        controller.setSeriesAll(seriesAll);
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
    public void handleMenuItemSeriesDeleteClicked(ActionEvent event) {
        Series series = listViewSeriesAll.getSelectionModel().getSelectedItem();
        if (seriesDatabase.remove(series)) {
            seriesAll.remove(series);
        }
    }

    @FXML
    public void handleMenuItemSeriesEditClicked(ActionEvent event) {
        openSeriesWindow(Mode.EDIT);
    }
}
