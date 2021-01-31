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
import logic.CustomParser;
import logic.DatabaseHandler;
import logic.GlobalListHandler;

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
    private MenuItem menuItemSkillEdit;

    @FXML
    private MenuItem menuItemSkillRemove;

    @FXML
    private Button buttonCreateSkill;

    @FXML
    private TextField textFieldPersonalPreference;

    @FXML
    private CheckBox checkBoxLimitBroken;

    @FXML
    private Button buttonSubmit;

    private DatabaseHandler databaseHandler;
    private GameCharacter character;
    private GlobalListHandler listHandler;
    private ObservableList<Skill> characterSkills;
    private Mode mode;

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    public GlobalListHandler getListHandler() {
        return listHandler;
    }

    public void setGlobalHandler(GlobalListHandler listHandler) {
        this.listHandler = listHandler;
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

        if (mode == Mode.EDIT) {
            setInputValuesWithCharacterData();
        } else {
            initializeSkillListView(new ArrayList<>());
        }
    }

    private void initializeSeriesComboBox() {
        List<Series> seriesAll = listHandler.getAllSeries();
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
        ObservableList<Weapon> list = mode == Mode.CREATE ?
                FXCollections.observableArrayList(listHandler.getNonExclusiveWeapons()) :
                FXCollections.observableArrayList(listHandler.getUsableWeapons(character));

        // a null is added to the list in the case that the user wants to de-assign the preferred weapon
        list.add(0, null);
        cmbBoxWeapon.setItems(list);
    }

    private void initializeSkillListView(List<Skill> initialSkills) {
        characterSkills = FXCollections.observableArrayList(initialSkills);
        listViewSkills.setItems(characterSkills);
    }

    private void setInputValuesWithCharacterData() {
        textFieldName.setText(character.getName());
        cmbBoxSeries.setValue(character.getSeries());
        cmbBoxElement.setValue(character.getCharacterElement());
        cmbBoxClass.setValue(character.getCharacterClass());
        textFieldWokeLevel.setText("" + character.getWokeLevel());
        textFieldOffensiveStat.setText("" + character.getOffensiveStat());
        textFieldDEF.setText("" + character.getDefense());
        textFieldMDF.setText("" + character.getMagicDefense());
        textFieldPersonalPreference.setText("" + character.getPersonalPreference());
        cmbBoxWeapon.setValue(character.getPreferredWeapon());
        initializeSkillListView(Skill.cloneSkills(character.getSkills()));
        checkBoxLimitBroken.setSelected(character.isLimitBroken());
    }

    @FXML
    public void handleCreateSkillButtonPressed(ActionEvent event) {
        openSkillWindow(Mode.CREATE);
    }

    private void openSkillWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/skill.fxml");
        SkillWindowController controller = new SkillWindowController();
        controller.setMode(mode);
        controller.setSkills(characterSkills);
        String windowTitle = mode == Mode.CREATE ? "Create a new skill" : "Edit a skill";

        if (mode == Mode.EDIT) {
            controller.setSkill(listViewSkills.getSelectionModel().getSelectedItem());
            controller.setSkillsSelectedIndex(listViewSkills.getSelectionModel().getSelectedIndex());
        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleSkillEditMenuItemClicked(ActionEvent event) {
        openSkillWindow(Mode.EDIT);
    }

    @FXML
    public void handleSkillRemoveMenuItemClicked(ActionEvent event) {
        characterSkills.remove(listViewSkills.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createCharacter();
        } else if (mode == Mode.EDIT) {
            updateCharacter();
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

        if (databaseHandler.addNewCharacter(newCharacter)) {
            listHandler.addNewCharacter(newCharacter);
            listHandler.sortAllCharacters();
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating characters.json failed", "New character was not saved to characters.json");
        }
    }

    private void updateCharacter() {
        character.setName(textFieldName.getText());
        character.setSeries(cmbBoxSeries.getValue());
        character.setCharacterElement(cmbBoxElement.getValue());
        character.setCharacterClass(cmbBoxClass.getValue());
        character.setWokeLevel(CustomParser.parseInteger(textFieldWokeLevel.getText()));
        character.setOffensiveStat(CustomParser.parseInteger(textFieldOffensiveStat.getText()));
        character.setDefense(CustomParser.parseInteger(textFieldDEF.getText()));
        character.setMagicDefense(CustomParser.parseInteger(textFieldMDF.getText()));
        character.setPersonalPreference(CustomParser.parseInteger(textFieldPersonalPreference.getText()));
        character.setPreferredWeapon(cmbBoxWeapon.getValue());
        character.setSkills(characterSkills);
        character.setLimitBroken(checkBoxLimitBroken.isSelected());

        if (databaseHandler.updateCharacter(character)) {
            listHandler.updateCharacter(character);
            listHandler.sortAllCharacters();
            listHandler.sortNonLimitBrokenCharacters();

            if (listHandler.eventCharactersContain(character)) {
                listHandler.sortEventCharacters();
            }
        } else {
            openErrorWindow("Updating characters.json failed", "Changes were not saved to characters.json");
        }
    }
}
