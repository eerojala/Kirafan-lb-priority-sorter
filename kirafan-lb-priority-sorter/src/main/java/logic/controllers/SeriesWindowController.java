package logic.controllers;

import domain.CreaStatus;
import domain.Mode;
import domain.model.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import logic.Database;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SeriesWindowController implements Initializable {
    @FXML
    private Button buttonSubmit;

    @FXML
    private TextField fieldNameEN;

    @FXML
    private TextField fieldNameJP;

    @FXML
    private ComboBox<CreaStatus> cmbBoxCrea;

    private Database<Series> seriesDatabase;
    private Mode mode;
    private ObservableList<Series> seriesAll;
    private Series series;

    public Database<Series> getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(Database<Series> seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public ObservableList<Series> getSeriesAll() {
        return seriesAll;
    }

    public void setSeriesAll(ObservableList<Series> seriesAll) {
        this.seriesAll = seriesAll;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBox();

        if (mode == Mode.EDIT) {
            fillInputFieldsWithCharacterData();
        }
    }

    private void initializeComboBox() {
        ObservableList<CreaStatus> list = FXCollections.observableArrayList(CreaStatus.COMPLETE, CreaStatus.INCOMPLETE, CreaStatus.NONE);
        cmbBoxCrea.setItems(list);

        Callback<ListView<CreaStatus>, ListCell<CreaStatus>> cellFactory = new Callback<ListView<CreaStatus>, ListCell<CreaStatus>>() {
            @Override
            public ListCell<CreaStatus> call(ListView<CreaStatus> param) {
                return new ListCell<CreaStatus>() {
                    @Override
                    protected void updateItem(CreaStatus item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNameEN());
                        }
                    }
                };
            }
        };

        cmbBoxCrea.setButtonCell(cellFactory.call(null));
        cmbBoxCrea.setCellFactory(cellFactory);
    }

    private void fillInputFieldsWithCharacterData() {
        fieldNameEN.setText(series.getNameEN());
        fieldNameJP.setText(series.getNameJP());
        cmbBoxCrea.setValue(series.getCreaStatus());
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createSeries();
        } else if (mode == Mode.EDIT){
            editSeries();
        }

        // Close the series window:
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close(); // close the instance
    }

    private void createSeries() {
        Series newSeries = new Series(fieldNameEN.getText(), fieldNameJP.getText(), cmbBoxCrea.getValue());
        seriesDatabase.createCollection();
        seriesDatabase.insert(newSeries);
        seriesAll.add(newSeries);
    }

    private void editSeries() {
        seriesAll.remove(series);
        series.setNameEN(fieldNameEN.getText());
        series.setNameJP(fieldNameJP.getText());
        series.setCreaStatus(cmbBoxCrea.getValue());
        seriesDatabase.update(series);
        seriesAll.add(series);
    }
}
