package logic.controllers;

import domain.CreaStatus;
import domain.model.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import logic.Database;

import java.net.URL;
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

    public Database<Series> getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(Database<Series> seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    @FXML
    public void handleSubmitButtonPressed(ActionEvent event) {
        Series series = new Series(fieldNameEN.getText(), fieldNameJP.getText(), cmbBoxCrea.getValue());
        seriesDatabase.createCollection();
        seriesDatabase.insert(series);
    }

}
