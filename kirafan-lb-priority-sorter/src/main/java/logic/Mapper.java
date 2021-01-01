package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.Skill;
import domain.SkillType;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.util.*;

public final class Mapper {
    private Mapper() {}

    public static Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> getCharactersByElementAndClass(List<GameCharacter> characters) {
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass
                = new HashMap<>();

        if (characters != null) {
            for (GameCharacter chara : characters) {
                addCharacterToCharactersByElementAndClass(chara, charactersByElementAndClass);
            }
        }

        return charactersByElementAndClass;
    }

    private static void addCharacterToCharactersByElementAndClass(
            GameCharacter chara,
            Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass) {
        if (chara == null || charactersByElementAndClass == null) {
            return;
        }

        AbstractMap.SimpleEntry<CharacterElement, CharacterClass> elementClassPair =
                new AbstractMap.SimpleEntry<>(chara.getCharacterElement(), chara.getCharacterClass());
        List<GameCharacter> charactersOfSameElementAndClass = charactersByElementAndClass.get(elementClassPair);

        if (charactersOfSameElementAndClass == null) {
            charactersOfSameElementAndClass = new ArrayList<>();
        }

        charactersOfSameElementAndClass.add(chara);
        charactersByElementAndClass.put(elementClassPair, charactersOfSameElementAndClass);
    }

    public static Map<Series, List<GameCharacter>> getCharactersBySeries(List<GameCharacter> characters) {
        Map<Series, List<GameCharacter>> charactersBySeries = new HashMap<>();

        if (characters != null) {
            for (GameCharacter chara : characters) {
                addCharacterToCharactersBySeries(chara, charactersBySeries);
            }
        }

        return charactersBySeries;
    }

    private static void addCharacterToCharactersBySeries(GameCharacter chara, Map<Series, List<GameCharacter>> charactersBySeries) {
        if (chara == null || charactersBySeries == null) {
            return;
        }

        Series series = chara.getSeries();
        List<GameCharacter> charactersOfSameSeries = charactersBySeries.get(series);

        if (charactersOfSameSeries == null) {
            charactersOfSameSeries = new ArrayList<>();
        }

        charactersOfSameSeries.add(chara);
        charactersBySeries.put(series, charactersOfSameSeries);
    }

    public static Map<GameCharacter, Weapon> getWeaponsByCharacter(List<Weapon> weapons) {
        Map<GameCharacter, Weapon> weaponsByCharacter = new HashMap<>();

        if (weapons != null) {
            for (Weapon weapon : weapons) {
                GameCharacter exclusiveCharacter = weapon.getExclusiveCharacter();

                if (exclusiveCharacter != null) {
                    weaponsByCharacter.put(exclusiveCharacter, weapon);
                }
            }
        }

        return weaponsByCharacter;
    }

    public static Map<Skill,Double> getSkillTotalPowers(GameCharacter chara) {
        Map<Skill, Double> skillTotalPowers = new HashMap<>();

        if (chara != null) {
            Weapon weapon = chara.getPreferredWeapon();

            for (Skill skill : chara.getSkills()) {
                addSkillPower(skill, skillTotalPowers);
            }

            if (weapon != null) {
                for (Skill skill : weapon.getSkills()) {
                    addSkillPower(skill, skillTotalPowers);
                }
            }

        }

        return skillTotalPowers;
    }

    private static void addSkillPower(Skill skill, Map<Skill, Double> skillTotalPowers) {
        SkillType type = skill.getType();
        double power = skill.getPower();
        Double previousTotalPower = skillTotalPowers.get(skill);
        previousTotalPower = previousTotalPower == null ? 0 : previousTotalPower;

        if (type == SkillType.NEXT_ATK || type == SkillType.NEXT_MAT) {
            // unlike other buffs and debuffs next ATK and next MAT buffs do not stack and instead overwrite the previous NEXT ATK/MAT buff
            Double totalPower = power > previousTotalPower ? power : previousTotalPower;
            skillTotalPowers.put(skill, totalPower);
        } else  {
            skillTotalPowers.put(skill, Calculator.sumDoubles(previousTotalPower, power));
        }
    }
}
