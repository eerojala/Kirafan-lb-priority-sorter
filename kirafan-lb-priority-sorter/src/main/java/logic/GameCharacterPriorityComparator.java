package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;
import logic.checks.*;

import java.util.*;

public class GameCharacterPriorityComparator implements Comparator<GameCharacter> {
    private final List<Check> checks;

    public GameCharacterPriorityComparator(
            Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charasByElementAndClass,
            Map<Series, List<GameCharacter>> charasBySeries,
            Map<GameCharacter, Weapon> weaponsByCharas,
            GameEvent currentEvent) {
        checks = new ArrayList<>();
        checks.add(new EventBonusCheck(currentEvent));
        checks.add(new PersonalPreferenceCheck(9));
        checks.add(new MissingElementClassCombinationCheck(charasByElementAndClass));
        checks.add(new CreaCheck(CreaStatus.INCOMPLETE));
        checks.add(new PersonalPreferenceCheck(7));
        checks.add(new SkillSetCheck(charasByElementAndClass));
        checks.add(new HighestWokeCheck(charasBySeries));
        checks.add(new NoWeaponCheck(weaponsByCharas));
        checks.add(new CreaCheck(CreaStatus.COMPLETE));
        checks.add(new PersonalPreferenceCheck(0));
    }
    /*
    * Sorting priority (from highest to lowest)
    * 01: Is a bonus character for current element
    * 02: Has a high personal preference (>=9)
    * 03: Has a class and element combination which no already limit broken character has (See MissingElementClassCombinationCheck for priority between different classes)
    * 04: Belongs to a series with incomplete crea
    * 05: Has a medium personal preference (>=7)
    * 06: Has a set of skills which is better than other characters of the same element and class (See SkillSetCheck for details)
    * 07: Highest woke level of all characters in their series (See HighestWokeCheck for details)
    * 08: Has no unique weapon
    * 09: Belongs to a series with complete crea
    * 10: Has a low personal preference (>=0)
    *
    * This comparator's performance is O(n^2) when assuming worst case scenario (List full of identical characters), but
    * this should not be an issue because:
    *   1.) The game currently (January 10th 2021) has only 218 different 5* characters (with an average of around ~70
    *   added per year) and the average veteran player will most likely have slightly less than half of these characters
    *   (unless they have spent huge amounts of money on the game).

    *  2.) The character that a player has will be more or less evenly be split between 30 different element/class
    *   combinations and ~20 series (the missing element class combination checks and skill set checks are done against
    *   characters of same element and class and highest woke check is done against characters of the same series (so
    *   against ~3-5 other characters on average instead of all characters))
    *
    */
    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        // Checking for null values
        if (c1 == null && c2 == null) {
            return 0;
        } else if (c2 == null) {
            return -1;
        } else if (c1 == null) {
            return 1;
        }

        for (Check check : checks) {
            int checkResult = check.compare(c1, c2);

            if (checkResult != 0) {
                return checkResult;
            }
        }

        return 0;
    }
}
