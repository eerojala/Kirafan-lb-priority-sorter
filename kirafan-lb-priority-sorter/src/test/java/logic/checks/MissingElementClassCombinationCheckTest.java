package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MissingElementClassCombinationCheckTest {
    private MissingElementClassCombinationCheck check;
    private Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map;
    private Series series;
    // Characters with non-existing element/class combinations
    private GameCharacter cA;
    private GameCharacter cB;
    private GameCharacter cC;
    private GameCharacter cD;
    private GameCharacter cE;
    private GameCharacter cF;
    private GameCharacter cG;
    private GameCharacter cH;
    private GameCharacter cI;
    private GameCharacter cJ;
    private GameCharacter cK;
    private GameCharacter cL;
    private GameCharacter cM;
    private GameCharacter cN;
    private GameCharacter cO;

    // Characters with pre-existing element/class combinations
    private GameCharacter cP;
    private GameCharacter cQ;
    private GameCharacter cR;
    private GameCharacter cS;
    private GameCharacter cT;
    private GameCharacter cU;
    private GameCharacter cV;
    private GameCharacter cW;
    private GameCharacter cX;
    private GameCharacter cY;

    public MissingElementClassCombinationCheckTest() {
        series = new Series("Series", CreaStatus.NONE);
        initializeGameCharacterVariables();
        initializeMap();
        check = new MissingElementClassCombinationCheck(map);
    }

    private void initializeGameCharacterVariables() {
        // Characters with non-existing element/class combinations
        // Knights
        cA = new GameCharacter.Builder("cA", series, CharacterElement.WIND, CharacterClass.KNIGHT).build();
        cB = new GameCharacter.Builder("cB", series, CharacterElement.WIND, CharacterClass.KNIGHT).build();
        cC = new GameCharacter.Builder("cC", series, CharacterElement.SUN, CharacterClass.KNIGHT).build();

        // Priests
        cD = new GameCharacter.Builder("cD", series, CharacterElement.MOON, CharacterClass.PRIEST).build();
        cE = new GameCharacter.Builder("cE", series, CharacterElement.MOON, CharacterClass.PRIEST).build();
        cF = new GameCharacter.Builder("cF", series, CharacterElement.FIRE, CharacterClass.PRIEST).build();

        // Mages
        cG = new GameCharacter.Builder("cG", series, CharacterElement.SUN, CharacterClass.MAGE).build();
        cH = new GameCharacter.Builder("cH", series, CharacterElement.SUN, CharacterClass.MAGE).build();
        cI = new GameCharacter.Builder("cI", series, CharacterElement.FIRE, CharacterClass.MAGE).build();

        // Warriors
        cJ = new GameCharacter.Builder("cJ", series, CharacterElement.WATER, CharacterClass.WARRIOR).build();
        cK = new GameCharacter.Builder("cK", series, CharacterElement.WATER, CharacterClass.WARRIOR).build();
        cL = new GameCharacter.Builder("cL", series, CharacterElement.WIND, CharacterClass.WARRIOR).build();

        // Alchemists
        cM = new GameCharacter.Builder("cM", series, CharacterElement.WIND, CharacterClass.ALCHEMIST).build();
        cN = new GameCharacter.Builder("cN", series, CharacterElement.WIND, CharacterClass.ALCHEMIST).build();
        cO = new GameCharacter.Builder("cO", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST).build();

        // Characters with pre-exiting element/class combinations
        // Knights
        cP = new GameCharacter.Builder("cP", series, CharacterElement.FIRE, CharacterClass.KNIGHT).build();
        cQ = new GameCharacter.Builder("cQ", series, CharacterElement.WATER, CharacterClass.KNIGHT).build();

        // Priests
        cR = new GameCharacter.Builder("cR", series, CharacterElement.SUN, CharacterClass.PRIEST).build();
        cS = new GameCharacter.Builder("cS", series, CharacterElement.WIND, CharacterClass.PRIEST).build();

        // Mages
        cT = new GameCharacter.Builder("cT", series, CharacterElement.WIND, CharacterClass.MAGE).build();
        cU = new GameCharacter.Builder("cU", series, CharacterElement.MOON, CharacterClass.MAGE).build();

        // Warriors
        cV = new GameCharacter.Builder("cV", series, CharacterElement.FIRE, CharacterClass.WARRIOR).build();
        cW = new GameCharacter.Builder("cW", series, CharacterElement.MOON, CharacterClass.WARRIOR).build();

        // Alchemists
        cX = new GameCharacter.Builder("cX", series, CharacterElement.SUN, CharacterClass.ALCHEMIST).build();
        cY = new GameCharacter.Builder("cY", series, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();
    }

    private void initializeMap() {
        map = new HashMap<>();

        // Limit broken characters which are not referenced directly during testing
        GameCharacter c1 = new GameCharacter.Builder("c1", series, CharacterElement.FIRE, CharacterClass.KNIGHT)
                .limitBroken().build();

        GameCharacter c2 = new GameCharacter.Builder("c2", series, CharacterElement.WATER, CharacterClass.KNIGHT)
                .limitBroken().build();

        GameCharacter c3 = new GameCharacter.Builder("c3", series, CharacterElement.SUN, CharacterClass.PRIEST)
                .limitBroken().build();

        GameCharacter c4 = new GameCharacter.Builder("c4", series, CharacterElement.WIND, CharacterClass.PRIEST)
                .limitBroken().build();

        GameCharacter c5 = new GameCharacter.Builder("c5", series, CharacterElement.WIND, CharacterClass.MAGE)
                .limitBroken().build();

        GameCharacter c6 = new GameCharacter.Builder("c6", series, CharacterElement.MOON, CharacterClass.MAGE)
                .limitBroken().build();

        GameCharacter c7 = new GameCharacter.Builder("c7", series, CharacterElement.FIRE, CharacterClass.WARRIOR)
                .limitBroken().build();

        GameCharacter c8 = new GameCharacter.Builder("c8", series, CharacterElement.MOON, CharacterClass.WARRIOR)
                .limitBroken().build();

        GameCharacter c9 = new GameCharacter.Builder("c9", series, CharacterElement.SUN, CharacterClass.ALCHEMIST)
                .limitBroken().build();

        GameCharacter c10 = new GameCharacter.Builder("c10", series, CharacterElement.MOON, CharacterClass.ALCHEMIST)
                .limitBroken().build();

        List<GameCharacter> nonLimitBrokenCharacters = new ArrayList<>(Arrays.asList(
                cA, cB, cC, cD, cE, cF, cG, cH, cI, cJ, cK, cL, cM, cN, cO, cP, cQ, cR, cS, cT, cU, cV, cW, cX, cY
        ));

        List<GameCharacter> limitBrokenCharacters = new ArrayList<>(Arrays.asList(
                c1, c2, c3, c4, c5, c6, c7, c8, c9, c10
        ));

        nonLimitBrokenCharacters.forEach(character -> putToMap(character, map));
        limitBrokenCharacters.forEach(character -> putToMap(character, map));
    }

    private void putToMap(GameCharacter character, Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map) {
        AbstractMap.SimpleEntry<CharacterElement, CharacterClass> elementAndClass =
                new AbstractMap.SimpleEntry<>(character.getCharacterElement(), character.getCharacterClass());

        List<GameCharacter> list = map.get(elementAndClass);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(character);
        map.put(elementAndClass, list);
    }

    @Test
    public void compare_returnsNegativeOne_whenAppropriate() {
        // when c1 has a non-existent element/class combination AND c2 has a pre-existing element/class combination AND
        // c2's class has higher priority
        assertEquals(-1, check.compare(cG, cP)); // Mage vs Knight
        assertEquals(-1, check.compare(cG, cR)); // Mage vs Priest
        assertEquals(-1, check.compare(cJ, cP)); // Warrior vs Knight
        assertEquals(-1, check.compare(cJ, cR)); // Warrior vs Priest
        assertEquals(-1, check.compare(cM, cP)); // Alchemist vs Knight
        assertEquals(-1, check.compare(cM, cR)); // Alchemist vs Priest
        assertEquals(-1, check.compare(cM, cT)); // Alchemist vs Mage
        assertEquals(-1, check.compare(cM, cV)); // Alchemist vs Warrior

        // OR c2's class has equal priority
        assertEquals(-1, check.compare(cA, cP)); // Knight vs Knight
        assertEquals(-1, check.compare(cA, cR)); // Knight vs Priest
        assertEquals(-1, check.compare(cD, cP)); // Priest vs Knight
        assertEquals(-1, check.compare(cD, cR)); // Priest vs Priest
        assertEquals(-1, check.compare(cG, cT)); // Mage vs Mage
        assertEquals(-1, check.compare(cG, cV)); // Mage vs Warrior
        assertEquals(-1, check.compare(cJ, cT)); // Warrior vs Mage
        assertEquals(-1, check.compare(cJ, cV)); // Warrior vs Warrior
        assertEquals(-1, check.compare(cM, cX)); // Alchemist vs Alchemist

        // OR c2's class has lower priority
        assertEquals(-1, check.compare(cA, cT)); // Knight vs Mage
        assertEquals(-1, check.compare(cA, cV)); // Knight vs Warrior
        assertEquals(-1, check.compare(cA, cX)); // Knight vs Alchemist
        assertEquals(-1, check.compare(cD, cT)); // Priest vs Mage
        assertEquals(-1, check.compare(cD, cV)); // Priest vs Warrior
        assertEquals(-1, check.compare(cD, cX)); // Priest vs Alchemist
        assertEquals(-1, check.compare(cG, cX)); // Mage vs Alchemist
        assertEquals(-1, check.compare(cJ, cX)); // Warrior vs Alchemist

        // when both characters have a non-existent element/class combination and c1's class has a higher priority
        assertEquals(-1, check.compare(cA, cG)); // Knight vs Mage
        assertEquals(-1, check.compare(cA, cJ)); // Knight vs Warrior
        assertEquals(-1, check.compare(cA, cM)); // Knight vs Alchemist
        assertEquals(-1, check.compare(cD, cG)); // Priest vs Mage
        assertEquals(-1, check.compare(cD, cJ)); // Priest vs Warrior
        assertEquals(-1, check.compare(cD, cM)); // Priest vs Alchemist
        assertEquals(-1, check.compare(cG, cM)); // Mage vs Alchemist
        assertEquals(-1, check.compare(cJ, cM)); // Warrior vs Alchemist
    }

    @Test
    public void compare_returnsOne_whenAppropriate() {
        // when c1 has a pre-existing element/class combination and c2 has a non-existent element/class combination AND
        // c1's class has higher priority
        assertEquals(1, check.compare(cP, cG)); // Knight vs Mage
        assertEquals(1, check.compare(cR, cG)); // Priest vs Mage
        assertEquals(1, check.compare(cP, cJ)); // Knight vs Warrior
        assertEquals(1, check.compare(cR, cJ)); // Priest vs Warrior
        assertEquals(1, check.compare(cP, cM)); // Knight vs Alchemist
        assertEquals(1, check.compare(cR, cM)); // Priest vs Alchemist
        assertEquals(1, check.compare(cT, cM)); // Mage vs Alchemist
        assertEquals(1, check.compare(cV, cM)); // Warrior vs Alchemist

        // OR c1's class has equal priority
        assertEquals(1, check.compare(cP, cA)); // Knight vs Knight
        assertEquals(1, check.compare(cR, cA)); // Priest vs Knight
        assertEquals(1, check.compare(cP, cD)); // Knight vs Priest
        assertEquals(1, check.compare(cR, cD)); // Priest vs Priest
        assertEquals(1, check.compare(cT, cG)); // Mage vs Mage
        assertEquals(1, check.compare(cV, cG)); // Warrior vs Mage
        assertEquals(1, check.compare(cT, cJ)); // Mage vs Warrior
        assertEquals(1, check.compare(cV, cJ)); // Warrior vs Warrior
        assertEquals(1, check.compare(cX, cM)); // Alchemist vs Alchemist

        // OR c1's class has lower priority
        assertEquals(1, check.compare(cT, cA)); // Mage vs Knight
        assertEquals(1, check.compare(cV, cA)); // Warrior vs Knight
        assertEquals(1, check.compare(cX, cA)); // Alchemist vs Knight
        assertEquals(1, check.compare(cT, cD)); // Mage vs Priest
        assertEquals(1, check.compare(cV, cD)); // Warrior vs Priest
        assertEquals(1, check.compare(cX, cD)); // Alchemist vs Priest
        assertEquals(1, check.compare(cX, cG)); // Alchemist vs Mage
        assertEquals(1, check.compare(cX, cJ)); // Alchemist vs Warrior

        // when both classes have a non-existent element/class combination and c2's class has higher priority
        assertEquals(1, check.compare(cG, cA)); // Mage vs Knight
        assertEquals(1, check.compare(cJ, cA)); // Warrior vs Knight
        assertEquals(1, check.compare(cM, cA)); // Alchemist vs Knight
        assertEquals(1, check.compare(cG, cD)); // Mage vs Priest
        assertEquals(1, check.compare(cJ, cD)); // Warrior vs Priest
        assertEquals(1, check.compare(cM, cD)); // Alchemist vs Priest
        assertEquals(1, check.compare(cM, cG)); // Alchemist vs Mage
        assertEquals(1, check.compare(cM, cJ)); // Alchemist vs Warrior
    }

    @Test
    public void compare_returnsZero_whenAppropriate() {
        // when both characters have a pre-existing element/class combination AND
        // c2's priority is higher than c1's
        assertEquals(0, check.compare(cT, cP)); // Mage vs Knight
        assertEquals(0, check.compare(cT, cR)); // Mage vs Priest
        assertEquals(0, check.compare(cV, cP)); // Warrior vs Knight
        assertEquals(0, check.compare(cV, cR)); // Warrior vs Priest
        assertEquals(0, check.compare(cX, cP)); // Alchemist vs Knight
        assertEquals(0, check.compare(cX, cR)); // Alchemist vs Priest
        assertEquals(0, check.compare(cX, cT)); // Alchemist vs Mage
        assertEquals(0, check.compare(cX, cV)); // Alchemist vs Warrior

        // OR c2's priority is equal to c1's
        assertEquals(0, check.compare(cP, cQ)); // Knight vs Knight
        assertEquals(0, check.compare(cP, cR)); // Knight vs Priest
        assertEquals(0, check.compare(cR, cP)); // Priest vs Knight
        assertEquals(0, check.compare(cR, cS)); // Priest vs Priest
        assertEquals(0, check.compare(cT, cU)); // Mage vs Mage
        assertEquals(0, check.compare(cT, cV)); // Mage vs Warrior
        assertEquals(0, check.compare(cV, cT)); // Warrior vs Mage
        assertEquals(0, check.compare(cV, cW)); // Warrior vs Warrior
        assertEquals(0, check.compare(cX, cY)); // Alchemist vs Alchemist

        // OR c2's priority is lower than 1's
        assertEquals(0, check.compare(cP, cT)); // Knight vs Mage
        assertEquals(0, check.compare(cP, cV)); // Knight vs Warrior
        assertEquals(0, check.compare(cP, cX)); // Knight vs Alchemist
        assertEquals(0, check.compare(cR, cT)); // Priest vs Mage
        assertEquals(0, check.compare(cR, cV)); // Priest vs Warrior
        assertEquals(0, check.compare(cR, cX)); // Priest vs Alchemist
        assertEquals(0, check.compare(cT, cX)); // Mage vs Alchemist
        assertEquals(0, check.compare(cV, cX)); // Warrior vs Alchemist

        // when both characters have non-existent element/class combinations AND
        // they both have the same element and class
        assertEquals(0, check.compare(cA, cB)); // Wind Knight VS Wind Knight
        assertEquals(0, check.compare(cD, cE)); // Moon Priest VS Moon Priest
        assertEquals(0, check.compare(cG, cH)); // Sun Mage vs Sun Mage
        assertEquals(0, check.compare(cJ, cK)); // Water Warrior vs Water Warrior
        assertEquals(0, check.compare(cM, cN)); // Wind Alchemist vs Wind alchemist

        // OR they both have a different element but the same class
        assertEquals(0, check.compare(cA, cC)); // Wind Knight vs Sun Knight
        assertEquals(0, check.compare(cD, cF)); // Moon Priest vs Fire Priest
        assertEquals(0, check.compare(cG, cI)); // Sun Mage vs Fire Mage
        assertEquals(0, check.compare(cJ, cL)); // Water Warrior vs Wind Warrior
        assertEquals(0, check.compare(cM, cO)); // Wind Alchemist vs Fire Alchemist

        // OR they both have a class with same priority
        assertEquals(0, check.compare(cA, cD)); // Knight vs Priest
        assertEquals(0, check.compare(cD, cA)); // Priest vs Knight
        assertEquals(0, check.compare(cG, cJ)); // Mage vs Warrior
        assertEquals(0, check.compare(cJ, cG)); // Warrior vs Mage
    }
}