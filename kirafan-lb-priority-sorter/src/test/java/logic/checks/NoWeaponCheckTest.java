package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoWeaponCheckTest {
    private

    @BeforeEach
    void setUp() {
    }

    @Test
    public void compare_returnsCorrectValues() {
        Series series = new Series("Series", null);

        GameCharacter chara1 = new GameCharacter.Builder("chara1", series, CharacterElement.FIRE, CharacterClass.ALCHEMIST).build();
        GameCharacter chara2 = new GameCharacter.Builder("chara2", series, CharacterElement.WIND, CharacterClass.KNIGHT).build();
        GameCharacter chara3 = new GameCharacter.Builder("chara3", series, CharacterElement.EARTH, CharacterClass.MAGE).build();
        GameCharacter chara4 = new GameCharacter.Builder("chara4", series, CharacterElement.WATER, CharacterClass.PRIEST).build();

        Weapon weapon1 = new Weapon.Builder("Weapon1")
                .isExclusiveTo(chara1)
                .build();

        Weapon weapon2 = new Weapon.Builder("Weapon2")
                .isExclusiveTo(chara2)
                .build();

        Weapon weapon3 = new Weapon.Builder("Weapon3")
                .build();


        NoWeaponCheck check = new NoWeaponCheck(new ArrayList<>(Arrays.asList(weapon1, weapon2, weapon3)));

        // Characters who DON'T have an unique weapons should be preferred
        assertEquals(-1, check.compare(chara3, chara1));
        assertEquals(1, check.compare(chara2, chara4));
        assertEquals(0, check.compare(chara4, chara3)); // two characters who don't have an unique weapon
        assertEquals(0, check.compare(chara1, chara2)); // two characters who have an unique weapon
    }
}