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
        boolean c1HasDesiredSkillset = charaHasDesiredSkillset(c1);
        boolean c2hasDesiredSkillset = charaHasDesiredSkillset(c2);

        if (c1HasDesiredSkillset && !c2hasDesiredSkillset) {
            // if c1 has a desired skillset but c2 doesn't
            return -1;
        } else if (!c1HasDesiredSkillset && c2hasDesiredSkillset) {
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
        * An alchemist's desire skillsets (compared with alchemists of the same element)
        *   1) Most DEF DOWN skillpower
        *   2) Most MDF DOWN skillpower
        *   3) Most element resistance DOWN skillpower (of her own element)
        *   4) Most SPD DOWN skillpower
        *   5) Most abnormal effect skillpower (of any abnormal effect)
        */

        // The elemental resist that corresponds to the characters element, e.g. fire resistance for a fire character
        SkillType appropriateElementalResistance = SkillType.getAppropriateElementalResistance(chara.getCharacterElement());

        return mostSkillPowerCheck(chara, SkillType.DEF, SkillChange.DOWN, true)
                || mostSkillPowerCheck(chara, SkillType.MDF, SkillChange.DOWN, true)
                || mostSkillPowerCheck(chara, appropriateElementalResistance, SkillChange.DOWN, true)
                || mostSkillPowerCheck(chara, SkillType.SPD, SkillChange.DOWN, true)
                || mostAbnormalEffectCheck(chara);
    }

    private boolean knightHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A knight's desired skillsets (compared with knights of the same element):
         *   1) Takes least physical damage (against element which the character is neutral against)
         *   2) Takes least magical damage (against element which the character is neutral against)
         *   3) Most abnormal effect disable skills
         *   4) Most abnormal effect recover skills
         *   5) Most ALLIES ALL 1x barrier skills
         *   6) Most self 3x barrier skills
         *   7) Most damage all enemies skills
         */

        Skill abnormalDisableSelf = new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.SELF, 0);
        Skill abnormalDisableAllySingle = new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0);
        Skill abnormalDisableAllyAll = new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0);
        Skill abnormalRecoverSelf = new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.SELF, 0);
        Skill abnormalRecoverAllySingle = new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0);
        Skill abnormalRecoverAllyAll = new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_ALL, 0);
        Skill singleBarrierAllyAll = new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_ALL, 0);
        Skill tripleBarrierSelfAll = new Skill(SkillType.BARRIER_FULL_TRIPLE, null, SkillTarget.SELF, 0);
        Skill damageAllEnemies = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0);

        return takesLeastDamage(chara, SkillType.DEF) || takesLeastDamage(chara, SkillType.MDF)
                || mostSkillAmountCheck(chara, true, 1, abnormalDisableSelf, abnormalDisableAllySingle, abnormalDisableAllyAll)
                || mostSkillAmountCheck(chara, true, 1, abnormalRecoverSelf, abnormalRecoverAllySingle, abnormalRecoverAllyAll)
                || mostSkillAmountCheck(chara, true, 1, singleBarrierAllyAll)
                || mostSkillAmountCheck(chara, true, 1, tripleBarrierSelfAll)
                || mostSkillAmountCheck(chara, true, 2, damageAllEnemies);
    }

    private boolean mageHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A mage's desired skillsets (compared with mages of the same element)
         *   1) Highest max damage
         *   2) Most damage ALL ENEMIES skills
         */
        Skill damageAllEnemies = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_ALL, 0);

        return hasHighestMaxDamage(chara) || mostSkillAmountCheck(chara, false, 2, damageAllEnemies);
    }

    private boolean priestHasDesiredSkillSet(GameCharacter chara) {
        /*
         * A priest's desired skillsets (compared with priests of the same element)
         *   1) Most heal card skills
         *   2) Most abnormal effect disable skills (abnormal disable skills which only target self not counted)
         *   3) Most abnormal effect recover skills (abnormal recover skills which only target self not counted)
         *   4) Most ally all 1x barrier skills
         *   5) Most SPD UP buffs skillpower
         */

        Skill healCard = new Skill(SkillType.HEAL_CARD, null, SkillTarget.ALLIES_ALL, 0);
        Skill abnormalDisableAllySingle = new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_SINGLE, 0);
        Skill abnormalDisableAllyAll = new Skill(SkillType.ABNORMAL_DISABLE, null, SkillTarget.ALLIES_ALL, 0);
        Skill abnormalRecoverAllySingle = new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_SINGLE, 0);
        Skill abnormalRecoverAllyAll = new Skill(SkillType.ABNORMAL_RECOVER, null, SkillTarget.ALLIES_ALL, 0);
        Skill singleBarrierAllyAll = new Skill(SkillType.BARRIER_FULL, null, SkillTarget.ALLIES_ALL, 0);

        return mostSkillAmountCheck(chara, true,1, healCard)
                || mostSkillAmountCheck(chara, true, 1, abnormalDisableAllySingle, abnormalDisableAllyAll)
                || mostSkillAmountCheck(chara, true,1, abnormalRecoverAllySingle, abnormalRecoverAllyAll)
                || mostSkillAmountCheck(chara, true,1, singleBarrierAllyAll)
                || mostSkillPowerCheck(chara, SkillType.SPD, SkillChange.UP, false);
    }

    private boolean warriorHasDesiredSkillset(GameCharacter chara) {
        /*
         * A warrior's desired skillset::
         *   1) Highest max damage
         *   2) Most damage SINGLE ENEMY skills
         */

        Skill damageSingleEnemy = new Skill(SkillType.DAMAGE, null, SkillTarget.ENEMY_SINGLE, 0);

        return hasHighestMaxDamage(chara)
                || mostSkillAmountCheck(chara, false, 2, damageSingleEnemy);
    }

    private boolean mostSkillPowerCheck(GameCharacter chara, SkillType skillType, SkillChange skillChange, boolean targetEnemy) {
        /*
        * Return true in the following cases:
        *   -When chara has the highest total power of the desired skill of her element/class combination
        *   (ties with other non-limit broken characters are allowed)
        *
        * Returns false in the following cases:
        *   -When there is an other character who is limit broken and has the same total power (or more) of the desired skills
        *   -When there is an other character who is not limit broken and has more total power of the desired skills
        */

        // Other characters of the same element and class as chara which the comparisons are done against
        List<GameCharacter> otherCharacters =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass())
                        .stream()
                        .filter(c -> !c.equals(chara))
                        .collect(Collectors.toList());

        // Weapons should be taken into account when counting skill power totals
        Map<Skill, Double> charasSkillPowerTotals = Mapper.getSkillTotalPowers(chara);
        double charasDesiredSkillsTotalPower = getAppropriateSkillPowerSum(charasSkillPowerTotals, skillType,
                skillChange, targetEnemy);

        if (charasDesiredSkillsTotalPower <= 0) {
            return false;
        }

        for (GameCharacter other : otherCharacters) {
            Map<Skill, Double> othersTotalSkillPowers = Mapper.getSkillTotalPowers(other);
            double othersDesiredSkillsTotalPower = getAppropriateSkillPowerSum(othersTotalSkillPowers, skillType,
                    skillChange, targetEnemy);

            if ((other.isLimitBroken() && othersDesiredSkillsTotalPower >= charasDesiredSkillsTotalPower)
                    || (!other.isLimitBroken() && othersDesiredSkillsTotalPower > charasDesiredSkillsTotalPower)) {

                return false;
            }
        }

        return true;
    }

    private List<GameCharacter> getCharactersOfSpecificElementAndClass(CharacterElement charaElement, CharacterClass charaClass) {
        return charactersByElementAndClass.get(new AbstractMap.SimpleEntry<>(charaElement, charaClass));
    }

    private double getAppropriateSkillPowerSum(Map<Skill, Double> skillPowerTotals, SkillType skillType,
                                               SkillChange skillChange, boolean targetEnemy) {

        if (SkillType.isBuffOrDebuff(skillType)) {
            if (skillChange == SkillChange.UP) {
                if (targetEnemy) {
                    return Calculator.sumBuffsToOpponent(skillType, skillPowerTotals);
                } else {
                    return Calculator.sumBuffsToSelf(skillType, skillPowerTotals);
                }
            } else if (skillChange == SkillChange.DOWN) {
                if (targetEnemy) {
                    return Calculator.sumDebuffsToOpponent(skillType, skillPowerTotals);
                } else {
                    return Calculator.sumDebuffsToSelf(skillType, skillPowerTotals);
                }
            } else {
                return 0;
            }
        } else {
            if (targetEnemy) {
                return Calculator.sumOtherEffectsToOpponent(skillType, skillPowerTotals);
            } else {
                return Calculator.sumOtherEffectsToSelf(skillType, skillPowerTotals);
            }
        }
    }

    private boolean mostAbnormalEffectCheck(GameCharacter chara) {
        return mostSkillPowerCheck(chara, SkillType.CONFUSION, null, true)
                || mostSkillPowerCheck(chara, SkillType.ISOLATION, null, true)
                || mostSkillPowerCheck(chara, SkillType.HUNGER, null, true)
                || mostSkillPowerCheck(chara, SkillType.MISFORTUNE, null, true)
                || mostSkillPowerCheck(chara, SkillType.PARALYSIS, null, true)
                || mostSkillPowerCheck(chara, SkillType.SILENCE, null, true)
                || mostSkillPowerCheck(chara, SkillType.SLEEP, null, true)
                || mostSkillPowerCheck(chara, SkillType.TIMID, null, true);
    }

    private boolean takesLeastDamage(GameCharacter chara, SkillType typeOfDefense) {
        /*
        * Returns true when
        *   -Chara takes the least damage of given type (physical or magical) when compared to other characters of the
        *   same element (ties are allowed with other characters if they are no timit broken)
        *
        * Returns false when
        *   -An other already limit broken character of the same element and class takes equal or less damage than chara OR
        *   -An other non-limit broken character of the same element and class takes less damage than chara
        * */

        if (typeOfDefense != SkillType.DEF && typeOfDefense != SkillType.MDF) {
            return false;
        }

        long takenDamageChara = Calculator.calculateDamageTaken(chara, typeOfDefense);

        // Other characters of the same element and class as chara which the comparisons are done against
        List<GameCharacter> otherCharacters =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass())
                .stream()
                .filter(c -> !c.equals(chara))
                .collect(Collectors.toList());

        for (GameCharacter other : otherCharacters) {
            long takenDamageOther = Calculator.calculateDamageTaken(other, typeOfDefense);

            if ((other.isLimitBroken() && takenDamageOther <= takenDamageChara)
                    || (!other.isLimitBroken() && takenDamageOther < takenDamageChara)) {
                return false;
            }
        }

        return true;
    }

    private boolean mostSkillAmountCheck(GameCharacter chara, boolean includeWeapon, int minAmount, Skill... skills) {
        /*
         * Return true in the following cases:
         *   -When chara has the most amount of certain skills (at least a min amount) of her element/class combination
         *   (ties with other non-limit broken characters are allowed)
         *
         * Returns false in the following cases:
         *   -When there is an other character who is limit broken and has the same total amount (or more) of the desired skills
         *   -When there is an other character who is not limit broken and has more total amount of the desired skills
         */

        long skillAmountsChara = Calculator.countAmountOfSpecificSkills(chara, includeWeapon, skills);

        if (skillAmountsChara < minAmount) {
            /* chara doesn't have min amount of the specific skills (e.g. disable status effects ALL ALLIES and
            disable status effects SINGLE ALLY) */
            return false;
        }

        // Other characters of the same element and class as chara which the comparisons are done against
        List<GameCharacter> otherCharacters =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass())
                .stream()
                .filter(c -> !c.equals(chara))
                .collect(Collectors.toList());

        for (GameCharacter other : otherCharacters) {
            long skillAmountsOther = Calculator.countAmountOfSpecificSkills(other, includeWeapon, skills);

            if ((other.isLimitBroken() && skillAmountsOther >= skillAmountsChara)
                    || (!other.isLimitBroken() && skillAmountsOther > skillAmountsChara)) {
                /*
                 * if there is a limit broken character who has more or equal amount of specific skills
                 * (when compared to the inputted chara) OR:
                 *
                 * if there is a non-limit broken character who has more of the inputted skills (when compared to the
                 * inputted chara)
                 *
                 * return false (otherwise return true)
                 */
                return false;
            }
        }

        return true;
    }

    private boolean hasHighestMaxDamage(GameCharacter chara) {
        /*
         * Returns true when
         *   -Chara causes the most amount of damage (physical or magical depending on class) when compared to other characters of the
         *   same element (ties are allowed with other characters if they are no timit broken)
         *
         * Returns false when
         *   -An other already limit broken character of the same element and class causes equal or more damage than chara OR
         *   -An other non-limit broken character of the same element and class causes more damage than chara
         * */

        long maxDamageCausedChara = Calculator.calculateMaxDamageCaused(chara);

        // Other characters of the same element and class as chara which the comparisons are done against
        List<GameCharacter> otherCharacters =
                getCharactersOfSpecificElementAndClass(chara.getCharacterElement(), chara.getCharacterClass())
                .stream()
                .filter(c -> !c.equals(chara))
                .collect(Collectors.toList());

        for (GameCharacter other : otherCharacters) {
            long maxDamageCausedOther = Calculator.calculateMaxDamageCaused(other);

            if ((other.isLimitBroken() && maxDamageCausedOther >= maxDamageCausedChara)
                    || (!other.isLimitBroken() && maxDamageCausedOther > maxDamageCausedChara)) {
                return false;
            }
        }

        return true;
    }
}
