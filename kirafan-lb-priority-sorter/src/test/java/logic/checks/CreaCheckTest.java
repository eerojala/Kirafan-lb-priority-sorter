package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreaCheckTest {
    private CreaCheck completeCheck;
    private CreaCheck incompleteCheck;
    private CreaCheck noneCheck;
    private GameCharacter completeSeries1Character1;
    private GameCharacter completeSeries1Character2;
    private GameCharacter completeSeries2Character1;
    private GameCharacter incompleteSeries1Character1;
    private GameCharacter incompleteSeries1Character2;
    private GameCharacter incompleteSeries2Character1;
    private GameCharacter noneSeries1Character1;
    private GameCharacter noneSeries1Character2;
    private GameCharacter noneSeries2Character1;
    private GameCharacter nullSeries1Character1;
    private GameCharacter nullSeries1Character2;
    private GameCharacter nullSeries2Character1;

    public CreaCheckTest() {}

    @BeforeEach
    public void setUp() {
        completeCheck = new CreaCheck(CreaStatus.COMPLETE);
        incompleteCheck = new CreaCheck(CreaStatus.INCOMPLETE);
        noneCheck = new CreaCheck(CreaStatus.NONE);

        Series completeSeries1 = new Series("series1", CreaStatus.COMPLETE);
        Series completeSeries2 = new Series("series2", CreaStatus.COMPLETE);
        Series incompleteSeries1 = new Series("series3", CreaStatus.INCOMPLETE);
        Series incompleteSeries2 = new Series("series4", CreaStatus.INCOMPLETE);
        Series noneSeries1 = new Series("series5", CreaStatus.NONE);
        Series noneSeries2 = new Series("series6", CreaStatus.NONE);
        Series nullSeries1 = new Series("series7", null);
        Series nullSeries2 = new Series("series8", null);

        completeSeries1Character1 = new GameCharacter.Builder("chara1", completeSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        completeSeries1Character2 = new GameCharacter.Builder("chara2", completeSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        completeSeries2Character1 = new GameCharacter.Builder("chara3", completeSeries2, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        incompleteSeries1Character1 = new GameCharacter.Builder("chara4", incompleteSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        incompleteSeries1Character2 = new GameCharacter.Builder("chara5", incompleteSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        incompleteSeries2Character1 = new GameCharacter.Builder("chara6", incompleteSeries2, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        noneSeries1Character1 = new GameCharacter.Builder("chara7", noneSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        noneSeries1Character2 = new GameCharacter.Builder("chara8", noneSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        noneSeries2Character1 = new GameCharacter.Builder("chara9", noneSeries2, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        nullSeries1Character1 = new GameCharacter.Builder("chara10", nullSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        nullSeries1Character2 = new GameCharacter.Builder("chara11", nullSeries1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
        nullSeries2Character1 = new GameCharacter.Builder("chara12", nullSeries2, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
    }

    @Test
    public void compare_returnsNegativeOne_whenAppropriate() {
        // When c1 belongs to a series with the desired crea status and c2 doesn't
        assertEquals(-1, completeCheck.compare(completeSeries2Character1, incompleteSeries2Character1));
        assertEquals(-1, completeCheck.compare(completeSeries1Character1, noneSeries1Character2));
        assertEquals(-1, completeCheck.compare(completeSeries1Character1, nullSeries1Character2));
        assertEquals(-1, incompleteCheck.compare(incompleteSeries1Character1, completeSeries1Character2));
        assertEquals(-1, incompleteCheck.compare(incompleteSeries1Character1, noneSeries1Character2));
        assertEquals(-1, incompleteCheck.compare(incompleteSeries1Character1, nullSeries1Character1));
        assertEquals(-1, noneCheck.compare(noneSeries1Character1, completeSeries1Character2));
        assertEquals(-1, noneCheck.compare(noneSeries1Character1, incompleteSeries1Character2));
        assertEquals(-1, noneCheck.compare(noneSeries1Character1, nullSeries1Character1));
    }

    @Test
    public void compare_returnsOne_whenAppropriate() {
        // when c2 belongs to a series with the desired crea status and c1 doesn't
        assertEquals(1, completeCheck.compare(incompleteSeries2Character1, completeSeries2Character1));
        assertEquals(1, completeCheck.compare(noneSeries1Character1, completeSeries1Character2));
        assertEquals(1, completeCheck.compare(nullSeries1Character1, completeSeries1Character2));
        assertEquals(1, incompleteCheck.compare(completeSeries2Character1, incompleteSeries2Character1));
        assertEquals(1, incompleteCheck.compare(noneSeries1Character1, incompleteSeries1Character2));
        assertEquals(1, incompleteCheck.compare(nullSeries1Character1, incompleteSeries1Character1));
        assertEquals(1, noneCheck.compare(completeSeries1Character1, noneSeries1Character2));
        assertEquals(1, noneCheck.compare(incompleteSeries1Character1, noneSeries1Character2));
        assertEquals(1, noneCheck.compare(nullSeries1Character1, noneSeries1Character2));
    }

    @Test
    public void compare_retunsZero_whenAppropriate() {
        // When both c1 and c2 belong to a series with the desired crea status
        assertEquals(0, completeCheck.compare(completeSeries1Character1, completeSeries1Character2)); // same series
        assertEquals(0, completeCheck.compare(completeSeries1Character1, completeSeries2Character1)); // different series with same crea status
        assertEquals(0, incompleteCheck.compare(incompleteSeries1Character1, incompleteSeries1Character2));
        assertEquals(0, incompleteCheck.compare(incompleteSeries1Character1, incompleteSeries2Character1));
        assertEquals(0, noneCheck.compare(noneSeries1Character1, noneSeries1Character2));
        assertEquals(0, noneCheck.compare(noneSeries1Character1, noneSeries2Character1));

        // OR when neither c1 and c2 belong to a series with the desired crea status
        assertEquals(0, completeCheck.compare(incompleteSeries1Character1, incompleteSeries2Character1));
        assertEquals(0, completeCheck.compare(incompleteSeries1Character1, noneSeries2Character1));
        assertEquals(0, completeCheck.compare(incompleteSeries1Character1, nullSeries1Character1));
        assertEquals(0, completeCheck.compare(noneSeries1Character1, noneSeries2Character1));
        assertEquals(0, completeCheck.compare(noneSeries1Character1, nullSeries1Character2));
        assertEquals(0, completeCheck.compare(nullSeries1Character1, nullSeries2Character1));

        assertEquals(0, incompleteCheck.compare(completeSeries1Character1, completeSeries2Character1));
        assertEquals(0, incompleteCheck.compare(completeSeries1Character1, noneSeries2Character1));
        assertEquals(0, incompleteCheck.compare(completeSeries1Character1, nullSeries2Character1));
        assertEquals(0, incompleteCheck.compare(noneSeries1Character1, noneSeries2Character1));
        assertEquals(0, incompleteCheck.compare(noneSeries1Character1, nullSeries2Character1));
        assertEquals(0, incompleteCheck.compare(nullSeries1Character1, nullSeries2Character1));

        assertEquals(0, noneCheck.compare(completeSeries1Character1, completeSeries2Character1));
        assertEquals(0, noneCheck.compare(completeSeries1Character1, incompleteSeries2Character1));
        assertEquals(0, noneCheck.compare(completeSeries1Character1, nullSeries1Character2));
        assertEquals(0, noneCheck.compare(incompleteSeries1Character1, incompleteSeries2Character1));
        assertEquals(0, noneCheck.compare(incompleteSeries1Character1, nullSeries1Character2));
        assertEquals(0, noneCheck.compare(nullSeries1Character1, nullSeries1Character2));
    }
}