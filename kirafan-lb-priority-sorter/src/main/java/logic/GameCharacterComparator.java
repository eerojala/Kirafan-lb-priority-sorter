package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Weapon;
import logic.checks.*;

import java.util.*;

public class GameCharacterComparator implements Comparator<GameCharacter> {
    private final List<Check> checks;
    private List<GameCharacter> characters;
    private List<Weapon> weapons;
    private GameEvent currentEvent;

    /* NOTE: MAKE SURE TO CREATE A NEW GameCharacterComparator OBJECT EVERY TIME AFTER CREATING/EDITING/DELETING
    * A CHARACTER/SERIES/WEAPON/EVENT
    */
    public GameCharacterComparator(List<GameCharacter> allCharacters, List<Weapon> weapons, GameEvent currentEvent) {
        this.characters = allCharacters;
        this.weapons = weapons;
        this.currentEvent = currentEvent;
        checks = new ArrayList<>();
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass
                = Mapper.getCharactersByElementAndClass(this.characters);

        checks.add(new EventBonusCheck(currentEvent));
        checks.add(new PersonalPreferenceCheck(9));
        checks.add(new MissingElementClassCombinationCheck(charactersByElementAndClass));
        checks.add(new CreaCheck(CreaStatus.INCOMPLETE));
        checks.add(new PersonalPreferenceCheck(7));
        checks.add(new SkillSetCheck(charactersByElementAndClass));
        checks.add(new HighestWokeCheck(Mapper.getCharactersBySeries(this.characters)));
        checks.add(new NoWeaponCheck(weapons));
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
    */
    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
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
