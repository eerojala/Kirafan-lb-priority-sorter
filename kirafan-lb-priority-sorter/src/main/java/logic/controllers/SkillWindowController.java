package logic.controllers;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.CustomParser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SkillWindowController extends Controller implements Initializable {
    @FXML
    private ComboBox<SkillType> cmbBoxType;

    @FXML
    private ComboBox<SkillChange> cmbBoxChange;

    @FXML
    private ComboBox<SkillTarget> cmbBoxTarget;

    @FXML
    private TextField textFieldPower;

    @FXML
    private Button buttonSubmit;

    private int skillsSelectedIndex;
    private List<Skill> skills;
    private Mode mode;
    private Skill skill;

    public int getSkillsSelectedIndex() {
        return skillsSelectedIndex;
    }

    public void setSkillsSelectedIndex(int skillsSelectedIndex) {
        this.skillsSelectedIndex = skillsSelectedIndex;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTypeComboBox();
        initializeChangeComboBox();
        initializeTargetComboBox();

        if (mode == Mode.EDIT) {
            setInputValuesWithSkillData();
        }
    }

    private void initializeTypeComboBox() {
        ObservableList<SkillType> list = FXCollections.observableArrayList(SkillType.values());
        cmbBoxType.setItems(list);
        cmbBoxType.setValue(SkillType.ATK);
    }

    private void initializeChangeComboBox() {
        ObservableList<SkillChange> list = FXCollections.observableArrayList(SkillChange.values());
        cmbBoxChange.setItems(list);
        cmbBoxChange.setValue(SkillChange.UP);
    }

    private void initializeTargetComboBox() {
        ObservableList<SkillTarget> list = FXCollections.observableArrayList(SkillTarget.values());
        cmbBoxTarget.setItems(list);
        cmbBoxTarget.setValue(SkillTarget.ALLY_SELF);
    }

    private void setInputValuesWithSkillData() {
        cmbBoxType.setValue(skill.getType());
        cmbBoxChange.setValue(skill.getChange());
        cmbBoxTarget.setValue(skill.getTarget());
        textFieldPower.setText("" + skill.getPower());
    }

    @FXML
    public void handleTypeComboBox(ActionEvent event) {
        SkillType selectedType = cmbBoxType.getValue();

        if (SkillType.isBuffOrDebuff(selectedType)) {
            cmbBoxChange.setDisable(false);
            cmbBoxTarget.setDisable(false);
            textFieldPower.setDisable(false);
        } else if (SkillType.isAbnormalEffect(selectedType) || SkillType.isDamageEffect(selectedType)) {
            cmbBoxChange.setDisable(true);
        } else if (SkillType.isMiscEffect(selectedType)) {
            cmbBoxChange.setDisable(true);
            textFieldPower.setDisable(true);
        }
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createSkill();
        } else if (mode == Mode.EDIT) {
            editSkill();
        }

        closeWindow((Node) event.getSource());
    }

    private void createSkill() {
        SkillType selectedType = cmbBoxType.getValue();
        SkillChange selectedChange = cmbBoxChange.isDisabled() ? null : cmbBoxChange.getValue();
        SkillTarget selectedTarget = cmbBoxTarget.isDisabled() ? null : cmbBoxTarget.getValue();
        double power = textFieldPower.isDisabled() ? 0 : CustomParser.parseDouble(textFieldPower.getText());
        Skill skill = new Skill(selectedType, selectedChange, selectedTarget, power);
        skills.add(skill);
    }

    private void editSkill() {
        skill.setType(cmbBoxType.getValue());
        skill.setChange(cmbBoxChange.isDisabled() ? null : cmbBoxChange.getValue());
        skill.setTarget( cmbBoxTarget.isDisabled() ? null : cmbBoxTarget.getValue());
        skill.setPower(textFieldPower.isDisabled() ? 0 : CustomParser.parseDouble(textFieldPower.getText()));
        skills.remove(skillsSelectedIndex);
        skills.add(skill);
    }
}
