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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private List<GameCharacter> charactersAll;
    private List<Series> seriesAll;
    private List<Weapon> weaponsAll;
    private ObservableList<Skill> skills;
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

        if (mode == Mode.EDIT) {
            setInputValuesWithCharacterData();
        } else {
            initializeSkillListView(new ArrayList<Skill>());
        }
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

    private void initializeSkillListView(List<Skill> initialSkills) {
        skills = FXCollections.observableArrayList(initialSkills);
        listViewSkills.setItems(skills);
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
        initializeSkillListView(cloneSkillList(character.getSkills()));
        checkBoxLimitBroken.setSelected(character.isLimitBroken());
    }

    private List<Skill> cloneSkillList(List<Skill> skills) {
        /*
        * Creates a new list of skills from the given list.
        * Skills in the new list are not the same objects but have the same values (so the original list is not affected
        * when making changes in the edit window)
        * If the given list has no values, a completely new list is created anyway
        *
        * This is done that so if the user edit's a characters skill(s) but cancels the editing process by not pressing
        * the submit button and closing the window manually, the character's skill list remains unaffected (the edits are
        * done to the cloned list and when pressing the submit button the cloned list is assigned as the characters
        * new skill list)
        */
        List<Skill> clonedList = new ArrayList<>();

        for (Skill skill : skills) {
            clonedList.add(new Skill(skill.getType(), skill.getChange(), skill.getTarget(), skill.getPower()));
        }

        return clonedList;
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
        skills.remove(listViewSkills.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createCharacter();
        } else if (mode == Mode.EDIT) {
            editCharacter();
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

        if (databaseHandler.createCharacter(newCharacter)) {
            charactersAll.add(newCharacter);
        } else {
            openWarningWindow("Updating characters.json failed", "New character was not saved to characters.json");
        }
    }

    private void editCharacter() {
        charactersAll.remove(character);

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
        character.setSkills(skills);
        character.setLimitBroken(checkBoxLimitBroken.isSelected());

        charactersAll.add(character);

        if (!databaseHandler.updateCharacter(character)) {
            openErrorWindow("Updating characters.json failed", "Changes were not saved to characters.json");
        }
    }
}
