package logic.controllers;

import domain.Mode;
import domain.Skill;
import domain.model.GameCharacter;
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

public class WeaponWindowController extends Controller implements Initializable {
    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldOffensiveStat;

    @FXML
    private TextField textFieldDEF;

    @FXML
    private TextField textFieldMDF;

    @FXML
    private ComboBox<GameCharacter> cmbBoxCharacter;

    @FXML
    private ListView<Skill> listViewSkills;

    @FXML
    private MenuItem menuItemEditSkill;

    @FXML
    private MenuItem menuItemDeleteSkill;

    @FXML
    private Button buttonCreateSkill;

    @FXML
    private Button buttonSubmit;

    private DatabaseHandler databaseHandler;
    private GlobalListHandler listHandler;
    private ObservableList<Skill> weaponSkills;
    private Mode mode;
    private Weapon weapon;

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public GlobalListHandler getListHandler() {
        return listHandler;
    }

    public void setGlobalListHandler(GlobalListHandler listHandler) {
        this.listHandler = listHandler;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCharacterComboBox();

        if (mode == Mode.EDIT) {
            setInputValuesWithWeaponData();
        } else {
            initializeSkillListView(new ArrayList<>());
        }
    }

    private void initializeCharacterComboBox() {
        List<GameCharacter> characters = listHandler.getAllCharacters();
        ObservableList<GameCharacter> list = FXCollections.observableArrayList(characters);
        list.add(0, null); // a null element is added if the user wishes to de-assign the exclusive character
        cmbBoxCharacter.setItems(list);
    }

    private void setInputValuesWithWeaponData() {
        textFieldName.setText(weapon.getName());
        textFieldOffensiveStat.setText("" + weapon.getOffensiveStat());
        textFieldDEF.setText("" + weapon.getDefense());
        textFieldMDF.setText("" + weapon.getMagicDefense());
        cmbBoxCharacter.setValue(weapon.getExclusiveCharacter());
        initializeSkillListView(Skill.cloneSkills(weapon.getSkills()));
    }

    private void initializeSkillListView(List<Skill> initialSkills) {
        weaponSkills = FXCollections.observableArrayList(initialSkills);
        listViewSkills.setItems(weaponSkills);
    }

    @FXML
    public void editSkillMenuItemClicked(ActionEvent event) {
        openSkillWindow(Mode.EDIT);
    }

    private void openSkillWindow(Mode mode) {
        URL url = getClass().getClassLoader().getResource("fxml/skill.fxml");
        SkillWindowController controller = new SkillWindowController();
        controller.setMode(mode);
        controller.setSkills(weaponSkills);
        String windowTitle = mode == Mode.CREATE ? "Create a new skill" : "Edit a skill";

        if (mode == Mode.EDIT) {
            controller.setSkill(listViewSkills.getSelectionModel().getSelectedItem());
            controller.setSkillsSelectedIndex(listViewSkills.getSelectionModel().getSelectedIndex());
        }

        Controller.openWindow(url, controller, windowTitle);
    }

    @FXML
    public void handleDeleteSkillMenuItemClicked(ActionEvent event) {
        weaponSkills.remove(listViewSkills.getSelectionModel().getSelectedIndex());
    }

    @FXML
    public void handleCreateSkillButtonPressed(ActionEvent event) {
        openSkillWindow(Mode.CREATE);
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createWeapon();
        } else if (mode == Mode.EDIT) {
            updateWeapon();
        }

        closeWindow((Node) event.getSource());
    }

    private void createWeapon() {
        String name = textFieldName.getText();
        int offensiveStat = CustomParser.parseInteger(textFieldOffensiveStat.getText());
        int defense = CustomParser.parseInteger(textFieldDEF.getText());
        int magicDefense = CustomParser.parseInteger(textFieldMDF.getText());
        GameCharacter exclusiveCharacter = cmbBoxCharacter.getValue();
        List<Skill> skills = new ArrayList<>(listViewSkills.getItems());

        Weapon newWeapon = new Weapon.Builder(name)
                .offensiveStatIs(offensiveStat)
                .defenseIs(defense)
                .magicDefenseIs(magicDefense)
                .isExclusiveTo(exclusiveCharacter)
                .withSkills(skills)
                .build();

        if (databaseHandler.addNewWeapon(newWeapon)) {
            listHandler.addNewWeapon(newWeapon);
            listHandler.sortAllWeapons();
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating weapons.json failed", "New weapon was not saved to weapons.json");
        }
    }

    private void updateWeapon() {
        GameCharacter exclusiveCharacter = cmbBoxCharacter.getValue();
        String exclusiveCharacterId = exclusiveCharacter == null ? null : exclusiveCharacter.getId();

        weapon.setName(textFieldName.getText());
        weapon.setOffensiveStat(CustomParser.parseInteger(textFieldOffensiveStat.getText()));
        weapon.setDefense(CustomParser.parseInteger(textFieldDEF.getText()));
        weapon.setExclusiveCharacter(exclusiveCharacter);
        weapon.setExclusiveCharacterId(exclusiveCharacterId);
        weapon.setSkills(weaponSkills);

        if (databaseHandler.updateWeapon(weapon, true)) {
            listHandler.updateWeapon(weapon, true);
            listHandler.sortAllWeapons();
            listHandler.sortNonLimitBrokenCharacters();
        } else {
            openErrorWindow("Updating weapons.json failed", "Changes were not saved to weapons.json");
        }
    }
}
