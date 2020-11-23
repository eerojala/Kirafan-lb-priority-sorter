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

    public static void addCharacterToCharactersByElementAndClass(
            GameCharacter chara,
            Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass
    ) {
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

    public static void addCharacterToCharactersBySeries(GameCharacter chara, Map<Series, List<GameCharacter>> charactersBySeries) {
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

    public static Map<Skill,Double> getSkillTotalAmounts(GameCharacter chara) {
        Map<Skill, Double> skillTotalAmounts = new HashMap<>();

        if (chara != null) {
            Weapon weapon = chara.getPreferredWeapon();

            for (Skill skill : chara.getSkills()) {
                addSkillAmount(skill, skillTotalAmounts);
            }

            if (weapon != null) {
                for (Skill skill : weapon.getSkills()) {
                    addSkillAmount(skill, skillTotalAmounts);
                }
            }

        }

        return skillTotalAmounts;
    }

    private static void addSkillAmount(Skill skill, Map<Skill, Double> skillTotalAmounts) {
        SkillType type = skill.getType();
        double amount = skill.getAmount();
        Double previousTotalAmount = skillTotalAmounts.get(skill);

        if (type == SkillType.NEXT_ATK || type == SkillType.NEXT_MAT) {
            // unlike other buffs and debuffs next ATK and next MAT buffs do not stack and instead overwrite the previous NEXT ATK/MAT buff
            Double totalAmount = amount > previousTotalAmount ? amount : previousTotalAmount;
            skillTotalAmounts.put(skill, totalAmount);
        } else  {
            previousTotalAmount = previousTotalAmount == null ? 0 : previousTotalAmount;
            skillTotalAmounts.put(skill, previousTotalAmount + amount);
        }
    }
}
