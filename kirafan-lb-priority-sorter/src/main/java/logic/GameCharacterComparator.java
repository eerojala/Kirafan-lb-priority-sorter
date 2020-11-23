package logic;

import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import logic.checks.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameCharacterComparator implements Comparator<GameCharacter> {
    private final List<Check> checks;
    private List<GameCharacter> characters;
    private GameEvent currentEvent;

    public GameCharacterComparator(List<GameCharacter> characters, GameEvent currentEvent) {
        this.currentEvent = currentEvent;
        this.characters = characters;
        checks = new ArrayList<>();

        checks.add(new EventBonusCheck(currentEvent));
        checks.add(new PersonalPreferenceCheck(9));
        checks.add(new MissingElementClassCombinationCheck(Mapper.getCharactersByElementAndClass(characters)));
        checks.add(new CreaCheck(CreaStatus.INCOMPLETE));
        checks.add(new PersonalPreferenceCheck(7));
    }

    public GameEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(GameEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    /*
    * Sorting priority (from highest to lowest)
    * 01: Is a bonus character for current element
    *
    * 02: Has a high personal preference (>=9)
    *
    * 03: Has a class and element combination which no already limit broken character has
    *   Sub-priority for classes:
    *
    *   A: Knights and Priests
    *   B: Warriors and Mages
    *   C: Alchemists
    *
    * 04: Belongs to a series with incomplete crea
    *
    * 05: Has a medium personal preference (>=7)
    *
    * 06: Has a set of skills which is better than other characters of the same element and class
    *   Desired types of skillsets for each class:
    *
    *   Alchemist
    *       A: DEF DOWN // Decrease defense of an enemy
    *       B: MDF DOWN // Decrease magic defense of an enemy
    *       C: Element resistance DOWN // Decrease elemental resistance of an enemy
    *       D: SPD DOWN // Decrease speed of an enemy
    *       E: Status effect // Cause a status effect
    *
    *   Knight
    *       A: Party barrier // Places a barrier for each party member which blocks a single attack
    *       B: Self 3x barrier // Places a barrier on self which blocks 3 separate attacks
    *       C: Defense  // Character has more defense than other knights of the same class
    *       D: Magic defense // Character has more magic defense than other knights of the same class
    *       E: Farm  // Characters has two or more skills which attack all enemies
    *
    *   Mage
    *       A: Max damage // Character has higher max damage than other mages of the same element
    *       B: Farm
    *
    *   Priest
    *       A: Heal cards // Character is able to place cards on the combat timeline which heal the party when activated
    *       B: Status effect clear // Character is able to clear status effects from party members
    *
    *   Warrior
    *       A: Max damage
    *
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
