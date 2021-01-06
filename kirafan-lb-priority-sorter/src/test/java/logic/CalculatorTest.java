package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Map<Skill, Double> skillPowerTotals;
    private GameCharacter chara1;
    private GameCharacter chara2;
    private GameCharacter chara3;
    private Weapon weapon1;
    private Weapon weapon2;
    private long chara1ResultAfterChanges;
    private long chara1ResultBeforeChanges;
    private long chara2ResultAfterChanges;
    private long chara2ResultBeforeChanges;
    private long defResultAfterChanges;
    private long defResultBeforeChanges;
    private long mdfResultAfterChanges;
    private long mdfResultBeforeChanges;

    @BeforeEach
    public void setUp() {
        skillPowerTotals = new HashMap<>();
        Series series = new Series("series", null);
        chara1 = new GameCharacter.Builder("warrior", series, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .offensiveStatIs(2938)
                .withSkill(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_SINGLE, 4695))
                .build();

        chara2 = new GameCharacter.Builder("mage", series, CharacterElement.WIND, CharacterClass.MAGE)
                .offensiveStatIs(2745)
                .withSkill(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 3759))
                .build();

        chara3 = new GameCharacter.Builder("knight", series, CharacterElement.MOON, CharacterClass.KNIGHT)
                .defenseIs(5196)
                .magicDefenseIs(3816)
                .build();

        weapon1 = new Weapon.Builder("weapon 1")
                .overwriteID("1")
                .offensiveStatIs(90)
                .withSkill(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 24.0))
                .build();

        weapon2 = new Weapon.Builder("weapon 2")
                .overwriteID("2")
                .defenseIs(200)
                .magicDefenseIs(200)
                .withSkill(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 24.0))
                .build();

        chara1ResultBeforeChanges = Calculator.calculateMaxDamageCaused(chara1);
        chara2ResultBeforeChanges = Calculator.calculateMaxDamageCaused(chara2);
        defResultBeforeChanges = Calculator.calculateDamageTaken(chara3, SkillType.DEF);
        mdfResultBeforeChanges = Calculator.calculateDamageTaken(chara3, SkillType.MDF);
    }

    private boolean chara1And2HaveOnlyTotteoki() {
        // chara1 and chara2 should only have the totteoki which they were constructed with in the method setUp
        return chara1And2HaveNAmountOfSkills(1);
    }

    private boolean chara1And2HaveNAmountOfSkills(int n) {
        return chara1.getSkills().size() == n && chara2.getSkills().size() == n;
    }

    private boolean chara3HasNoSkills() {
        return chara3HasNAmountOfSkills(0);
    }

    private boolean chara3HasNAmountOfSkills(int n) {
        return chara3.getSkills().size() == n;
    }

    private void addSkillsToCharas(List<Skill> skills) {
        chara1.getSkills().addAll(skills);
        chara2.getSkills().addAll(skills);
        chara3.getSkills().addAll(skills);
    }

    private void calculateMaxDamage() {
        // NOTE: Use this function only after adding the desired skills in the test
        chara1ResultAfterChanges = Calculator.calculateMaxDamageCaused(chara1);
        chara2ResultAfterChanges = Calculator.calculateMaxDamageCaused(chara2);
    }

    private void calculateDamageTaken() {
        // NOTE: Use this function only after adding the desired skills in the test
        defResultAfterChanges = Calculator.calculateDamageTaken(chara3, SkillType.DEF);
        mdfResultAfterChanges = Calculator.calculateDamageTaken(chara3, SkillType.MDF);

    }

    private boolean acceptableResult(long expected, long result) {
        return acceptableResult(expected, result, Math.abs(expected / 100));
    }

    private boolean acceptableResult(long expected, long result, long maxDeviation) {
        // Some times the results vary by under 1% (especially when modifying the defensive buff multiplier) from values given by the kirafan.moe calculator
        // But most of the time the results vary very little (under 10) or do not vary at all from the kirafan.moe calculator
        System.out.println("Expected: " + expected);
        System.out.println("Result: " + result);
        System.out.println("Difference: " + Math.abs(expected - result));
        System.out.println("---------------");
        return Math.abs(expected - result) <= maxDeviation;
    }

    private boolean damageGivenResultsAreDifferentAfterChanges() {
        return chara1ResultsAreDifferentAfterChanges() && chara2ResultsAreDifferentAfterChanges();
    }

    private boolean chara1ResultsAreDifferentAfterChanges() {
        return chara1ResultAfterChanges != chara1ResultBeforeChanges;
    }

    private boolean chara2ResultsAreDifferentAfterChanges() {
        return chara2ResultAfterChanges != chara2ResultBeforeChanges;
    }

    private boolean damageGivenResultsAreSameAfterChanges() {
        return chara1ResultsAreSameAfterChanges() && chara2ResultsAreSameAfterChanges();
    }

    private boolean chara1ResultsAreSameAfterChanges() {
        return chara1ResultAfterChanges == chara1ResultBeforeChanges;
    }

    private boolean chara2ResultsAreSameAfterChanges() {
        return chara2ResultAfterChanges == chara2ResultBeforeChanges;
    }

    private boolean damageTakenResultsAreSameAfterChanges() {
        return defResultsAreSameAfterChanges() && mdfResultsAreSameAfterChanges();
    }

    private boolean defResultsAreSameAfterChanges() {
        return defResultAfterChanges == defResultBeforeChanges;
    }

    private boolean mdfResultsAreSameAfterChanges() {
        return mdfResultAfterChanges == mdfResultBeforeChanges;
    }

    private boolean damageTakenResultsAreDifferentAfterChanges() {
        return defResultsAreDifferentAfterChanges() && mdfResultsAreDifferentAfterChanges();
    }

    private boolean defResultsAreDifferentAfterChanges() {
        return defResultAfterChanges != defResultBeforeChanges;
    }

    private boolean mdfResultsAreDifferentAfterChanges() {
        return mdfResultAfterChanges != mdfResultBeforeChanges;
    }

    /*
    * The following tests are compared with values which have been calculated using kirafan.moe damage calculator at
    * https://calc.kirafan.moe/#/damage#damage with the following default parameters for all tests:
    *   Defence = 200
    *   Critical = Critical
    *   Element = Weak (meaning that the enemy's element is weak against the character's element, e.g. fire is weak against water)
    *   Kirara Jump = 1st (Referred to as totteoki chain in my comments in the function)
    *
    * and rest of the parameters change depending on the test.
    *
    * My CalculateMaxDamage function always assumes a max damage hit, while the kirafan.moe calculator gives the possible
    * range of damage, e.g. as 23861 +- 1935, so the comparison is then made against 23861 + 1935.
    * */

    @Test
    public void calculateMaxDamageCaused_returnsZeroForEveryClassExceptMageAndWarrior() {
        assertNotEquals(0, Calculator.calculateMaxDamageCaused(chara1)); // chara1.charcterClass = WARRIOR
        assertNotEquals(0, Calculator.calculateMaxDamageCaused(chara2)); // chara2.characterClass = MAGE

        chara2.setCharacterClass(CharacterClass.KNIGHT);
        assertEquals(0, Calculator.calculateMaxDamageCaused(chara2));

        chara2.setCharacterClass(CharacterClass.PRIEST);
        assertEquals(0, Calculator.calculateMaxDamageCaused(chara2));

        chara2.setCharacterClass(CharacterClass.ALCHEMIST);
        assertEquals(0, Calculator.calculateMaxDamageCaused(chara2));
    }

    @Test
    public void CalculateMaxDamageCaused_calculatesDamageBasedOnTotteokiStrengthCorrectly() {
        assertTrue(chara1And2HaveOnlyTotteoki());
        // chara1 and chara2 should only have the totteoki which they were constructed with in the method setUp
        assertTrue(acceptableResult(31898 + 2586, Calculator.calculateMaxDamageCaused(chara1)));
        assertTrue(acceptableResult(23861 + 1935, Calculator.calculateMaxDamageCaused(chara2)));

        chara1.setSkills(new ArrayList<Skill>(Arrays.asList(
                new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 2000),
                new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 3000))));
        // A damage skill is added to make sure that damage really is calculated on totteoki and not the skill with highest damage
        // (Though damage skills should never have more raw attack power than a totteoki in the first place)
        chara2.setSkills(new ArrayList<Skill>(Arrays.asList(
                new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_SINGLE, 1000),
                new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 2000))));

        assertTrue(chara1And2HaveNAmountOfSkills(2));
        assertTrue(acceptableResult(13588 + 1102, Calculator.calculateMaxDamageCaused(chara1)));
        assertTrue(acceptableResult(6348 + 515, Calculator.calculateMaxDamageCaused(chara2)));
    }

    @Test
    public void calculateMaxDamageCaused_calculatesWeaponAttackPowerAndSkillsCorrectly() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        chara1.setPreferredWeapon(weapon1);
        chara2.setPreferredWeapon(weapon1);
        calculateMaxDamage();

        assertTrue(chara1And2HaveOnlyTotteoki());
        assertTrue(damageGivenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(40758 + 3305, chara1ResultAfterChanges));
        assertTrue(acceptableResult(24644 + 1998, chara2ResultAfterChanges));
        // chara2 is a mage so the weapon's +24.0% ATK skill should not affect the calculation since Mages use the MAT
        // stat for their damage (but the weapon's base offensive power should)
    }

    @Test
    public void calculateMaxDamageCaused_offensiveBuffMultiplier_isAffectedBy_character_ATK_or_MAT() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 10.4));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 2.0));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 3.2));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 0.5));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_ALL, 36.3));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_ALL, 1.3));
        // Total ATK UP: 53.7
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_SELF, 15.6));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_SELF, 2.4));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 10.75));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 1));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_ALL, 1.5));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_ALL, 2.6));
        // Total ATK DOWN: 33.85
        // Total ATK: 53.7 - 33.85 = 19.85 (UP)

        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_SELF, 13.79));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_SELF, 5.32));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 40.2));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 1.7));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_ALL, 15.1));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_ALL, 6.77));
        // Total MAT UP: 82.88
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 11.0));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 5.56));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 10.31));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 3.5));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_ALL, 3.6));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_ALL, 8.9));
        // Total MAT DOWN: 42.87
        // Total MAT: 82.88 - 42.87 = 40.01 (UP)

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(25));
        assertTrue(damageGivenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(38228 + 3100, chara1ResultAfterChanges));
        assertTrue(acceptableResult(33423 + 2710, chara2ResultAfterChanges));
        // MAT (de)buffs should not influence the result for a warrior
        // ATK (debuffs) should not influence the result for a mage
    }

    @Test
    public void calculateMaxDamageCaused_offensiveBuffMultiplier_isNotAffectedBy_enemy_ATK_or_MAT() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ENEMY_ALL, 50));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 5));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 6));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ENEMY_ALL, 50));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 5));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 6));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(9));
        assertTrue(damageGivenResultsAreSameAfterChanges());
        // Enemy's ATK/MAT (de)buffs should not influence the result
    }

    @Test
    public void calculateMaxDamageCaused_offensiveBuffMultiplier_doesNotIncreasBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 149.9));
        assertTrue(acceptableResult(79713 + 6463, Calculator.calculateMaxDamageCaused(chara1)));
        // Multiplier's max value is 2.5 (1 + 1.5)

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 0.1));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 0.1));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 10));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamageCaused(chara1)));
    }

    @Test
    public void calculateMaxDamageCaused_offensiveBuffMultiplier_doesNotDecreaseBelowMinValue() {
        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_ALL, 49.9));
        assertTrue(acceptableResult(11952 + 969, Calculator.calculateMaxDamageCaused(chara2)));
        // Multiplier's min value is 0.5 (1 - 0.5)

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 0.1));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamageCaused(chara2)));

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 001));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamageCaused(chara2)));

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 10));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamageCaused(chara2)));
    }

    @Test
    public void calculateMaxDamageCaused_nextAttackBuffMultiplier_isAffectedBy_character_NEXTATKUP_or_NEXTMATUP() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 50.13));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 200.36));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 160));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SINGLE, 170));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_ALL, 150));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_ALL, 150));
        // The chosen NEXT ATK UP should be 200.36

        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SELF, 15.13));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SELF, 350.32));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 10));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 350.33));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 10));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 250.11));
        // The chosen NEXT MAT UP should be 350.33

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(13));
        assertTrue(damageGivenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(95810 + 7768, chara1ResultAfterChanges));
        assertTrue(acceptableResult(107455 + 8713, chara2ResultAfterChanges));
        // only the strongest next attack buff should influence the result (they do not stack unlike the other buffs and debuffs)
        // For warriors only NEXT ATK UP and for mages NEXT MAT UP should influence the result
    }

    @Test
    public void calculateMaxDamageCaused_nextAttackBuffMultiplier_isNotAffectedBy_character_NEXTATKDOWN_or_NEXTMATDOWN() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLY_SELF, 100));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 110));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLY_ALL, 120));

        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 130));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 140));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLY_ALL, 150));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(7));
        assertTrue(damageGivenResultsAreSameAfterChanges());
        // Character NEXT ATK DOWN and NEXT MAT DOWN debuffs should not influence the result
    }

    @Test
    public void calculateMaxDamageCaused_nextAttackBuffMultiplier_isNotAffectedBy_enemy_NEXTATK_or_NEXTMAT() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 100));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ENEMY_ALL, 110));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 120));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 130));

        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 140));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ENEMY_ALL, 150));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 160));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN,SkillTarget.ENEMY_ALL, 170));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(9));
        assertTrue(damageGivenResultsAreSameAfterChanges());
        // Enemy NEXT ATK/MAT UP/DOWN (de)buffs should not influence the result
    }

    @Test
    public void calculateMaxDamageCaused_elementMultiplier_isAffectedBy_enemys_resistanceToCharactersElement() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 50.5));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 10.93));
        // Total Earth Resist DOWN: 61.43

        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 6.34));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 40.12));
        // Total Earth resist UP: 46.46
        // Total Earth resist: -61.43 + 46.46 = -14.97

        skills.add(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 7.44));
        skills.add(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 15.1));
        skills.add(new Skill(SkillType.WATER_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 12.2));
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 9.81));
        skills.add(new Skill(SkillType.SUN_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 16.0));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(10));
        assertTrue(chara1ResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(36674 + 2974, chara1ResultAfterChanges));
        // Result is affected by enemy's resistance to the character's element

        chara1.setCharacterElement(CharacterElement.WIND);
        assertTrue(acceptableResult(34259 + 2778, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.setCharacterElement(CharacterElement.FIRE);
        assertTrue(acceptableResult(36715 + 2977, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.setCharacterElement(CharacterElement.WATER);
        assertTrue(acceptableResult(35790 + 2902, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.setCharacterElement(CharacterElement.MOON);
        assertTrue(acceptableResult(35028 + 2840, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.setCharacterElement(CharacterElement.SUN);
        assertTrue(acceptableResult(37002 + 3000, Calculator.calculateMaxDamageCaused(chara1)));
        // The function picks the correct elemental resistance for each element
    }

    @Test
    public void calculateMaxDamageCaused_elementMultiplier_isAffectedBy_characters_weakElementBonus() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SELF, 11.5));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SELF, 12.13));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SINGLE, 9.15));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SINGLE, 50.9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_ALL, 1.4));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_ALL, 23.53));
        // Total weak element bonus UP: 108.61

        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_SELF, 49.81));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_SELF, 1.4));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_SINGLE, 9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_SINGLE, 13.5));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_ALL, 5.9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLY_ALL, 4.47));
        // Total weak element bonus DOWN: 84.08
        // Total weak element bonus: 108.61 - 84.08 = 24.53

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(13));
        assertTrue(chara1ResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(35811 + 2904, chara1ResultAfterChanges));
        // Result is affected by character's weak element bonus
    }

    @Test
    public void calculatedMaxDamageCaused_elementMultiplier_isAffectedBy_enemys_resistanceToCharactersElement_AND_charactersWeakElementBonus() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 14.97));
        long chara1ResultAfterResistDown = Calculator.calculateMaxDamageCaused(chara1);

        assertEquals(2, chara1.getSkills().size());
        assertTrue(acceptableResult(36674 + 2974, chara1ResultAfterResistDown));
        assertTrue(chara1ResultAfterResistDown > chara1ResultBeforeChanges);

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SELF, 24.53));
        long chara1ResultAfterWeakElementBonus = Calculator.calculateMaxDamageCaused(chara1);

        assertEquals(3, chara1.getSkills().size());
        assertTrue(chara1ResultAfterWeakElementBonus > chara1ResultAfterResistDown);
        assertTrue(acceptableResult(40586 + 3291, chara1ResultAfterWeakElementBonus));
    }

    @Test
    public void calculateMaxDamageCaused_elementMultiplier_isNotAffecteBy_characters_elementResistance() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SELF, 50.5));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 10.93));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLY_ALL, 1.93));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ALLY_SELF, 1.34));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ALLY_SINGLE, 6.34));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ALLY_ALL, 40.12));
        skills.add(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SELF, 7.44));
        skills.add(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 15.1));
        skills.add(new Skill(SkillType.WATER_RESIST, SkillChange.DOWN, SkillTarget.ALLY_ALL, 12.2));
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SELF, 9.81));
        skills.add(new Skill(SkillType.SUN_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 16.0));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(12));
        assertTrue(chara1ResultsAreSameAfterChanges());
        // Results should not be affected by characters element resistance
    }

    @Test
    public void calculateMaxDamageCaused_elementMultiplier_isNotAffectedBy_enemys_weakElementBonus() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 11));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_ALL, 12));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 21));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 22));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(5));
        assertTrue(chara1ResultsAreSameAfterChanges());
        // Results should not be affecte by enemys weak element bonus
    }
    
    @Test
    public void calculateMaxDamageCaused_elementMultiplier_maxValueTests() {
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 19.9));
        assertTrue(acceptableResult(38246 + 3101, Calculator.calculateMaxDamageCaused(chara1)));
        // elemental multipliers initial max value should be 2.4 (before applying weak element bonus)
        // 2 * (1 + 0.2) = 2.4

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.1));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.1));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 10.0));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SINGLE, 200));
        assertTrue(acceptableResult(70177 + 5690, Calculator.calculateMaxDamageCaused(chara1)));
        // weak element bonus should be applied after the initial value is set to 2.4
    }

    @Test
    public void calculateMaxDamageCaused_elementMultiplier_minValueTests() {
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 19.9));
        assertTrue(acceptableResult(25551 + 2072, Calculator.calculateMaxDamageCaused(chara1)));
        // elemental multipliers min value should be 1.6
        // 2 * (1 - 0.2) = 1.6

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 0.1));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 0.1));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 5.7));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SELF, 10));
        assertTrue(acceptableResult(27114 + 2198, Calculator.calculateMaxDamageCaused(chara1)));
        // weak element should bonus be applied after the initial value is set to 1.6
    }

    @Test
    public void calculateMaxDamageCaused_criticalDamageMultiplier_isAffectedBy_characters_critDamageBonus() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SELF, 30.67));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SELF, 1.7));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SINGLE, 25.89));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SINGLE, 5.33));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_ALL, 1.56));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_ALL, 31.22));
        // Total crit damage UP: 96.37

        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SELF, 17));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SELF, 2.6));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 15.12));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 2.79));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_ALL, 6));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_ALL, 25.66));
        // Total crit damage DOWN: 69.17
        // Total crit damage: 96.37 - 69.17 = 27.2 (UP)

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(13));
        assertTrue(chara1ResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(40575 + 3290, chara1ResultAfterChanges));
        // Result is affected by characters crit damage bonus (de)buffs
    }

    @Test
    public void calculateMaxDamageCaused_criticalDamageMultiplier_isNotAffectedBy_enemys_critDamageBonus() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 11));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 12));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 21));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 22));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(5));
        assertTrue(chara1ResultsAreSameAfterChanges());
        // Enemys crit damage bonus (de)buffs should not affect the results
    }

    @Test
    public void calculateMaxDamageCaused_criticalDamageMultiplier_doesNotIncreaseBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SINGLE, 99.9));
        assertTrue(acceptableResult(63765 + 5170, Calculator.calculateMaxDamageCaused(chara1)));
        // Critical damage multiplier's max value is 3.0 (1.5 * (1 + 1))

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SINGLE, 0.1));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SELF, 0.1));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SELF, 10.5));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamageCaused(chara1)));
    }

    @Test
    public void calculateMaxDamageCaused_criticalDamageMultiplier_doesNotDecreaseBelowMinValue() {
        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_ALL, 33));
        assertTrue(acceptableResult(21372 + 1733, Calculator.calculateMaxDamageCaused(chara1)));
        // Critical damage multiplier's min value is 1.0 (1.5 * (1 - (1/3)))

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SELF, 1));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SELF, 1));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 10.0));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamageCaused(chara1)));
    }

    @Test
    public void calculateMaxDamageCaused_defensiveBuffMultiplier_isAffectedBy_enemys_DEF_or_MDF() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 14.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 11.6));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 5.6));
        // Total DEF UP: 41.6

        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 20.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 3.46));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 5.6));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 8));
        // Total DEF DOWN: 37,46
        // Total DEF: 41.6 - 37.46 = 4.14 (UP)

        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 11.4));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 41.77));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_ALL,7.6));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_ALL,20));
        // Total MDF UP: 80,77

        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 30.1));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 2.11));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 5.9));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 90));
        // Total MDF DOWN: 128.11
        // Total MDF: 80.77 - 128.11 = -47.34 (DOWN)

        addSkillsToCharas(skills);
        calculateMaxDamage();
        assertTrue(chara1And2HaveNAmountOfSkills(17));
        assertTrue(damageGivenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(30672 + 2487, chara1ResultAfterChanges));
        assertTrue(acceptableResult(45450 + 3685, chara2ResultAfterChanges));
        // Alterations to the defensive multiplier seem to provide more different results from the kirafan.moe calculator
        // compared to the other multipliers (however it is still in a acceptable range, results vary only within 1% of the
        // kirafan.moe calculator results)
        // (This might be due to me misunderstanding the documentation or the kirafan.moe calculator rounding values more aggressively)
        // When chancing defence change value by a single per mille (1/10 of a percent) in the kirafan.moe calculator does
        // not always change the end result

        // Results should be affected by the enemy's DEF/MAT de(buffs)
        // The influencing stat should be DEF for warriors and MDF for mages
    }

    @Test
    public void calculateMaxDamageCaused_defensiveBuffMultiplier_providesAcceptableResultsWithVariousDefenceBuffValues() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 66.99));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamageCaused(chara1)));

        Skill skill = new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 21.11);
        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(59071 + 4790, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(42531 + 3448, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(33228 + 2694, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(27264 + 2211, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(23031 + 1867, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(skill);
        assertTrue(acceptableResult(19999 + 1622, Calculator.calculateMaxDamageCaused(chara1)));
    }

    @Test
    public void calculateMaxDamageCaused_defensiveBuffMultiplier_isNotAffectedBy_characters_DEF_OR_MDF() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SELF, 5.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SINGLE, 6.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_ALL, 7.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 19.3));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 20.3));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 21.3));

        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_SELF, 22.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_SINGLE, 23.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_ALL, 24.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLY_SELF, 2.4));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 3.4));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 4.4));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(chara1And2HaveNAmountOfSkills(13));
        assertTrue(damageGivenResultsAreSameAfterChanges());
        // Result is not affected by characters DEF/MDF (de)buffs
    }

    @Test
    public void calculateMaxDamageCaused_defensiveBuffMultiplier_doesNotIncreaseBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 399));
        assertTrue(acceptableResult(6392 + 518, Calculator.calculateMaxDamageCaused(chara1), 5));
        // Max value is 5.0 (1 + 4)
        // In the kirafan.moe calculator damages values do not always change per every per mille (1/10 of a percent),
        // so this and the following test use whole percentages instead

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 1));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamageCaused(chara1), 5));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 1));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamageCaused(chara1), 5));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamageCaused(chara1), 5));
    }

    @Test
    public void calculateMaxDamageCaused_defensiveBuffMultiplier_doesNotDecreaseBelowMinValue() {
        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 66));
        assertTrue(acceptableResult(93819 + 7607, Calculator.calculateMaxDamageCaused(chara1)));
        // Min value is 0.33 (1 - 0.67)

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 1));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 1));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamageCaused(chara1)));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 10));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamageCaused(chara1)));
    }

    @Test
    public void calculateMaxDamageCaused_modifyingAllMultipliersGivesAcceptableResult() {
        assertTrue(chara1And2HaveOnlyTotteoki());

        chara1.setPreferredWeapon(weapon1);
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 30)); // plus 24 from the equipped weapon
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 67));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 15));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ALLY_SELF, 20));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SELF, 25));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 34.84));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 41.53));

        addSkillsToCharas(skills);
        calculateMaxDamage();
        assertTrue(chara1And2HaveNAmountOfSkills(8));
        assertTrue(chara1ResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(51137 + 4164, chara1ResultAfterChanges));
    }

    @Test
    public void sumBuffsToSelf_sumsSkillPowers_and_convertsTheTotalPercentage_toDecimalCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SELF, 0), 15.3);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SINGLE, 0), 2.22);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_ALL, 0), 43.87);

        // The should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, SkillChange.UP, SkillTarget.ALLY_SINGLE, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.DEF, null, SkillTarget.ALLY_SELF, 0), 12.55); // Change is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 0), 12.55); // Wrong target

        // (15.3 + 2.22 + 43.87) / 100 = 0.6139
        assertEquals(0.6139, Calculator.sumBuffsToSelf(SkillType.DEF, skillPowerTotals));
    }

    @Test
    public void sumDebuffsToSelf_sumsSkillPowers_and_convertsTheTotalPercantage_toDecimalCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_SELF, 0), 4.44);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 0), 61.12);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 0), 21.73);

        // These should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.DEF, null, SkillTarget.ALLY_SELF, 0), 12.55); // Change is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0), 12.55); // Wrong target

        // (4.44 + 61.12 + 21.73) / 100 == 0.8729
        assertEquals(0.8729, Calculator.sumDebuffsToSelf(SkillType.DEF, skillPowerTotals));
    }

    @Test
    public void sumOtherEffectsToSelf_sumsSkillPowersCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_SELF, 0), 29.12);
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_SINGLE, 0), 1.14);
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_ALL, 0), 47.50);

        // These should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.ISOLATION, null, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, null, SkillTarget.ALLY_SINGLE, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.TIMID, SkillChange.UP, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.TIMID, SkillChange.DOWN, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ENEMY_ALL, 0), 12.55); // Wrong target

        // 29.12 + 1.14 + 47.50 = 77,76
        assertEquals(77.76, Calculator.sumOtherEffectsToSelf(SkillType.TIMID, skillPowerTotals));
    }

    @Test
    public void sumBuffsToOpponent_sumsSkillPowers_andConvertsTheTotalPercentage_toDecimalCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0), 5.41);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 0), 35.67);

        // These should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, SkillChange.UP, SkillTarget.ENEMY_ALL, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.DEF, null, SkillTarget.ENEMY_ALL, 0), 12.55); // Change is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong target

        // (5.41 + 35.67) / 100 = 0.4108
        assertEquals(0.4108, Calculator.sumBuffsToOpponent(SkillType.DEF, skillPowerTotals));
    }

    @Test
    public void sumDebuffsToOpponent_sumsSkillPowers_andConvertsTheTotalPercentage_toDecimalCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0), 67.14);
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0), 34.61);

        // These should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.DEF, null, SkillTarget.ENEMY_ALL, 0), 12.55); // Change is null
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong target

        // (67.14 + 34.61) / 100 = 1.0175
        assertEquals(1.0175, Calculator.sumDebuffsToOpponent(SkillType.DEF, skillPowerTotals));
    }

    @Test
    public void sumOtherEffectsToOpponent_sumsSkillPowersCorrectly() {
        // These should be summed:
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ENEMY_SINGLE, 0), 33.33);
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ENEMY_ALL, 0), 66.66);

        // These should NOT be summed:
        skillPowerTotals.put(new Skill(SkillType.ISOLATION, null, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong type
        skillPowerTotals.put(new Skill(null, null, SkillTarget.ENEMY_ALL, 0), 12.55); // Type is null
        skillPowerTotals.put(new Skill(SkillType.TIMID, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.TIMID, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0), 12.55); // Wrong change
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_SELF, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_SINGLE, 0), 12.55); // Wrong target
        skillPowerTotals.put(new Skill(SkillType.TIMID, null, SkillTarget.ALLY_ALL, 0), 12.55); // Wrong target

        // 33.33 + 66.66 + = 99.99
        assertEquals(99.99, Calculator.sumOtherEffectsToOpponent(SkillType.TIMID, skillPowerTotals));
    }

    @Test
    public void countAmountOfSpecificSkills_sumsAmountOfSpecificSkillsCorrectly() {
        List<Skill> skills = new ArrayList<>();
        // Skills that should be counted
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 23.1));
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 1.11));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 56.1));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 12.21));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 34.95));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 34.95));

        // Filler skills which should NOT be counted
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLY_ALL, 31.51));
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 6.75));
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLY_SELF, 61.5));
        skills.add(new Skill(SkillType.DAMAGE, null, SkillTarget.ALLY_SINGLE, 78.51));
        skills.add(new Skill(SkillType.DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 99.2));
        skills.add(new Skill(SkillType.DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 5.87));
        skills.add(new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ENEMY_ALL, 23.2));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 1.18));

        addSkillsToCharas(skills);
        weapon1.getSkills().add(new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 15.1));
        chara1.setPreferredWeapon(weapon1);

        Skill damageEnemyAll = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0);
        Skill defDownEnemySingle = new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0);

        assertEquals(2, Calculator.countAmountOfSpecificSkills(chara1, false, damageEnemyAll));
        assertEquals(3, Calculator.countAmountOfSpecificSkills(chara1, true, damageEnemyAll));
        assertEquals(4, Calculator.countAmountOfSpecificSkills(chara1, false, defDownEnemySingle));
        assertEquals(4, Calculator.countAmountOfSpecificSkills(chara1, true, defDownEnemySingle));
        assertEquals(6, Calculator.countAmountOfSpecificSkills(chara1, false, damageEnemyAll, defDownEnemySingle));
        assertEquals(7, Calculator.countAmountOfSpecificSkills(chara1, true, damageEnemyAll, defDownEnemySingle));
    }

    /*
     * The following tests are compared with values which have been calculated using kirafan.moe damage calculator at
     * https://calc.kirafan.moe/#/damage#damage with the following default parameters for all tests:
     *   Attack = 72000
     *   Defence = 5196 for physical def tests, 3816 for magical def tests
     *   Skill/% = 1000
     *   Critical = Not Critical
     *   Element = resist (meaning the character's element is strong against the enemy's element)
     *   Kirara Jump = 1st (Referred to as totteoki chain in my comments in the function)
     *
     * and rest of the parameters change depending on the test.
     *
     * My CalculateMaxDamage function always assumes a max damage hit, while the kirafan.moe calculator gives the possible
     * range of damage, e.g. as 23861 +- 1935, so the comparison is then made against 23861 + 1935.
     * */

    @Test
    public void calculateDamageTaken_returnsZero_whenGivenSkillTypeIsWrong_or_charaIsNotKnight() {
        assertNotEquals(0, Calculator.calculateDamageTaken(chara3, SkillType.DEF));
        assertNotEquals(0, Calculator.calculateDamageTaken(chara3, SkillType.MDF));

        assertEquals(0, Calculator.calculateDamageTaken(chara3, SkillType.ATK)); // Wrong skilltype
        assertEquals(0, Calculator.calculateDamageTaken(chara1, SkillType.DEF)); // Wrong class (warrior)
        assertEquals(0, Calculator.calculateDamageTaken(chara2, SkillType.DEF)); // Wrong class (mage)

        chara1.setCharacterClass(CharacterClass.PRIEST);
        chara2.setCharacterClass(CharacterClass.ALCHEMIST);

        assertEquals(0, Calculator.calculateDamageTaken(chara1, SkillType.DEF)); // Wrong class (priest)
        assertEquals(0, Calculator.calculateDamageTaken(chara2, SkillType.DEF)); // Wrong class (alchemist)
    }

    @Test
    public void calculateDamageTaken_offensiveStatBuffModifier_isAffectedBy_enemy_ATK_or_MAT() {
        assertTrue(chara3HasNoSkills());
        // Chara 3 should have no skills at the start

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ENEMY_ALL, 24.75));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 39.47));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 66.94));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 26.72));
        // ATK = -14.72, MAT = 40.22
        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(4));
        assertTrue(damageTakenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(455 + 37, defResultAfterChanges));
        assertTrue(acceptableResult(1020 + 83, mdfResultAfterChanges));
        // Changes to enemy's ATK or MAT should influence the results
    }

    @Test
    public void calculateDamageTaken_offensiveStatBuffModifier_isNotAffectedBy_chara_ATK_or_MAT() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 24.75));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 39.47));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLY_ALL, 66.94));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 26.72));

        addSkillsToCharas(skills);
        calculateDamageTaken();;

        assertTrue(chara3HasNAmountOfSkills(4));
        assertTrue(damageTakenResultsAreSameAfterChanges());
        // Changes to characters ATK or MAT should NOT influence the results
    }

    @Test
    public void calculateDamageTaken_nextAttackBuffMultiplier_isAlwaysOne() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 25));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 25));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLY_SELF, 25));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLY_ALL, 25));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ENEMY_ALL, 25));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 25));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLY_SINGLE, 25));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLY_SELF, 25));

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(8));
        assertTrue(damageTakenResultsAreSameAfterChanges());
        /* Any kind of NEXT ATK/MAT (de)buff should not affect the next attack buff multiplier
         (This is because the function only takes the damage dealers own next ATK/MAT buffs, and the default damage dealer
          used in the calculateDamageTaken function does not have any) */
    }

    @Test
    public void calculateDamageTaken_elementalMultiplier_isAlwaysOne() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.UP, SkillTarget.ALLY_SELF, 15.91));
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 2.13));
        skills.add(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ALLY_ALL, 8.84));
        skills.add(new Skill(SkillType.WIND_RESIST, SkillChange.UP, SkillTarget.ALLY_SELF, 4.98));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 30.69));
        skills.add(new Skill(SkillType.WATER_RESIST, SkillChange.UP, SkillTarget.ALLY_ALL, 26.91));
        skills.add(new Skill(SkillType.SUN_RESIST, SkillChange.DOWN, SkillTarget.ALLY_SELF, 23.19));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLY_SINGLE, 18.84));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ALLY_ALL, 4.93));

        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 15.91));
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 2.13));
        skills.add(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 8.84));
        skills.add(new Skill(SkillType.WIND_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 4.98));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 30.69));
        skills.add(new Skill(SkillType.WATER_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 26.91));
        skills.add(new Skill(SkillType.SUN_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 23.19));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_ALL, 18.84));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 4.93));

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(18));
        assertTrue(damageTakenResultsAreSameAfterChanges());
        /*
        * Any kind of elemental resistance and weak element bonus (de)buff to either chara or enemy should affect the
        * function result (this is because calculateDamageTaken gives the enemy the same element as chara and does not
        * take elemental resistance bonuses into account) (weak element bonus is also ignored, but it wouldnt apply in
        * the first place if both chara and the enemy are the same element)
        */
    }

    @Test
    public void calculateDamageTaken_criticalHitModifier_isAlwaysOne() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLY_SELF, 30));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 20));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 30));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 20));

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(4));
        assertTrue(damageTakenResultsAreSameAfterChanges());
        // The function assumes that the damage taken is not a critical hit, so crit damage (de)buffs should not affect the results
    }

    @Test
    public void calculateDamageTaken_calculatesDamageTakenCorrectly_basedOnDefensiveStats() {
        assertTrue(chara3HasNoSkills());

        assertTrue(acceptableResult(534 + 43, Calculator.calculateDamageTaken(chara3, SkillType.DEF)));
        assertTrue(acceptableResult(727 + 59, Calculator.calculateDamageTaken(chara3, SkillType.MDF)));

        chara3.setDefense(4000);
        chara3.setMagicDefense(6000);

        assertTrue(acceptableResult(694 + 56, Calculator.calculateDamageTaken(chara3, SkillType.DEF)));
        assertTrue(acceptableResult(463 + 38, Calculator.calculateDamageTaken(chara3, SkillType.MDF)));
        // The base defensive stats should affect the function result
    }

    @Test
    public void calculateDamageTaken_calculatesDamageTakenCorrectly_basedOnWeapon() {
        assertTrue(chara3HasNoSkills());

        chara3.setPreferredWeapon(weapon2);

        calculateDamageTaken();

        assertTrue(chara3HasNoSkills());
        assertTrue(damageTakenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(391 + 32, Calculator.calculateDamageTaken(chara3, SkillType.DEF)));
        assertTrue(acceptableResult(691 + 56, Calculator.calculateDamageTaken(chara3, SkillType.MDF)));
        // Characters preferred weapon's stats and skills should affect the function results
        // (The weapon gives +200 DEF and +200 MDF and has a -24% ATK down to single enemy)
        // (The skill should not be taken account in the MDF damage taken calculation because ATK affects physical damage)
    }

    @Test
    public void calculateDamageTaken_defensiveBuffMultiplier_isAffectedBy_charas_DEF_or_MDF() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SELF, 14.75));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLY_SINGLE, 66.81));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_ALL, 98.64));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLY_SELF, 84.54));
        // DEF = -52.06, MDF = 14.10

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(4));
        assertTrue(damageTakenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(1114 + 90, defResultAfterChanges));
        assertTrue(acceptableResult(637 + 52, mdfResultAfterChanges));
        // Chara's DEF/MDF (de)buffs should affect the function result
    }

    @Test
    public void calculateDamageTaken_defensiveBuffMultiplier_isNotAffectedBy_enemys_DEF_or_MDF() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 73.08));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 1.9));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ENEMY_ALL, 60.78));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 5.75));

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(4));
        assertTrue(damageTakenResultsAreSameAfterChanges());
        // Enemy's DEF/MDF (de)buffs should NOT affect the function result
     }

     @Test
    public void calculateDamageTaken_modifyingMultipleMultipliersGivesAcceptableResult() {
        assertTrue(chara3HasNoSkills());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 12.17));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ENEMY_ALL, 90.43));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 50)); // should not affect the result
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 50)); // should not affect the result
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.UP, SkillTarget.ALLY_SELF, 50)); // should not affect the result
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 50)); // should not affect the result
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 50)); // should not affect the result
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLY_SINGLE, 44.53));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLY_ALL, 20.16));
        chara3.setPreferredWeapon(weapon2); // total attack down = 36.17

        addSkillsToCharas(skills);
        calculateDamageTaken();

        assertTrue(chara3HasNAmountOfSkills(9));
        assertTrue(damageTakenResultsAreDifferentAfterChanges());
        assertTrue(acceptableResult(227 + 18, defResultAfterChanges));
        assertTrue(acceptableResult(1095 + 89, mdfResultAfterChanges));
     }
}