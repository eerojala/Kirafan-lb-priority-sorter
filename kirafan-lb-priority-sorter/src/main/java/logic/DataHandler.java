package logic;

import domain.model.Series;

import java.util.List;

public abstract class DataHandler {
    public DataHandler() {
    }

    public abstract List<Series> getAllSeries();

    protected abstract boolean insertSeriesToData(Series series);

    public boolean addNewSeries(Series series) {
        if (insertSeriesToData(series)) {
            return true;
        } else {
            System.out.println("Failed to add series " + series);
            return false;
        }
    }
}
