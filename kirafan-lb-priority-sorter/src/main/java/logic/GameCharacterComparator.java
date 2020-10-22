package logic;

import domain.model.GameCharacter;
import domain.model.GameEvent;
import logic.checks.Check;
import logic.checks.EventBonusCheck;
import logic.checks.MissingElementClassCombinationCheck;
import logic.checks.PersonalPreferenceCheck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCharacterComparator implements Comparator<GameCharacter> {
    private final List<Check> checks;
    private GameCharacterMapper mapper;
    private GameEvent currentEvent;


    public GameCharacterComparator(GameCharacterMapper mapper, GameEvent currentEvent) {
        this.mapper = mapper;
        this.currentEvent = currentEvent;

        checks = new ArrayList<>();
        checks.add(new EventBonusCheck(currentEvent));
        checks.add(new PersonalPreferenceCheck(9));
        checks.add(new MissingElementClassCombinationCheck(mapper.getCharactersByElementAndClass()));
    }

    public GameEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(GameEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    /*
    * Sorting priority (from highest to lowest)
    * 01: Bonus character for current event
    *
    * 02: High personal preference (>=9)
    *
    * 03: Has a class and element combination which no already limit broken character has
    *   Sub-priority for classes:
    *
    *   A: Knights and Priests
    *   B: Warriors and Mages
    *   C: Alchemists
    * */
    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        for (Check check : checks) {
            int checkResult = check.compare(c1, c2);

            if (checkResult != 0) {
                return checkResult;
            }
        }

        return 0;
    }
}
