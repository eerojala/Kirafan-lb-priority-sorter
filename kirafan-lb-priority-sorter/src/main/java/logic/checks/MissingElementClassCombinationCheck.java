package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class MissingElementClassCombinationCheck extends Check {
    private Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass;

    public MissingElementClassCombinationCheck(Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass) {
        this.charactersByElementAndClass = charactersByElementAndClass;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        if ((!limitBrokenCharacterWithSameElementAndClassExists(c1) && limitBrokenCharacterWithSameElementAndClassExists(c2))
                || (!limitBrokenCharacterWithSameElementAndClassExists(c1) && charactersClassHasAHigherPriority(c1, c2))) {
            /*
            * If c1 has an element/class combination which NO currently limit broken character has AND
            *       c2 has an element/class combination which a currently limit broken character has
            *       OR c1's class has higher priority than c2's class
            *
            * c1 has higher priority
            */
            return -1;
        } else if ((!limitBrokenCharacterWithSameElementAndClassExists(c2) && limitBrokenCharacterWithSameElementAndClassExists(c1))
                || (!limitBrokenCharacterWithSameElementAndClassExists(c2)) && charactersClassHasAHigherPriority(c2, c1)) {
            /*
            * If c2 has an element/class combination which NO currently limit broken character has AND
            *       c1 has an element/class combination which a currently limit broken character has
            *       OR c2's class has higher priority than c1's class
            *
            *  c2 has higher priority
            */
            return 1;
        } else {
            /*
            * Else, e.g. when neither character has an element/class combination which NO currently limit broken character has
            * OR both characters have an element/class combination which NO currently limit broken has and their classes have the same priority
            *
            * both characters have the same priority
            */
            return 0;
        }

    }

    private boolean limitBrokenCharacterWithSameElementAndClassExists(GameCharacter character) {
        AbstractMap.SimpleEntry<CharacterElement, CharacterClass> charactersElementAndClass =
                new AbstractMap.SimpleEntry<>(character.getCharacterElement(), character.getCharacterClass());

        List<GameCharacter> charactersWithSameElementAndClass = charactersByElementAndClass.get(charactersElementAndClass);

        for (GameCharacter characterWithSameElementAndClass : charactersWithSameElementAndClass) {
            if (!characterWithSameElementAndClass.equals(character) && characterWithSameElementAndClass.isLimitBroken()) {
                return true;
            }
        }

        return false;
    }

    /*
     * Class Priorities:
     * 01: Knight and Priest
     * 02: Mage and Warrior
     * 03: Alchemist
     */
    private boolean charactersClassHasAHigherPriority(GameCharacter character, GameCharacter comparedCharacter) {
        CharacterClass charactersClass = character.getCharacterClass();
        CharacterClass comparedCharactersClass = character.getCharacterClass();

        /*
         * if character's class is a knight or priest and the compared character's class is neither a knight or priest
         * OR if character's class is a mage or warrior and the compared character's class is an alchemist
         * return true
         *
         * else, e.g. if the compared character's class has a higher priority
         * OR both of the character's classes have the same priority
         * return false
         */

        return (classIsKnightOrPriest(charactersClass) && !classIsKnightOrPriest(comparedCharactersClass))
                || (classIsMageOrWarrior(charactersClass) && classIsAlchemist(comparedCharactersClass));
    }

    private boolean classIsKnightOrPriest(CharacterClass characterClass) {
        return characterClass == CharacterClass.KNIGHT || characterClass == CharacterClass.PRIEST;
    }

    private boolean classIsMageOrWarrior(CharacterClass characterClass) {
        return characterClass == CharacterClass.MAGE || characterClass == CharacterClass.WARRIOR;
    }

    private boolean classIsAlchemist(CharacterClass characterClass) {
        return characterClass == CharacterClass.ALCHEMIST;
    }
}
