package logic.checks;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import domain.model.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillSetCheckTest {
    private SkillSetCheck check;
    private Series series;
    private GameCharacter alchemist1;
    private GameCharacter alchemist2;
    private GameCharacter alchemist3;
    private GameCharacter alchemist4;

    @BeforeEach
    void setUp() {
        series = new Series("series", null);
        alchemist1 = new GameCharacter.Builder("alchemist 1", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist2 = new GameCharacter.Builder("alchemist 2", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist3 = new GameCharacter.Builder("alchemist 3", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist4 = new GameCharacter.Builder("alchemist 4", series, CharacterElement.WIND, CharacterClass.ALCHEMIST)
                .build();

        List<GameCharacter> fireAlchemists = new ArrayList<>(Arrays.asList(alchemist1, alchemist2, alchemist3));

        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = new HashMap<>();
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.FIRE, CharacterClass.ALCHEMIST), fireAlchemists);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WIND, CharacterClass.ALCHEMIST), new ArrayList(Arrays.asList(alchemist4)));

        check = new SkillSetCheck(map);
    }


    @Test
    public void compare_alchemistSkillSet_DEF_down_isTakenIntoAccount() {
        alchemist1.setLimitBroken(true);
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 35))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));

        /*
        * Alchemist 1 has 25 skillpower in DEF DOWN to enemies
        * Alchemist 2 has a DEF UP skill that has more skillpower than alchemist 1, but it is the wrong skill
        * (DEF UP to enemies instead of DEF DOWN to enemies)
        * Alchemist 3 has also 25 skillpower in DEF DOWN, but alchemist 1 is limit broken so alchemist 3 gets false
        * from the skillpower check
        */
        assertEquals(-1, check.compare(alchemist1, alchemist2));
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist2));
        assertEquals(-1, check.compare(alchemist1, alchemist3));
        assertEquals(0, check.compare(alchemist2, alchemist3));

        alchemist1.setLimitBroken(false);
        // alchemist 1 is now not limit broken, so both alchemist 1 and 3 should get true from the skillpower check
        assertEquals(-1, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(-1, check.compare(alchemist3, alchemist2));
        assertEquals(0, check.compare(alchemist1, alchemist3));
        assertEquals(1, check.compare(alchemist2, alchemist3));

    }

    @Test
    public void compare_alchemistSkillSet_MDF_down_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 35))));

        // alchemist 1 and 2 both have mdf down 25 to enemies and both are not limit broken
        // alchemist 3 has 35 mdf down (but the target is all allies, so it should not be taken into account)
        assertEquals(-1, check.compare(alchemist1, alchemist3));
        assertEquals(1, check.compare(alchemist3, alchemist2));
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist2, alchemist1));

        alchemist2.getSkills().add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0.01));
        // alchemist 2 now has the most MDF down skillpower
        assertEquals(0, check.compare(alchemist1, alchemist3));
        assertEquals(1, check.compare(alchemist3, alchemist2));
        assertEquals(1, check.compare(alchemist1, alchemist2));
        assertEquals(-1, check.compare(alchemist2, alchemist1));
    }

    @Test
    public void compare_alchemistSkillSet_elementResistance_down_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 50))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist4.setSkills(new ArrayList(Arrays.asList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 50))));
        alchemist2.setLimitBroken(true);

        /*
        * Alchemist 1 has wind resist down 50% to enemies (but alchemist 1's element is fire so it should not be taken
        * into account)
        * Alchemist 2 and 3 have both fire resist down 25% to enemies, but alchemist 2 is limit broken so alchemist 3
        * should get false and alchemist 2 should get true
        * Alchemist 4 has the most fire resist down, but her element is wind instead of fire like the others, so it should
        * not be taken into account
        */
        assertEquals(-1, check.compare(alchemist2, alchemist1));
        assertEquals(1, check.compare(alchemist3, alchemist2));
        assertEquals(0, check.compare(alchemist1, alchemist3));
        assertEquals(-1, check.compare(alchemist2, alchemist4));
        assertEquals(0, check.compare(alchemist1, alchemist4));

        /*
        * alchemist 4 now has the most wind resistance down (of all wind alchemists), so she should get true now (but
        * it should not affect the results when comparing just fire alchemists)
         */
        alchemist4.getSkills().add(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.01));
        assertEquals(-1, check.compare(alchemist2, alchemist1));
        assertEquals(1, check.compare(alchemist3, alchemist2));
        assertEquals(0, check.compare(alchemist1, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist2));
        assertEquals(1, check.compare(alchemist1, alchemist4));

        // after adding the weapon alchemist 3 should now have the most fire resist down skillpower
        Weapon weapon = new Weapon.Builder("weapon")
                .withSkill(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.01))
                .build();
        alchemist3.setPreferredWeapon(weapon);
        assertEquals(0, check.compare(alchemist2, alchemist1));
        assertEquals(-1, check.compare(alchemist3, alchemist2));
        assertEquals(1, check.compare(alchemist1, alchemist3));
        assertEquals(-1, check.compare(alchemist4, alchemist2));
        assertEquals(1, check.compare(alchemist1, alchemist4));
    }

    @Test
    public void compare_alchemistSkillSet_speed_down_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_ALL, 50))));
        alchemist2.setLimitBroken(true);
        /*
        * alchemist 1 and 2 both have 25 power spd down to enemies skillpower, but alchemist 2 is limit broken so alchemist 1
        * should get false and alchemist 2 should get true.
        * alchemist 3 has 50 power SPD Up to allies (it should not be taken into account)
        */
        assertEquals(-1, check.compare(alchemist2, alchemist1));
        assertEquals(1, check.compare(alchemist3, alchemist2));
        assertEquals(0, check.compare(alchemist1, alchemist3));

        alchemist1.getSkills().add(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0.01));
        // alchemist1 should now have the most SPD DOWN to enemies skill power
        assertEquals(1, check.compare(alchemist2, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist2));
        assertEquals(-1, check.compare(alchemist1, alchemist3));
    }

    @Test
    public void compare_alchemistSkillSet_statusEffects_areTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.CONFUSION, null, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.CONFUSION, null, SkillTarget.ALLIES_ALL, 50))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.CONFUSION, null, SkillTarget.ENEMY_SINGLE, 25.01))));

        // Alchemist 3 has most confusion to enemy skill power
        // Alcheist 2 has skill power 50 confusion to allies (not taken into consideration)
        assertEquals(-1, check.compare(alchemist3, alchemist1));
        assertEquals(1, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist1, alchemist2));

        // alchemist 1 now has the most isolation to enemies skillpower
        alchemist1.getSkills().add(new Skill(SkillType.ISOLATION, null, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(1, check.compare(alchemist2, alchemist3));
        assertEquals(-1, check.compare(alchemist1, alchemist2));

        //alchemist 2 now has the most hunger to enemies skillpower
        alchemist2.getSkills().add(new Skill(SkillType.HUNGER, null, SkillTarget.ENEMY_SINGLE, 0.01));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist1, alchemist2));

        // *** ALCHEMISTS' SKILLS ARE NOW RESET AND GIVEN NEW SKILLS ***
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MISFORTUNE, null, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MISFORTUNE, null, SkillTarget.ENEMY_ALL, 20))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.MISFORTUNE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 50))));

        /*
        * alchemist 1 has the most misfortune to enemy skill power
        * alchemist 3 has 50% misfortune to enemy skill, but the change is erroneously DOWN instead of null, so it
        * should not be taken into account
        */
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(-1, check.compare(alchemist1, alchemist2));

        // alchemist 2 now has the most paralysis to enemy skillpower
        alchemist2.getSkills().add(new Skill(SkillType.PARALYSIS, null, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(-1, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist1, alchemist2));

        // alchemist 3 now has the most silence to enemy skillpower
        alchemist3.getSkills().add(new Skill(SkillType.SILENCE, null, SkillTarget.ENEMY_SINGLE, 0.01));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist1, alchemist2));

        // *** ALCHEMISTS' SKILLS ARE NOW RESET AND GIVEN NEW SKILLS ***
        alchemist1.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SLEEP, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 50))));
        alchemist2.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SLEEP, null, SkillTarget.ENEMY_ALL, 25))));
        alchemist3.setSkills(new ArrayList<>(Arrays.asList(new Skill(SkillType.SLEEP, null, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist3.setLimitBroken(true);

        /*
        * alchemist 2 and 3 have equal skill power for sleep to enemies, but alchemist 3 is limit broken, so alchemist 2
        * should get false and alchemist 3 should get true.
        * alchemist 1 has 50% for sleep to enemies which is more than alchemist 2 and 3, but the skill has erroneously
        * UP instead of null as change, so it should not be taken into account
        */
        assertEquals(-1, check.compare(alchemist3, alchemist1));
        assertEquals(1, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist1, alchemist2));

        // alchemist 2 now has the most TIMID to enemies skill power
        alchemist2.getSkills().add(new Skill(SkillType.TIMID, null, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(-1, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(1, check.compare(alchemist1, alchemist2));
    }

    @Test
    public void compare_alchemistSkillSet_all_areTakenIntoAccount() {
        // at the start since no one has any skills they all get false
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));

        // alchemist 1 now has the most def down to enemies skill power
        alchemist1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(-1, check.compare(alchemist1, alchemist2));
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(1, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));

        // alchemist 2 now has the most mdf down to enemies skill power (for fire alchemists)
        // alchemist 4 now has the most mdf down to enemies skill power (for wind alchemists)
        alchemist2.getSkills().add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.01));
        alchemist4.getSkills().add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 100));
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(1, check.compare(alchemist3, alchemist1));
        assertEquals(-1, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist1));
        assertEquals(1, check.compare(alchemist3, alchemist4));

        // alchemist 3 now has the most fire resist down to enemies skill power
        alchemist3.getSkills().add(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));

        // alchemist 2 now has the most def/mdf down to enemies
        alchemist3.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.02));
        alchemist3.getSkills().add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0.02));
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(-1, check.compare(alchemist3, alchemist1));
        assertEquals(1, check.compare(alchemist2, alchemist3));
        assertEquals(-1, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));

        // alchemist 1 now has the most spd down to enemies
        alchemist1.getSkills().add(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.01));
        assertEquals(-1, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(1, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));

        // alchemist 2 now has the most confusion to enemies
        alchemist2.getSkills().add(new Skill(SkillType.CONFUSION, null, SkillTarget.ENEMY_ALL, 0.01));
        assertEquals(0, check.compare(alchemist1, alchemist2));
        assertEquals(0, check.compare(alchemist3, alchemist1));
        assertEquals(0, check.compare(alchemist2, alchemist3));
        assertEquals(0, check.compare(alchemist4, alchemist1));
        assertEquals(0, check.compare(alchemist3, alchemist4));
    }

    // testaa että skillpower 0
}