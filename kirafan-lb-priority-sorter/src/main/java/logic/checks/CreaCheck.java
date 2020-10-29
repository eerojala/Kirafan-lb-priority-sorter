package logic.checks;

import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;

public class CreaCheck extends Check {
    private CreaStatus desiredCreaStatus;

    public CreaCheck(CreaStatus desiredCreaStatus) {
        this.desiredCreaStatus = desiredCreaStatus;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        Series c1Series = c1.getSeries();
        Series c2Series = c2.getSeries();

        if (c1Series.getCreaStatus() == desiredCreaStatus && c2Series.getCreaStatus() != desiredCreaStatus) {
            // If c1 belongs to a series which has the desires crea status and c2 belongs to a series which doesn't
            // c1 has higher priority
            return -1;
        } else if (c1Series.getCreaStatus() != desiredCreaStatus && c2Series.getCreaStatus() == desiredCreaStatus) {
            // If c2 belongs to a series which has the desired crea status and c1 belongs to a series which doesn't
            // c2 has higher priority
            return 1;
        } else {
            // else (when both or neither character belongs to a series with the desires crea status)
            // both characters have equal priority
            return 0;
        }
    }
}
