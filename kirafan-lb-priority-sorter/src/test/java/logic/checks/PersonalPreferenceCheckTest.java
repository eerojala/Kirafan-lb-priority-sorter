package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalPreferenceCheckTest {
    private PersonalPreferenceCheck check;
    private GameCharacter cA;
    private GameCharacter cB;
    private GameCharacter cC;
    private GameCharacter cD;
    private GameCharacter cE;
    private GameCharacter cF;

    public PersonalPreferenceCheckTest() {
        Series s = new Series("s", CreaStatus.NONE);

        cA = new GameCharacter.Builder("c1", s, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .withPersonalPreference(9)
                .build();

        cB = new GameCharacter.Builder("c2", s, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .withPersonalPreference(8)
                .build();

        cC = new GameCharacter.Builder("c3", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(9)
                .build();

        cD = new GameCharacter.Builder("c4", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(7)
                .build();

        cE = new GameCharacter.Builder("c5", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(5)
                .build();

        cF = new GameCharacter.Builder("c6", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(7)
                .build();

        check = new PersonalPreferenceCheck(null, null, 8);
    }

    @Test
    public void check_returnsNegativeOne_whenC1HasHigherPreferenceWhichIsInRange() {
        // cA has a priority of 9 which is within the given range (>=8)
        assertEquals(-1, check.check(cA, cB)); // cB has a priority of 8 which is within range and lower than cA's
        assertNotEquals(-1, check.check(cA, cC)); // cC has a priority of 9 which is within range and equal to cA's
        assertEquals(-1, check.check(cA, cD)); // cD has a priority of 7 which is NOT within range and lower than cA's

        // cB has a priority of 8 which is the lowest value within the given range
        assertNotEquals(-1, check.check(cB, cA)); // cA has a priority of 9 which is within range and higher than cB's
        assertEquals(-1, check.check(cB, cD)); // cD has a priority of 7 which is NOT within range and lower than cB's

        // cD has a priority of 7 which is NOT within the given range
        assertNotEquals(-1, check.check(cD, cA)); // cA has a priority of 9 which is within range and higher than cD's.
        assertNotEquals(-1, check.check(cD, cE)); // cE has a priority of 5 which is NOT within range and lower than cD's
        assertNotEquals(-1, check.check(cD, cF)); // cF has a priority of 7 which is NOT within range and equal to cD's

        // cE has a priority of 5 which is NOT within the given range
        assertNotEquals(-1, check.check(cE, cD)); // cD has a priority of 7 which is NOT within range and higher than cE's
    }

    @Test
    public void check_returnsOne_whenC2HasHigherPreferenceWhichIsInRange() {
        // cA has a priority of 9 which is within the given range (>=8)
        assertNotEquals(1, check.check(cA, cB)); // cB has a priority of 8 which is within range and lower than cA's
        assertNotEquals(1, check.check(cA, cC)); // cC has a priority of 9 which is within range and equal to cA's
        assertNotEquals(1, check.check(cA, cD));  // cD has a priority of 7 which is NOT within range and lower than cA's

        // cB has a priority of 8 which is the lowest value within the given range
        assertEquals(1, check.check(cB, cA)); // cA has a priority of 9 which is within range and higher than cB's
        assertNotEquals(1, check.check(cB, cD)); // cD has a priority of 7 which is NOT within range and lower than cB's

        // cD has a priority of 7 which is NOT within the given range
        assertEquals(1, check.check(cD, cA)); // cA has a priority of 9 which is within range and higher than cD's.
        assertNotEquals(1, check.check(cD, cE)); // cE has a priority of 5 which is NOT within range and lower than cD's
        assertNotEquals(1, check.check(cD, cF)); // cF has a priority of 7 which is NOT within range and equal to cD's

        // cE has a priority of 5 which is NOT within the given range
        assertNotEquals(1, check.check(cE, cD)); // cD has a priority of 7 which is NOT within range and higher than cE's
    }

    @Test
    public void check_returnsZero_whenBothCharactersHaveEqualPreferenceWhichIsInRange() {
        assertEquals(0, check.check(cA, cC)); // Both have preference of 9 is within range
    }

    @Test
    public void check_returnsZero_whenBothCharactersPreferenceIsNotInRange() {
        // cD has a priority of 7 which is NOT within the given range
        assertEquals(0, check.check(cD, cE)); // cE has a preference of 5 which is NOT within range and lower than cD's
        assertEquals(0, check.check(cD, cF)); // cF has a preference of 7 which is NOT within range and equal to cD's

        // cE has a priority of 5 which is NOT within the given range
        assertEquals(0, check.check(cE, cD)); // cD has a preference of 7 which is NOT within the range and higher than cE's
    }

}