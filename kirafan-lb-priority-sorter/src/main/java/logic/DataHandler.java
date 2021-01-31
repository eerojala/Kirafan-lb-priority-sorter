package logic;

import domain.model.GameCharacter;
import domain.model.Series;

import java.util.List;

public abstract class DataHandler {
    public DataHandler() {
    }

    public abstract List<Series> getAllSeries();

    public boolean addNewSeries(Series series) {
        if (!insertToAllSeries(series)) {
            System.out.println("Failed to add series " + series);
            return false;
        }

        return true;
    }

    protected abstract boolean insertToAllSeries(Series series);



//    protected abstract boolean updateSeriesInData(Series series);
//
//    protected abstract boolean isEventSeries(Series series);
//
//    protected abstract boolean updateEventSeriesInData(Series series);
//
//
//    public boolean updateSeries(Series series) {
//        if (!updateSeriesInData(series)) {
//            System.out.println("Failed to update series " + series);
//            return false;
//        }
//
//        if (isEventSeries(series)) {
//            if(!updateEventSeriesInData(series)) {
//                System.out.println("Failed to update the series " + series + " in the event series list.");
//                System.out.println("The old version of the series still remains in the event series list.");
//                System.out.println("If you wish the event series list to have an updated version of the series, " +
//                        "remove it manually from the event series list and then add it back again.");
//            }
//        }
//    }

    public abstract List<GameCharacter> getAllCharacters();

    public boolean addNewCharacter(GameCharacter character) {
        if (!insertToAllCharacters(character)) {
            System.out.println("Failed to add character " + character);
            return false;
        }

        if (!character.isLimitBroken()) {
            if (!insertToNonLBCharacters(character)) {
                System.out.println("Failed to add character " + character + " to non-LB character list");
            }
        }

        return true;
    }

    protected abstract boolean insertToAllCharacters(GameCharacter character);

    protected abstract boolean insertToNonLBCharacters(GameCharacter character);


}
