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
    private GameCharacter c1;
    private GameCharacter c2;
    private GameCharacter c3;
    private GameCharacter c4;
    private GameCharacter c5;
    private GameCharacter c6;
    
    public PersonalPreferenceCheckTest() {
        Series s = new Series("s", CreaStatus.NONE);

        c1 = new GameCharacter.Builder("c1", s, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .withPersonalPreference(9)
                .build();

        c2 = new GameCharacter.Builder("c2", s, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .withPersonalPreference(8)
                .build();

        c3 = new GameCharacter.Builder("c3", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(9)
                .build();

        c4 = new GameCharacter.Builder("c4", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(7)
                .build();

        c5 = new GameCharacter.Builder("c5", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(5)
                .build();

        c6 = new GameCharacter.Builder("c6", s, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .withPersonalPreference(7)
                .build();

        check = new PersonalPreferenceCheck(null, null, 8);
    }

    @Test
    public void check_returnsNegativeOne_whenC1HasHigherPreferenceWhichIsInRange() {
        // c1 has a priority of 9 which is within the given range (>=8)
        assertEquals(-1, check.check(c1, c2)); // c2 has a priority of 8 which is within range and lower than c1's
        assertNotEquals(-1, check.check(c1, c3)); // c3 has a priority of 9 which is within range and equal to c1's
        assertEquals(-1, check.check(c1, c4)); // c4 has a priority of 7 which is NOT  within range and lower than c1's

        // c2 has a priority of 8 which is the lowest value within the given range
        assertNotEquals(-1, check.check(c2, c1)); // c1 has a priority of 9 which is within range and higher than c2's
        assertEquals(-1, check.check(c2, c4)); // c4 has a priority of 7 which is NOT within range and lower than c2's

        // c4 has a priority of 7 which is NOT within the given range
        assertNotEquals(-1, check.check(c4, c1)); // c1 has a priority of 9 which is within range and higher than c1's.
        assertNotEquals(-1, check.check(c4, c5)); // c5 has a priority of 5 which is NOT within range and lower than c4's
        assertNotEquals(-1, check.check(c4, c6)); // c6 has a priority of 7 which is NOT within range and equal to c4's

        // c5 has a priority of 5 which is NOT within the given range
        assertNotEquals(-1, check.check(c5, c4)); // c4 has a priority of 7 which is NOT within range and higher than c5's
    }

    @Test
    public void check_returnsOne_whenC2HasHigherPreferenceWhichIsInRange() {
        // c1 has a priority of 9 which is within the given range (>=8)
        assertNotEquals(1, check.check(c1, c2)); // c2 has a priority of 8 which is within range and lower than c1's
        assertNotEquals(1, check.check(c1, c3)); // c3 has a priority of 9 which is within range and equal to c1's
        assertNotEquals(1, check.check(c1, c4)); // c4 has a priority of 7 which is NOT  within range and lower than c1's

        // c2 has a priority of 8 which is the lowest value within the given range
        assertEquals(1, check.check(c2, c1)); // c1 has a priority of 9 which is within range and higher than c2's
        assertNotEquals(1, check.check(c2, c4)); // c4 has a priority of 7 which is NOT within range and lower than c2's

        // c4 has a priority of 7 which is NOT within the given range
        assertEquals(1, check.check(c4, c1)); // c1 has a priority of 9 which is within range and higher than c4's.
        assertNotEquals(1, check.check(c4, c5)); // c5 has a priority of 5 which is NOT within range and lower than c4's
        assertNotEquals(1, check.check(c4, c6)); // c6 has a priority of 7 which is NOT within range and equal to c4's

        // c5 has a priority of 5 which is NOT within the given range
        assertNotEquals(1, check.check(c5, c4)); // c4 has a priority of 7 which is NOT within range and higher than c5's
    }

    @Test
    public void check_returnsZero_whenBothCharactersHaveEqualPreferenceWhichIsInRange() {
        assertEquals(0, check.check(c1, c3)); // Both have preference of 9 is within range
    }

    @Test
    public void check_returnsZero_whenBothCharactersPreferenceIsNotInRange() {
        // c4 has a priority of 7 which is NOT within the given range
        assertEquals(0, check.check(c4, c5)); // c5 has a preference of 5 which is NOT within range and lower than c4's
        assertEquals(0, check.check(c4, c6)); // c6 has a preference of 7 which is NOT within range and equal to c4's

        // c5 has a priority of 5 which is NOT within the given range
        assertEquals(0, check.check(c5, c4)); // c4 has a preference of 7 which is NOT within the range and higher than c5's
    }

}