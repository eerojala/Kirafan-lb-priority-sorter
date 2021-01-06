package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameCharacterComparatorTest {
    public GameCharacterComparatorTest() {}

    @Test
    public void compare_sortsCharactersCorrectly() {
        Series series1 = new Series("Series 1", null, "1");

        GameCharacter chara1 = new GameCharacter.Builder("chara1", series1, CharacterElement.FIRE, CharacterClass.ALCHEMIST)
                .overwriteID("1")
                .build();
        GameCharacter chara2 = new GameCharacter.Builder("chara2", series1, CharacterElement.WIND, CharacterClass.PRIEST)
                .overwriteID("2")
                .build();
        GameCharacter chara3 = new GameCharacter.Builder("chara3", series1, CharacterElement.EARTH, CharacterClass.PRIEST)
                .overwriteID("3")
                .build();

        List<GameCharacter> nonLimitBrokenCharas = new ArrayList<>(Arrays.asList(chara1, chara2, chara3));
//        List<GameCharacter> allCharas = new ArrayList<>(Arrays.asList(chara1, chara2, chara3));
//        List<Weapon> weapons = new ArrayList<>();
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charasByElementAndClass
                = new HashMap<>();

        charasByElementAndClass.put(
                new AbstractMap.SimpleEntry<>(CharacterElement.FIRE, CharacterClass.ALCHEMIST),
                new ArrayList<>(Collections.singletonList(chara1)));

        charasByElementAndClass.put(
                new AbstractMap.SimpleEntry<>(CharacterElement.WIND, CharacterClass.PRIEST),
                new ArrayList<>(Collections.singletonList(chara2)));

        charasByElementAndClass.put(
                new AbstractMap.SimpleEntry<>(CharacterElement.EARTH, CharacterClass.PRIEST),
                new ArrayList<>(Collections.singletonList(chara3)));

        Map<Series, List<GameCharacter>> charasBySeries = new HashMap<>();
        charasBySeries.put(series1, new ArrayList<>(Arrays.asList(chara1, chara2, chara3)));

        Map<GameCharacter, Weapon> weaponsByChara = new HashMap();

        GameEvent event = new GameEvent("asdf");
        event.setBonusCharacters(new ArrayList<>(Collections.singletonList(chara2)));

        GameCharacterComparator comparator = new GameCharacterComparator(charasByElementAndClass, charasBySeries,
                weaponsByChara, event);

        // Initially the list should be ordered as such:
        assertEquals(chara1, nonLimitBrokenCharas.get(0));
        assertEquals(chara2, nonLimitBrokenCharas.get(1));
        assertEquals(chara3, nonLimitBrokenCharas.get(2));


        // GameCharacterComparator returns appropriate values when comparing with null charas
        assertEquals(-1, comparator.compare(chara1, null));
        assertEquals(1, comparator.compare(null, chara1));
        assertEquals(0, comparator.compare(null, null));

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
                .overwriteID("4")
                .limitBroken()
                .build();

        GameCharacter chara5 = new GameCharacter.Builder("chara5", series1, chara3.getCharacterElement(), chara3.getCharacterClass())
                .overwriteID("5")
                .limitBroken()
                .build();

        charasByElementAndClass.get(new AbstractMap.SimpleEntry<>(chara2.getCharacterElement(), chara2.getCharacterClass()))
                .add(chara4);

        charasByElementAndClass.get(new AbstractMap.SimpleEntry<>(chara3.getCharacterElement(), chara3.getCharacterClass()))
                .add(chara5);

        charasBySeries.get(series1).addAll(new ArrayList<>(Arrays.asList(chara4, chara5)));
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
                .overwriteID("6")
                .limitBroken()
                .build();

        charasByElementAndClass.get(new AbstractMap.SimpleEntry<>(chara1.getCharacterElement(), chara1.getCharacterClass()))
                .add(chara6);

        charasBySeries.get(series1).add(chara6);

        chara1.setPersonalPreference(0);
        chara2.setPersonalPreference(0);
        Series series2 = new Series("series 2", CreaStatus.INCOMPLETE, "2");
        chara2.setSeries(series2);
        charasBySeries.get(series1).remove(chara2);
        charasBySeries.put(series2, new ArrayList<>(Arrays.asList(chara2)));
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
        charasBySeries.get(series1).removeAll(new ArrayList<>(Arrays.asList(chara1, chara3)));
        charasBySeries.get(series2).addAll(new ArrayList<>(Arrays.asList(chara1, chara3)));
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
        chara2.getSkills().add(new Skill(SkillType.SPD, SkillChange.UP, SkillTarget.ALLY_ALL, 10));
        chara2.setWokeLevel(1);
        chara2.setPersonalPreference(0);
        chara3.getSkills().add(new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLY_ALL, 0));
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

        weaponsByChara.put(chara1, weapon1);
        series1.setCreaStatus(CreaStatus.INCOMPLETE);
        chara1.setWokeLevel(1);
        chara1.setSeries(series1);
        chara1.setSkills(new ArrayList<>());
        chara2.setWokeLevel(0);
        chara2.setSkills(new ArrayList<>());
        chara3.setWokeLevel(1);
        chara3.setSkills(new ArrayList<>());
        charasBySeries.get(series2).remove(chara1);
        charasBySeries.get(series1).add(chara1);
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
        weaponsByChara.remove(chara1);
        weaponsByChara.put(chara3, weapon1);
        Series series3 = new Series("series3", CreaStatus.COMPLETE, "3");
        Series series4 = new Series("series4", CreaStatus.NONE, "4");
        chara1.setWokeLevel(0);
        chara1.setSeries(series3);
        chara2.setWokeLevel(0);
        chara2.setSeries(series4);
        chara3.setWokeLevel(0);
        chara3.setSeries(series4);
        charasBySeries.get(series1).remove(chara1);
        charasBySeries.get(series2).removeAll(Arrays.asList(chara2, chara3));
        charasBySeries.put(series3, new ArrayList<>(Collections.singletonList(chara1)));
        charasBySeries.put(series4, new ArrayList<>(Arrays.asList(chara2, chara3)));
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
        weaponsByChara.remove(chara3);
        chara1.setSeries(series4);
        chara1.setPersonalPreference(4);
        chara2.setSeries(series3);
        chara2.setPersonalPreference(3);
        chara3.setSeries(series3);
        chara3.setPersonalPreference(2);
        charasBySeries.get(series3).remove(chara1);
        charasBySeries.get(series4).remove(Arrays.asList(chara2, chara3));
        charasBySeries.get(series3).addAll(Arrays.asList(chara2, chara3));
        charasBySeries.get(series4).add(chara1);
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
        charasBySeries.get(series3).removeAll(Arrays.asList(chara2, chara3));
        charasBySeries.get(series4).addAll(Arrays.asList(chara2, chara3));
        // we create a new comparator because chara2 and chara3 have a new series
//        comparator = new GameCharacterComparator(allCharas, weapons, null);
        Collections.sort(nonLimitBrokenCharas, comparator);
        // The positions should not shift because charas1-3 are considered  to be of totally equal priority now
        assertEquals(chara2, nonLimitBrokenCharas.get(0));
        assertEquals(chara3, nonLimitBrokenCharas.get(1));
        assertEquals(chara1, nonLimitBrokenCharas.get(2));
    }
}