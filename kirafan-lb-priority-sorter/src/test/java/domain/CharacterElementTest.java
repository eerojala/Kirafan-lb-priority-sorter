package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterElementTest {

    @Test
    public void getElementThatIsWeakTo_returnsAprropriateElementalWeakness() {
        assertEquals(CharacterElement.WIND, CharacterElement.getElementThatIsWeakTo(CharacterElement.FIRE));
        assertEquals(CharacterElement.EARTH, CharacterElement.getElementThatIsWeakTo(CharacterElement.WIND));
        assertEquals(CharacterElement.WATER, CharacterElement.getElementThatIsWeakTo(CharacterElement.EARTH));
        assertEquals(CharacterElement.FIRE, CharacterElement.getElementThatIsWeakTo(CharacterElement.WATER));
        assertEquals(CharacterElement.SUN, CharacterElement.getElementThatIsWeakTo(CharacterElement.MOON));
        assertEquals(CharacterElement.MOON, CharacterElement.getElementThatIsWeakTo(CharacterElement.SUN));
    }
}