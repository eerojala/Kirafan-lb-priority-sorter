package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// Laita vanhoihin testeihin skillejä joiden ei pitäisi vaikuttaa (esim offensivebuffiin vihulle tehtäviä buffeja)
// Laita loppuun pari testia joilla testaa kaikkia multiplieria samaan aikaan (sekä maksimi ja minimi chara1)
class CalculatorTest {
    private GameCharacter chara2;
    private GameCharacter chara1;
    private Weapon weapon;
    private long chara1ResultAfterSkills;
    private long chara1ResultBeforeSkills;
    private long chara2ResultAfterSkills;
    private long chara2ResultBeforeSkills;

    @BeforeEach
    public void setUp() {
        Series series = new Series("series", null);
        chara1 = new GameCharacter.Builder("warrior", series, CharacterElement.EARTH, CharacterClass.WARRIOR)
                .offensivePowerIs(2938)
                .withSkill(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_SINGLE, 4695))
                .build();

        chara2 = new GameCharacter.Builder("mage", series, CharacterElement.WIND, CharacterClass.MAGE)
                .offensivePowerIs(2745)
                .withSkill(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 3759))
                .build();

        weapon = new Weapon.Builder("Weapon")
                .offensivePowerIs(90)
                .withSkill(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 24.0))
                .build();

        chara1ResultBeforeSkills = Calculator.calculateMaxDamage(chara1);
        chara2ResultBeforeSkills = Calculator.calculateMaxDamage(chara2);
    }

    private boolean charasHaveOnlyTotteoki() {
        // chara1 and chara2 should only have the totteoki which they were constructed with in the method setUp
        return charasHaveXAmountOfSkills(1);
    }

    private boolean charasHaveXAmountOfSkills(int x) {
        return chara1.getSkills().size() == x && chara2.getSkills().size() == x;
    }

    private void addSkillsToCharas(List<Skill> skills) {
        chara1.getSkills().addAll(skills);
        chara2.getSkills().addAll(skills);
    }

    private void calculateMaxDamage() {
        // NOTE: Use this function only after adding the desired skills in the test
        chara1ResultAfterSkills = Calculator.calculateMaxDamage(chara1);
        chara2ResultAfterSkills = Calculator.calculateMaxDamage(chara2);
    }

    private boolean acceptableResult(long expected, long result, long maxDeviation) {
        /*
         * Due to possible rounding errors on my function and/or kirafan.moe's calculator, the results between them
         * usually vary a bit, but not on a scale which is unacceptable
         * */
        return result <= expected + maxDeviation && result >= expected - maxDeviation;
    }

    private boolean acceptableResult(long expected, long result) {
        return acceptableResult(expected, result, 20);
    }

    private boolean resultsAreDifferentAfterSkills() {
        return chara1ResultsAreDifferentAfterSkills() && chara2ResultsAreDifferentAfterSkills();
    }

    private boolean chara1ResultsAreDifferentAfterSkills() {
        return chara1ResultAfterSkills != chara1ResultBeforeSkills;
    }

    private boolean chara2ResultsAreDifferentAfterSkills() {
        return chara2ResultAfterSkills > chara2ResultBeforeSkills;
    }

    private boolean resultsAreSameAfterSkills() {
        return chara1ResultsAreSameAfterSkills() && chara2ResultsAreSameAfterSkills();
    }

    private boolean chara1ResultsAreSameAfterSkills() {
        return chara1ResultAfterSkills == chara1ResultBeforeSkills;
    }

    private boolean chara2ResultsAreSameAfterSkills() {
        return chara2ResultAfterSkills == chara2ResultBeforeSkills;
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
    public void calculateMaxDamage_returnsZeroForEveryClassExceptMageAndWarrior() {
        assertNotEquals(0, Calculator.calculateMaxDamage(chara1)); // chara1.charcterClass = WARRIOR
        assertNotEquals(0, Calculator.calculateMaxDamage(chara2)); // chara2.characterClass = MAGE

        chara2.setCharacterClass(CharacterClass.KNIGHT);
        assertEquals(0, Calculator.calculateMaxDamage(chara2));

        chara2.setCharacterClass(CharacterClass.PRIEST);
        assertEquals(0, Calculator.calculateMaxDamage(chara2));

        chara2.setCharacterClass(CharacterClass.ALCHEMIST);
        assertEquals(0, Calculator.calculateMaxDamage(chara2));
    }

    @Test
    public void CalculateMaxDamage_calculatesDamageBasedOnTotteokiStrengthCorrectly() {
        assertTrue(charasHaveOnlyTotteoki());
        // chara1 and chara2 should only have the totteoki which they were constructed with in the method setUp
        assertTrue(acceptableResult(31898 + 2586, Calculator.calculateMaxDamage(chara1)));
        assertTrue(acceptableResult(23861 + 1935, Calculator.calculateMaxDamage(chara2)));

        chara1.setSkills(new ArrayList<Skill>(Arrays.asList(
                new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 2000),
                new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 3000))));
        // A damage skill is added to make sure that damage really is calculated on totteoki and not the skill with highest damage
        // (Though damage skills should never have more raw attack power than a totteoki in the first place)
        chara2.setSkills(new ArrayList<Skill>(Arrays.asList(
                new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_SINGLE, 1000),
                new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 2000))));

        assertTrue(charasHaveXAmountOfSkills(2));
        assertTrue(acceptableResult(13588 + 1102, Calculator.calculateMaxDamage(chara1)));
        assertTrue(acceptableResult(6348 + 515, Calculator.calculateMaxDamage(chara2)));
    }

    @Test
    public void calculateMaxDamage_calculatesWeaponAttackPowerAndSkillsCorrectly() {
        chara1.setPreferredWeapon(weapon);
        chara2.setPreferredWeapon(weapon);
        assertTrue(acceptableResult(40758 + 3305, Calculator.calculateMaxDamage(chara1)));
        assertTrue(acceptableResult(24644 + 1998, Calculator.calculateMaxDamage(chara2)));
        // chara2 is a mage so the weapon's +24.0% ATK skill should not affect the calculation since Mages use the MAT
        // stat for their damage (but the weapon's base offensive power should)
    }

    @Test
    public void calculateMaxDamage_offensiveBuffMultiplier_isAffectedBy_character_ATK_or_MAT() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 10.4));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 2.0));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 3.2));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0.5));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 36.3));
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 1.3));
        // Total ATK UP: 53.7
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.SELF, 15.6));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.SELF, 2.4));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 10.75));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 1));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 1.5));
        skills.add(new Skill(SkillType.ATK, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 2.6));
        // Total ATK DOWN: 33.85
        // Total ATK: 53.7 - 33.85 = 19.85 (UP)

        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.SELF, 13.79));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.SELF, 5.32));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 40.2));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 1.7));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLIES_ALL, 15.1));
        skills.add(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLIES_ALL, 6.77));
        // Total MAT UP: 82.88
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.SELF, 11.0));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.SELF, 5.56));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 10.31));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 3.5));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 3.6));
        skills.add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 8.9));
        // Total MAT DOWN: 42.87
        // Total MAT: 82.88 - 42.87 = 40.01 (UP)

        addSkillsToCharas(skills);
        calculateMaxDamage();
        assertTrue(charasHaveXAmountOfSkills(25));
        assertTrue(acceptableResult(38228 + 3100, chara1ResultAfterSkills));
        assertTrue(acceptableResult(33423 + 2710, chara2ResultAfterSkills));
        assertTrue(resultsAreDifferentAfterSkills());
        // MAT (de)buffs should not influence the result for a warrior
        // ATK (debuffs) should not influence the result for a mage
    }

    @Test
    public void calculateMaxDamage_offensiveBuffMultiplier_isNotAffectedBy_enemy_ATK_or_MAT() {
        assertTrue(charasHaveOnlyTotteoki());

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

        assertTrue(charasHaveXAmountOfSkills(9));
        assertTrue(resultsAreSameAfterSkills());
        // Enemy's ATK/MAT (de)buffs should not influence the result
    }

    @Test
    public void calculateMaxDamage_offensiveBuffMultiplier_doesNotIncreasBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 149.9));
        assertTrue(acceptableResult(79713 + 6463, Calculator.calculateMaxDamage(chara1)));
        // Multiplier's max value is 2.5 (1 + 1.5)

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 0.1));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 0.1));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 10));
        assertTrue(acceptableResult(79746 + 6466, Calculator.calculateMaxDamage(chara1)));
    }

    @Test
    public void calculateMaxDamage_offensiveBuffMultiplier_doesNotDecreaseBelowMinValue() {
        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 49.9));
        assertTrue(acceptableResult(11952 + 969, Calculator.calculateMaxDamage(chara2)));
        // Multiplier's min value is 0.5 (1 - 0.5)

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.SELF, 0.1));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamage(chara2)));

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.SELF, 001));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamage(chara2)));

        chara2.getSkills().add(new Skill(SkillType.MAT, SkillChange.DOWN, SkillTarget.SELF, 10));
        assertTrue(acceptableResult(11926 + 967, Calculator.calculateMaxDamage(chara2)));
    }

    @Test
    public void calculateMaxDamage_nextAttackBuffMultiplier_isAffectedBy_character_NEXTATKUP_or_NEXTMATUP() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 50.13));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 200.36));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 160));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 170));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 150));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.ALLIES_ALL, 150));
        // The chosen NEXT ATK UP should be 200.36

        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.SELF, 15.13));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.SELF, 350.32));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 10));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 350.33));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 10));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 250.11));
        // The chosen NEXT MAT UP should be 350.33

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(13));
        assertTrue(acceptableResult(95810 + 7768, chara1ResultAfterSkills));
        assertTrue(acceptableResult(107455 + 8713, chara2ResultAfterSkills));
        assertTrue(resultsAreDifferentAfterSkills());
        // only the strongest next attack buff should influence the result (they do not stack unlike the other buffs and debuffs)
        // For warriors only NEXT ATK UP and for mages NEXT MAT UP should influence the result
    }

    @Test
    public void calculateMaxDamage_nextAttackBuffMultiplier_isNotAffectedBy_character_NEXTATKDOWN_or_NEXTMATDOWN() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.SELF, 100));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 110));
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 120));

        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.SELF, 130));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 140));
        skills.add(new Skill(SkillType.NEXT_MAT, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 150));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(7));
        assertTrue(resultsAreSameAfterSkills());
        // Character NEXT ATK DOWN and NEXT MAT DOWN debuffs should not influence the result
    }

    @Test
    public void calculateMaxDamage_nextAttackBuffMultiplier_isNotAffectedBy_enemy_NEXTATK_or_NEXTMAT() {
        assertTrue(charasHaveOnlyTotteoki());

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

        assertTrue(charasHaveXAmountOfSkills(9));
        assertTrue(resultsAreSameAfterSkills());
        // Enemy NEXT ATK/MAT UP/DOWN (de)buffs should not influence the result
    }

    @Test
    public void calculateMaxDamage_elementMultiplier_isAffectedBy_enemys_resistanceToCharactersElement() {
        assertTrue(charasHaveOnlyTotteoki());

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

        assertTrue(charasHaveXAmountOfSkills(10));
        assertTrue(acceptableResult(36674 + 2974, chara1ResultAfterSkills));
        assertTrue(chara1ResultsAreDifferentAfterSkills());
        // Result is affected by enemy's resistance to the character's element

        chara1.setCharacterElement(CharacterElement.WIND);
        assertTrue(acceptableResult(34259 + 2778, Calculator.calculateMaxDamage(chara1)));

        chara1.setCharacterElement(CharacterElement.FIRE);
        assertTrue(acceptableResult(36715 + 2977, Calculator.calculateMaxDamage(chara1)));

        chara1.setCharacterElement(CharacterElement.WATER);
        assertTrue(acceptableResult(35790 + 2902, Calculator.calculateMaxDamage(chara1)));

        chara1.setCharacterElement(CharacterElement.MOON);
        assertTrue(acceptableResult(35028 + 2840, Calculator.calculateMaxDamage(chara1)));

        chara1.setCharacterElement(CharacterElement.SUN);
        assertTrue(acceptableResult(37002 + 3000, Calculator.calculateMaxDamage(chara1)));
        // The function picks the correct elemental resistance for each element
    }

    @Test
    public void calculateMaxDamage_elementMultiplier_isAffectedBy_characters_weakElementBonus() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.SELF, 11.5));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.SELF, 12.13));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 9.15));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 50.9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLIES_ALL, 1.4));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLIES_ALL, 23.53));
        // Total weak element bonus UP: 108.61

        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.SELF, 49.81));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.SELF, 1.4));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLIES_SINGLE, 9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLIES_SINGLE, 13.5));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLIES_ALL, 5.9));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN,SkillTarget.ALLIES_ALL, 4.47));
        // Total weak element bonus DOWN: 84.08
        // Total weak element bonus: 108.61 - 84.08 = 24.53

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(13));
        assertTrue(acceptableResult(35811 + 2904, chara1ResultAfterSkills));
        assertTrue(chara1ResultsAreDifferentAfterSkills());
        // Result is affected by character's weak element bonus
    }

    @Test
    public void calculatedMaxDamage_elementMultiplier_isAffectedBy_enemys_resistanceToCharactersElement_AND_charactersWeakElementBonus() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 14.97));
        long chara1ResultAfterResistDown = Calculator.calculateMaxDamage(chara1);

        assertEquals(2, chara1.getSkills().size());
        assertTrue(acceptableResult(36674 + 2974, chara1ResultAfterResistDown));
        assertTrue(chara1ResultAfterResistDown > chara1ResultBeforeSkills);

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.SELF, 24.53));
        long chara1ResultAfterWeakElementBonus = Calculator.calculateMaxDamage(chara1);

        assertEquals(3, chara1.getSkills().size());
        assertTrue(acceptableResult(40586 + 3291, chara1ResultAfterWeakElementBonus));
        assertTrue(chara1ResultAfterWeakElementBonus > chara1ResultAfterResistDown);
    }

    @Test
    public void calculateMaxDamage_elementMultiplier_isNotAffecteBy_characters_elementResistance() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.SELF, 50.5));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 10.93));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 1.93));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.SELF, 1.34));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 6.34));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ALLIES_ALL, 40.12));
        skills.add(new Skill(SkillType.WIND_RESIST, SkillChange.DOWN, SkillTarget.SELF, 7.44));
        skills.add(new Skill(SkillType.FIRE_RESIST, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 15.1));
        skills.add(new Skill(SkillType.WATER_RESIST, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 12.2));
        skills.add(new Skill(SkillType.MOON_RESIST, SkillChange.DOWN, SkillTarget.SELF, 9.81));
        skills.add(new Skill(SkillType.SUN_RESIST, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 16.0));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(12));
        assertTrue(chara1ResultsAreSameAfterSkills());
        // Results should not be affected by characters element resistance
    }

    @Test
    public void calculateMaxDamage_elementMultiplier_isNotAffectedBy_enemys_weakElementBonus() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 11));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ENEMY_ALL, 12));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 21));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 22));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(5));
        assertTrue(chara1ResultsAreSameAfterSkills());
        // Results should not be affecte by enemys weak element bonus
    }
    
    @Test
    public void calculateMaxDamage_elementMultiplier_maxValueTests() {
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 19.9));
        assertTrue(acceptableResult(38246 + 3101, Calculator.calculateMaxDamage(chara1)));
        // elemental multipliers initial max value should be 2.4 (before applying weak element bonus)
        // 2 * (1 + 0.2) = 2.4

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.1));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0.1));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 10.0));
        assertTrue(acceptableResult(38278 + 3104, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 200));
        assertTrue(acceptableResult(70177 + 5690, Calculator.calculateMaxDamage(chara1)));
        // weak element bonus should be applied after the initial value is set to 2.4
    }

    @Test
    public void calculateMaxDamage_elementMultiplier_minValueTests() {
        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 19.9));
        assertTrue(acceptableResult(25551 + 2072, Calculator.calculateMaxDamage(chara1)));
        // elemental multipliers min value should be 1.6
        // 2 * (1 - 0.2) = 1.6

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 0.1));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 0.1));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_ALL, 5.7));
        assertTrue(acceptableResult(25519 + 2069, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.UP, SkillTarget.SELF, 10));
        assertTrue(acceptableResult(27114 + 2198, Calculator.calculateMaxDamage(chara1)));
        // weak element should bonus be applied after the initial value is set to 1.6
    }

    @Test
    public void calculateMaxDamage_criticalDamageMultiplier_isAffectedBy_characters_critDamageBonus() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.SELF, 30.67));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.SELF, 1.7));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 25.89));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 5.33));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_ALL, 1.56));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_ALL, 31.22));
        // Total crit damage UP: 96.37

        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.SELF, 17));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.SELF, 2.6));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 15.12));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 2.79));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 6));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 25.66));
        // Total crit damage DOWN: 69.17
        // Total crit damage: 96.37 - 69.17 = 27.2 (UP)

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(13));
        assertTrue(acceptableResult(40575 + 3290, chara1ResultAfterSkills));
        assertTrue(chara1ResultsAreDifferentAfterSkills());
        // Result is affected by characters crit damage bonus (de)buffs
    }

    @Test
    public void calculateMaxDamage_criticalDamageMultiplier_isNotAffectedBy_enemys_critDamageBonus() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 11));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ENEMY_ALL, 12));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 21));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 22));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(5));
        assertTrue(chara1ResultsAreSameAfterSkills());
        // Enemys crit damage bonus (de)buffs should not affect the results
    }

    @Test
    public void calculateMaxDamage_criticalDamageMultiplier_doesNotIncreaseBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 99.9));
        assertTrue(acceptableResult(63765 + 5170, Calculator.calculateMaxDamage(chara1)));
        // Critical damage multiplier's max value is 3.0 (1.5 * (1 + 1))

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0.1));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.SELF, 0.1));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.UP, SkillTarget.SELF, 10.5));
        assertTrue(acceptableResult(63797 + 5173, Calculator.calculateMaxDamage(chara1)));
    }

    @Test
    public void calculateMaxDamage_criticalDamageMultiplier_doesNotDecreaseBelowMinValue() {
        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 33));
        assertTrue(acceptableResult(21372 + 1733, Calculator.calculateMaxDamage(chara1)));
        // Critical damage multiplier's min value is 1.0 (1.5 * (1 - (1/3)))

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.SELF, 1));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.SELF, 1));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 10.0));
        assertTrue(acceptableResult(21266 + 1724, Calculator.calculateMaxDamage(chara1)));
    }

    @Test
    public void calculateMaxDamage_defensiveBuffMultiplier_isAffectedBy_enemys_DEF_or_MDF() {
        assertTrue(charasHaveOnlyTotteoki());

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

        assertTrue(charasHaveXAmountOfSkills(17));
        assertTrue(acceptableResult(30672 + 2487, chara1ResultAfterSkills, 300));
        assertTrue(acceptableResult(45450 + 3685, chara2ResultAfterSkills, 300));
        // Alterations to the defensive multiplier seem to provide more different results from the kirafan.moe calculator
        // compared to the other multipliers (however it is still in a acceptable range, results vary by few hundred at most)
        // (This might be due to me misunderstanding the documentation or the kirafan.moe calculator rounding values more aggressively)
        assertTrue(resultsAreDifferentAfterSkills());
        // Results should be affected by the enemy's DEF/MAT de(buffs)
        // The influencing stat should be DEF for warriors and MDF for mages
    }

    @Test
    public void calculateMaxDamage_defensiveBuffMultiplier_isNotAffectedBy_characters_DEF_OR_MDF() {
        assertTrue(charasHaveOnlyTotteoki());

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.SELF, 5.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 6.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ALLIES_ALL, 7.4));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 19.3));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 20.3));
        skills.add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 21.3));

        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.SELF, 22.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 23.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.UP, SkillTarget.ALLIES_ALL, 24.14));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.SELF, 2.4));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 3.4));
        skills.add(new Skill(SkillType.MDF, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 4.4));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(13));
        assertTrue(resultsAreSameAfterSkills());
        // Result is not affected by characters DEF/MDF (de)buffs
    }

    @Test
    public void calculateMaxDamage_defensiveBuffMultiplier_doesNotIncreaseBeyondMaxValue() {
        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_ALL, 399));
        assertTrue(acceptableResult(6392 + 518, Calculator.calculateMaxDamage(chara1), 5));
        // Max value is 5.0 (1 + 4)
        // In the kirafan.moe calculator damages values do not always change per every per mille, so this and the following
        // test use whole percentages instead

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 1));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamage(chara1), 5));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 1));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamage(chara1), 5));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 10));
        assertTrue(acceptableResult(6380 + 517, Calculator.calculateMaxDamage(chara1), 5));
    }

    @Test
    public void calculateMaxDamage_defensiveBuffMultiplier_doesNotDecreaseBelowMinValue() {
        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 66));
        assertTrue(acceptableResult(93819 + 7607, Calculator.calculateMaxDamage(chara1)));
        // Min value is 0.33 (1 - 0.67)

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 1));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 1));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamage(chara1)));

        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 10));
        assertTrue(acceptableResult(96662 + 7837, Calculator.calculateMaxDamage(chara1)));
    }

    @Test
    public void calculateMaxDamage_modifyingAllMultipliersGivesAcceptableResult() {
        assertTrue(charasHaveOnlyTotteoki());

        chara1.setPreferredWeapon(weapon);
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(SkillType.ATK, SkillChange.UP, SkillTarget.SELF, 30)); // plus 24 from the equipped weapon
        skills.add(new Skill(SkillType.NEXT_ATK, SkillChange.UP, SkillTarget.SELF, 67));
        skills.add(new Skill(SkillType.EARTH_RESIST, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 15));
        skills.add(new Skill(SkillType.WEAK_ELEMENT_BONUS, SkillChange.DOWN, SkillTarget.SELF, 20));
        skills.add(new Skill(SkillType.CRIT_DAMAGE, SkillChange.DOWN, SkillTarget.SELF, 25));
        skills.add(new Skill(SkillType.DEF, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 34));

        addSkillsToCharas(skills);
        calculateMaxDamage();

        assertTrue(charasHaveXAmountOfSkills(7));
        assertTrue(chara1ResultsAreDifferentAfterSkills());
        assertTrue(acceptableResult(34433 + 2792, chara1ResultAfterSkills, 2000));
        // Results seem to vary about 10% (mostly because of the above mentioned flaws), but I deem it acceptable enough
        // and
    }
}