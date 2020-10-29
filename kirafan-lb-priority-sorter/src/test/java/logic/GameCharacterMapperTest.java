package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.CreaStatus;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterMapperTest {
    private GameCharacterMapper mapper;
    private Series series1;
    private Series series2;
    private GameCharacter chara1;
    private GameCharacter chara2;
    private GameCharacter chara3;
    private GameCharacter chara4;

    public GameCharacterMapperTest() {
        mapper = new GameCharacterMapper();
        series1 = new Series("series1", CreaStatus.NONE);
        series2 = new Series("series2", CreaStatus.COMPLETE);
        chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.SUN, CharacterClass.ALCHEMIST).build();
        chara2 = new GameCharacter.Builder("chara2", series2, CharacterElement.SUN, CharacterClass.ALCHEMIST).build();
        chara3 = new GameCharacter.Builder("chara3", series1, CharacterElement.SUN, CharacterClass.WARRIOR).build();
        chara4 = new GameCharacter.Builder("chara4", series2, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();


    }

    @Test
    public void addCharacter_addsCharacter_toCharactersByElementAndClass() {
        assertTrue(mapper.getCharactersByElementAndClass().isEmpty());

        mapper.addCharacter(chara1);
        mapper.addCharacter(chara2);
        mapper.addCharacter(chara3);
        mapper.addCharacter(chara4);
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = mapper.getCharactersByElementAndClass();
        List<GameCharacter> sunAlchemists = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.ALCHEMIST));
        List<GameCharacter> sunWarriors = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.WARRIOR));
        List<GameCharacter> moonAlchemists = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.MOON, CharacterClass.ALCHEMIST));

        assertEquals(3, map.size());
        assertEquals(2, sunAlchemists.size());
        assertEquals(chara1, sunAlchemists.get(0));
        assertEquals(chara2, sunAlchemists.get(1));
        assertEquals(1, sunWarriors.size());
        assertEquals(chara3, sunWarriors.get(0));
        assertEquals(1, moonAlchemists.size());
        assertEquals(chara4, moonAlchemists.get(0));
    }

    @Test
    public void addCharacters_addCharacters_toCharactersByElementAndClass() {
        assertTrue(mapper.getCharactersByElementAndClass().isEmpty());

        mapper.addCharacters(Arrays.asList(chara1));
        mapper.addCharacters(Arrays.asList(chara2, chara3));
        mapper.addCharacters(Arrays.asList(chara4));

        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = mapper.getCharactersByElementAndClass();
        List<GameCharacter> sunAlchemists = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.ALCHEMIST));
        List<GameCharacter> sunWarriors = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.WARRIOR));
        List<GameCharacter> moonAlchemists = map.get(new AbstractMap.SimpleEntry<>(CharacterElement.MOON, CharacterClass.ALCHEMIST));

        assertEquals(3, map.size());
        assertEquals(2, sunAlchemists.size());
        assertEquals(chara1, sunAlchemists.get(0));
        assertEquals(chara2, sunAlchemists.get(1));
        assertEquals(1, sunWarriors.size());
        assertEquals(chara3, sunWarriors.get(0));
        assertEquals(1, moonAlchemists.size());
        assertEquals(chara4, moonAlchemists.get(0));
    }
}