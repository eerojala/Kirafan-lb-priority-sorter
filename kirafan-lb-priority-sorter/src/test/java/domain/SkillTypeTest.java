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
    void isElementalResist_returnsAppropriateBoolean() {
        assertTrue(SkillType.isElementalResist(SkillType.FIRE_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.WIND_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.EARTH_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.WATER_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.MOON_RESIST));
        assertTrue(SkillType.isElementalResist(SkillType.SUN_RESIST));

        assertFalse(SkillType.isElementalResist(SkillType.ATK));
        assertFalse(SkillType.isElementalResist(SkillType.MAT));
        assertFalse(SkillType.isElementalResist(SkillType.DEF));
        assertFalse(SkillType.isElementalResist(SkillType.MDF));
        assertFalse(SkillType.isElementalResist(SkillType.LUK));
        assertFalse(SkillType.isElementalResist(SkillType.SPD));
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
        assertFalse(SkillType.isElementalResist(SkillType.STATUS_EFFECT_CLEAR));
        assertFalse(SkillType.isElementalResist(SkillType.BARRIER));
        assertFalse(SkillType.isElementalResist(SkillType.BARRIER_TRIPLE));
        assertFalse(SkillType.isElementalResist(SkillType.DAMAGE));
        assertFalse(SkillType.isElementalResist(SkillType.HEAL_CARD));
        assertFalse(SkillType.isElementalResist(SkillType.TOTTEOKI));
    }

    @Test
    void isStatusEffect() {
        assertTrue(SkillType.isStatusEffect(SkillType.CONFUSION));
        assertTrue(SkillType.isStatusEffect(SkillType.ISOLATION));
        assertTrue(SkillType.isStatusEffect(SkillType.HUNGER));
        assertTrue(SkillType.isStatusEffect(SkillType.MISFORTUNE));
        assertTrue(SkillType.isStatusEffect(SkillType.PARALYSIS));
        assertTrue(SkillType.isStatusEffect(SkillType.SILENCE));
        assertTrue(SkillType.isStatusEffect(SkillType.SLEEP));
        assertTrue(SkillType.isStatusEffect(SkillType.TIMID));

        assertFalse(SkillType.isStatusEffect(SkillType.ATK));
        assertFalse(SkillType.isStatusEffect(SkillType.MAT));
        assertFalse(SkillType.isStatusEffect(SkillType.DEF));
        assertFalse(SkillType.isStatusEffect(SkillType.MDF));
        assertFalse(SkillType.isStatusEffect(SkillType.LUK));
        assertFalse(SkillType.isStatusEffect(SkillType.SPD));
        assertFalse(SkillType.isStatusEffect(SkillType.CRIT_DAMAGE));
        assertFalse(SkillType.isStatusEffect(SkillType.NEXT_ATK));
        assertFalse(SkillType.isStatusEffect(SkillType.NEXT_MAT));
        assertFalse(SkillType.isStatusEffect(SkillType.WEAK_ELEMENT_BONUS));
        assertFalse(SkillType.isStatusEffect(SkillType.FIRE_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.WIND_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.EARTH_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.WATER_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.MOON_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.SUN_RESIST));
        assertFalse(SkillType.isStatusEffect(SkillType.STATUS_EFFECT_CLEAR));
        assertFalse(SkillType.isStatusEffect(SkillType.BARRIER));
        assertFalse(SkillType.isStatusEffect(SkillType.BARRIER_TRIPLE));
        assertFalse(SkillType.isStatusEffect(SkillType.DAMAGE));
        assertFalse(SkillType.isStatusEffect(SkillType.HEAL_CARD));
        assertFalse(SkillType.isStatusEffect(SkillType.TOTTEOKI));
    }
}