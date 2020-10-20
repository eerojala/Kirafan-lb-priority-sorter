package logic;

import domain.model.Character;
import domain.model.Event;
import logic.checks.Check;
import logic.checks.EventBonusCheck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CharacterComparator implements Comparator<Character> {
    private Searchable characterDatabase;
    private Event currentEvent;
    private List<Check> checks;

    public CharacterComparator(Searchable characterDatabase, Event currentEvent) {
        this.characterDatabase = characterDatabase;
        this.currentEvent = currentEvent;

        checks = new ArrayList<>();
        checks.add(new EventBonusCheck(characterDatabase, currentEvent));
    }

    /*
    * Sorting priority (from highest to lowest)
    * 01: Bonus character for current event
    * */
    @Override
    public int compare(Character c1, Character c2) {
        for (Check check : checks) {
            int checkResult = check.check(c1, c2);

            if (checkResult != 0) {
                return checkResult;
            }
        }

        return 0;
    }
}
