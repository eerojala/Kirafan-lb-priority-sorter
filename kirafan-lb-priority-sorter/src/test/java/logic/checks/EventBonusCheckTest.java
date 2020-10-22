package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventBonusCheckTest {
    private EventBonusCheck check;
    private GameCharacter cA;
    private GameCharacter cB;
    private GameCharacter cC;
    private GameCharacter cD;
    private GameEvent event; // c1 and c2 are bonus characters

    public EventBonusCheckTest() {
        Series series = new Series("series", CreaStatus.COMPLETE);
        cA = new GameCharacter.Builder("c1", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        cB = new GameCharacter.Builder("c2", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        cC = new GameCharacter.Builder("c3", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        cD = new GameCharacter.Builder("c4", series, CharacterElement.SUN, CharacterClass.PRIEST).build();

        event = new GameEvent();
        List<GameCharacter> bonusCharacters = new ArrayList<>(Arrays.asList(cA, cB));
        event.setBonusCharacters(bonusCharacters);

        check = new EventBonusCheck(event);
    }


    @Test
    public void compare_returnsNegativeOne_whenC1IsBonusAndC2IsNot() {
        assertNotEquals(-1, check.compare(cA, cB)); // both characters are bonus
        assertNotEquals(-1, check.compare(cD, cA)); // cD is not bonus, cA is bonus
        assertEquals(-1, check.compare(cA, cC)); // cA is bonus,  cC is not bonus
        assertNotEquals(-1, check.compare(cC, cD)); // Neither character is bonus
    }

    @Test
    public void compare_returnsOne_whenC2IsBonusAndC1IsNot() {
        assertNotEquals(1, check.compare(cA, cB)); // both characters are bonus
        assertEquals(1, check.compare(cD, cA)); // cD is not bonus, cA is bonus
        assertNotEquals(1, check.compare(cA, cC)); // cA is bonus,  cC is not bonus
        assertNotEquals(1, check.compare(cC, cD)); // Neither character is bonus
    }

    @Test
    public void compare_returnsZero_whenEventIsNull() {
        EventBonusCheck anotherCheck = new EventBonusCheck(null);
        assertEquals(0, anotherCheck.compare(cA, cB));
        assertEquals(0, anotherCheck.compare(cA, cC));
        assertEquals(0, anotherCheck.compare(cA, cD));
        assertEquals(0, anotherCheck.compare(cB, cC));
        assertEquals(0, anotherCheck.compare(cB, cD));
        assertEquals(0, anotherCheck.compare(cC, cD));
    }

    @Test
    public void compare_returnsZero_whenBothCharactersAreBonus() {
        assertEquals(0, check.compare(cA, cB));
        assertEquals(0, check.compare(cB, cA));
    }

    @Test
    public void compare_returnsZero_whenNeitherCharacterIsBonus() {
        assertEquals(0, check.compare(cC, cD));
        assertEquals(0, check.compare(cD, cC));
    }
}