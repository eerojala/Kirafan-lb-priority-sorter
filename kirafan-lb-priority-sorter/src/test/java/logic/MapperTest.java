package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {
    private Series series1;
    private Series series2;
    private GameCharacter chara1;
    private GameCharacter chara2;
    private GameCharacter chara3;
    private GameCharacter chara4;
    private List<GameCharacter> charaList;

    public MapperTest() {
        series1 = new Series("series1", CreaStatus.NONE);
        series2 = new Series("series2", CreaStatus.COMPLETE);
        chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.SUN, CharacterClass.ALCHEMIST).build();
        chara2 = new GameCharacter.Builder("chara2", series2, CharacterElement.SUN, CharacterClass.ALCHEMIST).build();
        chara3 = new GameCharacter.Builder("chara3", series1, CharacterElement.SUN, CharacterClass.WARRIOR).build();
        chara4 = new GameCharacter.Builder("chara4", series1, CharacterElement.MOON, CharacterClass.ALCHEMIST).build();

        charaList = new ArrayList<>(Arrays.asList(chara1, chara2, chara3, chara4));
    }

    @Test
    public void getCharactersByElementAndClass_returnsEmptyMapWhenAppropriate() {
        assertTrue(Mapper.getCharactersByElementAndClass(null).isEmpty()); // when given list is null
        assertTrue(Mapper.getCharactersByElementAndClass(new ArrayList<GameCharacter>()).isEmpty()); // when given empty list
    }

    @Test
    public void getCharactersByElementAndClass_mapsCharactersCorrectly() {
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map =
                Mapper.getCharactersByElementAndClass(charaList);

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
    public void addCharacterToCharactersByElementAndClass_doesntCrashWithNullParameters() {
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = new HashMap<>();
        Mapper.addCharacterToCharactersByElementAndClass(null, map);
        assertTrue(map.isEmpty()); // Map is empty when character is null

        Mapper.addCharacterToCharactersByElementAndClass(chara1, null);
        Mapper.addCharacterToCharactersByElementAndClass(null, null);
        assertTrue(true); // Program doesn't crash when map or both parameters are null
    }

    @Test
    public void addCharacterToCharactersByElementAndClass_addsCharacterCorrectly() {
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = new HashMap<>();
        Mapper.addCharacterToCharactersByElementAndClass(chara1, map);
        Mapper.addCharacterToCharactersByElementAndClass(chara2, map);
        Mapper.addCharacterToCharactersByElementAndClass(chara3, map);
        Mapper.addCharacterToCharactersByElementAndClass(chara4, map);

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
    public void getCharactersBySeries_returnsEmptyMapWhenAppropriate() {
        assertTrue(Mapper.getCharactersBySeries(null).isEmpty()); // when given list is null
        assertTrue(Mapper.getCharactersBySeries(new ArrayList<GameCharacter>()).isEmpty()); // when given empty list
    }

    @Test
    public void getCharactersBySeries_mapsCharactersCorrectly() {
        Map<Series, List<GameCharacter>> map = Mapper.getCharactersBySeries(charaList);

        List<GameCharacter> series1Characters = map.get(series1);
        List<GameCharacter> series2Characters = map.get(series2);

        assertEquals(2, map.size());
        assertEquals(3, series1Characters.size());
        assertEquals(chara1, series1Characters.get(0));
        assertEquals(chara3, series1Characters.get(1));
        assertEquals(chara4, series1Characters.get(2));
        assertEquals(1, series2Characters.size());
        assertEquals(chara2, series2Characters.get(0));
    }

    @Test
    public void addCharacterToCharactersBySeries_doesntCrashWithNullParameters() {
        Map<Series, List<GameCharacter>> map = new HashMap<>();
        Mapper.addCharacterToCharactersBySeries(null, map);
        assertTrue(map.isEmpty()); // Map is empty when character is null

        Mapper.addCharacterToCharactersBySeries(chara1, null);
        Mapper.addCharacterToCharactersBySeries(null, null);
        assertTrue(true); // Program doesn't crash when map or both parameters are null
    }


    @Test
    public void addCharacterToCharactersBySeries_mapsCharactersCorrectly() {
        Map<Series, List<GameCharacter>> map = new HashMap<>();
        Mapper.addCharacterToCharactersBySeries(chara1, map);
        Mapper.addCharacterToCharactersBySeries(chara2, map);
        Mapper.addCharacterToCharactersBySeries(chara3, map);
        Mapper.addCharacterToCharactersBySeries(chara4, map);

        List<GameCharacter> series1Characters = map.get(series1);
        List<GameCharacter> series2Characters = map.get(series2);

        assertEquals(2, map.size());
        assertEquals(3, series1Characters.size());
        assertEquals(chara1, series1Characters.get(0));
        assertEquals(chara3, series1Characters.get(1));
        assertEquals(chara4, series1Characters.get(2));
        assertEquals(1, series2Characters.size());
        assertEquals(chara2, series2Characters.get(0));
    }

    @Test
    public void getCharacterTotalSkillAmounts_sumsSkillAmountsCorrectly() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 10.5));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 5.5));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_ALL, 15.33));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_ALL, 15.16));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 7.663));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 2.445));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10.33));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10.333));
        chara1.setSkills(skills);
        Map<Skill, Double> map = Mapper.getSkillTotalAmounts(chara1);

        assertEquals(16.0, map.get(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0)));
        assertEquals(30.49, map.get(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_ALL, 0)));
        assertEquals(10.108, map.get(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0)));
        assertEquals(20.663, map.get(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0)));
        assertEquals(null, map.get(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.SELF, 0)));
    }

    @Test
    public void getCharacterTotalSkillAmounts_returnsTheBestNextATKAndMATBuff() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 10.201));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 10.202));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 10.2));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 10.3));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 11.55));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 11.555));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 11.556));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 11.554));
        chara2.setSkills(skills);
        Map<Skill, Double> map = Mapper.getSkillTotalAmounts(chara2);

        assertEquals(10.202, map.get(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 0)));
        assertEquals(10.3, map.get(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 0)));
        assertEquals(11.556, map.get(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 0)));
    }
}