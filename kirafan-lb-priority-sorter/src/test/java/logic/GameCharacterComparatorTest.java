package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterComparatorTest {
    public GameCharacterComparatorTest() {}

    @Test
    public void compare_sortsCharactersCorrectly() {
        Series series1 = new Series("Series 1", null);

        GameCharacter chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.FIRE, CharacterClass.ALCHEMIST).build();
        GameCharacter chara2 = new GameCharacter.Builder("chara2", series1, CharacterElement.WIND, CharacterClass.PRIEST).build();
        GameCharacter chara3 = new GameCharacter.Builder("chara3", series1, CharacterElement.EARTH, CharacterClass.PRIEST).build();

        List<GameCharacter> nonLimitBrokenCharas = new ArrayList<>(Arrays.asList(chara1, chara2, chara3));
        List<GameCharacter> allCharas = new ArrayList<>(Arrays.asList(chara1, chara2, chara3));
        List<Weapon> weapons = new ArrayList<>();
        GameEvent event = new GameEvent("asdf");
        event.setBonusCharacters(new ArrayList<>(Collections.singletonList(chara2)));

        GameCharacterComparator comparator = new GameCharacterComparator(allCharas, weapons, event);

        // Initially the list should be ordered as such:
        assertEquals(chara1, nonLimitBrokenCharas.get(0));
        assertEquals(chara2, nonLimitBrokenCharas.get(1));
        assertEquals(chara3, nonLimitBrokenCharas.get(2));

        //
        // TESTING EventBonusCheck
        //
        event.setBonusCharacters(new ArrayList<>(Collections.singletonList(chara2)));
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara2 should be first on the list because she is the only event bonus character
        assertEquals(chara2, nonLimitBrokenCharas.get(0));

        ///
        // TESTING THE FIRST PersonalPreferenceCheck (>=9)
        //
        event.getBonusCharacters().add(chara3);
        event.getBonusCharacters().add(chara1);
        chara3.setPersonalPreference(10);
        chara1.setPersonalPreference(9);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara 3 should be first because she is an event bonus character and has a personal preference of 10
        // chara 1 should be second because she is an event bonus character and has a personal preference of 9
        // chara 2 should be third because she is an event bonus character and has a personal preference of 0
        assertEquals(chara3, nonLimitBrokenCharas.get(0));
        assertEquals(chara1, nonLimitBrokenCharas.get(1));
        assertEquals(chara2, nonLimitBrokenCharas.get(2));

        //
        // TESTING MissingElementClassCombinationCheck
        //
        GameCharacter chara4 = new GameCharacter.Builder("chara4", series1, chara2.getCharacterElement(), chara2.getCharacterClass())
                .limitBroken()
                .build();

        GameCharacter chara5 = new GameCharacter.Builder("chara5", series1, chara3.getCharacterElement(), chara3.getCharacterClass())
                .limitBroken()
                .build();

        allCharas.add(chara4);
        allCharas.add(chara5);
        // we create a new comparator because there are new limit broken characters
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        chara1.setPersonalPreference(10);
        chara2.setPersonalPreference(10);
        chara3.setPersonalPreference(0);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara 1 should be the first because there are no already limit broken characters of the same element/class combination
        // chara 2 should be the second because there are already limit broken characters of the same element/class combination but she has a personal preference of 10
        // chara 3 should be third because there are already limit broken characters of the same element/class combination but she has a personal preference of 0
        assertEquals(chara1, nonLimitBrokenCharas.get(0));
        assertEquals(chara2, nonLimitBrokenCharas.get(1));
        assertEquals(chara3, nonLimitBrokenCharas.get(2));

        //
        // TESTING THE FIRST CreaCheck(CreaStatus.INCOMPLETE))
        //
        GameCharacter chara6 = new GameCharacter.Builder("chara6", series1, chara1.getCharacterElement(), chara1.getCharacterClass())
                .limitBroken()
                .build();

        allCharas.add(chara6); // now chara1 is not the only non-limit broken member of her element/class combination
        chara1.setPersonalPreference(0);
        chara2.setPersonalPreference(0);
        Series series2 = new Series("series 2", CreaStatus.INCOMPLETE);
        chara2.setSeries(series2);
        // we create a new comparator because there is a new limit broken character and chara2 has an another series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara2 should be the first because her series has an incomplete crea
        assertEquals(chara2, nonLimitBrokenCharas.get(0));

        //
        // TESTING THE SECOND PersonalPreferenceCheck(>=7)
        //
        chara3.setSeries(series2);
        chara3.setPersonalPreference(8);
        chara1.setSeries(series2);
        chara1.setPersonalPreference(7);
        // we create a new comparator because chara3 and chara1 have new series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara 3 should be first because she belongs to an incomplete crea series and has a personal preference of 8
        // chara 1 should be second because she belongs to an incomplete crea series and has a personal preference of 7
        // chara 2 should be third because she belogns to an incomplete crea series and has a personal preference of 0
        assertEquals(chara3, nonLimitBrokenCharas.get(0));
        assertEquals(chara1, nonLimitBrokenCharas.get(1));
        assertEquals(chara2, nonLimitBrokenCharas.get(2));

        //
        // TESTING SkillSetCheck
        //
        chara3.setPersonalPreference(7);
        chara2.setPersonalPreference(8);
        chara1.setPersonalPreference(8);
        chara1.getSkills().add(new Skill(SkillType.DEF, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 10));
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara1 should be first because she has the most DEF DOWN skill power out of all fire alchemists and a personal preference of 8
        // chara2 should be second because she has a personal preference of 8 (but no desired skill set)
        // chara3 should be third because she has a personal preference of 7 (but no desired skill set)
        assertEquals(chara1, nonLimitBrokenCharas.get(0));
        assertEquals(chara2, nonLimitBrokenCharas.get(1));
        assertEquals(chara3, nonLimitBrokenCharas.get(2));

        //
        // TESTING HighestWokeCheck
        //
        chara1.setSkills(new ArrayList<>()); // chara1 has no skills anymore
        chara1.setPersonalPreference(0);
        chara2.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLIES_ALL, 10));
        chara2.setWokeLevel(1);
        chara2.setPersonalPreference(0);
        chara3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_ALL, 0));
        chara3.setPersonalPreference(0);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara2 should be first because she has a desired skill set and is the highest woke in her series
        // chara3 should be second because she has a desired skill set but is not the highest woke in her series
        // chara  should be third because she has no desired skill set
        assertEquals(chara2, nonLimitBrokenCharas.get(0));
        assertEquals(chara3, nonLimitBrokenCharas.get(1));
        assertEquals(chara1, nonLimitBrokenCharas.get(2));

        //
        // TESTING NoWeaponCheck
        //
        Weapon weapon1 = new Weapon.Builder("weapon1")
                .isExclusiveTo(chara1)
                .build();

        weapons.add(weapon1);
        series1.setCreaStatus(CreaStatus.INCOMPLETE);
        chara1.setWokeLevel(1);
        chara1.setSeries(series1);
        chara1.setSkills(new ArrayList<>());
        chara2.setWokeLevel(0);
        chara2.setSkills(new ArrayList<>());
        chara3.setWokeLevel(1);
        chara3.setSkills(new ArrayList<>());
        // we create a new comparator because there is a new exclusive weapon for chara1 and chara1 has a new series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara3 should be first because she has no exclusive weapon and is the highest woke in her series
        // chara1 should be second because she has an exclusive weapon  and is the highest woke in her series
        // chara2 should be third because she is not the highest woke in her series
        assertEquals(chara3, nonLimitBrokenCharas.get(0));
        assertEquals(chara1, nonLimitBrokenCharas.get(1));
        assertEquals(chara2, nonLimitBrokenCharas.get(2));


        //
        // TESTING THE SECOND CreaCheck(CreaStatus.COMPLETE)
        //
        weapon1.setExclusiveCharacter(chara3);
        Series series3 = new Series("series3", CreaStatus.COMPLETE);
        Series series4 = new Series("series4", CreaStatus.NONE);
        chara1.setWokeLevel(0);
        chara1.setSeries(series3);
        chara2.setWokeLevel(0);
        chara2.setSeries(series4);
        chara3.setWokeLevel(0);
        chara3.setSeries(series4);
        // we create a new comparator because charas1-3 have a new series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara1 should be first because she belongs to a series with completed crea status and has no exclusive weapon
        // chara2 should be second because she belongs to a series with none crea status and has no exclusive weapon
        // chara3 should be third because she belongs to a series with none crea status and has an exclusive weapon
        assertEquals(chara1, nonLimitBrokenCharas.get(0));
        assertEquals(chara2, nonLimitBrokenCharas.get(1));
        assertEquals(chara3, nonLimitBrokenCharas.get(2));

        //
        // TESTING THE THIRD PersonalPreferenceCheck(>0)
        //
        weapon1.setExclusiveCharacter(null);
        chara1.setSeries(series4);
        chara1.setPersonalPreference(4);
        chara2.setSeries(series3);
        chara2.setPersonalPreference(3);
        chara3.setSeries(series3);
        chara3.setPersonalPreference(2);
        // we create a new comparator because charas1-3 have a new series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // chara2 should be first because she has a personal preference of 3 and belongs to a series with complete crea
        // chara3 should be second because she has a personal preference of 2 and belongs to a series with complete crea
        // chara1 should be third because she has a personal preference of 4 and belongs to a series with none status crea
        assertEquals(chara2, nonLimitBrokenCharas.get(0));
        assertEquals(chara3, nonLimitBrokenCharas.get(1));
        assertEquals(chara1, nonLimitBrokenCharas.get(2));

        chara1.setPersonalPreference(0);
        chara2.setSeries(series4);
        chara2.setPersonalPreference(0);
        chara3.setSeries(series4);
        chara3.setPersonalPreference(0);
        // we create a new comparator because chara2 and chara3 have a new series
        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // The positions should not shift because charas1-3 are considered  to be of totally equal priority now
        assertEquals(chara2, nonLimitBrokenCharas.get(0));
        assertEquals(chara3, nonLimitBrokenCharas.get(1));
        assertEquals(chara1, nonLimitBrokenCharas.get(2));
    }
}