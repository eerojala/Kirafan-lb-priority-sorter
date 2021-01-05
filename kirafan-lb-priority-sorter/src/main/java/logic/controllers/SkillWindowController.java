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
import logic.Parser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SkillWindowController implements Controller, Initializable {
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

    private List<Skill> skills;
    private Mode mode;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTypeComboBox();
        initializeChangeComboBox();
        initializeTargetComboBox();
        textFieldPower.setText("0");
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

    @FXML
    void handleTypeComboBox(ActionEvent event) {
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
        }

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close(); // close the instance
    }

    private void createSkill() {
        SkillType selectedType = cmbBoxType.getValue();
        SkillChange selectedChange = cmbBoxChange.isDisabled() ? null : cmbBoxChange.getValue();
        SkillTarget selectedTarget = cmbBoxTarget.isDisabled() ? null : cmbBoxTarget.getValue();
        double power = textFieldPower.isDisabled() ? null : Parser.parseDouble(textFieldPower.getText());

        Skill skill = new Skill(selectedType, selectedChange, selectedTarget, power);
        skills.add(skill);
    }
}
