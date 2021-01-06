package logic.controllers;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import logic.CustomParser;
import logic.Database;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CharacterWindowController extends Controller implements Initializable {
    @FXML
    private TextField textFieldName;

    @FXML
    private ComboBox<Series> cmbBoxSeries;

    @FXML
    private ComboBox<CharacterElement> cmbBoxElement;

    @FXML
    private ComboBox<CharacterClass> cmbBoxClass;

    @FXML
    private TextField textFieldWokeLevel;

    @FXML
    private TextField textFieldOffensiveStat;

    @FXML
    private TextField textFieldDEF;

    @FXML
    private TextField textFieldMDF;

    @FXML
    private ComboBox<Weapon> cmbBoxWeapon;

    @FXML
    private ListView<Skill> listViewSkills;

    @FXML
    private Button buttonCreateSkill;

    @FXML
    private TextField textFieldPersonalPreference;

    @FXML
    private CheckBox checkBoxLimitBroken;

    @FXML
    private Button buttonSubmit;

    private Database<GameCharacter> characterDatabase;
    private GameCharacter character;
    private List<GameCharacter> charactersAll;
    private List<Series> seriesAll;
    private List<Weapon> weaponsAll;
    private ObservableList<Skill> skills;
    private Mode mode;

    public Database<GameCharacter> getCharacterDatabase() {
        return characterDatabase;
    }

    public void setCharacterDatabase(Database<GameCharacter> characterDatabase) {
        this.characterDatabase = characterDatabase;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    public List<GameCharacter> getCharactersAll() {
        return charactersAll;
    }

    public void setCharactersAll(List<GameCharacter> charactersAll) {
        this.charactersAll = charactersAll;
    }

    public List<Weapon> getWeaponsAll() {
        return weaponsAll;
    }

    public List<Series> getSeriesAll() {
        return seriesAll;
    }

    public void setSeriesAll(List<Series> seriesAll) {
        this.seriesAll = seriesAll;
    }

    public void setWeaponsAll(List<Weapon> weaponsAll) {
        this.weaponsAll = weaponsAll;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSeriesComboBox();
        initializeElementComboBox();
        initializeClassComboBox();
        initializeWeaponComboBox();
        initializeSkillListView();
    }

    private void initializeSeriesComboBox() {
        ObservableList<Series> list = FXCollections.observableArrayList(seriesAll);
        cmbBoxSeries.setItems(list);

        if (!seriesAll.isEmpty()) {
            cmbBoxSeries.setValue(list.get(0));
        }
    }

    private void initializeElementComboBox() {
        ObservableList<CharacterElement> list = FXCollections.observableArrayList(CharacterElement.values());
        cmbBoxElement.setItems(list);
        cmbBoxElement.setValue(CharacterElement.FIRE);
    }

    private void initializeClassComboBox() {
        ObservableList<CharacterClass> list = FXCollections.observableArrayList(CharacterClass.values());
        cmbBoxClass.setItems(list);
        cmbBoxClass.setValue(CharacterClass.ALCHEMIST);
    }

    private void initializeWeaponComboBox() {
        ObservableList<Weapon> list = FXCollections.observableArrayList(weaponsAll);
        cmbBoxWeapon.setItems(list);
    }

    private void initializeSkillListView() {
        skills = FXCollections.observableArrayList();
        listViewSkills.setItems(skills);
    }

    @FXML
    public void handleCreateSkillButtonPressed(ActionEvent event) {
        openSkillWindow(Mode.CREATE);
    }

    private void openSkillWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/skill.fxml");
        SkillWindowController controller = new SkillWindowController();
        controller.setMode(mode);
        controller.setSkills(skills);
        String windowTitle = mode == Mode.CREATE ? "Create a new skill" : "Edit a skill";

        if (mode == Mode.EDIT) {

        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createCharacter();
        }

        closeWindow((Node) event.getSource());
    }

    private void createCharacter() {
        String name = textFieldName.getText();
        Series series = cmbBoxSeries.getValue();
        CharacterElement charaElement = cmbBoxElement.getValue();
        CharacterClass charaClass = cmbBoxClass.getValue();
        int wokeLevel = CustomParser.parseInteger(textFieldWokeLevel.getText());
        int offensiveStat = CustomParser.parseInteger(textFieldOffensiveStat.getText());
        int def = CustomParser.parseInteger(textFieldDEF.getText());
        int mdf = CustomParser.parseInteger(textFieldMDF.getText());
        int preference = CustomParser.parseInteger(textFieldPersonalPreference.getText());
        Weapon preferredWeapon = cmbBoxWeapon.getValue();
        List<Skill> skills = new ArrayList<>(listViewSkills.getItems());
        boolean limitBroken = checkBoxLimitBroken.isSelected();

        GameCharacter newCharacter = new GameCharacter.Builder(name, series, charaElement, charaClass)
                .wokeLevelIs(wokeLevel)
                .offensiveStatIs(offensiveStat)
                .defenseIs(def)
                .magicDefenseIs(mdf)
                .personalPreferenceIs(preference)
                .prefersWeapon(preferredWeapon)
                .withSkills(skills)
                .limitBroken(limitBroken)
                .build();

        if (characterDatabase.insert(newCharacter)) {
            charactersAll.add(newCharacter);
        } else {
            openWarningWindow("Updating characters.json failed", "New character was not saved to characters.json");
        }
    }
}