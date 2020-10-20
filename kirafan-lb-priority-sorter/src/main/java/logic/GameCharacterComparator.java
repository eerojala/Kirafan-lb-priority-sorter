package logic;

import domain.model.GameCharacter;
import domain.model.GameEvent;
import logic.checks.Check;
import logic.checks.EventBonusCheck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCharacterComparator implements Comparator<GameCharacter> {
    private List<GameCharacter> characters;
    private GameEvent currentEvent;
    private List<Check> checks;

    public GameCharacterComparator(List<GameCharacter> characters, GameEvent currentEvent) {
        this.characters = characters;
        this.currentEvent = currentEvent;

        checks = new ArrayList<>();
        checks.add(new EventBonusCheck(characters, currentEvent));
    }

    /*
    * Sorting priority (from highest to lowest)
    * 01: Bonus character for current event
    * */
    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        for (Check check : checks) {
            int checkResult = check.check(c1, c2);

            if (checkResult != 0) {
                return checkResult;
            }
        }

        return 0;
    }
}
