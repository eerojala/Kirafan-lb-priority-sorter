package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HighestWokeCheckTest {
    private HighestWokeCheck check;
    private Map<Series, List<GameCharacter>> charasBySeries;
    private Series series1;
    private Series series2;
    private Series series3;
    private GameCharacter chara1;
    private GameCharacter chara2;
    private GameCharacter chara3;
    private GameCharacter chara4;
    private GameCharacter chara5;

    @BeforeEach
    public void setUp() {
        series1 = new Series("Series 1", null, "1");
        series2 = new Series("Series 2", null, "2");
        series3 = new Series("Series 3", null, "3");

        charasBySeries = new HashMap<>();
        charasBySeries.put(series1, new ArrayList<>());
        charasBySeries.put(series2, new ArrayList<>());
        charasBySeries.put(series3, new ArrayList<>());

        check = new HighestWokeCheck(charasBySeries);
    }

    private void addCharasToTheMap(GameCharacter... charas) {
        for (GameCharacter chara : charas) {
            addCharaToTheMap(chara);
        }
    }

    private void addCharaToTheMap(GameCharacter chara) {
        charasBySeries.get(chara.getSeries()).add(chara);
    }

    @Test
    public void compare_considers_wokeLevel1_asTheMinimum_forHighestWoke() {
        chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .overwriteID("1")
                .build();

        chara2 = new GameCharacter.Builder("chara2", series1, CharacterElement.WIND, CharacterClass.KNIGHT)
                .overwriteID("2")
                .build();

        chara3 = new GameCharacter.Builder("chara3", series1, CharacterElement.EARTH, CharacterClass.MAGE)
                .overwriteID("3")
                .wokeLevelIs(1)
                .build();

        chara4 = new GameCharacter.Builder("chara4", series2, CharacterElement.WATER, CharacterClass.PRIEST)
                .overwriteID("4")
                .wokeLevelIs(1)
                .build();

        chara5 = new GameCharacter.Builder("chara5", series3, CharacterElement.MOON, CharacterClass.WARRIOR)
                .overwriteID("5")
                .build();

        addCharasToTheMap(chara1, chara2, chara3, chara4, chara5);

        // chara 2 and 3 are no the highest wokes in their series
        assertEquals(0, check.compare(chara1, chara2));

        // chara 1 and 3 are of same series and chara3 has the highest woke at level 1
        assertEquals(-1, check.compare(chara3, chara1));

        // chara 4 is the only character in her series and has the highest woke at level 1 and is of a different series
        // than chara 2
        assertEquals(-1, check.compare(chara4, chara2));

        // chara 3 and 4 are the highest wokes (at least level 1) of their respective series
        assertEquals(0, check.compare(chara3, chara4));

        // chara 5 has the highest woke in her series (woke level 0), but it is not at least woke level 1
        assertEquals(1, check.compare(chara5, chara3));
        assertEquals(0, check.compare(chara2, chara5));
    }

    @Test
    public void compare_considers_theCharacter_withTheHighestWokeLevel_asTheHighestWoke() {
        chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.SUN, CharacterClass.ALCHEMIST)
                .overwriteID("1")
                .build();
        chara2 = new GameCharacter.Builder("chara2", series1, CharacterElement.FIRE, CharacterClass.KNIGHT)
                .overwriteID("2")
                .wokeLevelIs(1)
                .build();

        chara3 = new GameCharacter.Builder("chara3", series2, CharacterElement.WIND, CharacterClass.MAGE)
                .overwriteID("3")
                .wokeLevelIs(1)
                .build();

        chara4 = new GameCharacter.Builder("chara4", series2, CharacterElement.EARTH, CharacterClass.PRIEST)
                .overwriteID("4")
                .wokeLevelIs(2)
                .build();

        addCharasToTheMap(chara1, chara2, chara3, chara4);

        // comparing the highest woke with another chara of the same series should return either -1 or 1
        assertEquals(-1, check.compare(chara2, chara1));
        assertEquals(1, check.compare(chara3, chara4));

        // comparing the highest woke chara with another series' non-highest woke chara should also return -1 or 1
        assertEquals(1, check.compare(chara3, chara2));
        assertEquals(-1, check.compare(chara4, chara1));

        // comparing two charas who are the highest wokes in their respective series should return 0
        assertEquals(0, check.compare(chara2, chara4));

        // comparing two charas who are NOT the highest wokes in their respective series should also return 0
        assertEquals(0, check.compare(chara1, chara3));

    }

    @Test
    public void compare_functionsCorrectly_whenSeries_hasTwoOrMoreCharacters_asHighestWokes() {
        chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.WATER, CharacterClass.WARRIOR)
                .overwriteID("1")
                .wokeLevelIs(3)
                .build();

        chara2 = new GameCharacter.Builder("chara2", series1, CharacterElement.MOON, CharacterClass.ALCHEMIST)
                .overwriteID("2")
                .wokeLevelIs(3)
                .build();

        chara3 = new GameCharacter.Builder("chara3", series2, CharacterElement.SUN, CharacterClass.KNIGHT)
               .overwriteID("3")
                .wokeLevelIs(4)
                .limitBroken()
                .build();

        chara4 = new GameCharacter.Builder("chara4", series2, CharacterElement.FIRE, CharacterClass.MAGE)
                .overwriteID("4")
                .wokeLevelIs(4)
                .build();

        addCharasToTheMap(chara1, chara2, chara3, chara4);

        // chara1 and chara2 should both be considered as the highest woke in series1 because neither of them are limit broken
        // chara3 should be considered as the highest woke in series2 because she is limit broken while chara4 is not
        assertEquals(-1, check.compare(chara1, chara4));
        assertEquals(1, check.compare(chara4, chara2));
        assertEquals(0, check.compare(chara2, chara1));
        assertEquals(0, check.compare(chara2, chara3));
        assertEquals(1, check.compare(chara4, chara3));

        // after setting chara4's woke level to 5, she should be considered as series2's sole highest woke
        chara4.setWokeLevel(5);
        assertEquals(-1, check.compare(chara4, chara3));
        assertEquals(1, check.compare(chara3, chara1));
        assertEquals(0, check.compare(chara2, chara4));
    }
}