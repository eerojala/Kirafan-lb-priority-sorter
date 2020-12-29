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
    private GameCharacter alchemist1;
    private GameCharacter alchemist2;
    private GameCharacter alchemist3;
    private GameCharacter alchemist4;
    private GameCharacter knight1;
    private GameCharacter knight2;
    private GameCharacter knight3;
    private GameCharacter knight5;
    private GameCharacter knight6;
    private GameCharacter knight7;
    private GameCharacter knight8;
    private GameCharacter mage1;
    private GameCharacter mage2;
    private GameCharacter mage3;
    private GameCharacter mage4;
    private GameCharacter mage5;
    private GameCharacter mage6;
    private GameCharacter priest1;
    private GameCharacter priest2;
    private GameCharacter priest3;
    private GameCharacter priest4;
    private GameCharacter warrior1;
    private GameCharacter warrior2;
    private GameCharacter warrior3;
    private GameCharacter warrior4;

    @BeforeEach
    void setUp() {
        Series series = new Series("series", null);
        Skill totteoki = new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 3500);

        alchemist1 = new GameCharacter.Builder("alchemist 1", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist2 = new GameCharacter.Builder("alchemist 2", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist3 = new GameCharacter.Builder("alchemist 3", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .build();

        alchemist4 = new GameCharacter.Builder("alchemist 4", series, CharacterElement.WIND, CharacterClass.ALCHEMIST)
                .build();

        List<GameCharacter> fireAlchemists = new ArrayList<>(Arrays.asList(alchemist1, alchemist2, alchemist3));

        knight1 = new GameCharacter.Builder("knight 1", series, CharacterElement.MOON, CharacterClass.KNIGHT)
                .defenseIs(3800)
                .magicDefenseIs(3500)
                .build();

        knight2 = new GameCharacter.Builder("knight 2", series, CharacterElement.MOON, CharacterClass.KNIGHT)
                .defenseIs(3800)
                .magicDefenseIs(3500)
                .build();

        knight3 = new GameCharacter.Builder("knight 3", series, CharacterElement.MOON, CharacterClass.KNIGHT)
                .defenseIs(3800)
                .magicDefenseIs(3500)
                .build();

        GameCharacter knight4 = new GameCharacter.Builder("knight 4", series, CharacterElement.MOON, CharacterClass.KNIGHT)
                .defenseIs(4000)
                .magicDefenseIs(4000)
                .build();

        knight5 = new GameCharacter.Builder("knight 5", series, CharacterElement.WATER, CharacterClass.KNIGHT)
                .defenseIs(8000)
                .magicDefenseIs(1000)
                .build();

        knight6 = new GameCharacter.Builder("knight 6", series, CharacterElement.WATER, CharacterClass.KNIGHT)
                .defenseIs(1000)
                .magicDefenseIs(8000)
                .build();

        knight7 = new GameCharacter.Builder("knight 7", series, CharacterElement.WATER, CharacterClass.KNIGHT)
                .defenseIs(1000)
                .magicDefenseIs(1000)
                .build();

        knight8 = new GameCharacter.Builder("knight 8", series, CharacterElement.SUN, CharacterClass.KNIGHT)
                .defenseIs(4000)
                .magicDefenseIs(4000)
                .build();

        List<GameCharacter> moonKnights = new ArrayList<>(Arrays.asList(knight1, knight2, knight3, knight4));
        List<GameCharacter> waterKnights = new ArrayList<>(Arrays.asList(knight5, knight6, knight7));

        mage1 = new GameCharacter.Builder("mage 1", series, CharacterElement.SUN, CharacterClass.MAGE)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        mage2 = new GameCharacter.Builder("mage 2", series, CharacterElement.SUN, CharacterClass.MAGE)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        mage3 = new GameCharacter.Builder("mage 3", series, CharacterElement.SUN, CharacterClass.MAGE)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        mage4 = new GameCharacter.Builder("mage 4", series, CharacterElement.EARTH, CharacterClass.MAGE)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        mage5 = new GameCharacter.Builder("mage 5", series, CharacterElement.EARTH, CharacterClass.MAGE)
                .offensiveStatIs(5000)
                .withSkill(totteoki)
                .build();

        mage6 = new GameCharacter.Builder("mage 6", series, CharacterElement.MOON, CharacterClass.MAGE)
                .offensiveStatIs(5000)
                .withSkill(totteoki)
                .build();

        GameCharacter mage7 = new GameCharacter.Builder("mage 7", series, CharacterElement.SUN, CharacterClass.MAGE)
                .offensiveStatIs(4500)
                .withSkill(totteoki)
                .build();

        List<GameCharacter> sunMages = new ArrayList<>(Arrays.asList(mage1, mage2, mage3, mage7));
        List<GameCharacter> earthMages = new ArrayList<>(Arrays.asList(mage4, mage5));

        priest1 = new GameCharacter.Builder("priest 1", series, CharacterElement.WIND, CharacterClass.PRIEST).build();
        priest2 = new GameCharacter.Builder("priest 2", series, CharacterElement.WIND, CharacterClass.PRIEST).build();
        priest3 = new GameCharacter.Builder("priest 3", series, CharacterElement.WIND, CharacterClass.PRIEST).build();
        priest4 = new GameCharacter.Builder("priest 4", series, CharacterElement.WATER, CharacterClass.PRIEST).build();

        List<GameCharacter> windPriests = new ArrayList<>(Arrays.asList(priest1, priest2, priest3));

        warrior1 = new GameCharacter.Builder("warrior 1", series, CharacterElement.WATER, CharacterClass.WARRIOR)
                .offensiveStatIs(3000)
                .withSkill(totteoki)
                .build();

        warrior2 = new GameCharacter.Builder("warrior 2", series, CharacterElement.WATER, CharacterClass.WARRIOR)
                .offensiveStatIs(3000)
                .withSkill(totteoki)
                .build();

        warrior3 = new GameCharacter.Builder("warrior 3", series, CharacterElement.WATER, CharacterClass.WARRIOR)
                .offensiveStatIs(3000)
                .withSkill(totteoki)
                .build();

        warrior4 = new GameCharacter.Builder("warrior 4", series, CharacterElement.WIND, CharacterClass.WARRIOR)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        GameCharacter warrior5 = new GameCharacter.Builder("warrior 5", series, CharacterElement.WATER, CharacterClass.WARRIOR)
                .offensiveStatIs(4000)
                .withSkill(totteoki)
                .build();

        List<GameCharacter> waterWarriors = new ArrayList<>(Arrays.asList(warrior1, warrior2, warrior3, warrior5));

        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> map = new HashMap<>();
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.FIRE, CharacterClass.ALCHEMIST), fireAlchemists);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WIND, CharacterClass.ALCHEMIST), new ArrayList<>(Collections.singletonList(alchemist4)));
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.MOON, CharacterClass.KNIGHT),moonKnights);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WATER, CharacterClass.KNIGHT), waterKnights);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.KNIGHT), new ArrayList<>(Collections.singletonList(knight8)));
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.SUN, CharacterClass.MAGE), sunMages);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.EARTH, CharacterClass.MAGE), earthMages);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.MOON, CharacterClass.MAGE), new ArrayList<>(Collections.singletonList(mage6)));
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WIND, CharacterClass.PRIEST), windPriests);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WATER, CharacterClass.PRIEST), new ArrayList<>(Collections.singletonList(priest4)));
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WATER, CharacterClass.WARRIOR), waterWarriors);
        map.put(new AbstractMap.SimpleEntry<>(CharacterElement.WIND, CharacterClass.WARRIOR), new ArrayList<>(Collections.singletonList(warrior4)));

        check = new SkillSetCheck(map);
    }

    @Test
    public void compare_alchemistSkillSet_most_DEF_down_toEnemies_skillpower_isTakenIntoAccount() {
        alchemist1.setLimitBroken(true);
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 35))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));

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
    public void compare_alchemistSkillSet_most_MDF_down_toEnemies_skillpower_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 35))));

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
    public void compare_alchemistSkillSet_most_elementResistance_down_toEnemies_skillpower_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 50))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist4.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 50))));
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
    public void compare_alchemistSkillSet_most_speed_down_toEnemies_skillpower_isTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_ALL, 50))));
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
    public void compare_alchemistSkillSet_most_statusEffect_toEnemies_skillPowers_areTakenIntoAccount() {
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.CONFUSION, null, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.CONFUSION, null, SkillTarget.ALLIES_ALL, 50))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.CONFUSION, null, SkillTarget.ENEMY_SINGLE, 25.01))));

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
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MISFORTUNE, null, SkillTarget.ENEMY_SINGLE, 25))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MISFORTUNE, null, SkillTarget.ENEMY_ALL, 20))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.MISFORTUNE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 50))));

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
        alchemist1.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SLEEP, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 50))));
        alchemist2.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SLEEP, null, SkillTarget.ENEMY_ALL, 25))));
        alchemist3.setSkills(new ArrayList<>(Collections.singletonList(new Skill(SkillType.SLEEP, null, SkillTarget.ENEMY_SINGLE, 25))));
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

    @Test
    public void compare_knightSkillSet_leastPhysicalDamageTaken_isTakenIntoAccount() {
        /*
        * Knights 1-3 should initially get false from the checks because knight 4 has the most physical and magical defense
        * out of all moon knights
        *
        * Knight 5 should get true from the check because she takes the least physical damage out of all the water knights
        */
        assertEquals(0, check.compare(knight1, knight3));
        assertEquals(0, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight2, knight1));
        assertEquals(-1, check.compare(knight5, knight1));
        assertEquals(1, check.compare(knight2, knight5));

        /*
         * After setting the new defense values knight 1 and 2 should now both take the least physical damage (and since
         * neither of them is limit broken, hey should both get true from the physical damage taken check)
         */
        knight1.setDefense(6000);
        knight2.setDefense(6000);
        assertEquals(-1, check.compare(knight1, knight3));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight2, knight1));
        assertEquals(0, check.compare(knight5, knight1));
        assertEquals(0, check.compare(knight2, knight5));

        // After setting knight1 as limit broken, knight2 should not get true from the check anymore
        knight1.setLimitBroken(true);
        assertEquals(-1, check.compare(knight1, knight3));
        assertEquals(0, check.compare(knight3, knight2));
        assertEquals(1, check.compare(knight2, knight1));
        assertEquals(0, check.compare(knight5, knight1));
        assertEquals(1, check.compare(knight2, knight5));

        // After setting knight 2's defense as 7000, she should get true and knight 1 should get false from the check
        knight2.setDefense(7000);
        assertEquals(0, check.compare(knight1, knight3));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(-1, check.compare(knight2, knight1));
        assertEquals(-1, check.compare(knight5, knight1));
        assertEquals(0, check.compare(knight2, knight5));
    }

    @Test
    public void compare_knightSkillSet_leastMagicalDamageTaken_isTakenIntoAccount() {
        // After setting knight 2's magic defense to 6k, she should get true from the check
        // Knight 6 should get get true from the check because she is the water knight with most magic defense
        knight2.setMagicDefense(6000);
        knight2.setLimitBroken(true);
        assertEquals(-1, check.compare(knight2, knight1));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(0,check.compare(knight1, knight3));
        assertEquals(-1, check.compare(knight6, knight3));
        assertEquals(0, check.compare(knight6, knight2));

        // after setting her magic defense to 7000, knight 3 should get true from the check while knight 2 should get false
        knight3.setMagicDefense(7000);
        assertEquals(0, check.compare(knight2, knight1));
        assertEquals(-1, check.compare(knight3, knight2));
        assertEquals(1, check.compare(knight1, knight3));
        assertEquals(0, check.compare(knight6, knight3));
        assertEquals(-1, check.compare(knight6, knight2));
    }

    @Test
    public void compare_knightSkillSet_most_amountOf_abnormalEffectDisable_toAllies_skills_isTakenIntoAccount() {
        // knight 1 and 2 both should get true because they have the most amount of abnormal disable to allies skills
        // knight 3's skills should not be taken into account because her skills have the wrong change or wrong target
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));

        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));

        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));

        assertEquals(-1, check.compare(knight1, knight3));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight2, knight1));

        // After knight 1 is set to be limit broken, only knight 1 should get true)
        knight1.setLimitBroken(true);
        assertEquals(-1, check.compare(knight1, knight3));
        assertEquals(0, check.compare(knight3, knight2));
        assertEquals(1, check.compare(knight2, knight1));

        // After knight 2 gets another abnormal disable to allies skill only knight 2 should get true
        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        assertEquals(0, check.compare(knight1, knight3));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(-1, check.compare(knight2, knight1));
    }

    @Test
    public void compare_knightSkillSet_most_amountOf_abnormalEffectRecover_toAllies_skills_isTakenIntoAccount() {
        // only knight 2 should get true because she has  the most abnormal recover to allies skills
        // knight 3 should return false because her skill have the wrong change (not null) or wrong target (enemies)
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0));
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0));

        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0));

        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_ALL, 0));

        assertEquals(-1, check.compare(knight2, knight1));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight1, knight3));
    }

    @Test
    public void compare_knightSkillSet_most_amountOf_singleBarrier_toAllAllies_skills_isTakenIntoAccount() {
        // Only knight 3 should get true because the others have the wrong targets (not allies all) or the wrong change (not null)
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_SINGLE, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_SINGLE, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_SINGLE, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));

        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_SINGLE, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_SINGLE, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_SINGLE, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.DOWN,SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.DOWN,SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.DOWN,SkillTarget.ALLIES_ALL, 0));

        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null,SkillTarget.ALLIES_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null,SkillTarget.ALLIES_ALL, 0));

        assertEquals(-1, check.compare(knight3, knight1));
        assertEquals(1, check.compare(knight2, knight3));
        assertEquals(0, check.compare(knight1, knight2));
    }

    @Test
    public void compare_knightSkillSet_most_amountOf_tripleBarrier_toSelf_skills_isTakenIntoAccount() {
        // only knight 1 should get true because she has the most triple barrier to self skills
        // others have wrong skills (wrong change or target)
        knight1.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.SELF, 0));

        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, SkillChange.UP, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, SkillChange.UP, SkillTarget.SELF, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ALLIES_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ENEMY_ALL, 0));
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ENEMY_ALL, 0));

        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, SkillChange.DOWN, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, SkillChange.DOWN, SkillTarget.SELF, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.ENEMY_SINGLE, 0));

        assertEquals(-1, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
    }

    @Test
    public void compare_knightSkillSet_most_amountOf_damage_toAllEnemies_skills_isTakenIntoAccount() {
        // knight 2 should get false because she has below 2 damage all enemies skills
        // knight 1 and 3 should get false because their skills are wrong (wrong target or change)

        knight1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.SELF, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.SELF, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_ALL, 0));

        knight2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));

        knight3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        knight3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        knight3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));

        assertEquals(0, check.compare(knight2, knight1));
        assertEquals(0, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight1, knight3));

        // After knight 2 gets another damage to all enemies skill (=total amount is now 2), she should get true
        knight2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        assertEquals(-1, check.compare(knight2, knight1));
        assertEquals(1, check.compare(knight3, knight2));
        assertEquals(0, check.compare(knight1, knight3));
    }

    @Test
    public void compare_knightSkillSet_all_areTakenIntoAccount() {
        // After setting knight 1's defense to 10k, only she should get true
        // Knight 5 has the most defense of all water knights, and she should get true
        // Knight 7 has no skills and the least defense and magic defense of all water knights, so she should get false
        knight1.setDefense(10000);
        assertEquals(-1, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
        assertEquals(-1, check.compare(knight5, knight3));
        assertEquals(0, check.compare(knight7, knight2));
        assertEquals(0, check.compare(knight7, knight3));

        // After setting knight 2's magic defense to 10k, she should get true as well
        knight2.setMagicDefense(10000);
        assertEquals(0, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(-1, check.compare(knight2, knight3));
        assertEquals(-1, check.compare(knight5, knight3));
        assertEquals(1, check.compare(knight7, knight2));
        assertEquals(0, check.compare(knight7, knight3));

        // After adding an abnormal effect disable allies skill to knight 3, she should get true as well
        knight3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(0, check.compare(knight1, knight2));
        assertEquals(0, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
        assertEquals(0, check.compare(knight5, knight3));
        assertEquals(1, check.compare(knight7, knight2));
        assertEquals(1, check.compare(knight7, knight3));

        // ** RESET SKILLS AND STATS BACK TO NORMAL **
        knight1.setDefense(3800);
        knight2.setMagicDefense(3500);
        knight3.setSkills(new ArrayList<>());

        // After adding an abnormal effect recover skill allies to knight 1, she should get true
        knight1.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0));
        assertEquals(-1, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
        assertEquals(-1, check.compare(knight5, knight3));
        assertEquals(0, check.compare(knight7, knight2));
        assertEquals(0, check.compare(knight7, knight3));

        // After adding an all allies 1x barrier skill to knight 2, she should get true
        knight2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(0, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(-1, check.compare(knight2, knight3));
        assertEquals(-1, check.compare(knight5, knight3));
        assertEquals(1, check.compare(knight7, knight2));
        assertEquals(0, check.compare(knight7, knight3));

        // After adding an self triple barrier skill to knight 3, she should get true
        knight3.getSkills().add(new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.SELF, 0));
        assertEquals(0, check.compare(knight1, knight2));
        assertEquals(0, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
        assertEquals(0, check.compare(knight5, knight3));
        assertEquals(1, check.compare(knight7, knight2));
        assertEquals(1, check.compare(knight7, knight3));

        // ** RESET SKILLS **
        knight1.setSkills(new ArrayList<>());
        knight2.setSkills(new ArrayList<>());
        knight3.setSkills(new ArrayList<>());

        // After adding two damage all enemies skill to knight1, she should get true
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        knight1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        assertEquals(-1, check.compare(knight1, knight2));
        assertEquals(1, check.compare(knight3, knight1));
        assertEquals(0, check.compare(knight2, knight3));
        assertEquals(-1, check.compare(knight5, knight3));
        assertEquals(0, check.compare(knight7, knight2));
        assertEquals(0, check.compare(knight7, knight3));
    }

    @Test
    public void compare_mageSkillSet_maxDamageCaused_isTakenIntoAccount() {
        /*
        * Mages 1-3 should all get false because they are sun mages and have the same totteoki, equal offensive stat and
        * no other skills (mage 6 has a bigger offensive stat than them)
        * Mage 4-5 are earth mages and 5 should get true because she has more offensive stat than mage 4, while mage 4
        * should get false
        * */
        assertEquals(0, check.compare(mage1, mage2));
        assertEquals(0, check.compare(mage2, mage3));
        assertEquals(0, check.compare(mage3, mage1));
        assertEquals(0, check.compare(mage4, mage1));
        assertEquals(-1, check.compare(mage5, mage1));

        // After setting their offensive power to 5k, mage1 should get true
        mage1.setOffensiveStat(5000);
        assertEquals(-1, check.compare(mage1, mage2));
        assertEquals(0, check.compare(mage2, mage3));
        assertEquals(1, check.compare(mage3, mage1));
        assertEquals(1, check.compare(mage4, mage1));
        assertEquals(0, check.compare(mage5, mage1));

        // After setting mage 2's offensive stat to 5k, both mage 1 and 2 should get true
        mage2.setOffensiveStat(5000);
        assertEquals(0, check.compare(mage1, mage2));
        assertEquals(-1, check.compare(mage2, mage3));
        assertEquals(1, check.compare(mage3, mage1));
        assertEquals(1, check.compare(mage4, mage1));
        assertEquals(0, check.compare(mage5, mage1));

        // After setting mage 1 as limit broken, mage 2 should not get true anymore
        mage1.setLimitBroken(true);
        assertEquals(-1, check.compare(mage1, mage2));
        assertEquals(0, check.compare(mage2, mage3));
        assertEquals(1, check.compare(mage3, mage1));
        assertEquals(1, check.compare(mage4, mage1));
        assertEquals(0, check.compare(mage5, mage1));
    }

    @Test
    public void compare_mageSkillSet_mostAmountOf_damage_toAllEnemies_skills_isTakenIntoAccount() {
        // Mage 1 and 3 should get false because they have the false skill (wrong change or target)
        // Mage 2 should get false because she only has one damage all enemies skill
        // Mage 4 should get false because she doesn't have any damage all enemies skill
        mage1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        mage1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        mage1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));
        mage1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));

        mage2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));

        mage3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        mage3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));

        assertEquals(0, check.compare(mage2, mage1));
        assertEquals(0, check.compare(mage3, mage2));
        assertEquals(0, check.compare(mage1, mage3));
        assertEquals(0, check.compare(mage4, mage2));
        assertEquals(0, check.compare(mage1, mage4));

        // After mage 2 gets another damage all enemies skill, she should get true (biggest amount within sun mages)
        // After mage 4 gets 2 damage all enemies skills, she should get true  (biggest amount within earth mages)
        mage2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage4.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage4.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        assertEquals(-1, check.compare(mage2, mage1));
        assertEquals(1, check.compare(mage3, mage2));
        assertEquals(0, check.compare(mage1, mage3));
        assertEquals(0, check.compare(mage4, mage2));
        assertEquals(1, check.compare(mage1, mage4));

        // After mage 3 gets 3 damage all enemies skills, she should get true and mage 2 should get false
        mage3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        assertEquals(0, check.compare(mage2, mage1));
        assertEquals(-1, check.compare(mage3, mage2));
        assertEquals(1, check.compare(mage1, mage3));
        assertEquals(-1, check.compare(mage4, mage2));
        assertEquals(1, check.compare(mage1, mage4));
    }

    @Test
    public void compare_mageSkillSet_all_areTakenIntoAccount() {
        // After setting her offensive stat to 10k, mage1 should get true
        mage1.setOffensiveStat(10000);
        assertEquals(-1, check.compare(mage1, mage2));
        assertEquals(1, check.compare(mage3, mage1));
        assertEquals(0, check.compare(mage2, mage3));
        assertEquals(1, check.compare(mage4, mage1));
        assertEquals(-1, check.compare(mage5, mage2));

        // After getting 2 damage all enemies skills, mage2 and 4 should get true
        mage2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage4.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        mage4.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        assertEquals(0, check.compare(mage1, mage2));
        assertEquals(1, check.compare(mage3, mage1));
        assertEquals(-1, check.compare(mage2, mage3));
        assertEquals(0, check.compare(mage4, mage1));
        assertEquals(0, check.compare(mage5, mage2));
    }

    @Test
    public void compare_priestSkillSet_mostAmountOf_healCard_skills_isTakenIntoAccount() {
        // Priest 1 should get true because she has the most amount of heal card skills
        // Priest 2 and 3 should get false because they don't have the correct skill (wrong target or change)
        priest1.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_ALL, 0));

        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.SELF, 0));
        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.SELF, 0));
        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ENEMY_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ENEMY_ALL, 0));

        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ENEMY_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ENEMY_SINGLE, 0));
    }

    @Test
    public void compare_priestSkillSet_mostAmountOf_abnormalEffectClear_toAllAndSingleAlly_skills_isTakenIntoAccount() {
        // Priest 1 should get false because she does not have the correct abnormal disable skills (wrong change or target)
        // Priest 2 and 3 should both get true because they both have the most abnormal disable to a single ally/all allies skills
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_SINGLE, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ENEMY_ALL, 0));


        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0));

        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0));

        assertEquals(-1, check.compare(priest2, priest1));
        assertEquals(1, check.compare(priest1, priest3));
        assertEquals(0, check.compare(priest2, priest3));
    }

    @Test
    public void compare_priestSkillSet_mostAmountOf_abnormalEffectDisable_toAllAndSingleAlly_skills_isTakenIntoAccount() {
        // Priest 1 and 3 have the most abnormal recover skills, and should get true
        // Priest 2 should get false because she has the wrong skills (wrong change and target)
        priest1.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0));

        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.SELF, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_SINGLE, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_ALL, 0));
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ENEMY_ALL, 0));

        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_ALL, 0));

        assertEquals(-1, check.compare(priest1, priest2));
        assertEquals(1, check.compare(priest2, priest3));
        assertEquals(0, check.compare(priest3, priest1));
    }

    @Test
    public void compare_priestSkillSet_mostAmountOf_singleFullBarrier_toAllAllies_skills_isTakenIntoAccount() {
        // Priest 1 and 3 should get false because they have the wrong skills (wrong change or target)
        // Priest 2 should get true because she has the most single full barrier to all allies skills
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.SELF, 0));
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 0));
        priest1.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 0));

        priest2.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_ALL, 0));

        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_SINGLE, 0));
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_SINGLE, 0));

        assertEquals(-1, check.compare(priest2, priest1));
        assertEquals(1, check.compare(priest3, priest2));
        assertEquals(0, check.compare(priest1, priest3));
    }

    @Test
    public void compare_priestSkillSet_most_SPD_UP_toAllies_skillpower_isTakenIntoAccount() {
        // priest 2 should get false because she has the wrong skill (wrong change)
        // priest 3 should get false because her spd UP allies skill power is 0
        priest2.getSkills().add(new Skill(SkillType.SPD, null, SkillTarget.ALLIES_SINGLE, 100));
        priest3.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));

        // After adding the skill, priest 1 should get true because she has the most SPD up allies skill power
        priest1.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_ALL, 25));
        assertEquals(-1, check.compare(priest1, priest2));
        assertEquals(1, check.compare(priest3, priest1));
        assertEquals(0, check.compare(priest2, priest3));

        // After adding the skill, priest 3 should also get true because she is tied with priest 1 for most SPD up to allies skill power
        priest3.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 25));
        assertEquals(-1, check.compare(priest1, priest2));
        assertEquals(0, check.compare(priest3, priest1));
        assertEquals(1, check.compare(priest2, priest3));
    }

    @Test
    public void compare_priestSkillSet_all_areTakenIntoAccount() {
        // at first everyone returns false because they have no desired skillset
        assertEquals(0, check.compare(priest1, priest2));
        assertEquals(0, check.compare(priest3, priest1));
        assertEquals(0, check.compare(priest2, priest3));

        // After adding the skill priest 1 has the most skill card to all allies skills and should get true
        priest1.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(-1, check.compare(priest1, priest2));
        assertEquals(1, check.compare(priest3, priest1));
        assertEquals(0, check.compare(priest2, priest3));

        // After adding the skill priest 2 has the most abnormal effect disable to allies skills and should get true
        priest2.getSkills().add(new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0));
        assertEquals(0, check.compare(priest1, priest2));
        assertEquals(1, check.compare(priest3, priest1));
        assertEquals(-1, check.compare(priest2, priest3));

        // After adding the skill priest 3 has the most abnormal effect recover to allies skills and should get true
        priest3.getSkills().add(new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(0, check.compare(priest1, priest2));
        assertEquals(0, check.compare(priest3, priest1));
        assertEquals(0, check.compare(priest2, priest3));

        // ** SKILLS ARE RESET **
        priest1.setSkills(new ArrayList<>());
        priest2.setSkills(new ArrayList<>());
        priest3.setSkills(new ArrayList<>());

        // After adding the skill priest 3 has the most ally 1x barrier skills and should get true
        priest3.getSkills().add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(0, check.compare(priest1, priest2));
        assertEquals(-1, check.compare(priest3, priest1));
        assertEquals(1, check.compare(priest2, priest3));

        // After adding the skill priest 2 has the most SPD UP to allies skillpower and should get true
        priest2.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.SELF, 0.01));
        assertEquals(1, check.compare(priest1, priest2));
        assertEquals(-1, check.compare(priest3, priest1));
        assertEquals(0, check.compare(priest2, priest3));
    }

    @Test
    public void compare_warriorSkillSet_maxDamageCaused_isTakenIntoAccount() {
        // Initially warriors 1-3 get false because warrior4 has higher max damage than them
        assertEquals(0, check.compare(warrior1, warrior2));
        assertEquals(0, check.compare(warrior3, warrior1));
        assertEquals(0, check.compare(warrior2, warrior3));

        // After warrior 1 gets a new weapon, she has the biggest max damage and gets true
        warrior1.setPreferredWeapon(new Weapon.Builder("weapon").offensiveStatIs(2000).build());
        assertEquals(-1, check.compare(warrior1, warrior2));
        assertEquals(1, check.compare(warrior3, warrior1));
        assertEquals(0 , check.compare(warrior2, warrior3));
    }

    @Test
    public void compare_warriorSkillSet_mostAmountOf_damageSingleEnemy_skills_isTakenIntoAccount() {
        // Warrior 1 and 3 get false because their skills are not correct (wrong change or target)
        // Warrior 2 gets false because she has under 2 damage single enemy skills
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.SELF, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.SELF, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_SINGLE, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_SINGLE, 0));

        warrior2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));

        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0));
        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0));
        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_ALL, 0));
        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLIES_ALL, 0));
        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));
        warrior3.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0));

        assertEquals(0, check.compare(warrior2, warrior1));
        assertEquals(0, check.compare(warrior3, warrior2));
        assertEquals(0, check.compare(warrior1, warrior3));

        // After adding another damage single enemy skill warrior 2 should get true
        warrior2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        assertEquals(-1, check.compare(warrior2, warrior1));
        assertEquals(1, check.compare(warrior3, warrior2));
        assertEquals(0, check.compare(warrior1, warrior3));
    }

    @Test
    public void compare_warriorSkillSet_all_areTakenIntoAccount() {
        // After adding the skill, warrior 3 should have the highest max damage and therefore get true
        warrior3.getSkills().add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 100));
        assertEquals(-1, check.compare(warrior3, warrior1));
        assertEquals(1, check.compare(warrior2, warrior3));
        assertEquals(0, check.compare(warrior1, warrior2));

        // After adding two damage single enemy skills, warrior 2 should get true
        warrior2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        warrior2.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        assertEquals(-1, check.compare(warrior3, warrior1));
        assertEquals(0, check.compare(warrior2, warrior3));
        assertEquals(1, check.compare(warrior1, warrior2));
    }

    @Test
    public void compare_comparingDifferentClasses_worksCorrectly() {
        // Initially all characters should get false
        assertEquals(0, check.compare(alchemist1, knight1));
        assertEquals(0, check.compare(mage1, alchemist1));
        assertEquals(0, check.compare(alchemist1, priest1));
        assertEquals(0, check.compare(warrior1, alchemist1));
        assertEquals(0, check.compare(knight1, mage1));
        assertEquals(0, check.compare(priest1, knight1));
        assertEquals(0, check.compare(knight1, warrior1));
        assertEquals(0, check.compare(priest1, mage1));
        assertEquals(0, check.compare(mage1, warrior1));
        assertEquals(0, check.compare(warrior1, priest1));

        // After adding the skill alchemist 1 should get true
        alchemist1.getSkills().add(new Skill(SkillType.SPD, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.01));
        assertEquals(-1, check.compare(alchemist1, knight1));
        assertEquals(1, check.compare(mage1, alchemist1));
        assertEquals(-1, check.compare(alchemist1, priest1));
        assertEquals(1, check.compare(warrior1, alchemist1));
        assertEquals(0, check.compare(knight1, mage1));
        assertEquals(0, check.compare(priest1, knight1));
        assertEquals(0, check.compare(knight1, warrior1));
        assertEquals(0, check.compare(priest1, mage1));
        assertEquals(0, check.compare(mage1, warrior1));
        assertEquals(0, check.compare(warrior1, priest1));

        // After setting a new defense value knight 1 should get true
        knight1.setDefense(10000);
        assertEquals(0, check.compare(alchemist1, knight1));
        assertEquals(1, check.compare(mage1, alchemist1));
        assertEquals(-1, check.compare(alchemist1, priest1));
        assertEquals(1, check.compare(warrior1, alchemist1));
        assertEquals(-1, check.compare(knight1, mage1));
        assertEquals(1, check.compare(priest1, knight1));
        assertEquals(-1, check.compare(knight1, warrior1));
        assertEquals(0, check.compare(priest1, mage1));
        assertEquals(0, check.compare(mage1, warrior1));
        assertEquals(0, check.compare(warrior1, priest1));

        // After setting a new offensive stat value mage 1 should get true
        mage1.setOffensiveStat(6000);
        assertEquals(0, check.compare(alchemist1, knight1));
        assertEquals(0, check.compare(mage1, alchemist1));
        assertEquals(-1, check.compare(alchemist1, priest1));
        assertEquals(1, check.compare(warrior1, alchemist1));
        assertEquals(0, check.compare(knight1, mage1));
        assertEquals(1, check.compare(priest1, knight1));
        assertEquals(-1, check.compare(knight1, warrior1));
        assertEquals(1, check.compare(priest1, mage1));
        assertEquals(-1, check.compare(mage1, warrior1));
        assertEquals(0, check.compare(warrior1, priest1));

        // After adding a new skill priest 1 should get true
        priest1.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_ALL, 0));
        assertEquals(0, check.compare(alchemist1, knight1));
        assertEquals(0, check.compare(mage1, alchemist1));
        assertEquals(0, check.compare(alchemist1, priest1));
        assertEquals(1, check.compare(warrior1, alchemist1));
        assertEquals(0, check.compare(knight1, mage1));
        assertEquals(0, check.compare(priest1, knight1));
        assertEquals(-1, check.compare(knight1, warrior1));
        assertEquals(0, check.compare(priest1, mage1));
        assertEquals(-1, check.compare(mage1, warrior1));
        assertEquals(1, check.compare(warrior1, priest1));

        // After adding two damage single enemy skills warrior 1 should get true
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        warrior1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0));
        assertEquals(0, check.compare(alchemist1, knight1));
        assertEquals(0, check.compare(mage1, alchemist1));
        assertEquals(0, check.compare(alchemist1, priest1));
        assertEquals(0, check.compare(warrior1, alchemist1));
        assertEquals(0, check.compare(knight1, mage1));
        assertEquals(0, check.compare(priest1, knight1));
        assertEquals(0, check.compare(knight1, warrior1));
        assertEquals(0, check.compare(priest1, mage1));
        assertEquals(0, check.compare(mage1, warrior1));
        assertEquals(0, check.compare(warrior1, priest1));
    }

    @Test
    public void compare_worksProperly_whenComparing_characters_whoAreTheSoleMembersOfTheirElementClassCombination() {
        // alchemist4 has none of the desired skills
        // knight8 takes the least physical and magical damage out of all sun knights (because she is the only one)
        assertEquals(1, check.compare(alchemist4, knight8));

        // mage6 has the biggest max damage out all moon mages (because she is the only one)
        assertEquals(0, check.compare(knight8, mage6));

        // priest4 has none of the desired skills
        assertEquals(-1, check.compare(mage6, priest4));

        // warrior has the biggest max damage out of all wind warrior (because she is the only one)
        assertEquals(1, check.compare(priest4, warrior4));
    }
}