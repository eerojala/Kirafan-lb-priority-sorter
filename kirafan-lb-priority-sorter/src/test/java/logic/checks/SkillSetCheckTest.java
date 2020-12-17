package logic.checks;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillSetCheckTest {
    private SkillSetCheck check;
    private Series series;
    private GameCharacter alchemist1;
    private GameCharacter alchemist2;
    private GameCharacter alchemist3;

    @BeforeEach
    void setUp() {
        series = new Series("series", null);
        alchemist1 = new GameCharacter.Builder("alchemist 1", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist2 = new GameCharacter.Builder("alchemist 2", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist3 = new GameCharacter.Builder("alchemist 3", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        List<GameCharacter> fireAlchemists = new ArrayList<>(Arrays.asList(alchemist1, alchemist2, alchemist3));

        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = new HashMap<>();
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.FIRE, CharacterClass.ALCHEMIST), fireAlchemists);

        check = new SkillSetCheck(map);
    }


    @Test
    public void compare_alchemistSkillSet_DEF_down() {
        alchemist1.setLimitBroken(true);
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 35))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));

        // Alchemist 1 has 25 skillpower in DEF DOWN
        // Alchemist 2 has a DEF UP skill that has more skillpower than alchemist 1, but it is the wrong skill (DEF UP instead of DEF DOWN)
        // Alchemist 3 has also 25 skillpower in DEF DOWN, but alchemist 1 is limit broken so alchemist 3 gets false from the skillpower check

        assertEquals(-1, check.compare(alchemist1, alchemist2));
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist2));

        alchemist1.setLimitBroken(false);
        // alchemist 1 is now not limit broken, so both alchemist 1 and 3 should get true from the skillpower check
        assertEquals(0, check.compare(alchemist1, alchemist3));
        assertEquals(-1, check.compare(alchemist3, alchemist2));
        assertEquals(1, check.compare(alchemist2, alchemist3));

        // testaa MDF down testissä että sen sijaan että muuttaa alchemist 1 epä-limitbroken. alchemist3 on 0.01 enemmän skill power
    }


}