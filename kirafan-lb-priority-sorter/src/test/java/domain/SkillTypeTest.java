package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillTypeTest {
    @Test
    void getAppropriateElementalResistance_returnsAppropriateElementalResistanceToGivenCharacterElement() {
        assertEquals(SkillType.FIRE_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.FIRE));
        assertEquals(SkillType.WIND_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.WIND));
        assertEquals(SkillType.EARTH_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.EARTH));
        assertEquals(SkillType.WATER_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.WATER));
        assertEquals(SkillType.MOON_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.MOON));
        assertEquals(SkillType.SUN_RESIST, SkillType.getAppropriateElementalResistance(CharacterElement.SUN));
    }

    @Test
    void isStat_ReturnsAppropriateBoolean() {
        assertTrue(SkillType.isStat(SkillType.ATK));
        assertTrue(SkillType.isStat(SkillType.MAT));
        assertTrue(SkillType.isStat(SkillType.DEF));
        assertTrue(SkillType.isStat(SkillType.MDF));
        assertTrue(SkillType.isStat(SkillType.LUK));
        assertTrue(SkillType.isStat(SkillType.SPD));

        assertFalse(SkillType.isStat(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isStat(SkillType.WIND_RESIST));
        assertFalse(SkillType.isStat(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isStat(SkillType.WATER_RESIST));
        assertFalse(SkillType.isStat(SkillType.MOON_RESIST));
        assertFalse(SkillType.isStat(SkillType.SUN_RESIST));

        assertFalse(SkillType.isStat(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isStat(SkillType.NEXT_ATK));
        assertFalse(SkillType.isStat(SkillType.NEXT_MAT));

        assertFalse(SkillType.isStat(SkillType.CONFUSION));
        assertFalse(SkillType.isStat(SkillType.ISOLATION));
        assertFalse(SkillType.isStat(SkillType.HUNGER));
        assertFalse(SkillType.isStat(SkillType.MISFORTUNE));
        assertFalse(SkillType.isStat(SkillType.PARALYSIS));
        assertFalse(SkillType.isStat(SkillType.SILENCE));
        assertFalse(SkillType.isStat(SkillType.SLEEP));
        assertFalse(SkillType.isStat(SkillType.TIMID));

        assertFalse(SkillType.isStat(SkillType.DAMAGE));
        assertFalse(SkillType.isStat(SkillType.HEAL_CARD));

        assertFalse(SkillType.isStat(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isStat(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isStat(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isStat(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isStat(SkillType.TOTTEOKI));
    }

    @Test
    void isElementalResist_returnsAppropriateBoolean() {
        assertFalse(SkillType.isElementalResist(SkillType.ATK));
        assertFalse(SkillType.isElementalResist(SkillType.MAT));
        assertFalse(SkillType.isElementalResist(SkillType.DEF));
        assertFalse(SkillType.isElementalResist(SkillType.MDF));
        assertFalse(SkillType.isElementalResist(SkillType.LUK));
        assertFalse(SkillType.isElementalResist(SkillType.SPD));

        assertTrue(SkillType.isElementalResist(SkillType.FIRE_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.WIND_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.EARTH_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.WATER_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.MOON_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.SUN_RESIST));

        assertFalse(SkillType.isElementalResist(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isElementalResist(SkillType.NEXT_ATK));
        assertFalse(SkillType.isElementalResist(SkillType.NEXT_MAT));
        assertFalse(SkillType.isElementalResist(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isElementalResist(SkillType.CONFUSION));
        assertFalse(SkillType.isElementalResist(SkillType.ISOLATION));
        assertFalse(SkillType.isElementalResist(SkillType.HUNGER));
        assertFalse(SkillType.isElementalResist(SkillType.MISFORTUNE));
        assertFalse(SkillType.isElementalResist(SkillType.PARALYSIS));
        assertFalse(SkillType.isElementalResist(SkillType.SILENCE));
        assertFalse(SkillType.isElementalResist(SkillType.SLEEP));
        assertFalse(SkillType.isElementalResist(SkillType.TIMID));

        assertFalse(SkillType.isElementalResist(SkillType.DAMAGE));
        assertFalse(SkillType.isElementalResist(SkillType.TOTTEOKI));

        assertFalse(SkillType.isElementalResist(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isElementalResist(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isElementalResist(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isElementalResist(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isElementalResist(SkillType.HEAL_CARD));
    }

    @Test
    void isOtherMultiplier_returnsAppropriateBoolean() {
        assertFalse(SkillType.isOtherMultiplier(SkillType.ATK));
        assertFalse(SkillType.isOtherMultiplier(SkillType.MAT));
        assertFalse(SkillType.isOtherMultiplier(SkillType.DEF));
        assertFalse(SkillType.isOtherMultiplier(SkillType.MDF));
        assertFalse(SkillType.isOtherMultiplier(SkillType.LUK));
        assertFalse(SkillType.isOtherMultiplier(SkillType.SPD));

        assertFalse(SkillType.isOtherMultiplier(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isOtherMultiplier(SkillType.WIND_RESIST));
        assertFalse(SkillType.isOtherMultiplier(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isOtherMultiplier(SkillType.WATER_RESIST));
        assertFalse(SkillType.isOtherMultiplier(SkillType.SUN_RESIST));
        assertFalse(SkillType.isOtherMultiplier(SkillType.MOON_RESIST));

        assertTrue(SkillType.isOtherMultiplier(SkillType.CRIT_DAMAGE));
        assertTrue(SkillType.isOtherMultiplier(SkillType.NEXT_ATK));
        assertTrue(SkillType.isOtherMultiplier(SkillType.NEXT_MAT));
        assertTrue(SkillType.isOtherMultiplier(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isOtherMultiplier(SkillType.CONFUSION));
        assertFalse(SkillType.isOtherMultiplier(SkillType.ISOLATION));
        assertFalse(SkillType.isOtherMultiplier(SkillType.HUNGER));
        assertFalse(SkillType.isOtherMultiplier(SkillType.MISFORTUNE));
        assertFalse(SkillType.isOtherMultiplier(SkillType.PARALYSIS));
        assertFalse(SkillType.isOtherMultiplier(SkillType.SILENCE));
        assertFalse(SkillType.isOtherMultiplier(SkillType.SLEEP));
        assertFalse(SkillType.isOtherMultiplier(SkillType.TIMID));

        assertFalse(SkillType.isOtherMultiplier(SkillType.DAMAGE));
        assertFalse(SkillType.isOtherMultiplier(SkillType.TOTTEOKI));

        assertFalse(SkillType.isOtherMultiplier(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isOtherMultiplier(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isOtherMultiplier(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isOtherMultiplier(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isOtherMultiplier(SkillType.HEAL_CARD));

    }

    @Test
    void isAbnormalEffect_returnsAppropriateBoolean() {
        assertFalse(SkillType.isAbnormalEffect(SkillType.ATK));
        assertFalse(SkillType.isAbnormalEffect(SkillType.MAT));
        assertFalse(SkillType.isAbnormalEffect(SkillType.DEF));
        assertFalse(SkillType.isAbnormalEffect(SkillType.MDF));
        assertFalse(SkillType.isAbnormalEffect(SkillType.LUK));
        assertFalse(SkillType.isAbnormalEffect(SkillType.SPD));

        assertFalse(SkillType.isAbnormalEffect(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isAbnormalEffect(SkillType.NEXT_ATK));
        assertFalse(SkillType.isAbnormalEffect(SkillType.NEXT_MAT));
        assertFalse(SkillType.isAbnormalEffect(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isAbnormalEffect(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isAbnormalEffect(SkillType.WIND_RESIST));
        assertFalse(SkillType.isAbnormalEffect(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isAbnormalEffect(SkillType.WATER_RESIST));
        assertFalse(SkillType.isAbnormalEffect(SkillType.MOON_RESIST));
        assertFalse(SkillType.isAbnormalEffect(SkillType.SUN_RESIST));

        assertTrue(SkillType.isAbnormalEffect(SkillType.CONFUSION));
        assertTrue(SkillType.isAbnormalEffect(SkillType.ISOLATION));
        assertTrue(SkillType.isAbnormalEffect(SkillType.HUNGER));
        assertTrue(SkillType.isAbnormalEffect(SkillType.MISFORTUNE));
        assertTrue(SkillType.isAbnormalEffect(SkillType.PARALYSIS));
        assertTrue(SkillType.isAbnormalEffect(SkillType.SILENCE));
        assertTrue(SkillType.isAbnormalEffect(SkillType.SLEEP));
        assertTrue(SkillType.isAbnormalEffect(SkillType.TIMID));

        assertFalse(SkillType.isAbnormalEffect(SkillType.DAMAGE));
        assertFalse(SkillType.isAbnormalEffect(SkillType.TOTTEOKI));

        assertFalse(SkillType.isAbnormalEffect(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isAbnormalEffect(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isAbnormalEffect(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isAbnormalEffect(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isAbnormalEffect(SkillType.HEAL_CARD));
    }

    @Test
    public void isDamageEffect_returnsAppropriateBoolean() {
        assertFalse(SkillType.isDamageEffect(SkillType.ATK));
        assertFalse(SkillType.isDamageEffect(SkillType.MAT));
        assertFalse(SkillType.isDamageEffect(SkillType.DEF));
        assertFalse(SkillType.isDamageEffect(SkillType.MDF));
        assertFalse(SkillType.isDamageEffect(SkillType.LUK));
        assertFalse(SkillType.isDamageEffect(SkillType.SPD));

        assertFalse(SkillType.isDamageEffect(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isDamageEffect(SkillType.NEXT_ATK));
        assertFalse(SkillType.isDamageEffect(SkillType.NEXT_MAT));
        assertFalse(SkillType.isDamageEffect(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isDamageEffect(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isDamageEffect(SkillType.WIND_RESIST));
        assertFalse(SkillType.isDamageEffect(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isDamageEffect(SkillType.WATER_RESIST));
        assertFalse(SkillType.isDamageEffect(SkillType.MOON_RESIST));
        assertFalse(SkillType.isDamageEffect(SkillType.SUN_RESIST));

        assertFalse(SkillType.isDamageEffect(SkillType.CONFUSION));
        assertFalse(SkillType.isDamageEffect(SkillType.ISOLATION));
        assertFalse(SkillType.isDamageEffect(SkillType.HUNGER));
        assertFalse(SkillType.isDamageEffect(SkillType.MISFORTUNE));
        assertFalse(SkillType.isDamageEffect(SkillType.PARALYSIS));
        assertFalse(SkillType.isDamageEffect(SkillType.SILENCE));
        assertFalse(SkillType.isDamageEffect(SkillType.SLEEP));
        assertFalse(SkillType.isDamageEffect(SkillType.TIMID));

        assertTrue(SkillType.isDamageEffect(SkillType.DAMAGE));
        assertTrue(SkillType.isDamageEffect(SkillType.TOTTEOKI));

        assertFalse(SkillType.isDamageEffect(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isDamageEffect(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isDamageEffect(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isDamageEffect(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isDamageEffect(SkillType.HEAL_CARD));
    }

    @Test
    public void isMiscEffect_returnsAppropriateBoolean() {
        assertFalse(SkillType.isMiscEffect(SkillType.ATK));
        assertFalse(SkillType.isMiscEffect(SkillType.MAT));
        assertFalse(SkillType.isMiscEffect(SkillType.DEF));
        assertFalse(SkillType.isMiscEffect(SkillType.MDF));
        assertFalse(SkillType.isMiscEffect(SkillType.LUK));
        assertFalse(SkillType.isMiscEffect(SkillType.SPD));

        assertFalse(SkillType.isMiscEffect(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isMiscEffect(SkillType.NEXT_ATK));
        assertFalse(SkillType.isMiscEffect(SkillType.NEXT_MAT));
        assertFalse(SkillType.isMiscEffect(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isMiscEffect(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isMiscEffect(SkillType.WIND_RESIST));
        assertFalse(SkillType.isMiscEffect(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isMiscEffect(SkillType.WATER_RESIST));
        assertFalse(SkillType.isMiscEffect(SkillType.MOON_RESIST));
        assertFalse(SkillType.isMiscEffect(SkillType.SUN_RESIST));

        assertFalse(SkillType.isMiscEffect(SkillType.CONFUSION));
        assertFalse(SkillType.isMiscEffect(SkillType.ISOLATION));
        assertFalse(SkillType.isMiscEffect(SkillType.HUNGER));
        assertFalse(SkillType.isMiscEffect(SkillType.MISFORTUNE));
        assertFalse(SkillType.isMiscEffect(SkillType.PARALYSIS));
        assertFalse(SkillType.isMiscEffect(SkillType.SILENCE));
        assertFalse(SkillType.isMiscEffect(SkillType.SLEEP));
        assertFalse(SkillType.isMiscEffect(SkillType.TIMID));

        assertFalse(SkillType.isMiscEffect(SkillType.DAMAGE));
        assertFalse(SkillType.isMiscEffect(SkillType.TOTTEOKI));

        assertTrue(SkillType.isMiscEffect(SkillType.ABNORMAL_DISABLE));
        assertTrue(SkillType.isMiscEffect(SkillType.ABNORMAL_RECOVER));
        assertTrue(SkillType.isMiscEffect(SkillType.BARRIER_FULL));
        assertTrue(SkillType.isMiscEffect(SkillType.BARRIER_FULL_TRIPLE));
        assertTrue(SkillType.isMiscEffect(SkillType.HEAL_CARD));

    }

    @Test
    public void isBuffOrDebuff_returnsAppropriateBoolean() {
        assertTrue(SkillType.isBuffOrDebuff(SkillType.ATK));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.MAT));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.DEF));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.MDF));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.LUK));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.SPD));

        assertTrue(SkillType.isBuffOrDebuff(SkillType.FIRE_RESIST));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.WIND_RESIST));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.EARTH_RESIST));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.WATER_RESIST));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.MOON_RESIST));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.SUN_RESIST));

        assertTrue(SkillType.isBuffOrDebuff(SkillType.CRIT_DAMAGE));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.NEXT_ATK));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.NEXT_MAT));
        assertTrue(SkillType.isBuffOrDebuff(SkillType.WEAK_ELEMENT_BONUS));

        assertFalse(SkillType.isBuffOrDebuff(SkillType.CONFUSION));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.ISOLATION));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.HUNGER));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.MISFORTUNE));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.PARALYSIS));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.SILENCE));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.SLEEP));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.TIMID));

        assertFalse(SkillType.isBuffOrDebuff(SkillType.DAMAGE));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.TOTTEOKI));

        assertFalse(SkillType.isBuffOrDebuff(SkillType.ABNORMAL_DISABLE));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.ABNORMAL_RECOVER));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.BARRIER_FULL));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.BARRIER_FULL_TRIPLE));
        assertFalse(SkillType.isBuffOrDebuff(SkillType.HEAL_CARD));
    }

    @Test
    public void isOtherEffect_returnsAppropriateBoolean() {
        assertFalse(SkillType.isOtherEffect(SkillType.ATK));
        assertFalse(SkillType.isOtherEffect(SkillType.MAT));
        assertFalse(SkillType.isOtherEffect(SkillType.DEF));
        assertFalse(SkillType.isOtherEffect(SkillType.MDF));
        assertFalse(SkillType.isOtherEffect(SkillType.LUK));
        assertFalse(SkillType.isOtherEffect(SkillType.SPD));

        assertFalse(SkillType.isOtherEffect(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isOtherEffect(SkillType.WIND_RESIST));
        assertFalse(SkillType.isOtherEffect(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isOtherEffect(SkillType.WATER_RESIST));
        assertFalse(SkillType.isOtherEffect(SkillType.MOON_RESIST));
        assertFalse(SkillType.isOtherEffect(SkillType.SUN_RESIST));

        assertFalse(SkillType.isOtherEffect(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isOtherEffect(SkillType.NEXT_ATK));
        assertFalse(SkillType.isOtherEffect(SkillType.NEXT_MAT));
        assertFalse(SkillType.isOtherEffect(SkillType.WEAK_ELEMENT_BONUS));

        assertTrue(SkillType.isOtherEffect(SkillType.CONFUSION));
        assertTrue(SkillType.isOtherEffect(SkillType.ISOLATION));
        assertTrue(SkillType.isOtherEffect(SkillType.HUNGER));
        assertTrue(SkillType.isOtherEffect(SkillType.MISFORTUNE));
        assertTrue(SkillType.isOtherEffect(SkillType.PARALYSIS));
        assertTrue(SkillType.isOtherEffect(SkillType.SILENCE));
        assertTrue(SkillType.isOtherEffect(SkillType.SLEEP));
        assertTrue(SkillType.isOtherEffect(SkillType.TIMID));

        assertTrue(SkillType.isOtherEffect(SkillType.DAMAGE));
        assertTrue(SkillType.isOtherEffect(SkillType.TOTTEOKI));

        assertTrue(SkillType.isOtherEffect(SkillType.ABNORMAL_DISABLE));
        assertTrue(SkillType.isOtherEffect(SkillType.ABNORMAL_RECOVER));
        assertTrue(SkillType.isOtherEffect(SkillType.BARRIER_FULL));
        assertTrue(SkillType.isOtherEffect(SkillType.BARRIER_FULL_TRIPLE));
        assertTrue(SkillType.isOtherEffect(SkillType.HEAL_CARD));
    }

}