package logic.controllers;

import domain.Skill;
import domain.model.GameCharacter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class WeaponController extends Controller {
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

    @FXML
    void editSkillMenuItemClicked(ActionEvent event) {

    }

    @FXML
    void handleDeleteSkillMenuItemClicked(ActionEvent event) {

    }

    @FXML
    void handleCreateSkillButtonPressed(ActionEvent event) {

    }

    @FXML
    void handleSubmitButtonPressed(ActionEvent event) {

    }
}
