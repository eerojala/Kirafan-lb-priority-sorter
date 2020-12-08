package logic.checks;

import domain.*;
import domain.model.GameCharacter;
import logic.Calculator;
import logic.Mapper;

import java.util.*;
import java.util.stream.Collectors;

public class SkillSetCheck extends Check {
    private Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass;

    public SkillSetCheck(Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass) {
        this.charactersByElementAndClass = charactersByElementAndClass;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        if (charaHasDesiredSkillset(c1) && !charaHasDesiredSkillset(c2)) {
            // if c1 has a desired skillset but c2 doesn't
            return -1;
        } else if (!charaHasDesiredSkillset(c1) && charaHasDesiredSkillset(c2)) {
            // if c2 has a desired skillset but c1 doesn't
            return 1;
        } else {
            // if neither or both players have a desired skillset
            return 0;
        }
    }

    private boolean charaHasDesiredSkillset(GameCharacter chara) {
        switch (chara.getCharacterClass()) {
            case ALCHEMIST:
                return alchemistHasDesiredSkillSet(chara);

            case KNIGHT:
                return knightHasDesiredSkillSet(chara);

            case MAGE:
                return mageHasDesiredSkillSet(chara);

            case PRIEST:
                return priestHasDesiredSkillSet(chara);

            case WARRIOR:
                return warriorHasDesiredSkillset(chara);

            default:
                return false;

        }
    }

    private boolean alchemistHasDesiredSkillSet(GameCharacter chara) {
        /*
        * An alchemist has a desired skill set if she has :
        *   1) more DEF DOWN skillpower OR
        *   2) more MDF DOWN skillpower OR
        *   3) more element resistance DOWN skillpower (of her own element) OR
        *   4) more SPD DOWN skillpower OR
        *   5) more status effect skillpower OR
        *
        * than other alchemists of the same element
        *
        * */

        return mostSkillPower(chara, SkillType.DEF, SkillChange.DOWN)
                || mostSkillPower(chara, SkillType.MDF, SkillChange.DOWN)
                || mostSkillPower(chara, SkillType.getAppropriateElementalResistance(chara.getCharacterElement()), SkillChange.DOWN)
                || mostSkillPower(chara, SkillType.SPD, SkillChange.DOWN)
                || mostStatusEffectPower(chara);
    }

    private boolean knightHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A knight has a desired skillset IF she has more:
         *   1) physical defense
         *   2) magical defense
         *   3) party 1x barrier skills
         *   4) self triple barrier skills
         *
         * than other knights of the same element OR:
         *   5) When the knight has 2 or more AoE attacks and there are no other already limit broken knights of the same
         *   element that already fullfill this requirement
         */

        Skill partySingleBarrier = new Skill(SkillType.BARRIER, null, SkillTarget.ALLIES_ALL, 0);
        Skill selfTripleBarrier = new Skill(SkillType.BARRIER_TRIPLE, null, SkillTarget.SELF, 0);
        Skill damageAllEnemies = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0);

        return leastDamageTaken(chara, SkillType.DEF) || leastDamageTaken(chara, SkillType.MDF)
                || desiredSkillAmount(chara, true, 1, partySingleBarrier)
                || desiredSkillAmount(chara, true, 1, selfTripleBarrier)
                || desiredSkillAmount(chara, true, 2, damageAllEnemies);
    }

    private boolean mageHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A mage has a desired skillset IF she:
         *   1) Has higher max damage than other mages of the same element, OR
         *   2) She has two attack skills which target all enemies (weapon skills not counted) AND there are no other
         *   already limit broken characters of the same element and class who fill this niche
         */
        Skill damageAllEnemies = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0);

        return highestMaxDamage(chara) || desiredSkillAmount(chara, false, 2, damageAllEnemies);
    }

    private boolean priestHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A priest has a desired skillset IF she has more:
         *   1) heal card skills
         *   2) status effect clear skills
         *   3) party 1x barrier skills
         *
         * than other priests of the same element OR
         *   4) When the priest SPEED UP skillpower than other priests of the same element
         */

        Skill healCard = new Skill(SkillType.HEAL_CARD, null, null, 0);
        Skill statusEffectClearSingle = new Skill(SkillType.STATUS_EFFECT_CLEAR, null, SkillTarget.ALLIES_SINGLE, 0);
        Skill statusEffectClearAll = new Skill(SkillType.STATUS_EFFECT_CLEAR, null, SkillTarget.ALLIES_ALL, 0);
        Skill partySingleBarrier = new Skill(SkillType.BARRIER, null, SkillTarget.ALLIES_ALL, 0);

        return desiredSkillAmount(chara, true,1, healCard)
                || desiredSkillAmount(chara, true,1, statusEffectClearSingle, statusEffectClearAll)
                || desiredSkillAmount(chara, true,1, partySingleBarrier)
                || mostSkillPower(chara, SkillType.SPD, SkillChange.UP);
    }

    private boolean warriorHasDesiredSkillset(GameCharacter chara) {
        /*
         * A mage has a desired skillset IF she:
         *   1) Has higher max damage than other warriors of the same element, OR
         *   2) She has two attack skills which target a single (weapon skills not counted) AND there are no already limit
         *   broken characters who already fulfill this condition
         */

        Skill damageSingleEnemy = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0);

        return highestMaxDamage(chara)
                || desiredSkillAmount(chara, false, 2, damageSingleEnemy);
    }

    private boolean mostSkillPower(GameCharacter chara, SkillType desiredSkillType, SkillChange desiredSkillChange) {
        /*
        * Return true in the following cases:
        *   -When chara has the highest total power of the desired skill of her element/class combination (whether
        *   chara is the only one or if there is another character who is not limit broken and is tied with chara for
        *   highest damage of their element/class combination)
        *
        * Returns false in the following cases:
        *   -When there is an other character who is limit broken and has the same total power of the desired skill
        *   -When there is an other character who has more total power of the desired skill
        */

        List<GameCharacter> charactersOfSameElementAndClass =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass());

        // Weapons should be taken into account when counting skill power totals
        Map<Skill, Double> charasSkillPowerTotals = Mapper.getSkillTotalPowers(chara);
        double charasDesiredSkillsTotalPower = Calculator.sumSkillTotalPowers(charasSkillPowerTotals, desiredSkillType, desiredSkillChange);

        for (GameCharacter other : charactersOfSameElementAndClass) {
            Map<Skill, Double> othersTotalSkillPowers = Mapper.getSkillTotalPowers(other);
            double othersDesiredSkillsTotalPower = Calculator.sumSkillTotalPowers(othersTotalSkillPowers, desiredSkillType, desiredSkillChange);

            if (othersDesiredSkillsTotalPower >= charasDesiredSkillsTotalPower) {
                if (other.isLimitBroken() || othersDesiredSkillsTotalPower > charasDesiredSkillsTotalPower) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<GameCharacter> getCharactersOfSpecificElementAndClass(CharacterElement charaElement, CharacterClass charaClass) {
        return charactersByElementAndClass.get(new AbstractMap.SimpleEntry<>(charaElement, charaClass));
    }

    private boolean mostStatusEffectPower(GameCharacter chara) {
        return mostSkillPower(chara, SkillType.CONFUSION, null)
                || mostSkillPower(chara, SkillType.ISOLATION, null)
                || mostSkillPower(chara, SkillType.HUNGER, null)
                || mostSkillPower(chara, SkillType.MISFORTUNE, null)
                || mostSkillPower(chara, SkillType.PARALYSIS, null)
                || mostSkillPower(chara, SkillType.SILENCE, null)
                || mostSkillPower(chara, SkillType.SLEEP, null)
                || mostSkillPower(chara, SkillType.TIMID, null);
    }

    private boolean leastDamageTaken(GameCharacter chara, SkillType typeOfDefense) {
        if (typeOfDefense != SkillType.DEF && typeOfDefense != SkillType.MDF) {
            return false;
        }

        List<GameCharacter> charactersOfSameElementAndClass =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass());

        GameCharacter leastDamageTakenCharacter = charactersOfSameElementAndClass
                .stream()
                .min(Comparator.comparing(c -> Calculator.calculateDamageTaken(c, typeOfDefense)))
                .orElse(null);

        // chara takes the least damage when compared to other characters with same element and class
        return leastDamageTakenCharacter.equals(chara);
    }

    private boolean desiredSkillAmount(GameCharacter chara, boolean includeWeapon, int desiredAmount, Skill desiredSkill) {
        return desiredSkillAmount(chara, includeWeapon, desiredAmount, desiredSkill);
    }

    private boolean desiredSkillAmount(GameCharacter chara, boolean includeWeapon, int desiredAmount, Skill... desiredSkills) {
        long skillAmounts = Calculator.countAmountOfSkills(chara, includeWeapon, desiredSkills);

        if (skillAmounts < desiredAmount) {
            return false; // chara doesn't have desired amount of the desired skill (e.g. two damage skills)
        }

        List<GameCharacter> limitBrokenSameElementClassCharas =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass())
                        .stream()
                        .filter(c -> c.isLimitBroken())
                        .collect(Collectors.toList());

        for (GameCharacter c : limitBrokenSameElementClassCharas) {
            skillAmounts = Calculator.countAmountOfSkills(c, includeWeapon, desiredSkills);

            if (skillAmounts >= desiredAmount) {
                // There exists a limit broken character of the same element and class who has a desired amount of skill (or more)
                return false;
            }
        }

        // chara has the desired amount of skill and there doesn't exist a limit broken character who fulfills this condition too
        // (other non-limit broken characters who fulfill this condition are allowed though)
        return true;
    }

    private boolean highestMaxDamage(GameCharacter chara) {
        List<GameCharacter> charactersOfSameElementAndClass =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass());

        GameCharacter highestDamageCharacter = charactersOfSameElementAndClass
                .stream()
                .max(Comparator.comparing(c -> Calculator.calculateMaxDamageGiven(c)))
                .orElse(null);

        // chara has gives the highest max damage when compared to other characters with same element and class
        return highestDamageCharacter.equals(chara);
    }
}
