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
    private GameCharacter c1;
    private GameCharacter c2;
    private GameCharacter c3;
    private GameCharacter c4;
    private GameEvent event; // c1 and c2 are bonus characters

    public EventBonusCheckTest() {
        Series series = new Series("series", CreaStatus.COMPLETE);
        c1 = new GameCharacter.Builder("c1", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        c2 = new GameCharacter.Builder("c2", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        c3 = new GameCharacter.Builder("c3", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        c4 = new GameCharacter.Builder("c4", series, CharacterElement.SUN, CharacterClass.PRIEST).build();

        event = new GameEvent();
        List<GameCharacter> bonusCharacters = new ArrayList<>(Arrays.asList(c1, c2));
        event.setBonusCharacters(bonusCharacters);

        check = new EventBonusCheck(new ArrayList<GameCharacter>(), event);
    }

    @Test
    public void check_returnsZeroWhenEventIsNull() {
        EventBonusCheck anotherCheck = new EventBonusCheck(new ArrayList<GameCharacter>(), null);
        assertEquals(0, anotherCheck.check(c2, c3));
    }

    @Test
    public void check_returnsNegativeOneWhenC1IsBonusAndC2IsNot() {
        assertEquals(-1, check.check(c1, c3));
        assertEquals(-1, check.check(c1, c4));
        assertEquals(-1, check.check(c2, c3));
        assertEquals(-1, check.check(c2, c4));
    }

    @Test
    public void check_returnsOneWhenC2IsBonusAndC1IsNot() {
        assertEquals(1, check.check(c3, c1));
        assertEquals(1, check.check(c3, c2));
        assertEquals(1, check.check(c4, c1));
        assertEquals(1, check.check(c4, c2));
    }

    @Test
    public void check_returnsZeroWhenBothCharactersAreBonus() {
        assertEquals(0, check.check(c1, c2));
        assertEquals(0, check.check(c2, c1));
    }

    @Test
    public void check_returnsZeroWhenNeitherCharacterIsBonus() {
        assertEquals(0, check.check(c3, c4));
        assertEquals(0, check.check(c4, c3));
    }
}