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
import javafx.util.Callback;
import logic.Database;

import java.net.URL;
import java.util.ResourceBundle;

public class SeriesWindowController extends Controller implements Initializable {
    @FXML
    private Button buttonSubmit;

    @FXML
    private TextField textFieldName;

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
        ObservableList<CreaStatus> list = FXCollections.observableArrayList(CreaStatus.values());
        cmbBoxCrea.setItems(list);

//        Callback<ListView<CreaStatus>, ListCell<CreaStatus>> cellFactory = new Callback<ListView<CreaStatus>, ListCell<CreaStatus>>() {
//            @Override
//            public ListCell<CreaStatus> call(ListView<CreaStatus> param) {
//                return new ListCell<CreaStatus>() {
//                    @Override
//                    protected void updateItem(CreaStatus item, boolean empty) {
//                        super.updateItem(item, empty);
//
//                        if (item == null || empty) {
//                            setGraphic(null);
//                        } else {
//                            setText(item.getNameEN());
//                        }
//                    }
//                };
//            }
//        };
//
//        cmbBoxCrea.setButtonCell(cellFactory.call(null));
//        cmbBoxCrea.setCellFactory(cellFactory);
        cmbBoxCrea.setValue(CreaStatus.NONE);
    }

    private void fillInputFieldsWithCharacterData() {
        textFieldName.setText(series.getName());
        cmbBoxCrea.setValue(series.getCreaStatus());
    }

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        if (mode == Mode.CREATE) {
            createSeries();
        } else if (mode == Mode.EDIT){
            editSeries();
        }

        closeWindow((Node) event.getSource());
    }

    private void createSeries() {
        Series newSeries = new Series(textFieldName.getText(), cmbBoxCrea.getValue());

        if (seriesDatabase.insert(newSeries)) {
            seriesAll.add(newSeries);
        } else {
            openWarningWindow("Updating series.json failed", "New series was not saved to series.json");
        }
    }

    private void editSeries() {
        seriesAll.remove(series);
        series.setName(textFieldName.getText());
        series.setCreaStatus(cmbBoxCrea.getValue());
        seriesAll.add(series);

        if (!seriesDatabase.update(series)) {
            openWarningWindow("Updating series.json failed", "Changes were not saved to series.json");
        }
    }
}
